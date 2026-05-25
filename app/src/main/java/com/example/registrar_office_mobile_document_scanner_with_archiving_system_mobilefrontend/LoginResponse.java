package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class LoginResponse {

    private boolean success;
    private String message;
    private String token;
    private String role;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
}