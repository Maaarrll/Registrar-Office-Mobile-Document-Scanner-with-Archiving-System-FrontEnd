package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class AdmissionSubmitRequest {

    private String first_name;
    private String middle_name;
    private String last_name;
    private String birth_date;
    private String place_birth;
    private String gender;
    private String home_address;
    private String email;
    private String school;
    private String year_graduated;

    public AdmissionSubmitRequest(
            String first_name,
            String middle_name,
            String last_name,
            String birth_date,
            String place_birth,
            String gender,
            String home_address,
            String email,
            String school,
            String year_graduated
    ) {
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.place_birth = place_birth;
        this.gender = gender;
        this.home_address = home_address;
        this.email = email;
        this.school = school;
        this.year_graduated = year_graduated;
    }
}