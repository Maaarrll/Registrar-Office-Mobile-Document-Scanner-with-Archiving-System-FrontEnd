package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

public class RequestModel {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_REQUEST = 1;

    int type;
    String headerTitle;

    String studentName;
    String requestType;
    String studentId;
    String status;
    String requestTime;

    public RequestModel(String headerTitle) {
        this.type = TYPE_HEADER;
        this.headerTitle = headerTitle;
    }

    public RequestModel(String studentName, String requestType, String studentId, String status, String requestTime) {
        this.type = TYPE_REQUEST;
        this.studentName = studentName;
        this.requestType = requestType;
        this.studentId = studentId;
        this.status = status;
        this.requestTime = requestTime;
    }

    public int getType() { return type; }
    public String getHeaderTitle() { return headerTitle; }
    public String getStudentName() { return studentName; }
    public String getRequestType() { return requestType; }
    public String getStudentId() { return studentId; }
    public String getStatus() { return status; }
    public String getRequestTime() { return requestTime; }
}