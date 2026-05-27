package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class StudentLoginRequest {

    private String student_id;
    private String password;

    public StudentLoginRequest(String student_id, String password) {
        this.student_id = student_id;
        this.password = password;
    }
}