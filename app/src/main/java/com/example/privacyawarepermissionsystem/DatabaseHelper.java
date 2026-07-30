package com.example.privacyawarepermissionsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME =
            "privacy_permission.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_APPS = "apps";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_APP_NAME = "app_name";
    public static final String COLUMN_PACKAGE_NAME = "package_name";
    public static final String COLUMN_PERMISSIONS = "permissions";
    public static final String COLUMN_PERMISSION_COUNT =
            "permission_count";
    public static final String COLUMN_PRIVACY_SCORE =
            "privacy_score";
    public static final String COLUMN_RISK_LEVEL =
            "risk_level";
    public static final String COLUMN_SCAN_TIME =
            "scan_time";

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + TABLE_APPS + " (" +
                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_APP_NAME + " TEXT, " +
                        COLUMN_PACKAGE_NAME + " TEXT, " +
                        COLUMN_PERMISSIONS + " TEXT, " +
                        COLUMN_PERMISSION_COUNT + " INTEGER, " +
                        COLUMN_PRIVACY_SCORE + " INTEGER, " +
                        COLUMN_RISK_LEVEL + " TEXT, " +
                        COLUMN_SCAN_TIME + " TEXT" +
                        ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_APPS);
        onCreate(db);
    }

    public void insertApp(AppInfo app) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.insert(
                    TABLE_APPS,
                    null,
                    createValues(app));
        } finally {
            db.close();
        }
    }

    /**
     * Replaces the complete scan snapshot in one transaction.
     * The database is never left half-updated if insertion fails.
     */
    public void replaceAllApps(List<AppInfo> applications) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            db.delete(TABLE_APPS, null, null);

            for (AppInfo app : applications) {
                db.insertOrThrow(
                        TABLE_APPS,
                        null,
                        createValues(app));
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private ContentValues createValues(AppInfo app) {
        ContentValues values = new ContentValues();

        values.put(
                COLUMN_APP_NAME,
                app.getAppName());
        values.put(
                COLUMN_PACKAGE_NAME,
                app.getPackageName());
        values.put(
                COLUMN_PERMISSIONS,
                app.getPermissions());
        values.put(
                COLUMN_PERMISSION_COUNT,
                app.getPermissionCount());
        values.put(
                COLUMN_PRIVACY_SCORE,
                app.getPrivacyScore());
        values.put(
                COLUMN_RISK_LEVEL,
                app.getRiskLevel());
        values.put(
                COLUMN_SCAN_TIME,
                app.getScanTime());

        return values;
    }

    public List<AppInfo> getAllApps() {
        List<AppInfo> appList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_APPS +
                        " ORDER BY " +
                        COLUMN_PRIVACY_SCORE +
                        " ASC, " +
                        COLUMN_APP_NAME +
                        " COLLATE NOCASE ASC",
                null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    AppInfo app = new AppInfo(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            COLUMN_APP_NAME)),
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            COLUMN_PACKAGE_NAME)),
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            COLUMN_PERMISSIONS)),
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            COLUMN_PERMISSION_COUNT)),
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            COLUMN_PRIVACY_SCORE)),
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            COLUMN_RISK_LEVEL)),
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            COLUMN_SCAN_TIME)));

                    appList.add(app);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return appList;
    }

    public void clearDatabase() {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.delete(TABLE_APPS, null, null);
        } finally {
            db.close();
        }
    }

    public int getTotalApps() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_APPS,
                null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            cursor.close();
            db.close();
        }
    }

    public int getHighRiskCount() {
        return getRiskCount("High");
    }

    public int getMediumRiskCount() {
        return getRiskCount("Medium");
    }

    public int getLowRiskCount() {
        return getRiskCount("Low");
    }

    private int getRiskCount(String riskLevel) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_APPS +
                        " WHERE " +
                        COLUMN_RISK_LEVEL +
                        " = ?",
                new String[]{riskLevel});

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            cursor.close();
            db.close();
        }
    }

    /**
     * Uses the same score thresholds as individual applications.
     * This avoids reporting Low when a small device sample contains
     * several high-risk applications.
     */
    public String getOverallStatus() {
        if (getTotalApps() == 0) {
            return "Not Scanned";
        }

        int averageScore =
                getAveragePrivacyScore();

        if (averageScore >= 80) {
            return "Low";
        }

        if (averageScore >= 50) {
            return "Medium";
        }

        return "High";
    }

    public String getRecommendation() {
        String status = getOverallStatus();

        switch (status) {
            case "Not Scanned":
                return "Run an application scan to generate a device privacy summary.";

            case "High":
                return "The average privacy score is low. Review high-risk applications and disable unnecessary permissions.";

            case "Medium":
                return "Some applications request sensitive access. Review permission settings regularly.";

            default:
                return "The current scan indicates a relatively low privacy risk. Continue reviewing permissions after app updates.";
        }
    }

    public int getAveragePrivacyScore() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT AVG(" +
                        COLUMN_PRIVACY_SCORE +
                        ") FROM " +
                        TABLE_APPS,
                null);

        try {
            if (cursor.moveToFirst() &&
                    !cursor.isNull(0)) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            cursor.close();
            db.close();
        }
    }

    public String getHighestPrivacyScoreApp() {
        return getScoreExtreme(true);
    }

    public String getLowestPrivacyScoreApp() {
        return getScoreExtreme(false);
    }

    private String getScoreExtreme(boolean highest) {
        SQLiteDatabase db = getReadableDatabase();

        String direction =
                highest ? "DESC" : "ASC";

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        COLUMN_APP_NAME +
                        ", " +
                        COLUMN_PRIVACY_SCORE +
                        " FROM " +
                        TABLE_APPS +
                        " ORDER BY " +
                        COLUMN_PRIVACY_SCORE +
                        " " +
                        direction +
                        ", " +
                        COLUMN_APP_NAME +
                        " COLLATE NOCASE ASC LIMIT 1",
                null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getString(0) +
                        " (" +
                        cursor.getInt(1) +
                        " / 100)";
            }
            return "N/A";
        } finally {
            cursor.close();
            db.close();
        }
    }
}
