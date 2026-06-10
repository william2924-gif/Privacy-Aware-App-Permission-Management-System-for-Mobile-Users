package com.example.privacyawarepermissionsystem;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class AppScanner {

    private final Context context;

    public AppScanner(Context context) {
        this.context = context;
    }

    public List<String> scanInstalledApps() {
        List<String> appResults = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = packageManager.queryIntentActivities(intent, 0);

        for (ResolveInfo app : apps) {
            String appName = app.loadLabel(packageManager).toString();
            String packageName = app.activityInfo.packageName;

            StringBuilder result = new StringBuilder();
            result.append("App Name: ").append(appName).append("\n");
            result.append("Package Name: ").append(packageName).append("\n");

            String[] permissions = getPermissions(packageManager, packageName);

            if (permissions == null || permissions.length == 0) {
                result.append("Permissions: No requested permissions\n");
                result.append("Risk Level: Low\n");
            } else {
                result.append("Permission Count: ").append(permissions.length).append("\n");
                result.append("Risk Level: ").append(getRiskLevel(permissions)).append("\n");
                result.append("Permissions:\n");

                for (String permission : permissions) {
                    result.append("- ").append(permission).append("\n");
                }
            }

            result.append("-----------------------------\n");
            appResults.add(result.toString());
        }

        return appResults;
    }

    private String[] getPermissions(PackageManager packageManager, String packageName) {
        try {
            PackageInfo packageInfo;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageInfo = packageManager.getPackageInfo(
                        packageName,
                        PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS)
                );
            } else {
                packageInfo = packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_PERMISSIONS
                );
            }

            return packageInfo.requestedPermissions;

        } catch (PackageManager.NameNotFoundException e) {
            return new String[0];
        }
    }

    private String getRiskLevel(String[] permissions) {
        int highRiskCount = 0;

        for (String permission : permissions) {
            if (permission.contains("CAMERA")
                    || permission.contains("LOCATION")
                    || permission.contains("CONTACTS")
                    || permission.contains("SMS")
                    || permission.contains("RECORD_AUDIO")
                    || permission.contains("PHONE")) {
                highRiskCount++;
            }
        }

        if (highRiskCount >= 3) {
            return "High";
        } else if (highRiskCount >= 1) {
            return "Medium";
        } else {
            return "Low";
        }
    }
}