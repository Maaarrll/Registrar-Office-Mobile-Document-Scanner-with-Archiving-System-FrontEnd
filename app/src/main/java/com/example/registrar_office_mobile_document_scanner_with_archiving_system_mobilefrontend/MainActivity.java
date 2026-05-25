package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentOptionsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnStaff).setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    StaffLoginActivity.class
            );

            startActivity(intent);
        });
    }
}
