package com.example.privacyawarepermissionsystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private TextView txtAppName;
    private TextView txtPackageName;
    private TextView txtPermissionCount;
    private TextView txtRiskLevel;
    private TextView txtPermissions;
    private TextView txtScanTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        txtAppName = findViewById(R.id.txtAppName);
        txtPackageName = findViewById(R.id.txtPackageName);
        txtPermissionCount = findViewById(R.id.txtPermissionCount);
        txtRiskLevel = findViewById(R.id.txtRiskLevel);
        txtPermissions = findViewById(R.id.txtPermissions);
        txtScanTime = findViewById(R.id.txtScanTime);

        String appName =
                getIntent().getStringExtra("appName");

        String packageName =
                getIntent().getStringExtra("packageName");

        int permissionCount =
                getIntent().getIntExtra("permissionCount",0);

        String riskLevel =
                getIntent().getStringExtra("riskLevel");

        String permissions =
                getIntent().getStringExtra("permissions");

        String scanTime =
                getIntent().getStringExtra("scanTime");

        txtAppName.setText(appName);

        txtPackageName.setText(
                "Package Name\n\n" + packageName);

        txtPermissionCount.setText(
                "Permission Count\n\n" + permissionCount);

        txtRiskLevel.setText(
                "Risk Level\n\n" + riskLevel);

        txtPermissions.setText(
                "Permissions\n\n" + permissions);

        txtScanTime.setText(
                "Scan Time\n\n" + scanTime);

    }

}