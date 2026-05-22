package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class VerifyAdmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_admission);

        TextView tvFirstName = findViewById(R.id.tvFirstName);
        TextView tvMiddleName = findViewById(R.id.tvMiddleName);
        TextView tvLastName = findViewById(R.id.tvLastName);
        TextView tvDOB = findViewById(R.id.tvDOB);
        TextView tvPOB = findViewById(R.id.tvPOB);
        TextView tvGender = findViewById(R.id.tvGender);
        TextView tvHomeAddress = findViewById(R.id.tvHomeAddress);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvSchool = findViewById(R.id.tvSchool);
        TextView tvYearGraduated = findViewById(R.id.tvYearGraduated);

        TextView tvFileName = findViewById(R.id.tvFileName);

        ImageView ivDocumentPreview =
                findViewById(R.id.ivDocumentPreview);

        String firstName =
                getIntent().getStringExtra("first_name");

        String middleName =
                getIntent().getStringExtra("middle_name");

        String lastName =
                getIntent().getStringExtra("last_name");

        String birthDate =
                getIntent().getStringExtra("birth_date");

        String placeBirth =
                getIntent().getStringExtra("place_birth");

        String gender =
                getIntent().getStringExtra("gender");

        String homeAddress =
                getIntent().getStringExtra("home_address");

        String email =
                getIntent().getStringExtra("email");

        String school =
                getIntent().getStringExtra("school");

        String yearGraduated =
                getIntent().getStringExtra("year_graduated");

        String fileName =
                getIntent().getStringExtra("file_name");

        String imageUri =
                getIntent().getStringExtra("image_uri");

        tvFirstName.setText("First Name: " + firstName);

        tvMiddleName.setText("Middle Name: " + middleName);

        tvLastName.setText("Last Name: " + lastName);

        tvDOB.setText("Date of Birth: " + birthDate);

        tvPOB.setText("Place of Birth: " + placeBirth);

        tvGender.setText("Gender: " + gender);

        tvHomeAddress.setText("Home Address: " + homeAddress);

        tvEmail.setText("E-mail Account: " + email);

        tvSchool.setText("School Last Attended: " + school);

        tvYearGraduated.setText(
                "Year Graduated: " + yearGraduated
        );

        tvFileName.setText(
                fileName != null ?
                        fileName :
                        "No document added"
        );

        if (imageUri != null) {

            ivDocumentPreview.setImageURI(
                    Uri.parse(imageUri)
            );
        }

        findViewById(R.id.btnSubmitAdmission)
                .setOnClickListener(v -> {

                    Toast.makeText(
                            VerifyAdmissionActivity.this,
                            "Admission Form Submitted Successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                });

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(
                v -> onBackPressed()
        );
    }
}