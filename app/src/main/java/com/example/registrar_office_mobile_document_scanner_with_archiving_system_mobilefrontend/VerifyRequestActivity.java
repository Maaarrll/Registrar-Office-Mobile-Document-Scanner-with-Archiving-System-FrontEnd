package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class VerifyRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_request);

        TextView tvStudentId = findViewById(R.id.tvStudentId);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvRequestType = findViewById(R.id.tvRequestType);
        TextView tvUrgentRequest = findViewById(R.id.tvUrgentRequest);

        SessionManager sessionManager =
                new SessionManager(VerifyRequestActivity.this);

        String studentId =
                sessionManager.getStaffId();

        String firstName = getIntent().getStringExtra("first_name");
        String middleName = getIntent().getStringExtra("middle_name");
        String lastName = getIntent().getStringExtra("last_name");
        String requestType = getIntent().getStringExtra("request_type");

        boolean urgentRequest = getIntent().getBooleanExtra(
                "urgent_request",
                false
        );

        tvStudentId.setText("Student ID: " + studentId);

        tvName.setText(
                "Name: " +
                        safeText(firstName) + " " +
                        safeText(middleName) + " " +
                        safeText(lastName)
        );

        tvRequestType.setText("Request Type: " + safeText(requestType));

        tvUrgentRequest.setText(
                "Urgent Request: " +
                        (urgentRequest ? "CHECKED" : "NOT CHECKED")
        );

        findViewById(R.id.btnSubmitRequest).setOnClickListener(v -> {

            String token = sessionManager.getToken();

            if (token == null || token.trim().isEmpty()) {
                Toast.makeText(
                        VerifyRequestActivity.this,
                        "No login token found. Please login again.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (studentId == null || studentId.trim().isEmpty()) {
                Toast.makeText(
                        VerifyRequestActivity.this,
                        "No logged-in student ID found. Please login again.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Toast.makeText(
                    VerifyRequestActivity.this,
                    "Submitting student ID: " + studentId,
                    Toast.LENGTH_LONG
            ).show();

            RequestSubmitRequest request =
                    new RequestSubmitRequest(
                            studentId,
                            urgentRequest,
                            java.util.Collections.singletonList(
                                    new RequestSubmitRequest.RequestItem(
                                            requestType,
                                            1
                                    )
                            )
                    );

            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            apiService.submitRequest(
                    "Bearer " + token,
                    request
            ).enqueue(new retrofit2.Callback<ApiEnvelope<Object>>() {
                @Override
                public void onResponse(
                        retrofit2.Call<ApiEnvelope<Object>> call,
                        retrofit2.Response<ApiEnvelope<Object>> response
                ) {
                    if (response.isSuccessful() && response.body() != null) {

                        ApiEnvelope<Object> apiResponse =
                                response.body();

                        if ("success".equals(apiResponse.getStatus())) {

                            Toast.makeText(
                                    VerifyRequestActivity.this,
                                    "Request submitted successfully",
                                    Toast.LENGTH_LONG
                            ).show();

                            android.content.Intent intent =
                                    new android.content.Intent(
                                            VerifyRequestActivity.this,
                                            MainActivity.class
                                    );

                            intent.setFlags(
                                    android.content.Intent.FLAG_ACTIVITY_NEW_TASK |
                                            android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(
                                    VerifyRequestActivity.this,
                                    apiResponse.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                    } else {
                        String errorMessage =
                                "Request submission failed";

                        try {
                            if (response.errorBody() != null) {
                                errorMessage =
                                        response.errorBody().string();
                            }
                        } catch (Exception e) {
                            errorMessage =
                                    e.getMessage();
                        }

                        Toast.makeText(
                                VerifyRequestActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        retrofit2.Call<ApiEnvelope<Object>> call,
                        Throwable t
                ) {
                    Toast.makeText(
                            VerifyRequestActivity.this,
                            "Submission failed: " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private String safeText(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        return value;
    }
}