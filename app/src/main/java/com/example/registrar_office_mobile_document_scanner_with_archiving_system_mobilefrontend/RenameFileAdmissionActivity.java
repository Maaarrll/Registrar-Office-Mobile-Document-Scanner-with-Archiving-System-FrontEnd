package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RenameFileAdmissionActivity extends AppCompatActivity {

    ImageView ivPreview;
    EditText etFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_file_admission);

        ivPreview = findViewById(R.id.ivPreview);
        etFileName = findViewById(R.id.etFileName);

        if (ImageStorage.bitmap != null) {
            ivPreview.setImageBitmap(ImageStorage.bitmap);
        }

        String imageUriString = getIntent().getStringExtra("image_uri");

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            ivPreview.setImageURI(imageUri);
        }

        findViewById(R.id.btnAddPhoto).setOnClickListener(v -> {

            String newFileName = etFileName.getText().toString().trim();

            if (newFileName.isEmpty()) {

                Toast.makeText(
                        this,
                        "Please enter file name",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent = new Intent(
                    RenameFileAdmissionActivity.this,
                    VerifyAdmissionActivity.class
            );

            intent.putExtra("file_name", newFileName + ".jpg");

            startActivity(intent);
        });

        findViewById(R.id.btnRetake).setOnClickListener(v -> onBackPressed());

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}