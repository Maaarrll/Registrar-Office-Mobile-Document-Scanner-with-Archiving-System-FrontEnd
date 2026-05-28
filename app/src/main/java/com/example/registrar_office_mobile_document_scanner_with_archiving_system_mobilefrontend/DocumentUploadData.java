package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class DocumentUploadData {

    private int document_id;
    private String status;
    private boolean processing_queued;

    public int getDocument_id() {
        return document_id;
    }

    public String getStatus() {
        return status;
    }

    public boolean isProcessing_queued() {
        return processing_queued;
    }
}