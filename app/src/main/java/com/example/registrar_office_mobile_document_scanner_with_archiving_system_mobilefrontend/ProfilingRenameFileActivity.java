package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfilingRenameFileActivity extends AppCompatActivity {

    private ImageView ivPreview;
    private EditText etFileName;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiling_rename);

        ivPreview = findViewById(R.id.ivPreview);
        etFileName = findViewById(R.id.etFileName);

        String imageUriString = getIntent().getStringExtra("image_uri");

        if (imageUriString != null && !imageUriString.isEmpty()) {
            selectedImageUri = Uri.parse(imageUriString);
            ivPreview.setImageURI(selectedImageUri);
        } else {
            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            String fileName = etFileName.getText().toString().trim();

            if (selectedImageUri == null) {
                Toast.makeText(this, "No document selected", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fileName.isEmpty()) {
                etFileName.setError("File name is required");
                etFileName.requestFocus();
                return;
            }

            Intent intent = new Intent(
                    ProfilingRenameFileActivity.this,
                    ProfilingLinkStudentActivity.class
            );

            intent.putExtra("image_uri", selectedImageUri.toString());
            intent.putExtra("file_name", fileName);

            startActivity(intent);
        });

        findViewById(R.id.btnRetake).setOnClickListener(v -> onBackPressed());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}