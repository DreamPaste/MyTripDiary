package com.project.mytripdiary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbCreate_travelList extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "travel_database";
    private static final int DATABASE_VERSION = 2;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_STARTDATE = "startdate";
    public static final String COLUMN_ENDDATE = "enddate";
    public static final String COLUMN_TRAVELED_PLACE = "traveled_place";
    public static final String COLUMN_TRAVELED_MAP = "traveled_map";
    private static final String CREATE_TABLE_TRAVEL =
            "CREATE TABLE travel (" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME+" TEXT," +
                    COLUMN_IMAGE+" BLOB);";
    private static final String CREATE_TABLE_NOWTRAVEL=
            "CREATE TABLE nowtravel(" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_PLACE+" TEXT," +
                    COLUMN_STARTDATE+" TEXT," +
                    COLUMN_ENDDATE+" TEXT," +
                    COLUMN_TRAVELED_PLACE+" TEXT," +
                    COLUMN_TRAVELED_MAP+" TEXT);";
    public DbCreate_travelList(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_TRAVEL);
        db.execSQL(CREATE_TABLE_NOWTRAVEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS travel");
        db.execSQL("DROP TABLE IF EXISTS nowtravel");
        onCreate(db);
    }

}
