package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    private List<RequestModel> requestList;
    private List<RequestModel> fullRequestList;

    public RequestAdapter(List<RequestModel> requestList) {
        this.requestList = new ArrayList<>(requestList);
        this.fullRequestList = new ArrayList<>(requestList);
    }

    @Override
    public int getItemViewType(int position) {
        return requestList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        if (viewType == RequestModel.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_request_header, parent, false);

            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_request, parent, false);

            return new RequestViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position
    ) {
        RequestModel request = requestList.get(position);

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvHeaderTitle.setText(request.getHeaderTitle());
        }

        if (holder instanceof RequestViewHolder) {
            RequestViewHolder requestHolder = (RequestViewHolder) holder;

            requestHolder.tvStudentName.setText(request.getStudentName());
            requestHolder.tvRequestType.setText(request.getRequestType());
            requestHolder.tvStudentId.setText("Student ID: " + request.getStudentId());
            requestHolder.tvRequestTime.setText("Time: " + request.getRequestTime());
            requestHolder.tvStatus.setText(request.getStatus());

            requestHolder.tvStatus.setBackgroundResource(R.drawable.status_badge);

            if (request.getStatus().equals("URGENT")) {
                requestHolder.tvStatus.getBackground().setTint(
                        requestHolder.itemView.getContext().getColor(R.color.red)
                );
            } else if (request.getStatus().equals("DONE")) {
                requestHolder.tvStatus.getBackground().setTint(
                        requestHolder.itemView.getContext().getColor(R.color.green)
                );
            } else {
                requestHolder.tvStatus.getBackground().setTint(
                        requestHolder.itemView.getContext().getColor(R.color.yellow)
                );
            }

            requestHolder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(
                        requestHolder.itemView.getContext(),
                        RequestDetailsActivity.class
                );

                intent.putExtra("student_name", request.getStudentName());
                intent.putExtra("student_id", request.getStudentId());
                intent.putExtra("request_type", request.getRequestType());
                intent.putExtra("status", request.getStatus());
                intent.putExtra("request_time", request.getRequestTime());

                requestHolder.itemView.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    @Override
    public Filter getFilter() {
        return requestFilter;
    }

    private final Filter requestFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RequestModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.toString().trim().isEmpty()) {
                filteredList.addAll(fullRequestList);
            } else {
                String filterText = constraint.toString().toLowerCase().trim();

                for (RequestModel request : fullRequestList) {
                    if (request.getType() == RequestModel.TYPE_REQUEST) {
                        if (request.getStudentName().toLowerCase().contains(filterText)
                                || request.getStudentId().toLowerCase().contains(filterText)
                                || request.getRequestType().toLowerCase().contains(filterText)
                                || request.getStatus().toLowerCase().contains(filterText)) {

                            filteredList.add(request);
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            requestList.clear();
            requestList.addAll((List<RequestModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeaderTitle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeaderTitle = itemView.findViewById(R.id.tvHeaderTitle);
        }
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView tvStudentName;
        TextView tvRequestType;
        TextView tvStudentId;
        TextView tvStatus;
        TextView tvRequestTime;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvRequestType = itemView.findViewById(R.id.tvRequestType);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvRequestTime = itemView.findViewById(R.id.tvRequestTime);
        }
    }
}