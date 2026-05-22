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

    ImageView ivPreview;
    EditText etFileName;
    Uri selectedImageUri;
    Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_file_admission);

        ivPreview = findViewById(R.id.ivPreview);
        etFileName = findViewById(R.id.etFileName);

        String imageUriString = getIntent().getStringExtra("image_uri");

        if (imageUriString != null) {
            selectedImageUri = Uri.parse(imageUriString);
            ivPreview.setImageURI(selectedImageUri);
        }

        findViewById(R.id.btnAddPhoto).setOnClickListener(v -> {
            String fileName = etFileName.getText().toString().trim();

            if (fileName.isEmpty()) {
                Toast.makeText(this, "Please enter file name", Toast.LENGTH_SHORT).show();
                return;
            }

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

    private File createPdfFromImage(String fileName) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

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

        return pdfFile;
    }
}