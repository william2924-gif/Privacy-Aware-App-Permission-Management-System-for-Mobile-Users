package com.example.privacyawarepermissionsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtResults;
    private TextView txtScanStatus;
    private LinearProgressIndicator progressScan;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResults = findViewById(R.id.txtResults);
        txtScanStatus = findViewById(R.id.txtScanStatus);
        progressScan = findViewById(R.id.progressScan);

        Button btnScan = findViewById(R.id.btnScan);
        Button btnHistory = findViewById(R.id.btnHistory);
        Button btnDashboard = findViewById(R.id.btnDashboard);

        databaseHelper = new DatabaseHelper(this);

        btnScan.setOnClickListener(view -> scanInstalledApplications());

        btnHistory.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        btnDashboard.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, DashboardActivity.class)));
    }

    private void scanInstalledApplications() {
        progressScan.setVisibility(View.VISIBLE);
        txtScanStatus.setText("Scanning installed applications...");
        txtResults.setText("Analyzing permissions and calculating privacy scores.");

        AppScanner scanner = new AppScanner(this);
        List<AppInfo> applications = scanner.scanInstalledApps();

        databaseHelper.clearDatabase();

        int highRisk = 0;
        int mediumRisk = 0;
        int lowRisk = 0;

        StringBuilder preview = new StringBuilder();

        for (int i = 0; i < applications.size(); i++) {
            AppInfo app = applications.get(i);
            databaseHelper.insertApp(app);

            if ("High".equalsIgnoreCase(app.getRiskLevel())) {
                highRisk++;
            } else if ("Medium".equalsIgnoreCase(app.getRiskLevel())) {
                mediumRisk++;
            } else {
                lowRisk++;
            }

            if (i < 8) {
                preview.append(app.getAppName())
                        .append("\n")
                        .append("Score: ")
                        .append(app.getPrivacyScore())
                        .append(" / 100  ·  ")
                        .append(app.getRiskLevel())
                        .append(" risk\n")
                        .append("Sensitive permissions: ")
                        .append(PrivacyRiskAnalyzer.countSensitivePermissions(app.getPermissions()))
                        .append("\n\n");
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("SCAN COMPLETE\n\n")
                .append("Applications analyzed: ").append(applications.size()).append("\n")
                .append("High risk: ").append(highRisk).append("\n")
                .append("Medium risk: ").append(mediumRisk).append("\n")
                .append("Low risk: ").append(lowRisk).append("\n\n");

        if (applications.isEmpty()) {
            result.append("No launchable applications were found.");
        } else {
            result.append("APPLICATION PREVIEW\n\n").append(preview);
            if (applications.size() > 8) {
                result.append("Open Scan History to review all applications.");
            }
        }

        progressScan.setVisibility(View.GONE);
        txtScanStatus.setText("Last scan completed. Results were saved locally.");
        txtResults.setText(result.toString());
    }
}
