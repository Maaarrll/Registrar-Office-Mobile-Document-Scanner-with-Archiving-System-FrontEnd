package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdmissionFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_form);

        EditText etFirstName = findViewById(R.id.etFirstName);
        EditText etMiddleName = findViewById(R.id.etMiddleName);
        EditText etLastName = findViewById(R.id.etLastName);
        EditText etBirthDate = findViewById(R.id.etBirthDate);
        EditText etPlaceBirth = findViewById(R.id.etPlaceBirth);
        EditText etHomeAddress = findViewById(R.id.etHomeAddress);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etSchool = findViewById(R.id.etSchool);
        EditText etYearGraduated = findViewById(R.id.etYearGraduated);

        RadioGroup radioGroupGender = findViewById(R.id.radioGroupGender);

        findViewById(R.id.btnClearAll).setOnClickListener(v -> {

            etFirstName.setText("");
            etMiddleName.setText("");
            etLastName.setText("");
            etBirthDate.setText("");
            etPlaceBirth.setText("");
            etHomeAddress.setText("");
            etEmail.setText("");
            etSchool.setText("");
            etYearGraduated.setText("");

            radioGroupGender.clearCheck();

            Toast.makeText(
                    AdmissionFormActivity.this,
                    "Form Cleared",
                    Toast.LENGTH_SHORT
            ).show();
        });

        findViewById(R.id.btnNextAdmission).setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdmissionFormActivity.this,
                    SelectPhotoAdmissionActivity.class
            );

            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}