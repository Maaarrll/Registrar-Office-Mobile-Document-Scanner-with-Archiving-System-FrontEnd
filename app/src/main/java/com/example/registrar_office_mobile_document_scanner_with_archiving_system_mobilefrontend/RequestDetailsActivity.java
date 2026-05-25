package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RequestDetailsActivity extends AppCompatActivity {

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

        final String studentName = getIntent().getStringExtra("student_name");
        final String studentId = getIntent().getStringExtra("student_id");
        final String requestType = getIntent().getStringExtra("request_type");
        final String status = getIntent().getStringExtra("status");
        final String requestTime = getIntent().getStringExtra("request_time");

        tvDetailName.setText("Name: " + safeText(studentName));
        tvDetailStudentId.setText("Student ID: " + safeText(studentId));
        tvDetailRequestType.setText("Request Type: " + safeText(requestType));
        tvDetailStatus.setText("Status: " + safeText(status));
        tvDetailTime.setText("Time: " + safeText(requestTime));

        findViewById(R.id.btnUploadDocument).setOnClickListener(v -> {
            Intent intent = new Intent(
                    RequestDetailsActivity.this,
                    SelectPhotoAdmissionActivity.class
            );

            intent.putExtra("staff_upload_mode", true);
            intent.putExtra("student_name", studentName);
            intent.putExtra("student_id", studentId);
            intent.putExtra("request_type", requestType);
            intent.putExtra("status", status);
            intent.putExtra("request_time", requestTime);

            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}