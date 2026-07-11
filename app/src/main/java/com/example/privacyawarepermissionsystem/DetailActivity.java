package com.example.privacyawarepermissionsystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        AppInfo app =
                (AppInfo) getIntent().getSerializableExtra("selectedApp");

        if (app != null) {

            txtAppName.setText(app.getAppName());

            txtPackageName.setText(app.getPackageName());

            String level = app.getRiskLevel();

            if (level.equalsIgnoreCase("High")) {
                txtRiskLevel.setText("🟥 HIGH");
            }
            else if (level.equalsIgnoreCase("Medium")) {
                txtRiskLevel.setText("🟧 MEDIUM");
            }
            else {
                txtRiskLevel.setText("🟩 LOW");
            }

            txtPrivacyScore.setText(app.getPrivacyScore() + " / 100");

            txtPrivacySummary.setText(
                    PrivacyRiskAnalyzer.generateSummary(
                            app.getPrivacyScore()
                    )
            );

            txtPermissionCount.setText(
                    String.valueOf(
                            PrivacyRiskAnalyzer.countSensitivePermissions(
                                    app.getPermissions()
                            )
                    )
            );

            txtPermissions.setText(
                    formatPermissions(app.getPermissions())
            );

            txtRecommendation.setText(
                    generateRecommendation(app)
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

        String result = permissions;

        result = result.replace("android.permission.", "");

        result = result.replace("ACCESS_FINE_LOCATION", "Location");

        result = result.replace("ACCESS_COARSE_LOCATION", "Location");

        result = result.replace("READ_CONTACTS", "Contacts");

        result = result.replace("WRITE_CONTACTS", "Contacts");

        result = result.replace("CAMERA", "Camera");

        result = result.replace("RECORD_AUDIO", "Microphone");

        result = result.replace("READ_SMS", "SMS");

        result = result.replace("SEND_SMS", "SMS");

        result = result.replace("CALL_PHONE", "Phone");

        result = "• " + result.replace("\n", "\n• ");

        return result;

    }

    /**
     * Generate privacy recommendation.
     */
    private String generateRecommendation(AppInfo app) {

        String permissions = app.getPermissions();

        if (permissions == null) {
            permissions = "";
        }

        int privacyScore = app.getPrivacyScore();

        StringBuilder builder = new StringBuilder();

        if (permissions.contains("CAMERA")) {
            builder.append("• Disable Camera permission if it is not required.\n\n");
        }

        if (permissions.contains("LOCATION")) {
            builder.append("• Allow Location only while using the application.\n\n");
        }

        if (permissions.contains("CONTACTS")) {
            builder.append("• Review Contacts permission before granting access.\n\n");
        }

        if (permissions.contains("SMS")) {
            builder.append("• SMS permission may expose personal messages.\n\n");
        }

        if (permissions.contains("PHONE")) {
            builder.append("• Phone permission should only be granted to trusted apps.\n\n");
        }

        if (permissions.contains("RECORD_AUDIO")) {
            builder.append("• Disable Microphone permission unless voice recording is needed.\n\n");
        }

        if (builder.length() == 0) {
            builder.append("No significant privacy risks were detected.");
        }

        builder.append("\nOverall Recommendation\n\n");

        if (privacyScore >= 80) {
            builder.append(
                    "This application presents a low privacy risk. Current permissions appear reasonable.");
        }
        else if (privacyScore >= 50) {
            builder.append(
                    "Review sensitive permissions and disable any permissions that are not necessary.");
        }
        else {
            builder.append(
                    "This application requests many sensitive permissions. Review all permissions carefully before continuing to use the application.");
        }

        return builder.toString();

    }

}