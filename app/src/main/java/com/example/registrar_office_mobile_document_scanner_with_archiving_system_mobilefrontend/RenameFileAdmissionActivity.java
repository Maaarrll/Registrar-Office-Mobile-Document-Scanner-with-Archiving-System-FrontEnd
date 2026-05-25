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

public class RenameFileAdmissionActivity extends AppCompatActivity {

    private ImageView ivPreview;
    private EditText etFileName;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_file_admission);

        ivPreview = findViewById(R.id.ivPreview);
        etFileName = findViewById(R.id.etFileName);

        String imageUriString = getIntent().getStringExtra("image_uri");

        if (imageUriString != null && !imageUriString.isEmpty()) {
            selectedImageUri = Uri.parse(imageUriString);
            ivPreview.setImageURI(selectedImageUri);
        } else {
            Toast.makeText(this, "No image found. Please retake or select a photo.", Toast.LENGTH_LONG).show();
        }

        findViewById(R.id.btnAddPhoto).setOnClickListener(v -> {
            String fileName = etFileName.getText().toString().trim();

            if (selectedImageUri == null) {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fileName.isEmpty()) {
                etFileName.setError("File name is required");
                etFileName.requestFocus();
                return;
            }

            fileName = cleanFileName(fileName);

            try {
                File pdfFile = createPdfFromImage(fileName);

                Toast.makeText(this, "PDF created successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(
                        RenameFileAdmissionActivity.this,
                        VerifyAdmissionActivity.class
                );

                intent.putExtras(getIntent());
                intent.putExtra("file_name", pdfFile.getName());
                intent.putExtra("pdf_path", pdfFile.getAbsolutePath());
                intent.putExtra("image_uri", selectedImageUri.toString());

                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(this, "PDF failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btnRetake).setOnClickListener(v -> onBackPressed());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private String cleanFileName(String fileName) {
        fileName = fileName.trim();

        if (fileName.toLowerCase().endsWith(".pdf")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }

        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");

        return fileName;
    }

    private File createPdfFromImage(String fileName) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

        if (inputStream == null) {
            throw new Exception("Unable to read selected image");
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        if (bitmap == null) {
            throw new Exception("Invalid image file");
        }

        PdfDocument pdfDocument = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                bitmap.getWidth(),
                bitmap.getHeight(),
                1
        ).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        page.getCanvas().drawBitmap(bitmap, 0, 0, null);
        pdfDocument.finishPage(page);

        File folder = new File(getExternalFilesDir(null), "AdmissionPDFs");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File pdfFile = new File(folder, fileName + ".pdf");

        FileOutputStream outputStream = new FileOutputStream(pdfFile);

        pdfDocument.writeTo(outputStream);

        pdfDocument.close();
        outputStream.close();
        inputStream.close();

        return pdfFile;
    }
}