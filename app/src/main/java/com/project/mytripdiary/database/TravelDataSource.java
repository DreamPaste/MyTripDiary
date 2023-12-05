package com.project.mytripdiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.project.mytripdiary.ui.addtrip.travellist.TravelListData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TravelDataSource {
    // 이미지는 byte 배열로 변환하여 데이터베이스에 저장하고, 불러올 때는 다시 비트맵으로 변환합니다.
    private SQLiteDatabase database;
    private DbCreate_travelList dbHelper;

    public TravelDataSource(Context context) {
        dbHelper = new DbCreate_travelList(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertTravelData(TravelListData travelData) {
        ContentValues values = new ContentValues();
        values.put(DbCreate_travelList.COLUMN_NAME, travelData.getName());
        values.put(DbCreate_travelList.COLUMN_IMAGE, getBytes(travelData.getImage())); // 이미지를 byte 배열로 변환

        database.insert("travel", null, values);
    }

    public List<TravelListData> getAllTravelData() {
        List<TravelListData> travelDataList = new ArrayList<>();
        String[] allColumns = {DbCreate_travelList.COLUMN_ID, DbCreate_travelList.COLUMN_NAME, DbCreate_travelList.COLUMN_IMAGE};

        Cursor cursor = database.query("travel", allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TravelListData travelData = cursorToTravelData(cursor);
            travelDataList.add(travelData);
            cursor.moveToNext();
        }
        cursor.close();

        return travelDataList;
    }

    private TravelListData cursorToTravelData(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(DbCreate_travelList.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(DbCreate_travelList.COLUMN_NAME));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(DbCreate_travelList.COLUMN_IMAGE));
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        return new TravelListData( name,imageBitmap);
    }

    // 이미지를 byte 배열로 변환하는 메서드
    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
