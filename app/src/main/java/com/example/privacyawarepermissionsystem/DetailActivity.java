package com.example.privacyawarepermissionsystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private TextView txtAppName;
    private TextView txtPackageName;
    private TextView txtRiskLevel;
    private TextView txtPermissionCount;
    private TextView txtPermissions;
    private TextView txtRecommendation;
    private TextView txtScanTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        txtAppName = findViewById(R.id.txtAppName);
        txtPackageName = findViewById(R.id.txtPackageName);
        txtRiskLevel = findViewById(R.id.txtRiskLevel);
        txtPermissionCount = findViewById(R.id.txtPermissionCount);
        txtPermissions = findViewById(R.id.txtPermissions);
        txtRecommendation = findViewById(R.id.txtRecommendation);
        txtScanTime = findViewById(R.id.txtScanTime);

        AppInfo app =
                (AppInfo) getIntent().getSerializableExtra("selectedApp");

        if (app != null) {

            txtAppName.setText(app.getAppName());

            txtPackageName.setText(app.getPackageName());

            txtRiskLevel.setText(app.getRiskLevel());

            txtPermissionCount.setText(
                    String.valueOf(app.getPermissionCount())
            );

            txtPermissions.setText(
                    formatPermissions(app.getPermissions())
            );

            txtRecommendation.setText(
                    generateRecommendation(app.getPermissions())
            );

            txtScanTime.setText(app.getScanTime());

        }

    }

    /**
     * Format permission list.
     */
    private String formatPermissions(String permissions) {

        if (permissions == null || permissions.isEmpty()) {

            return "No permissions requested.";

        }

        return permissions.replace(",", "\n");

    }

    /**
     * Generate privacy recommendation.
     */
    private String generateRecommendation(String permissions) {

        StringBuilder recommendation = new StringBuilder();

        if (permissions.contains("CAMERA")) {

            recommendation.append("• Disable Camera permission if unnecessary.\n\n");

        }

        if (permissions.contains("LOCATION")) {

            recommendation.append("• Allow Location only while using the app.\n\n");

        }

        if (permissions.contains("CONTACTS")) {

            recommendation.append("• Review Contacts permission carefully.\n\n");

        }

        if (permissions.contains("SMS")) {

            recommendation.append("• SMS permission may access personal messages.\n\n");

        }

        if (permissions.contains("RECORD_AUDIO")) {

            recommendation.append("• Disable Microphone permission unless required.\n\n");

        }

        if (permissions.contains("PHONE")) {

            recommendation.append("• Review Phone permission before granting access.\n\n");

        }

        if (recommendation.length() == 0) {

            recommendation.append(
                    "This application requests only low-risk permissions."
            );

        }

        return recommendation.toString();

    }

}