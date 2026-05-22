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

        String studentId = getIntent().getStringExtra("student_id");
        String firstName = getIntent().getStringExtra("first_name");
        String middleName = getIntent().getStringExtra("middle_name");
        String lastName = getIntent().getStringExtra("last_name");
        String requestType = getIntent().getStringExtra("request_type");

        tvStudentId.setText("Student ID: " + studentId);

        tvName.setText(
                "Name: " +
                        firstName + " " +
                        middleName + " " +
                        lastName
        );

        boolean urgentRequest = getIntent().getBooleanExtra(
                "urgent_request",
                false
        );
        tvRequestType.setText("Request Type: " + requestType);

        tvUrgentRequest.setText(
                "Urgent Request: " +
                        (urgentRequest ? "CHECKED" : "NOT CHECKED")
        );

        findViewById(R.id.btnSubmitRequest).setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Request Submitted Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}