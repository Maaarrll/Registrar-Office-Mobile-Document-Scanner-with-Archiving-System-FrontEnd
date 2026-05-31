package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class DocumentRequestModel {

    private String student_id;
    private String student_name;
    private String full_name;
    private String request_type;
    private String document_type;
    private String status;
    private boolean is_urgent;
    private String created_at;
    private String request_time;

    public String getStudentId() {
        return student_id;
    }

    public String getStudentName() {
        if (student_name != null && !student_name.isEmpty()) {
            return student_name;
        }

        if (full_name != null && !full_name.isEmpty()) {
            return full_name;
        }

        return "Unknown Student";
    }

    public String getRequestType() {
        if (request_type != null && !request_type.isEmpty()) {
            return request_type;
        }

        if (document_type != null && !document_type.isEmpty()) {
            return document_type;
        }

        return "Document Request";
    }

    public String getStatus() {
        if (is_urgent) {
            return "URGENT";
        }

        if (status != null && !status.isEmpty()) {
            return status.toUpperCase();
        }

        return "PENDING";
    }

    public String getRequestTime() {
        if (request_time != null && !request_time.isEmpty()) {
            return request_time;
        }

        if (created_at != null && !created_at.isEmpty()) {
            return created_at;
        }

        return "N/A";
    }
}