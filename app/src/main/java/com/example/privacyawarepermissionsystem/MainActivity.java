package com.example.privacyawarepermissionsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView txtResults;
    private TextView txtScanStatus;
    private LinearProgressIndicator progressScan;
    private MaterialButton btnScan;

    private DatabaseHelper databaseHelper;
    private final ExecutorService scanExecutor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResults = findViewById(R.id.txtResults);
        txtScanStatus = findViewById(R.id.txtScanStatus);
        progressScan = findViewById(R.id.progressScan);

        btnScan = findViewById(R.id.btnScan);
        MaterialButton btnHistory = findViewById(R.id.btnHistory);
        MaterialButton btnDashboard = findViewById(R.id.btnDashboard);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        btnScan.setOnClickListener(view -> scanInstalledApplications());

        btnHistory.setOnClickListener(view ->
                startActivity(new Intent(
                        MainActivity.this,
                        HistoryActivity.class)));

        btnDashboard.setOnClickListener(view ->
                startActivity(new Intent(
                        MainActivity.this,
                        DashboardActivity.class)));
    }

    /**
     * Runs package scanning and database replacement outside the UI thread.
     * This prevents the screen from freezing on devices with many apps.
     */
    private void scanInstalledApplications() {
        setScanningState(true);

        scanExecutor.execute(() -> {
            try {
                AppScanner scanner =
                        new AppScanner(getApplicationContext());

                List<AppInfo> applications =
                        scanner.scanInstalledApps();

                databaseHelper.replaceAllApps(applications);

                String resultText =
                        buildScanResult(applications);

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    txtResults.setText(resultText);
                    txtScanStatus.setText(
                            "Last scan completed. Results were saved locally.");
                    setScanningState(false);
                });
            } catch (Exception exception) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    txtResults.setText(
                            "The scan could not be completed.\n\n" +
                            "Reason: " +
                            exception.getClass().getSimpleName());
                    txtScanStatus.setText(
                            "Scan failed. Try again after restarting the app.");
                    setScanningState(false);
                });
            }
        });
    }

    private void setScanningState(boolean scanning) {
        btnScan.setEnabled(!scanning);

        if (scanning) {
            progressScan.setIndeterminate(true);
            progressScan.setVisibility(View.VISIBLE);
            txtScanStatus.setText(
                    "Scanning launchable applications...");
            txtResults.setText(
                    "Analyzing permissions and calculating privacy scores.");
        } else {
            progressScan.setVisibility(View.GONE);
        }
    }

    private String buildScanResult(List<AppInfo> applications) {
        int highRisk = 0;
        int mediumRisk = 0;
        int lowRisk = 0;

        StringBuilder preview = new StringBuilder();

        for (int i = 0; i < applications.size(); i++) {
            AppInfo app = applications.get(i);

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
                        .append("Sensitive permission categories: ")
                        .append(
                                PrivacyRiskAnalyzer
                                        .countSensitivePermissions(
                                                app.getPermissions()))
                        .append("\n\n");
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("SCAN COMPLETE\n\n")
                .append("Launchable applications analyzed: ")
                .append(applications.size())
                .append("\n")
                .append("High risk: ")
                .append(highRisk)
                .append("\n")
                .append("Medium risk: ")
                .append(mediumRisk)
                .append("\n")
                .append("Low risk: ")
                .append(lowRisk)
                .append("\n\n");

        if (applications.isEmpty()) {
            result.append(
                    "No launchable applications were available for analysis.");
        } else {
            result.append("APPLICATION PREVIEW\n\n")
                    .append(preview);

            if (applications.size() > 8) {
                result.append(
                        "Open Scan History to review all applications.");
            }
        }

        return result.toString();
    }

    @Override
    protected void onDestroy() {
        scanExecutor.shutdown();
        super.onDestroy();
    }
}
