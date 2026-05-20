package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginRequestActivity extends AppCompatActivity {

    EditText etStudentId, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_request);

        etStudentId = findViewById(R.id.etStudentId);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnSubmitLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentId = etStudentId.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (studentId.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginRequestActivity.this, "Please enter Student ID and Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Temporary login check
                // Later, replace this with database/API checking
                if (studentId.equals("232747") && password.equals("thisisatest")) {
                    Toast.makeText(LoginRequestActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginRequestActivity.this, DocumentRequestFormActivity.class);
                    intent.putExtra("student_id", studentId);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginRequestActivity.this, "Invalid Student ID or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}