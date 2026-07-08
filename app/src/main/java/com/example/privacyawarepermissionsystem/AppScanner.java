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
import java.util.List;
import java.util.Locale;

public class AppScanner {

    private Context context;

    public AppScanner(Context context) {
        this.context = context;
    }

    public List<AppInfo> scanInstalledApps() {

        List<AppInfo> appList = new ArrayList<>();

        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps =
                packageManager.queryIntentActivities(intent, 0);

        for (ResolveInfo app : apps) {

            String appName =
                    app.loadLabel(packageManager).toString();

            String packageName =
                    app.activityInfo.packageName;

            String[] permissions =
                    getPermissions(packageManager, packageName);

            String permissionText;

            int permissionCount;

            if (permissions == null) {

                permissionText = "";

                permissionCount = 0;

            } else {

                permissionCount = permissions.length;

                StringBuilder builder = new StringBuilder();

                for (String permission : permissions) {

                    builder.append(permission)
                            .append("\n");

                }

                permissionText = builder.toString();

            }

            String riskLevel =
                    getRiskLevel(permissions);

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

                    riskLevel,

                    scanTime

            );

            appList.add(appInfo);

        }

        return appList;

    }

    private String[] getPermissions(
            PackageManager packageManager,
            String packageName) {

        try {

            PackageInfo packageInfo;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                packageInfo =
                        packageManager.getPackageInfo(
                                packageName,
                                PackageManager.PackageInfoFlags.of(
                                        PackageManager.GET_PERMISSIONS));

            } else {

                packageInfo =
                        packageManager.getPackageInfo(
                                packageName,
                                PackageManager.GET_PERMISSIONS);

            }

            return packageInfo.requestedPermissions;

        }

        catch (PackageManager.NameNotFoundException e) {

            return new String[0];

        }

    }

    private String getRiskLevel(String[] permissions) {

        if (permissions == null)
            return "Low";

        int highRisk = 0;

        for (String permission : permissions) {

            if (permission.contains("CAMERA")
                    || permission.contains("LOCATION")
                    || permission.contains("CONTACTS")
                    || permission.contains("SMS")
                    || permission.contains("PHONE")
                    || permission.contains("RECORD_AUDIO")) {

                highRisk++;

            }

        }

        if (highRisk >= 3)

            return "High";

        if (highRisk >= 1)

            return "Medium";

        return "Low";

    }

}