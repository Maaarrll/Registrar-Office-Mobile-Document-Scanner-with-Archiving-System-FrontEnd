package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Header;

public interface ApiService {

    // Staff Login
    @POST("staff/login")
    Call<LoginResponse> staffLogin(
            @Body StaffLoginRequest request
    );

    // Student Login
    @POST("student/login")
    Call<LoginResponse> studentLogin(
            @Body StudentLoginRequest request
    );

    // Get Requests
    @GET("requests")
    Call<List<DocumentRequestModel>> getRequests();

    // Get Student by ID
    @GET("students/{student_id}")
    Call<StudentModel> getStudent(
            @Path("student_id") String studentId
    );

    // Upload Document
    @Multipart
    @POST("documents/upload")
    Call<ApiEnvelope<DocumentUploadData>> uploadDocument(
            @Header("Authorization") String bearerToken,
            @Header("X-Content-SHA256") String sha256,
            @Part MultipartBody.Part document
    );

    @POST("documents/{document_id}/link")
    Call<ApiEnvelope<Object>> linkDocumentToStudent(
            @Header("Authorization") String bearerToken,
            @Path("document_id") int documentId,
            @Body LinkDocumentRequest request
    );
    @POST("admission/submit")
    Call<ApiEnvelope<AdmissionSubmitData>> submitAdmission(
            @Body AdmissionSubmitRequest request
    );
    @POST("requests")
    Call<ApiEnvelope<Object>> submitRequest(
            @Header("Authorization") String bearerToken,
            @Body RequestSubmitRequest request
    );
}