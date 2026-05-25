package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class DocumentRequestModel {

    private int request_id;
    private String student_name;
    private String student_id;
    private String request_type;
    private String status;
    private String request_time;

    public int getRequest_id() {
        return request_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public String getStatus() {
        return status;
    }

    public String getRequest_time() {
        return request_time;
    }
}