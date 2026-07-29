package com.example.privacyawarepermissionsystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtTotalApps;
    private TextView txtAverageScore;
    private TextView txtHighRisk;
    private TextView txtMediumRisk;
    private TextView txtLowRisk;
    private TextView txtHighestApp;
    private TextView txtLowestApp;
    private TextView txtOverallStatus;
    private TextView txtRecommendation;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        databaseHelper = new DatabaseHelper(this);

        txtTotalApps = findViewById(R.id.txtTotalApps);
        txtAverageScore = findViewById(R.id.txtAverageScore);
        txtHighRisk = findViewById(R.id.txtHighRisk);
        txtMediumRisk = findViewById(R.id.txtMediumRisk);
        txtLowRisk = findViewById(R.id.txtLowRisk);
        txtHighestApp = findViewById(R.id.txtHighestApp);
        txtLowestApp = findViewById(R.id.txtLowestApp);
        txtOverallStatus = findViewById(R.id.txtOverallStatus);
        txtRecommendation = findViewById(R.id.txtRecommendation);

        loadDashboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboard();
    }

    private void loadDashboard() {
        int totalApps = databaseHelper.getTotalApps();
        int averageScore = databaseHelper.getAveragePrivacyScore();
        int highRisk = databaseHelper.getHighRiskCount();
        int mediumRisk = databaseHelper.getMediumRiskCount();
        int lowRisk = databaseHelper.getLowRiskCount();

        txtTotalApps.setText(String.valueOf(totalApps));
        txtAverageScore.setText(averageScore + " / 100");
        txtHighRisk.setText(String.valueOf(highRisk));
        txtMediumRisk.setText(String.valueOf(mediumRisk));
        txtLowRisk.setText(String.valueOf(lowRisk));
        txtHighestApp.setText(databaseHelper.getHighestPrivacyScoreApp());
        txtLowestApp.setText(databaseHelper.getLowestPrivacyScoreApp());

        String status = databaseHelper.getOverallStatus();
        txtOverallStatus.setText(status.toUpperCase());
        txtRecommendation.setText(databaseHelper.getRecommendation());
        applyStatusStyle(status);
    }

    private void applyStatusStyle(String status) {
        if ("High".equalsIgnoreCase(status)) {
            txtOverallStatus.setBackgroundResource(R.drawable.bg_risk_high);
            txtOverallStatus.setTextColor(ContextCompat.getColor(this, R.color.risk_high));
        } else if ("Medium".equalsIgnoreCase(status)) {
            txtOverallStatus.setBackgroundResource(R.drawable.bg_risk_medium);
            txtOverallStatus.setTextColor(ContextCompat.getColor(this, R.color.risk_medium));
        } else {
            txtOverallStatus.setBackgroundResource(R.drawable.bg_risk_low);
            txtOverallStatus.setTextColor(ContextCompat.getColor(this, R.color.risk_low));
        }
    }
}
