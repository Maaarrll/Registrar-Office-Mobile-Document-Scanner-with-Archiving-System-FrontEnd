package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class VerifyAdmissionActivity extends AppCompatActivity {

    private String safeText(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }
        return value;
    }

    private String displayText(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "N/A";
        }
        return value;
    }

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
        ImageView ivDocumentPreview = findViewById(R.id.ivDocumentPreview);

        String firstName = getIntent().getStringExtra("first_name");
        String middleName = getIntent().getStringExtra("middle_name");
        String lastName = getIntent().getStringExtra("last_name");
        String birthDate = getIntent().getStringExtra("birth_date");
        String placeBirth = getIntent().getStringExtra("place_birth");
        String gender = getIntent().getStringExtra("gender");
        String homeAddress = getIntent().getStringExtra("home_address");
        String email = getIntent().getStringExtra("email");
        String school = getIntent().getStringExtra("school");
        String yearGraduated = getIntent().getStringExtra("year_graduated");
        String fileName = getIntent().getStringExtra("file_name");
        String imageUri = getIntent().getStringExtra("image_uri");

        tvFirstName.setText("First Name: " + displayText(firstName));
        tvMiddleName.setText("Middle Name: " + displayText(middleName));
        tvLastName.setText("Last Name: " + displayText(lastName));
        tvDOB.setText("Date of Birth: " + displayText(birthDate));
        tvPOB.setText("Place of Birth: " + displayText(placeBirth));
        tvGender.setText("Gender: " + displayText(gender));
        tvHomeAddress.setText("Home Address: " + displayText(homeAddress));
        tvEmail.setText("E-mail Account: " + displayText(email));
        tvSchool.setText("School Last Attended: " + displayText(school));
        tvYearGraduated.setText("Year Graduated: " + displayText(yearGraduated));
        tvFileName.setText(displayText(fileName));

        if (imageUri != null && !imageUri.trim().isEmpty()) {
            ivDocumentPreview.setImageURI(Uri.parse(imageUri));
        }

        findViewById(R.id.btnSubmitAdmission).setOnClickListener(v -> {

            String formattedBirthDate =
                    convertDateToBackendFormat(birthDate);

            AdmissionSubmitRequest admissionRequest =
                    new AdmissionSubmitRequest(
                            safeText(firstName),
                            safeText(middleName),
                            safeText(lastName),
                            formattedBirthDate,
                            safeText(placeBirth),
                            safeText(gender),
                            safeText(homeAddress),
                            safeText(email),
                            safeText(school),
                            safeText(yearGraduated)
                    );

            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            apiService.submitAdmission(admissionRequest)
                    .enqueue(new retrofit2.Callback<ApiEnvelope<AdmissionSubmitData>>() {
                        @Override
                        public void onResponse(
                                retrofit2.Call<ApiEnvelope<AdmissionSubmitData>> call,
                                retrofit2.Response<ApiEnvelope<AdmissionSubmitData>> response
                        ) {
                            if (response.isSuccessful() && response.body() != null) {

                                ApiEnvelope<AdmissionSubmitData> apiResponse = response.body();

                                if ("success".equals(apiResponse.getStatus())) {

                                    String reference = "N/A";

                                    if (apiResponse.getData() != null) {
                                        if (apiResponse.getData().getStudent_reference() != null) {
                                            reference = apiResponse.getData().getStudent_reference();
                                        } else if (apiResponse.getData().getAdmission_no() != null) {
                                            reference = apiResponse.getData().getAdmission_no();
                                        }
                                    }

                                    Toast.makeText(
                                            VerifyAdmissionActivity.this,
                                            "Admission submitted successfully\nReference: " + reference,
                                            Toast.LENGTH_LONG
                                    ).show();

                                    android.content.Intent intent = new android.content.Intent(
                                            VerifyAdmissionActivity.this,
                                            MainActivity.class
                                    );

                                    intent.setFlags(
                                            android.content.Intent.FLAG_ACTIVITY_NEW_TASK |
                                                    android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    );

                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(
                                            VerifyAdmissionActivity.this,
                                            apiResponse.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                            } else {
                                String errorMessage = "Admission submission failed";

                                try {
                                    if (response.errorBody() != null) {
                                        errorMessage = response.errorBody().string();
                                    }
                                } catch (Exception e) {
                                    errorMessage = e.getMessage();
                                }

                                Toast.makeText(
                                        VerifyAdmissionActivity.this,
                                        errorMessage,
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(
                                retrofit2.Call<ApiEnvelope<AdmissionSubmitData>> call,
                                Throwable t
                        ) {
                            Toast.makeText(
                                    VerifyAdmissionActivity.this,
                                    "Submission failed: " + t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private String convertDateToBackendFormat(String date) {
        if (date == null || date.trim().isEmpty()) {
            return "";
        }

        try {
            String[] parts = date.split("/");

            if (parts.length == 3) {
                String month = parts[0];
                String day = parts[1];
                String year = parts[2];

                return year + "-" + month + "-" + day;
            }

        } catch (Exception e) {
            return date;
        }

        return date;
    }
}