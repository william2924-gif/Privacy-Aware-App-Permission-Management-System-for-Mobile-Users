package com.example.privacyawarepermissionsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
 * Adapter used to display scanned applications
 * inside the RecyclerView.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<AppInfo> appList;

    public HistoryAdapter(List<AppInfo> appList) {
        this.appList = appList;
    }

    /**
     * Inflate one row layout.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);

        return new ViewHolder(view);

    }

    /**
     * Bind AppInfo data to one row.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppInfo app = appList.get(position);

        holder.txtAppName.setText(app.getAppName());

        holder.txtRisk.setText("Risk: " + app.getRiskLevel());

        holder.txtPermissionCount.setText(
                "Permissions: " + app.getPermissionCount());

    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    /**
     * ViewHolder stores references
     * to all controls inside one row.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAppName;
        TextView txtRisk;
        TextView txtPermissionCount;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtAppName = itemView.findViewById(R.id.txtAppName);

            txtRisk = itemView.findViewById(R.id.txtRisk);

            txtPermissionCount = itemView.findViewById(R.id.txtPermissionCount);

        }

    }

}