package com.example.unistay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VisitConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_confirmation);

        TextView tvConfirmation = findViewById(R.id.tv_confirmation);
        String confirmationText = getIntent().getStringExtra("confirmationDetails");
        if (confirmationText != null) {
            tvConfirmation.setText(confirmationText);
        } else {
            tvConfirmation.setText("Booking details not available.");
        }
    }

    public void backtohome(View view) {
        Intent intent =new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}
