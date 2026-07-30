package com.example.privacyawarepermissionsystem;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AppScanner {

    private final Context context;

    public AppScanner(Context context) {
        this.context = context;
    }

    /**
     * Scans launchable applications visible to this app.
     * Duplicate launcher activities from the same package are ignored.
     */
    public List<AppInfo> scanInstalledApps() {
        List<AppInfo> appList = new ArrayList<>();
        Set<String> scannedPackages = new HashSet<>();

        PackageManager packageManager =
                context.getPackageManager();

        Intent intent =
                new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps =
                packageManager.queryIntentActivities(
                        intent,
                        0);

        for (ResolveInfo app : apps) {
            String packageName =
                    app.activityInfo.packageName;

            if (!scannedPackages.add(packageName)) {
                continue;
            }

            String appName =
                    app.loadLabel(packageManager)
                            .toString();

            String[] permissions =
                    getPermissions(
                            packageManager,
                            packageName);

            String permissionText =
                    formatPermissionText(permissions);

            int permissionCount =
                    permissions == null
                            ? 0
                            : permissions.length;

            int privacyScore =
                    PrivacyRiskAnalyzer
                            .calculatePrivacyScore(
                                    permissionText);

            String riskLevel =
                    PrivacyRiskAnalyzer
                            .calculateRiskLevel(
                                    privacyScore);

            String scanTime =
                    new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault())
                            .format(new Date());

            AppInfo appInfo = new AppInfo(
                    appName,
                    packageName,
                    permissionText,
                    permissionCount,
                    privacyScore,
                    riskLevel,
                    scanTime);

            appList.add(appInfo);
        }

        return appList;
    }

    private String formatPermissionText(
            String[] permissions) {

        if (permissions == null ||
                permissions.length == 0) {
            return "";
        }

        StringBuilder builder =
                new StringBuilder();

        for (String permission : permissions) {
            builder.append(permission)
                    .append("\n");
        }

        return builder.toString();
    }

    private String[] getPermissions(
            PackageManager packageManager,
            String packageName) {

        try {
            PackageInfo packageInfo;

            if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.TIRAMISU) {

                packageInfo =
                        packageManager.getPackageInfo(
                                packageName,
                                PackageManager
                                        .PackageInfoFlags
                                        .of(
                                                PackageManager
                                                        .GET_PERMISSIONS));
            } else {
                packageInfo =
                        packageManager.getPackageInfo(
                                packageName,
                                PackageManager
                                        .GET_PERMISSIONS);
            }

            return packageInfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException |
                 SecurityException exception) {

            return new String[0];
        }
    }
}
