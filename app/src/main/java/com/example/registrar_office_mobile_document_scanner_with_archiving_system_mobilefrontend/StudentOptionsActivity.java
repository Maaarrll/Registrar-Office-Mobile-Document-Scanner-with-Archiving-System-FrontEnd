package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StudentOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_options);

        findViewById(R.id.btnRequestCredentials).setOnClickListener(v -> {
            Intent intent = new Intent(
                    StudentOptionsActivity.this,
                    LoginRequestActivity.class
            );

            intent.putExtra("next_screen", "document_request");

            startActivity(intent);
        });

        findViewById(R.id.btnFillAdmission).setOnClickListener(v -> {
            Intent intent = new Intent(
                    StudentOptionsActivity.this,
                    AdmissionFormActivity.class
            );

            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}