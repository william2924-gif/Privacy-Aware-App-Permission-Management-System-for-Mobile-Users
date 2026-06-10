package com.example.privacyawarepermissionsystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "privacy_permission.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_APPS = "apps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAppsTable = "CREATE TABLE " + TABLE_APPS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "app_name TEXT, " +
                "package_name TEXT, " +
                "permissions TEXT, " +
                "permission_count INTEGER, " +
                "risk_level TEXT, " +
                "scan_time TEXT" +
                ")";

        db.execSQL(createAppsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);
        onCreate(db);
    }
}