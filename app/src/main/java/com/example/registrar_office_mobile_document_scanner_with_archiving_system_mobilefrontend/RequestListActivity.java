package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RequestListActivity extends AppCompatActivity {

    private RecyclerView recyclerRequests;
    private RequestAdapter adapter;
    private TextView tvEmptyState;

    private TextView tvPendingCount;
    private TextView tvUrgentCount;
    private TextView tvDoneCount;

    private final List<RequestModel> requestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        recyclerRequests = findViewById(R.id.recyclerRequests);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvUrgentCount = findViewById(R.id.tvUrgentCount);
        tvDoneCount = findViewById(R.id.tvDoneCount);

        EditText etSearchRequest = findViewById(R.id.etSearchRequest);

        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RequestAdapter(requestList);
        recyclerRequests.setAdapter(adapter);

        etSearchRequest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s, filterCount -> {
                    if (filterCount == 0) {
                        tvEmptyState.setVisibility(View.VISIBLE);
                        recyclerRequests.setVisibility(View.GONE);
                    } else {
                        tvEmptyState.setVisibility(View.GONE);
                        recyclerRequests.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loadRequestsFromBackend();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadRequestsFromBackend() {
        SessionManager sessionManager =
                new SessionManager(RequestListActivity.this);

        String token = sessionManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(
                    this,
                    "No staff token found. Please login again.",
                    Toast.LENGTH_SHORT
            ).show();

            showEmptyState();
            return;
        }

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        apiService.getRequests("Bearer " + token)
                .enqueue(new retrofit2.Callback<ApiEnvelope<List<DocumentRequestModel>>>() {
                    @Override
                    public void onResponse(
                            retrofit2.Call<ApiEnvelope<List<DocumentRequestModel>>> call,
                            retrofit2.Response<ApiEnvelope<List<DocumentRequestModel>>> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {

                            ApiEnvelope<List<DocumentRequestModel>> apiResponse =
                                    response.body();

                            if ("success".equals(apiResponse.getStatus())
                                    && apiResponse.getData() != null) {

                                convertBackendRequests(apiResponse.getData());

                            } else {
                                Toast.makeText(
                                        RequestListActivity.this,
                                        apiResponse.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();

                                showEmptyState();
                            }

                        } else {
                            String errorMessage = "Failed to load requests";

                            try {
                                if (response.errorBody() != null) {
                                    errorMessage = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                errorMessage = e.getMessage();
                            }

                            Toast.makeText(
                                    RequestListActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();

                            showEmptyState();
                        }
                    }

                    @Override
                    public void onFailure(
                            retrofit2.Call<ApiEnvelope<List<DocumentRequestModel>>> call,
                            Throwable t
                    ) {
                        Toast.makeText(
                                RequestListActivity.this,
                                "Failed to load requests: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                        showEmptyState();
                    }
                });
    }

    private void convertBackendRequests(List<DocumentRequestModel> backendRequests) {
        requestList.clear();

        if (backendRequests.isEmpty()) {
            showEmptyState();
            updateCounters(requestList);
            adapter.notifyDataSetChanged();
            return;
        }

        requestList.add(new RequestModel("Backend Requests"));

        for (DocumentRequestModel backendRequest : backendRequests) {
            requestList.add(
                    new RequestModel(
                            backendRequest.getStudentName(),
                            backendRequest.getRequestType(),
                            backendRequest.getStudentId(),
                            backendRequest.getStatus(),
                            backendRequest.getRequestTime()
                    )
            );
        }

        tvEmptyState.setVisibility(View.GONE);
        recyclerRequests.setVisibility(View.VISIBLE);

        adapter = new RequestAdapter(requestList);
        recyclerRequests.setAdapter(adapter);

        updateCounters(requestList);
    }

    private void showEmptyState() {
        tvEmptyState.setVisibility(View.VISIBLE);
        recyclerRequests.setVisibility(View.GONE);
    }

    private void updateCounters(List<RequestModel> requestList) {
        int pending = 0;
        int urgent = 0;
        int done = 0;

        for (RequestModel request : requestList) {
            if (request.getType() == RequestModel.TYPE_REQUEST) {
                if ("PENDING".equals(request.getStatus())) {
                    pending++;
                } else if ("URGENT".equals(request.getStatus())) {
                    urgent++;
                } else if ("DONE".equals(request.getStatus())) {
                    done++;
                }
            }
        }

        tvPendingCount.setText(String.valueOf(pending));
        tvUrgentCount.setText(String.valueOf(urgent));
        tvDoneCount.setText(String.valueOf(done));
    }
}