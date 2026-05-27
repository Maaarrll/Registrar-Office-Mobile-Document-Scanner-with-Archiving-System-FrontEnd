package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class LoginResponse {

    private String status;
    private LoginData data;
    private String message;

    public String getStatus() {
        return status;
    }

    public LoginData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class LoginData {

        private String access_token;
        private String token_type;
        private String role;

        public String getAccess_token() {
            return access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public String getRole() {
            return role;
        }

        // This keeps your old StaffLoginActivity code compatible
        public String getToken() {
            return access_token;
        }
    }
}