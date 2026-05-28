package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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

            try {
                File pdfFile = createPdfFromImage(fileName);

                Intent intent = new Intent(
                        ProfilingRenameFileActivity.this,
                        ProfilingLinkStudentActivity.class
                );

                intent.putExtra("image_uri", selectedImageUri.toString());
                intent.putExtra("pdf_uri", Uri.fromFile(pdfFile).toString());
                intent.putExtra("file_name", pdfFile.getName());

                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "PDF conversion failed: " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        findViewById(R.id.btnRetake).setOnClickListener(v -> onBackPressed());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private File createPdfFromImage(String fileName) throws Exception {
        InputStream inputStream =
                getContentResolver().openInputStream(selectedImageUri);

        Bitmap bitmap =
                BitmapFactory.decodeStream(inputStream);

        if (bitmap == null) {
            throw new Exception("Unable to read image");
        }

        PdfDocument pdfDocument =
                new PdfDocument();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(
                        bitmap.getWidth(),
                        bitmap.getHeight(),
                        1
                ).create();

        PdfDocument.Page page =
                pdfDocument.startPage(pageInfo);

        page.getCanvas().drawBitmap(
                bitmap,
                0,
                0,
                null
        );

        pdfDocument.finishPage(page);

        File folder =
                new File(getExternalFilesDir(null), "ProfilingPDFs");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File pdfFile =
                new File(folder, fileName + ".pdf");

        FileOutputStream outputStream =
                new FileOutputStream(pdfFile);

        pdfDocument.writeTo(outputStream);

        pdfDocument.close();
        outputStream.close();

        return pdfFile;
    }
}