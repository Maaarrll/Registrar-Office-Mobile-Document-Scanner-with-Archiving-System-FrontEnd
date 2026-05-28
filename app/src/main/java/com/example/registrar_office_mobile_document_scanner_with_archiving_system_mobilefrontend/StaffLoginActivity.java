package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            StaffLoginRequest loginRequest =
                    new StaffLoginRequest(staffId, password);

            apiService.staffLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        LoginResponse loginResponse = response.body();

                        if ("success".equals(loginResponse.getStatus())) {

                            String token = loginResponse.getData().getToken();
                            String role = "staff";

                            SessionManager sessionManager =
                                    new SessionManager(StaffLoginActivity.this);

                            sessionManager.saveLoginSession(
                                    token,
                                    role,
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

                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(
                                    StaffLoginActivity.this,
                                    "ID or password incorrect",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    } else {
                        Toast.makeText(
                                StaffLoginActivity.this,
                                "ID or password incorrect",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(
                            StaffLoginActivity.this,
                            "Login failed: " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}