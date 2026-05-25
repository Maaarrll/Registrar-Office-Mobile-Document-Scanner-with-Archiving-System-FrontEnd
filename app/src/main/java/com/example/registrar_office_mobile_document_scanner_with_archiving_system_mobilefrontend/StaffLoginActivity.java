package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StaffLoginActivity extends AppCompatActivity {

    EditText etStaffId, etStaffPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        etStaffId = findViewById(R.id.etStaffId);
        etStaffPassword = findViewById(R.id.etStaffPassword);

        findViewById(R.id.btnStaffLogin).setOnClickListener(v -> {
            String staffId = etStaffId.getText().toString().trim();
            String password = etStaffPassword.getText().toString().trim();

            if (staffId.isEmpty()) {
                etStaffId.setError("Staff ID is required");
                etStaffId.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etStaffPassword.setError("Password is required");
                etStaffPassword.requestFocus();
                return;
            }

            if (staffId.equals("STAFF001") && password.equals("admin123")) {

                SessionManager sessionManager =
                        new SessionManager(StaffLoginActivity.this);

                sessionManager.saveLoginSession(
                        "fake-token-for-now",
                        "staff",
                        staffId
                );

                Toast.makeText(
                        StaffLoginActivity.this,
                        "Staff login successful",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        StaffLoginActivity.this,
                        StaffOptionsActivity.class
                );

                intent.putExtra("staff_id", staffId);

                startActivity(intent);
                finish();

            } else {

                Toast.makeText(
                        StaffLoginActivity.this,
                        "Invalid Staff ID or Password",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}