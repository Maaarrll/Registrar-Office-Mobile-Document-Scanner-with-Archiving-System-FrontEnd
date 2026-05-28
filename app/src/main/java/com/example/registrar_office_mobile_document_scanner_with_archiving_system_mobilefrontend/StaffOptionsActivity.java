package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StaffOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_options);
        findViewById(R.id.btnCheckRequests).setOnClickListener(v -> {
            Intent intent = new Intent(
                    StaffOptionsActivity.this,
                    RequestListActivity.class
            );

            startActivity(intent);
        });

        findViewById(R.id.btnProfiling).setOnClickListener(v -> {
            Intent intent = new Intent(
                    StaffOptionsActivity.this,
                    ProfilingSelectPhotoActivity.class
            );

            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}