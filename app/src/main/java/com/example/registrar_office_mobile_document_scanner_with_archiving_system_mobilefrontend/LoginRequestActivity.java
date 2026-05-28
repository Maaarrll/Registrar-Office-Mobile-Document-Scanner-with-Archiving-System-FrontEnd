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

public class LoginRequestActivity extends AppCompatActivity {

    EditText etStudentId, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_request);

        etStudentId = findViewById(R.id.etStudentId);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnSubmitLogin).setOnClickListener(v -> {
            String studentId = etStudentId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (studentId.isEmpty()) {
                etStudentId.setError("Student ID is required");
                etStudentId.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Password is required");
                etPassword.requestFocus();
                return;
            }

            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            StudentLoginRequest loginRequest =
                    new StudentLoginRequest(studentId, password);

            apiService.studentLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(
                        Call<LoginResponse> call,
                        Response<LoginResponse> response
                ) {
                    if (response.isSuccessful() && response.body() != null) {

                        LoginResponse loginResponse = response.body();

                        if ("success".equals(loginResponse.getStatus())) {

                            String token =
                                    loginResponse.getData().getAccess_token();

                            String role =
                                    loginResponse.getData().getRole();

                            SessionManager sessionManager =
                                    new SessionManager(LoginRequestActivity.this);

                            sessionManager.saveLoginSession(
                                    token,
                                    role,
                                    studentId
                            );

                            Toast.makeText(
                                    LoginRequestActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();

                            String nextScreen =
                                    getIntent().getStringExtra("next_screen");

                            Intent intent;

                            if ("admission_form".equals(nextScreen)) {

                                intent = new Intent(
                                        LoginRequestActivity.this,
                                        AdmissionFormActivity.class
                                );

                            } else {

                                intent = new Intent(
                                        LoginRequestActivity.this,
                                        DocumentRequestFormActivity.class
                                );
                            }

                            intent.putExtra("student_id", studentId);

                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(
                                    LoginRequestActivity.this,
                                    "ID or password incorrect",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    } else {
                        String errorMessage = "Invalid Student ID or Password";

                        Toast.makeText(
                                LoginRequestActivity.this,
                                "ID or password incorrect",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(
                            LoginRequestActivity.this,
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