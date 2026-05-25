package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.documentscanner.GmsDocumentScanner;
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult;

public class DocumentScannerActivity extends AppCompatActivity {

    private ActivityResultLauncher<IntentSenderRequest> scannerLauncher;
    private String scannerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerMode = getIntent().getStringExtra("scanner_mode");

        scannerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                        GmsDocumentScanningResult scanResult =
                                GmsDocumentScanningResult.fromActivityResultIntent(result.getData());

                        if (scanResult != null && scanResult.getPdf() != null) {
                            Uri pdfUri = scanResult.getPdf().getUri();

                            openNextScreen(pdfUri);
                        } else {
                            Toast.makeText(this, "No scanned document found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } else {
                        finish();
                    }
                }
        );

        startScanner();
    }

    private void startScanner() {
        GmsDocumentScannerOptions options =
                new GmsDocumentScannerOptions.Builder()
                        .setGalleryImportAllowed(true)
                        .setPageLimit(5)
                        .setResultFormats(
                                GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                                GmsDocumentScannerOptions.RESULT_FORMAT_PDF
                        )
                        .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
                        .build();

        GmsDocumentScanner scanner =
                GmsDocumentScanning.getClient(options);

        scanner.getStartScanIntent(this)
                .addOnSuccessListener(intentSender -> {
                    scannerLauncher.launch(
                            new IntentSenderRequest.Builder(intentSender).build()
                    );
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            this,
                            "Scanner failed: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                    finish();
                });
    }

    private void openNextScreen(Uri pdfUri) {
        Intent intent;

        if ("admission".equals(scannerMode)) {

            intent = new Intent(
                    DocumentScannerActivity.this,
                    RenameFileAdmissionActivity.class
            );

            intent.putExtras(getIntent());
            intent.putExtra("image_uri", pdfUri.toString());

        } else if ("profiling".equals(scannerMode)) {

            intent = new Intent(
                    DocumentScannerActivity.this,
                    ProfilingRenameFileActivity.class
            );

            intent.putExtra("image_uri", pdfUri.toString());

        } else {

            intent = new Intent(
                    DocumentScannerActivity.this,
                    ProfilingLinkStudentActivity.class
            );

            intent.putExtra("image_uri", pdfUri.toString());
            intent.putExtra("file_name", "Scanned Document.pdf");
        }

        startActivity(intent);
        finish();
    }
}