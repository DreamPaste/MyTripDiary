package com.project.mytripdiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbCreate_Diary extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "diary_database";
    private static final int DATABASE_VERSION = 1;

    // Table information
    private static final String TABLE_NAME = "diary_table";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TEXT = "text";

    // DiaryEntry class to represent each entry
    public static class DiaryEntry {
        public String name;
        public String text;

        public DiaryEntry(String name, String text) {
            this.name = name;
            this.text = text;
        }
    }

    // Constructor
    public DbCreate_Diary(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the table
        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT PRIMARY KEY," +
                COLUMN_TEXT + " TEXT)";
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade the database if needed (not implemented in this example)
    }

    // Add a new entry to the database
    public void addDiaryEntry(String name, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_TEXT, text);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    // Get text based on name from the database
    public String getTextByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String text = null;

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_TEXT}, COLUMN_NAME + "=?", new String[]{name}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // If the entry with the specified name exists, retrieve the text
            int columnIndex = cursor.getColumnIndexOrThrow(COLUMN_TEXT); // Make sure to use the correct column name
            text = cursor.getString(columnIndex);
            cursor.close();
        }

        db.close();
        return text;
    }

    // Get all entries from the database
    public List<DiaryEntry> getAllEntries() {
        List<DiaryEntry> entriesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String text = cursor.getString(2);
                entriesList.add(new DiaryEntry(name, text));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return entriesList;
    }

    // Update an entry in the database based on name and text
    // Update an entry in the database based on name and text
    public void updateEntry(String name, String newText) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the entry with the specified name already exists
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // If the entry exists, update it
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEXT, newText);
            db.update(TABLE_NAME, values, COLUMN_NAME + "=?", new String[]{name});
        } else {
            // If the entry does not exist, add a new entry
            addDiaryEntry(name, newText);
        }

        // Close the cursor and the database
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    // Delete an entry from the database
    public void deleteEntry(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME + "=?", new String[]{name});
        db.close();
    }
}
