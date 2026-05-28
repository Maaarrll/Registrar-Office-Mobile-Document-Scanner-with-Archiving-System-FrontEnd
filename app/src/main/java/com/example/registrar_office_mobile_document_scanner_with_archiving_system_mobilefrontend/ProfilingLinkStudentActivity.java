package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfilingLinkStudentActivity extends AppCompatActivity {

    private ImageView ivDocumentPreview;
    private EditText etSearchStudentId;

    private TextView tvDocumentName;
    private TextView tvStudentId;
    private TextView tvStudentName;
    private TextView tvStudentCourse;
    private TextView tvStudentYear;

    private View studentInfoLayout;

    private boolean isStudentFound = false;

    private Uri selectedDocumentUri;
    private String selectedFileName = "Profile Document";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiling_link_student);

        ivDocumentPreview = findViewById(R.id.ivDocumentPreview);
        etSearchStudentId = findViewById(R.id.etSearchStudentId);
        tvDocumentName = findViewById(R.id.tvDocumentName);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentCourse = findViewById(R.id.tvStudentCourse);
        tvStudentYear = findViewById(R.id.tvStudentYear);
        studentInfoLayout = findViewById(R.id.studentInfoLayout);

        String imageUri = getIntent().getStringExtra("image_uri");
        String pdfUri = getIntent().getStringExtra("pdf_uri");
        String fileName = getIntent().getStringExtra("file_name");

        if (imageUri != null && !imageUri.isEmpty()) {
            ivDocumentPreview.setImageURI(Uri.parse(imageUri));
        }

        if (pdfUri != null && !pdfUri.isEmpty()) {
            selectedDocumentUri = Uri.parse(pdfUri);
        } else if (imageUri != null && !imageUri.isEmpty()) {
            selectedDocumentUri = Uri.parse(imageUri);
        }

        if (fileName != null && !fileName.isEmpty()) {
            selectedFileName = fileName;
        }

        tvDocumentName.setText(selectedFileName);

        findViewById(R.id.btnSearchStudent).setOnClickListener(v -> searchStudent());
        findViewById(R.id.btnLink).setOnClickListener(v -> searchStudent());

        findViewById(R.id.btnAddProfile).setOnClickListener(v -> {
            if (!isStudentFound) {
                Toast.makeText(this, "Search student first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedDocumentUri == null) {
                Toast.makeText(this, "No document selected", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Add to Profile")
                    .setMessage("Upload and link this document to the student's profile?")
                    .setPositiveButton("Yes", (dialog, which) -> uploadAndLinkDocument())
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void searchStudent() {
        String studentIdInput = etSearchStudentId.getText().toString().trim();

        if (studentIdInput.isEmpty()) {
            etSearchStudentId.setError("Enter Student ID");
            etSearchStudentId.requestFocus();
            return;
        }

        isStudentFound = true;
        studentInfoLayout.setVisibility(View.VISIBLE);

        tvStudentId.setText("✓ Student ID: " + studentIdInput);
        tvStudentName.setText("✓ Name: Student record ready for linking");
        tvStudentCourse.setText("✓ Course: To be verified by backend");
        tvStudentYear.setText("✓ Year Level: To be verified by backend");

        Toast.makeText(this, "Student selected", Toast.LENGTH_SHORT).show();
    }

    private void uploadAndLinkDocument() {
        try {
            SessionManager sessionManager =
                    new SessionManager(ProfilingLinkStudentActivity.this);

            String token = sessionManager.getToken();

            if (token == null || token.trim().isEmpty()) {
                Toast.makeText(
                        this,
                        "No staff token found. Please login again.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            String studentIdInput =
                    etSearchStudentId.getText().toString().trim();

            File file =
                    FileUtils.copyUriToCache(this, selectedDocumentUri);

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

            Toast.makeText(this, "Uploading document...", Toast.LENGTH_SHORT).show();

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
                    if (response.isSuccessful()
                            && response.body() != null
                            && "success".equals(response.body().getStatus())) {

                        int documentId =
                                response.body().getData().getDocument_id();

                        linkDocument(
                                token,
                                documentId,
                                studentIdInput,
                                file.getName()
                        );

                    } else {
                        showError(response, "Document upload failed");
                    }
                }

                @Override
                public void onFailure(
                        retrofit2.Call<ApiEnvelope<DocumentUploadData>> call,
                        Throwable t
                ) {
                    Toast.makeText(
                            ProfilingLinkStudentActivity.this,
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

    private void linkDocument(
            String token,
            int documentId,
            String studentId,
            String fileName
    ) {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        LinkDocumentRequest linkRequest =
                new LinkDocumentRequest(
                        studentId,
                        selectedFileName,
                        fileName,
                        false
                );

        Toast.makeText(this, "Linking document...", Toast.LENGTH_SHORT).show();

        apiService.linkDocumentToStudent(
                "Bearer " + token,
                documentId,
                linkRequest
        ).enqueue(new retrofit2.Callback<ApiEnvelope<Object>>() {
            @Override
            public void onResponse(
                    retrofit2.Call<ApiEnvelope<Object>> call,
                    retrofit2.Response<ApiEnvelope<Object>> response
            ) {
                if (response.isSuccessful()
                        && response.body() != null
                        && "success".equals(response.body().getStatus())) {

                    Toast.makeText(
                            ProfilingLinkStudentActivity.this,
                            "Document linked to student profile successfully",
                            Toast.LENGTH_LONG
                    ).show();

                    finish();

                } else {
                    showError(response, "Document linking failed");
                }
            }

            @Override
            public void onFailure(
                    retrofit2.Call<ApiEnvelope<Object>> call,
                    Throwable t
            ) {
                Toast.makeText(
                        ProfilingLinkStudentActivity.this,
                        "Link failed: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void showError(
            retrofit2.Response<?> response,
            String defaultMessage
    ) {
        String errorMessage = defaultMessage;

        try {
            if (response.errorBody() != null) {
                errorMessage = response.errorBody().string();
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        Toast.makeText(
                ProfilingLinkStudentActivity.this,
                errorMessage,
                Toast.LENGTH_LONG
        ).show();
    }
}