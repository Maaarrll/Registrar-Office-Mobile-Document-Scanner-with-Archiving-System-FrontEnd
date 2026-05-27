package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class StaffLoginRequest {

    private String staff_id;
    private String password;

    public StaffLoginRequest(String staff_id, String password) {
        this.staff_id = staff_id;
        this.password = password;
    }
}