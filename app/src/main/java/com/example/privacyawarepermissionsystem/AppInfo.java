package com.example.privacyawarepermissionsystem;

import java.io.Serializable;

/**
 * AppInfo stores all information about one scanned application.
 */
public class AppInfo implements Serializable {

    private String appName;
    private String packageName;
    private String permissions;
    private int permissionCount;
    private int privacyScore;
    private String riskLevel;
    private String scanTime;

    public AppInfo(
            String appName,
            String packageName,
            String permissions,
            int permissionCount,
            int privacyScore,
            String riskLevel,
            String scanTime) {

        this.appName = appName;
        this.packageName = packageName;
        this.permissions = permissions;
        this.permissionCount = permissionCount;
        this.privacyScore = privacyScore;
        this.riskLevel = riskLevel;
        this.scanTime = scanTime;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPermissions() {
        return permissions;
    }

    public int getPermissionCount() {
        return permissionCount;
    }

    public int getPrivacyScore() {
        return privacyScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getScanTime() {
        return scanTime;
    }

}