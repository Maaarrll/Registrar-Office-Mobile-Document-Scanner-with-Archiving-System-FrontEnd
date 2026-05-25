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
        String fileName = getIntent().getStringExtra("file_name");

        if (imageUri != null && !imageUri.isEmpty()) {
            ivDocumentPreview.setImageURI(Uri.parse(imageUri));
        }

        tvDocumentName.setText(
                fileName != null && !fileName.isEmpty()
                        ? fileName
                        : "Unnamed Document"
        );

        findViewById(R.id.btnSearchStudent).setOnClickListener(v -> searchStudent());
        findViewById(R.id.btnLink).setOnClickListener(v -> searchStudent());

        findViewById(R.id.btnAddProfile).setOnClickListener(v -> {
            if (!isStudentFound) {
                Toast.makeText(this, "Search student first", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Add to Profile")
                    .setMessage("Are you sure you want to add this document to the student's profile?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(this, "Profile added successfully", Toast.LENGTH_LONG).show();
                        finish();
                    })
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

        if (studentIdInput.equals("234418")) {
            isStudentFound = true;
            studentInfoLayout.setVisibility(View.VISIBLE);

            tvStudentId.setText("✓ Student ID: 234418");
            tvStudentName.setText("✓ Name: PAOLO LEANDRO LOVERITA PINCA");
            tvStudentCourse.setText("✓ Course: BS Information Technology");
            tvStudentYear.setText("✓ Year Level: 3rd Year");

            Toast.makeText(this, "Student found", Toast.LENGTH_SHORT).show();
        } else {
            isStudentFound = false;
            studentInfoLayout.setVisibility(View.GONE);
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
        }
    }
}