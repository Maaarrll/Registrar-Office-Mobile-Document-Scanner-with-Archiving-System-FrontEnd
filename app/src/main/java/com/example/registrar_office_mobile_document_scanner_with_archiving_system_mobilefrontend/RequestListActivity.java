package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class RequestListActivity extends AppCompatActivity {

    RecyclerView recyclerRequests;
    RequestAdapter adapter;
    TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        recyclerRequests = findViewById(R.id.recyclerRequests);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        EditText etSearchRequest = findViewById(R.id.etSearchRequest);

        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));

        List<RequestModel> requestList = new ArrayList<>();

        requestList.add(new RequestModel("Today"));
        requestList.add(new RequestModel("Juan Dela Cruz", "Certificate of Registration (COR)", "234418", "PENDING", "9:47 AM"));
        requestList.add(new RequestModel("Maria Santos", "Transcript of Records (TOR)", "202455", "URGENT", "10:15 AM"));

        requestList.add(new RequestModel("Yesterday"));
        requestList.add(new RequestModel("John Reyes", "Good Moral Certificate", "202199", "PENDING", "3:20 PM"));

        requestList.add(new RequestModel("May 22, 2026"));
        requestList.add(new RequestModel("Ana Cruz", "Diploma", "201987", "DONE", "8:30 AM"));

        updateCounters(requestList);

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

        findViewById(R.id.btnLogout).setOnClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        SessionManager sessionManager =
                                new SessionManager(RequestListActivity.this);

                        sessionManager.logout();

                        Intent intent = new Intent(
                                RequestListActivity.this,
                                MainActivity.class
                        );

                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );

                        startActivity(intent);
                        finish();
                    })

                    .setNegativeButton("Cancel", null)
                    .show();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void updateCounters(List<RequestModel> requestList) {
        TextView tvPendingCount = findViewById(R.id.tvPendingCount);
        TextView tvUrgentCount = findViewById(R.id.tvUrgentCount);
        TextView tvDoneCount = findViewById(R.id.tvDoneCount);

        int pending = 0;
        int urgent = 0;
        int done = 0;

        for (RequestModel request : requestList) {
            if (request.getType() == RequestModel.TYPE_REQUEST) {
                if (request.getStatus().equals("PENDING")) {
                    pending++;
                } else if (request.getStatus().equals("URGENT")) {
                    urgent++;
                } else if (request.getStatus().equals("DONE")) {
                    done++;
                }
            }
        }

        tvPendingCount.setText(String.valueOf(pending));
        tvUrgentCount.setText(String.valueOf(urgent));
        tvDoneCount.setText(String.valueOf(done));
    }
}