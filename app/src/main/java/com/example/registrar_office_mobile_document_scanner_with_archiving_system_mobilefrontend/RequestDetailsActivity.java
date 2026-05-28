package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestDetailsActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> filePickerLauncher;

    private String safeText(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "N/A";
        }
        return value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        TextView tvDetailName = findViewById(R.id.tvDetailName);
        TextView tvDetailStudentId = findViewById(R.id.tvDetailStudentId);
        TextView tvDetailRequestType = findViewById(R.id.tvDetailRequestType);
        TextView tvDetailStatus = findViewById(R.id.tvDetailStatus);
        TextView tvDetailTime = findViewById(R.id.tvDetailTime);

        String studentName = getIntent().getStringExtra("student_name");
        String studentId = getIntent().getStringExtra("student_id");
        String requestType = getIntent().getStringExtra("request_type");
        String status = getIntent().getStringExtra("status");
        String requestTime = getIntent().getStringExtra("request_time");

        tvDetailName.setText("Name: " + safeText(studentName));
        tvDetailStudentId.setText("Student ID: " + safeText(studentId));
        tvDetailRequestType.setText("Request Type: " + safeText(requestType));
        tvDetailStatus.setText("Status: " + safeText(status));
        tvDetailTime.setText("Time: " + safeText(requestTime));

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uploadDocument(uri);
                    }
                }
        );

        findViewById(R.id.btnUploadDocument).setOnClickListener(v -> {
            filePickerLauncher.launch("*/*");
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void uploadDocument(Uri fileUri) {
        try {
            SessionManager sessionManager =
                    new SessionManager(RequestDetailsActivity.this);

            String token = sessionManager.getToken();

            if (token == null || token.trim().isEmpty()) {
                Toast.makeText(
                        this,
                        "No staff token found. Please login again.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            File file =
                    FileUtils.copyUriToCache(this, fileUri);

            String sha256 =
                    FileUtils.calculateSha256(file);

            RequestBody requestBody =
                    RequestBody.create(
                            file,
                            MediaType.parse("application/octet-stream")
                    );

            MultipartBody.Part documentPart =
                    MultipartBody.Part.createFormData(
                            "document",
                            file.getName(),
                            requestBody
                    );

            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            apiService.uploadDocument(
                    "Bearer " + token,
                    sha256,
                    documentPart
            ).enqueue(new retrofit2.Callback<ApiEnvelope<DocumentUploadData>>() {
                @Override
                public void onResponse(
                        retrofit2.Call<ApiEnvelope<DocumentUploadData>> call,
                        retrofit2.Response<ApiEnvelope<DocumentUploadData>> response
                ) {
                    if (response.isSuccessful() && response.body() != null) {

                        ApiEnvelope<DocumentUploadData> apiResponse =
                                response.body();

                        if ("success".equals(apiResponse.getStatus())) {

                            int documentId =
                                    apiResponse.getData().getDocument_id();

                            Toast.makeText(
                                    RequestDetailsActivity.this,
                                    "Document uploaded successfully. ID: " + documentId,
                                    Toast.LENGTH_LONG
                            ).show();

                        } else {
                            Toast.makeText(
                                    RequestDetailsActivity.this,
                                    apiResponse.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                    } else {
                        String errorMessage = "Upload failed";

                        try {
                            if (response.errorBody() != null) {
                                errorMessage = response.errorBody().string();
                            }
                        } catch (Exception e) {
                            errorMessage = e.getMessage();
                        }

                        Toast.makeText(
                                RequestDetailsActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        retrofit2.Call<ApiEnvelope<DocumentUploadData>> call,
                        Throwable t
                ) {
                    Toast.makeText(
                            RequestDetailsActivity.this,
                            "Upload failed: " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "File error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}