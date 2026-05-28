package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import java.util.List;

public class RequestSubmitRequest {

    private String student_id;
    private boolean is_urgent;
    private List<RequestItem> items;

    public RequestSubmitRequest(
            String student_id,
            boolean is_urgent,
            List<RequestItem> items
    ) {
        this.student_id = student_id;
        this.is_urgent = is_urgent;
        this.items = items;
    }

    public static class RequestItem {

        private String document_type;
        private int quantity;

        public RequestItem(
                String document_type,
                int quantity
        ) {
            this.document_type = document_type;
            this.quantity = quantity;
        }
    }
}