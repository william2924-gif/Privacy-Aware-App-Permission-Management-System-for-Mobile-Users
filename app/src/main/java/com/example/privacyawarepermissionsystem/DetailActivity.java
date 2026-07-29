package com.example.privacyawarepermissionsystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class DetailActivity extends AppCompatActivity {

    private TextView txtAppName;
    private TextView txtPackageName;
    private TextView txtRiskLevel;
    private TextView txtPermissionCount;
    private TextView txtPrivacyScore;
    private TextView txtPermissions;
    private TextView txtPrivacySummary;
    private TextView txtRecommendation;
    private TextView txtScanTime;
    private LinearProgressIndicator progressPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtAppName = findViewById(R.id.txtAppName);
        txtPackageName = findViewById(R.id.txtPackageName);
        txtRiskLevel = findViewById(R.id.txtRiskLevel);
        txtPermissionCount = findViewById(R.id.txtPermissionCount);
        txtPrivacyScore = findViewById(R.id.txtPrivacyScore);
        txtPermissions = findViewById(R.id.txtPermissions);
        txtPrivacySummary = findViewById(R.id.txtPrivacySummary);
        txtRecommendation = findViewById(R.id.txtRecommendation);
        txtScanTime = findViewById(R.id.txtScanTime);
        progressPrivacy = findViewById(R.id.progressPrivacy);

        AppInfo app = (AppInfo) getIntent().getSerializableExtra("selectedApp");

        if (app != null) {
            txtAppName.setText(app.getAppName());
            txtPackageName.setText(app.getPackageName());
            txtPrivacyScore.setText(app.getPrivacyScore() + " / 100");
            progressPrivacy.setProgressCompat(app.getPrivacyScore(), false);

            applyRiskStyle(app.getRiskLevel());

            txtPrivacySummary.setText(
                    PrivacyRiskAnalyzer.generateSummary(app.getPrivacyScore()));

            txtPermissionCount.setText(String.valueOf(
                    PrivacyRiskAnalyzer.countSensitivePermissions(app.getPermissions())));

            txtPermissions.setText(formatPermissions(app.getPermissions()));
            txtRecommendation.setText(generateRecommendation(app));
            txtScanTime.setText(app.getScanTime());
        }
    }

    private void applyRiskStyle(String level) {
        txtRiskLevel.setText(level.toUpperCase());

        if ("High".equalsIgnoreCase(level)) {
            txtRiskLevel.setBackgroundResource(R.drawable.bg_risk_high);
            txtRiskLevel.setTextColor(ContextCompat.getColor(this, R.color.risk_high));
            progressPrivacy.setIndicatorColor(
                    ContextCompat.getColor(this, R.color.risk_high));
        } else if ("Medium".equalsIgnoreCase(level)) {
            txtRiskLevel.setBackgroundResource(R.drawable.bg_risk_medium);
            txtRiskLevel.setTextColor(ContextCompat.getColor(this, R.color.risk_medium));
            progressPrivacy.setIndicatorColor(
                    ContextCompat.getColor(this, R.color.risk_medium));
        } else {
            txtRiskLevel.setBackgroundResource(R.drawable.bg_risk_low);
            txtRiskLevel.setTextColor(ContextCompat.getColor(this, R.color.risk_low));
            progressPrivacy.setIndicatorColor(
                    ContextCompat.getColor(this, R.color.risk_low));
        }
    }

    private String formatPermissions(String permissions) {
        if (permissions == null || permissions.trim().isEmpty()) {
            return "No permissions requested.";
        }

        String[] permissionLines = permissions.split("\n");
        StringBuilder result = new StringBuilder();

        for (String permission : permissionLines) {
            if (permission.trim().isEmpty()) {
                continue;
            }

            String readable = permission.replace("android.permission.", "")
                    .replace("ACCESS_FINE_LOCATION", "Precise location")
                    .replace("ACCESS_COARSE_LOCATION", "Approximate location")
                    .replace("READ_CONTACTS", "Read contacts")
                    .replace("WRITE_CONTACTS", "Modify contacts")
                    .replace("CAMERA", "Camera")
                    .replace("RECORD_AUDIO", "Microphone")
                    .replace("READ_SMS", "Read SMS")
                    .replace("SEND_SMS", "Send SMS")
                    .replace("CALL_PHONE", "Phone calls")
                    .replace("_", " ");

            result.append("• ").append(readable).append("\n");
        }

        return result.toString().trim();
    }

    private String generateRecommendation(AppInfo app) {
        String permissions = app.getPermissions() == null ? "" : app.getPermissions();
        StringBuilder builder = new StringBuilder();

        if (permissions.contains("CAMERA")) {
            builder.append("• Disable Camera access when it is not required.\n\n");
        }
        if (permissions.contains("LOCATION")) {
            builder.append("• Prefer “Allow only while using the app” for Location.\n\n");
        }
        if (permissions.contains("CONTACTS")) {
            builder.append("• Confirm that Contacts access is essential to the app’s purpose.\n\n");
        }
        if (permissions.contains("SMS")) {
            builder.append("• SMS access can expose personal messages and verification codes.\n\n");
        }
        if (permissions.contains("PHONE")) {
            builder.append("• Grant Phone access only to applications you trust.\n\n");
        }
        if (permissions.contains("RECORD_AUDIO")) {
            builder.append("• Disable Microphone access unless voice recording is needed.\n\n");
        }

        if (builder.length() == 0) {
            builder.append("No significant sensitive permissions were detected.\n\n");
        }

        if (app.getPrivacyScore() >= 80) {
            builder.append("Overall: Low privacy risk. Continue reviewing permissions after app updates.");
        } else if (app.getPrivacyScore() >= 50) {
            builder.append("Overall: Review each sensitive permission and disable unnecessary access.");
        } else {
            builder.append("Overall: High attention required. Review all permissions before continuing to use this app.");
        }

        return builder.toString();
    }
}
