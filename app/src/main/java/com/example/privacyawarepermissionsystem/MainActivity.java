package com.example.privacyawarepermissionsystem;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtResults;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        txtResults = findViewById(R.id.txtResults);

        Button btnScan = findViewById(R.id.btnScan);

        Button btnHistory = findViewById(R.id.btnHistory);

        Button btnDashboard = findViewById(R.id.btnDashboard);

        databaseHelper = new DatabaseHelper(this);

        btnScan.setOnClickListener(view -> scanInstalledApplications());

        btnHistory.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    HistoryActivity.class
            );

            startActivity(intent);

        });

        btnDashboard.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    DashboardActivity.class
            );

            startActivity(intent);

        });

    }

    /**
     * Scan all installed applications.
     * Save every application into SQLite.
     * Display scan results.
     */
    private void scanInstalledApplications() {

        AppScanner scanner = new AppScanner(this);

        List<AppInfo> applications = scanner.scanInstalledApps();

        databaseHelper.clearDatabase();

        StringBuilder builder = new StringBuilder();

        builder.append("Privacy-Aware App Permission Management System\n\n");

        builder.append("Total Applications: ")
                .append(applications.size())
                .append("\n\n");

        int displayLimit = Math.min(applications.size(), 20);

        for (int i = 0; i < applications.size(); i++) {

            AppInfo app = applications.get(i);

            databaseHelper.insertApp(app);

            if (i < displayLimit) {

                builder.append("Application Name: ")
                        .append(app.getAppName())
                        .append("\n");

                builder.append("Package Name: ")
                        .append(app.getPackageName())
                        .append("\n");

                builder.append("Permission Count: ")
                        .append(app.getPermissionCount())
                        .append("\n");

                builder.append("Risk Level: ")
                        .append(app.getRiskLevel())
                        .append("\n");

                builder.append("----------------------------------------\n\n");

            }

        }

        builder.append("Database Updated Successfully.");

        txtResults.setText(builder.toString());

    }

}