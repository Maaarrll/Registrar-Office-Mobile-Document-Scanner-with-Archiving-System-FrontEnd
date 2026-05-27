package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraAdmissionActivity extends AppCompatActivity {

    private PreviewView cameraPreview;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_admission);

        cameraPreview = findViewById(R.id.cameraPreview);
        cameraExecutor = Executors.newSingleThreadExecutor();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }

        findViewById(R.id.shutterButton).setOnClickListener(v -> takePhoto());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider =
                        cameraProviderFuture.get();

                Preview preview =
                        new Preview.Builder().build();

                imageCapture =
                        new ImageCapture.Builder().build();

                CameraSelector cameraSelector =
                        CameraSelector.DEFAULT_BACK_CAMERA;

                preview.setSurfaceProvider(
                        cameraPreview.getSurfaceProvider()
                );

                cameraProvider.unbindAll();

                cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                );

            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "Camera failed: " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) {
            Toast.makeText(
                    this,
                    "Camera not ready yet",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        findViewById(R.id.shutterButton).setEnabled(false);

        Toast.makeText(
                this,
                "Capturing photo...",
                Toast.LENGTH_SHORT
        ).show();

        File photoFile = new File(
                getExternalFilesDir(null),
                "captured_admission_photo.jpg"
        );

        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(
                            ImageCapture.OutputFileResults outputFileResults
                    ) {
                        Uri savedUri = Uri.fromFile(photoFile);

                        Intent intent = new Intent(
                                CameraAdmissionActivity.this,
                                RenameFileAdmissionActivity.class
                        );

                        intent.putExtras(getIntent());
                        intent.putExtra("image_uri", savedUri.toString());

                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(ImageCaptureException exception) {
                        findViewById(R.id.shutterButton).setEnabled(true);

                        Toast.makeText(
                                CameraAdmissionActivity.this,
                                "Photo failed: " + exception.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
}