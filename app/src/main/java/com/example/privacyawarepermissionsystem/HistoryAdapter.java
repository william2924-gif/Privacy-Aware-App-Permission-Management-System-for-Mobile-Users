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

    /**
     * Listener used to handle item click events.
     */
    public interface OnItemClickListener {
        void onItemClick(AppInfo app);
    }

    private final OnItemClickListener listener;

    public HistoryAdapter(List<AppInfo> appList,
                          OnItemClickListener listener) {

        this.appList = appList;
        this.listener = listener;

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

        holder.txtPrivacyScore.setText(
                "Privacy Score: " + app.getPrivacyScore() + " / 100");

        holder.txtRisk.setText(
                "Risk Level: " + app.getRiskLevel());

        holder.txtPermissionCount.setText(
                "Sensitive Permissions: " +
                        PrivacyRiskAnalyzer.countSensitivePermissions(app.getPermissions()));

        holder.itemView.setOnClickListener(view -> {

            listener.onItemClick(app);

        });

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
        TextView txtPrivacyScore;
        TextView txtRisk;
        TextView txtPermissionCount;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtAppName = itemView.findViewById(R.id.txtAppName);

            txtPrivacyScore = itemView.findViewById(R.id.txtPrivacyScore);

            txtRisk = itemView.findViewById(R.id.txtRisk);

            txtPermissionCount = itemView.findViewById(R.id.txtPermissionCount);

        }

    }

}