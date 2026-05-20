package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CameraAdmissionActivity extends AppCompatActivity {

    ImageView cameraPreview;

    ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_admission);

        cameraPreview = findViewById(R.id.cameraPreview);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK &&
                            result.getData() != null) {

                        Bundle extras = result.getData().getExtras();

                        Bitmap imageBitmap =
                                (Bitmap) extras.get("data");

                        cameraPreview.setImageBitmap(imageBitmap);
                    }
                }
        );

        findViewById(R.id.shutterButton).setOnClickListener(v -> {

            Intent cameraIntent =
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                cameraLauncher.launch(cameraIntent);
            } else {

                Toast.makeText(
                        this,
                        "Camera not available",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        findViewById(R.id.btnDoneCamera).setOnClickListener(v -> {

            Intent intent = new Intent(
                    CameraAdmissionActivity.this,
                    RenameFileAdmissionActivity.class
            );

            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}