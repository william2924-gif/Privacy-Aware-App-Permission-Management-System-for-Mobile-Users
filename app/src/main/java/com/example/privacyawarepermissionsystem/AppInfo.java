package com.example.privacyawarepermissionsystem;

import java.io.Serializable;
public class AppInfo {

    private String appName;
    private String packageName;
    private String permissions;
    private int permissionCount;
    private String riskLevel;
    private String scanTime;

    public AppInfo implements Serializable (String appName,
                   String packageName,
                   String permissions,
                   int permissionCount,
                   String riskLevel,
                   String scanTime) {

        this.appName = appName;
        this.packageName = packageName;
        this.permissions = permissions;
        this.permissionCount = permissionCount;
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

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getScanTime() {
        return scanTime;
    }
}