package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfilingSelectPhotoActivity extends AppCompatActivity {

    ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiling_select_photo);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        Intent intent = new Intent(
                                ProfilingSelectPhotoActivity.this,
                                ProfilingRenameFileActivity.class
                        );

                        intent.putExtra("image_uri", uri.toString());
                        startActivity(intent);
                    }
                }
        );

        findViewById(R.id.btnTakePhoto).setOnClickListener(v -> {
            Intent intent = new Intent(
                    ProfilingSelectPhotoActivity.this,
                    DocumentScannerActivity.class
            );

            intent.putExtra("scanner_mode", "profiling");

            startActivity(intent);
        });

        findViewById(R.id.btnSelectGallery).setOnClickListener(v -> {
            galleryLauncher.launch("image/*");
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}