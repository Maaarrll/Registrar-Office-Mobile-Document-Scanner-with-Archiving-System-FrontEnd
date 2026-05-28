package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class LinkDocumentRequest {

    private String student_id;
    private String document_type;
    private String file_name;
    private boolean is_urgent;

    public LinkDocumentRequest(
            String student_id,
            String document_type,
            String file_name,
            boolean is_urgent
    ) {
        this.student_id = student_id;
        this.document_type = document_type;
        this.file_name = file_name;
        this.is_urgent = is_urgent;
    }
}