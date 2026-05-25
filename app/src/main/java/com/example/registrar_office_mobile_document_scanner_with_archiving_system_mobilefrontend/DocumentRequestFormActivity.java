package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DocumentRequestFormActivity extends AppCompatActivity {

    private String loggedStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_request_form);

        EditText etStudentId = findViewById(R.id.etStudentId);
        EditText etFirstName = findViewById(R.id.etFirstName);
        EditText etMiddleName = findViewById(R.id.etMiddleName);
        EditText etLastName = findViewById(R.id.etLastName);

        RadioGroup radioGroupRequestType = findViewById(R.id.radioGroupRequestType);
        CheckBox cbUrgent = findViewById(R.id.cbUrgent);

        loggedStudentId = getIntent().getStringExtra("student_id");

        findViewById(R.id.btnAutoFill).setOnClickListener(v -> {
            if (loggedStudentId != null && !loggedStudentId.isEmpty()) {
                etStudentId.setText(loggedStudentId);
            } else {
                Toast.makeText(
                        DocumentRequestFormActivity.this,
                        "No logged-in Student ID found",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(v -> {

            String studentId = etStudentId.getText().toString().trim();
            String firstName = etFirstName.getText().toString().trim();
            String middleName = etMiddleName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();

            if (studentId.isEmpty()) {
                etStudentId.setError("Student ID is required");
                etStudentId.requestFocus();
                return;
            }

            if (studentId.length() != 6) {
                etStudentId.setError("Student ID must be 6 digits");
                etStudentId.requestFocus();
                return;
            }

            if (firstName.isEmpty()) {
                etFirstName.setError("First name is required");
                etFirstName.requestFocus();
                return;
            }

            if (lastName.isEmpty()) {
                etLastName.setError("Last name is required");
                etLastName.requestFocus();
                return;
            }

            int selectedId = radioGroupRequestType.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(
                        DocumentRequestFormActivity.this,
                        "Please select a request type",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            RadioButton selectedRadio = findViewById(selectedId);
            String requestType = selectedRadio.getText().toString();

            Intent intent = new Intent(
                    DocumentRequestFormActivity.this,
                    VerifyRequestActivity.class
            );

            intent.putExtra("student_id", studentId);
            intent.putExtra("first_name", firstName);
            intent.putExtra("middle_name", middleName);
            intent.putExtra("last_name", lastName);
            intent.putExtra("request_type", requestType);
            intent.putExtra("urgent_request", cbUrgent.isChecked());

            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}