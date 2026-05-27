package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class ApiEnvelope<T> {

    private String status;
    private T data;
    private String message;

    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}