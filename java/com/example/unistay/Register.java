package com.example.unistay;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {

    EditText fullname, emailid, password, confirmpassword;
    TextView loginbtn;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        fullname = findViewById(R.id.fullname);
        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        loginbtn = findViewById(R.id.loginredirect);

        loginbtn.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });
    }

    public void signupbtn(View view) {
        String name = fullname.getText().toString().trim();
        String email = emailid.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmpassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        user.updateProfile(profileUpdate).addOnCompleteListener(task -> {
                            Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, DashboardActivity.class));
                            finish();
                        });
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(Register.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
