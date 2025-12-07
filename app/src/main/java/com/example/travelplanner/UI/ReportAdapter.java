package com.example.travelplanner.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.R;
import com.example.travelplanner.entities.ReportItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private final LayoutInflater mInflater;
    private List<ReportItem> mItems = new ArrayList<>();
    private List<ReportItem> mAllItems = new ArrayList<>();

    public ReportAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleView;
        private final TextView primaryView;
        private final TextView secondaryView;
        private final TextView typeView;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.textViewReportTitle);
            primaryView = itemView.findViewById(R.id.textViewReportPrimary);
            secondaryView = itemView.findViewById(R.id.textViewReportSecondary);
            typeView = itemView.findViewById(R.id.textViewReportType);
        }
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.report_list_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        if (mItems != null && !mItems.isEmpty()) {
            ReportItem current = mItems.get(position);
            holder.titleView.setText(current.getTitle());
            holder.primaryView.setText(current.getPrimaryInfo());
            holder.secondaryView.setText(current.getSecondaryInfo());
            holder.typeView.setText(current.getItemType());
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public void setItems(List<ReportItem> items) {
        if (items == null) {
            mItems = new ArrayList<>();
            mAllItems = new ArrayList<>();
        } else {
            mItems = new ArrayList<>(items);
            mAllItems = new ArrayList<>(items);
        }
        notifyDataSetChanged();
    }

    public void filterByVacationName(String query) {
        if (query == null || query.trim().isEmpty()) {
            // Reset to full list
            mItems = new ArrayList<>(mAllItems);
        } else {
            String lower = query.toLowerCase(Locale.US);
            List<ReportItem> filtered = new ArrayList<>();
            for (ReportItem item : mAllItems) {
                if (item.getTitle().toLowerCase(Locale.US).contains(lower)) {
                    filtered.add(item);
                }
            }
            mItems = filtered;
        }
        notifyDataSetChanged();
    }
}
