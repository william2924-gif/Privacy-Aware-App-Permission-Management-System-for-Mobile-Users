package com.example.privacyawarepermissionsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends
        RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<AppInfo> appList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AppInfo app);
    }

    public HistoryAdapter(
            List<AppInfo> appList,
            OnItemClickListener listener) {

        this.appList = appList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.history_item,
                                parent,
                                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        AppInfo app =
                appList.get(position);

        holder.txtAppName.setText(
                app.getAppName());

        holder.txtPrivacyScore.setText(
                "Privacy Score: " +
                        app.getPrivacyScore() +
                        " / 100");

        holder.txtPermissionCount.setText(
                "Sensitive permission categories: " +
                        PrivacyRiskAnalyzer
                                .countSensitivePermissions(
                                        app.getPermissions()));

        String risk =
                app.getRiskLevel();

        holder.txtRisk.setText(
                risk.toUpperCase(Locale.ROOT));

        applyRiskStyle(
                holder,
                risk);

        holder.itemView.setOnClickListener(
                view ->
                        listener.onItemClick(app));
    }

    private void applyRiskStyle(
            ViewHolder holder,
            String risk) {

        if ("High".equalsIgnoreCase(risk)) {
            holder.txtRisk.setBackgroundResource(
                    R.drawable.bg_risk_high);
            holder.txtRisk.setTextColor(
                    ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.risk_high));

        } else if ("Medium"
                .equalsIgnoreCase(risk)) {

            holder.txtRisk.setBackgroundResource(
                    R.drawable.bg_risk_medium);
            holder.txtRisk.setTextColor(
                    ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.risk_medium));

        } else {
            holder.txtRisk.setBackgroundResource(
                    R.drawable.bg_risk_low);
            holder.txtRisk.setTextColor(
                    ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.risk_low));
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        final TextView txtAppName;
        final TextView txtPrivacyScore;
        final TextView txtRisk;
        final TextView txtPermissionCount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAppName =
                    itemView.findViewById(
                            R.id.txtAppName);

            txtPrivacyScore =
                    itemView.findViewById(
                            R.id.txtPrivacyScore);

            txtRisk =
                    itemView.findViewById(
                            R.id.txtRisk);

            txtPermissionCount =
                    itemView.findViewById(
                            R.id.txtPermissionCount);
        }
    }
}
