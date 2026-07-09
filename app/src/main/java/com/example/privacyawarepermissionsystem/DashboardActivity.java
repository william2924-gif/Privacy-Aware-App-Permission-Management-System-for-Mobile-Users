package com.example.privacyawarepermissionsystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtTotalApps;
    private TextView txtHighRisk;
    private TextView txtMediumRisk;
    private TextView txtLowRisk;
    private TextView txtOverallStatus;
    private TextView txtRecommendation;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        databaseHelper = new DatabaseHelper(this);

        txtTotalApps = findViewById(R.id.txtTotalApps);
        txtHighRisk = findViewById(R.id.txtHighRisk);
        txtMediumRisk = findViewById(R.id.txtMediumRisk);
        txtLowRisk = findViewById(R.id.txtLowRisk);
        txtOverallStatus = findViewById(R.id.txtOverallStatus);
        txtRecommendation = findViewById(R.id.txtRecommendation);

        loadDashboard();

    }

    /**
     * Load all dashboard statistics from the database.
     */
    /**
     * Loads dashboard statistics from the SQLite database.
     */
    private void loadDashboard() {

        int totalApps = databaseHelper.getTotalApps();

        int highRisk = databaseHelper.getHighRiskCount();

        int mediumRisk = databaseHelper.getMediumRiskCount();

        int lowRisk = databaseHelper.getLowRiskCount();

        String overallStatus = databaseHelper.getOverallStatus();

        String recommendation = databaseHelper.getRecommendation();

        txtTotalApps.setText(
                "Total Applications\n\n" + totalApps
        );

        txtHighRisk.setText(
                "High Risk Applications\n\n" + highRisk
        );

        txtMediumRisk.setText(
                "Medium Risk Applications\n\n" + mediumRisk
        );

        txtLowRisk.setText(
                "Low Risk Applications\n\n" + lowRisk
        );

        txtOverallStatus.setText(
                "Overall Privacy Status\n\n" + overallStatus
        );

        txtRecommendation.setText(
                "Recommendation\n\n" + recommendation
        );

    }

}