package com.example.privacyawarepermissionsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "privacy_permission.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_APPS = "apps";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_APP_NAME = "app_name";
    public static final String COLUMN_PACKAGE_NAME = "package_name";
    public static final String COLUMN_PERMISSIONS = "permissions";
    public static final String COLUMN_PERMISSION_COUNT = "permission_count";
    public static final String COLUMN_RISK_LEVEL = "risk_level";
    public static final String COLUMN_SCAN_TIME = "scan_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable =
                "CREATE TABLE " + TABLE_APPS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_APP_NAME + " TEXT, " +
                        COLUMN_PACKAGE_NAME + " TEXT, " +
                        COLUMN_PERMISSIONS + " TEXT, " +
                        COLUMN_PERMISSION_COUNT + " INTEGER, " +
                        COLUMN_RISK_LEVEL + " TEXT, " +
                        COLUMN_SCAN_TIME + " TEXT" +
                        ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);
        onCreate(db);

    }

    // Insert one application
    public void insertApp(AppInfo app) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_APP_NAME, app.getAppName());
        values.put(COLUMN_PACKAGE_NAME, app.getPackageName());
        values.put(COLUMN_PERMISSIONS, app.getPermissions());
        values.put(COLUMN_PERMISSION_COUNT, app.getPermissionCount());
        values.put(COLUMN_RISK_LEVEL, app.getRiskLevel());
        values.put(COLUMN_SCAN_TIME, app.getScanTime());

        db.insert(TABLE_APPS, null, values);

        db.close();

    }

    // Get all applications
    public List<AppInfo> getAllApps() {

        List<AppInfo> appList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_APPS +
                        " ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                AppInfo app = new AppInfo(

                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6)

                );

                appList.add(app);

            } while (cursor.moveToNext());

        }

        cursor.close();

        db.close();

        return appList;

    }

    // Delete all records
    public void clearDatabase() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_APPS, null, null);

        db.close();

    }

    // Count total apps
    public int getTotalApps() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_APPS,
                null
        );

        cursor.moveToFirst();

        int count = cursor.getInt(0);

        cursor.close();

        db.close();

        return count;

    }

    /**
     * Returns the number of high-risk applications.
     */
    public int getHighRiskCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_APPS +
                        " WHERE " + COLUMN_RISK_LEVEL + " = ?",
                new String[]{"High"}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    /**
     * Returns the number of medium-risk applications.
     */
    public int getMediumRiskCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_APPS +
                        " WHERE " + COLUMN_RISK_LEVEL + " = ?",
                new String[]{"Medium"}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    /**
     * Returns the number of low-risk applications.
     */
    public int getLowRiskCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_APPS +
                        " WHERE " + COLUMN_RISK_LEVEL + " = ?",
                new String[]{"Low"}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    /**
     * Determines the overall privacy status.
     */
    public String getOverallStatus() {

        int high = getHighRiskCount();
        int medium = getMediumRiskCount();

        if (high >= 10) {
            return "High";
        } else if (medium >= 10) {
            return "Medium";
        } else {
            return "Low";
        }
    }

    /**
     * Generates a privacy recommendation.
     */
    public String getRecommendation() {

        String status = getOverallStatus();

        switch (status) {

            case "High":
                return "Several applications request sensitive permissions. Review High Risk apps and disable unnecessary permissions.";

            case "Medium":
                return "Some applications request sensitive permissions. Check permission settings regularly.";

            default:
                return "Your device currently has a relatively low privacy risk. Continue reviewing permissions before installing new apps.";
        }
    }

}