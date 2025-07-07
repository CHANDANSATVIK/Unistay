package com.example.unistay;

import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private EditText emailid, password;
    private TextView signup, forgotpassword;
    private CredentialManager credentialManager;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, DashboardActivity.class));
            finish();
        }

        credentialManager = CredentialManager.create(this);

        findViewById(R.id.googleLoginBtn).setOnClickListener(view -> startGoogleSignIn());

        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.registeredirect);
        forgotpassword = findViewById(R.id.forgotpassword);

        signup.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));
    }

    private void startGoogleSignIn() {
        GetGoogleIdOption getGoogleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.web_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(getGoogleIdOption)
                .build();

        CancellationSignal cancellationSignal = new CancellationSignal();

        CredentialManagerCallback<GetCredentialResponse, GetCredentialException> callback =
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        runOnUiThread(() -> handleSignIn(result.getCredential()));
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(Login.this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Google Sign-In error: ", e);
                        });
                    }
                };

        credentialManager.getCredentialAsync(
                this,
                request,
                cancellationSignal,
                executor,
                callback
        );
    }

    private void handleSignIn(Credential credential) {
        Log.d(TAG, "Received credential type: " + credential.getType());

        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;

            // Check for the correct type string
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                try {
                    GoogleIdTokenCredential googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(customCredential.getData());

                    Log.d(TAG, "Google ID Token received, proceeding with Firebase auth");
                    firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
                } catch (Exception e) {
                    Log.e(TAG, "Error creating GoogleIdTokenCredential: ", e);
                    Toast.makeText(this, "Failed to process Google credentials", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.w(TAG, "Unexpected credential type: " + credential.getType());
                Toast.makeText(this, "Unexpected credential type received", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.w(TAG, "Credential is not a CustomCredential");
            Toast.makeText(this, "Invalid credential format", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        Log.d(TAG, "Starting Firebase authentication with Google ID token");

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d(TAG, "User signed in: " + user.getEmail());
                            Toast.makeText(Login.this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        }
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(Login.this, "Firebase authentication failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Log.d(TAG, "updateUI called with user: " + (user != null ? user.getEmail() : "null"));

        if (user != null) {
            Log.d(TAG, "Navigating to DashboardActivity");
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "User is null, staying on login screen");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(mAuth.getCurrentUser());
    }

    public void loginbtn(View view) {
        String email = emailid.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(Login.this, "Enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, DashboardActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(Login.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
