package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class UploadResponse {

    private boolean success;
    private String message;
    private String file_url;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getFile_url() {
        return file_url;
    }
}