package com.example.privacyawarepermissionsystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    /**
     * Load all dashboard statistics from the database.
     */
    /**
     * Loads dashboard statistics from the SQLite database.
     */
    private void loadDashboard() {

        int totalApps = databaseHelper.getTotalApps();

        int averageScore = databaseHelper.getAveragePrivacyScore();

        int highRisk = databaseHelper.getHighRiskCount();

        int mediumRisk = databaseHelper.getMediumRiskCount();

        int lowRisk = databaseHelper.getLowRiskCount();

        String highestApp = databaseHelper.getHighestPrivacyScoreApp();

        String lowestApp = databaseHelper.getLowestPrivacyScoreApp();

        String overallStatus = databaseHelper.getOverallStatus();

        String recommendation = databaseHelper.getRecommendation();

        txtTotalApps.setText(String.valueOf(totalApps));

        txtAverageScore.setText(
                averageScore + " / 100"
        );

        txtHighRisk.setText(
                String.valueOf(highRisk)
        );

        txtMediumRisk.setText(
                String.valueOf(mediumRisk)
        );

        txtLowRisk.setText(
                String.valueOf(lowRisk)
        );

        txtHighestApp.setText(
                highestApp
        );

        txtLowestApp.setText(
                lowestApp
        );

        txtOverallStatus.setText(
                overallStatus
        );

        txtRecommendation.setText(
                recommendation
        );

    }

}