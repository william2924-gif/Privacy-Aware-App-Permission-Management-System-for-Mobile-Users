package com.example.privacyawarepermissionsystem;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<AppInfo> appList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AppInfo app);
    }

    public HistoryAdapter(List<AppInfo> appList, OnItemClickListener listener) {
        this.appList = appList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfo app = appList.get(position);

        holder.txtAppName.setText(app.getAppName());
        holder.txtPrivacyScore.setText("Privacy Score: " + app.getPrivacyScore() + " / 100");
        holder.txtPermissionCount.setText(
                "Sensitive permissions: " +
                        PrivacyRiskAnalyzer.countSensitivePermissions(app.getPermissions()));

        String risk = app.getRiskLevel();
        holder.txtRisk.setText(risk.toUpperCase());

        if ("High".equalsIgnoreCase(risk)) {
            holder.txtRisk.setBackgroundResource(R.drawable.bg_risk_high);
            holder.txtRisk.setTextColor(ContextCompat.getColor(
                    holder.itemView.getContext(), R.color.risk_high));
        } else if ("Medium".equalsIgnoreCase(risk)) {
            holder.txtRisk.setBackgroundResource(R.drawable.bg_risk_medium);
            holder.txtRisk.setTextColor(ContextCompat.getColor(
                    holder.itemView.getContext(), R.color.risk_medium));
        } else {
            holder.txtRisk.setBackgroundResource(R.drawable.bg_risk_low);
            holder.txtRisk.setTextColor(ContextCompat.getColor(
                    holder.itemView.getContext(), R.color.risk_low));
        }

        holder.itemView.setOnClickListener(view -> listener.onItemClick(app));
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAppName;
        TextView txtPrivacyScore;
        TextView txtRisk;
        TextView txtPermissionCount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAppName = itemView.findViewById(R.id.txtAppName);
            txtPrivacyScore = itemView.findViewById(R.id.txtPrivacyScore);
            txtRisk = itemView.findViewById(R.id.txtRisk);
            txtPermissionCount = itemView.findViewById(R.id.txtPermissionCount);
        }
    }
}
