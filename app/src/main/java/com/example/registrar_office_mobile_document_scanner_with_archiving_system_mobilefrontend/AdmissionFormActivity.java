package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

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

        etBirthDate.addTextChangedListener(new TextWatcher() {

            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (isFormatting) return;

                isFormatting = true;

                String input = s.toString().replace("/", "");

                StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < input.length(); i++) {

                    formatted.append(input.charAt(i));

                    if ((i == 1 || i == 3) && i != input.length() - 1) {
                        formatted.append("/");
                    }
                }

                etBirthDate.setText(formatted.toString());
                etBirthDate.setSelection(formatted.length());

                isFormatting = false;
            }
        });

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

            if (etFirstName.getText().toString().trim().isEmpty()) {
                etFirstName.setError("Required");
                etFirstName.requestFocus();
                return;
            }

            if (etMiddleName.getText().toString().trim().isEmpty()) {
                etMiddleName.setError("Required");
                etMiddleName.requestFocus();
                return;
            }

            if (etLastName.getText().toString().trim().isEmpty()) {
                etLastName.setError("Required");
                etLastName.requestFocus();
                return;
            }

            if (etBirthDate.getText().toString().trim().isEmpty()) {
                etBirthDate.setError("Required");
                etBirthDate.requestFocus();
                return;
            }

            if (etPlaceBirth.getText().toString().trim().isEmpty()) {
                etPlaceBirth.setError("Required");
                etPlaceBirth.requestFocus();
                return;
            }

            if (etHomeAddress.getText().toString().trim().isEmpty()) {
                etHomeAddress.setError("Required");
                etHomeAddress.requestFocus();
                return;
            }

            if (etEmail.getText().toString().trim().isEmpty()) {
                etEmail.setError("Required");
                etEmail.requestFocus();
                return;
            }

            if (etSchool.getText().toString().trim().isEmpty()) {
                etSchool.setError("Required");
                etSchool.requestFocus();
                return;
            }

            if (etYearGraduated.getText().toString().trim().isEmpty()) {
                etYearGraduated.setError("Required");
                etYearGraduated.requestFocus();
                return;
            }

            if (radioGroupGender.getCheckedRadioButtonId() == -1) {

                Toast.makeText(
                        AdmissionFormActivity.this,
                        "Please select gender",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent = new Intent(
                    AdmissionFormActivity.this,
                    SelectPhotoAdmissionActivity.class
            );

            intent.putExtra(
                    "first_name",
                    etFirstName.getText().toString().trim()
            );

            intent.putExtra(
                    "middle_name",
                    etMiddleName.getText().toString().trim()
            );

            intent.putExtra(
                    "last_name",
                    etLastName.getText().toString().trim()
            );

            intent.putExtra(
                    "birth_date",
                    etBirthDate.getText().toString().trim()
            );

            intent.putExtra(
                    "place_birth",
                    etPlaceBirth.getText().toString().trim()
            );

            intent.putExtra(
                    "home_address",
                    etHomeAddress.getText().toString().trim()
            );

            intent.putExtra(
                    "email",
                    etEmail.getText().toString().trim()
            );

            intent.putExtra(
                    "school",
                    etSchool.getText().toString().trim()
            );

            intent.putExtra(
                    "year_graduated",
                    etYearGraduated.getText().toString().trim()
            );

            int selectedGenderId =
                    radioGroupGender.getCheckedRadioButtonId();

            RadioButton selectedGender =
                    findViewById(selectedGenderId);

            intent.putExtra(
                    "gender",
                    selectedGender.getText().toString()
            );

            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}