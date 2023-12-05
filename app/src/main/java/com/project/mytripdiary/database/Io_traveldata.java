package com.project.mytripdiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.project.mytripdiary.ui.addtrip.travellist.TravelListData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Io_traveldata {
    // 데이터 추가
    public void insertData(Context context,String name ,Bitmap bitmap) {
        SQLiteDatabase db = new DbCreate_travelList(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", getBytes(bitmap)); // 비트맵을 바이트 배열로 변환하는 함수 필요
        db.insert("travel", null, values);
        db.close();
    }

    // 데이터 불러오기
    public List<TravelListData> getAllData(Context context) {
        List<TravelListData> lists = new ArrayList<>();
        SQLiteDatabase db = new DbCreate_travelList(context).getReadableDatabase();
        Cursor cursor = db.query("travel", null, null, null, null, null, null);
        int nameColumnIndex = cursor.getColumnIndex("name");
        int imageColumnIndex = cursor.getColumnIndex("image");
        while (cursor.moveToNext()) {
            if (nameColumnIndex >= 0 && imageColumnIndex >= 0) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            TravelListData data = new TravelListData(name, image);
            lists.add(data);
        }
            else{
                Toast.makeText(context, "여행 데이터 접근 인덱싱 오류", Toast.LENGTH_SHORT).show();
            }

            }

        cursor.close();
        db.close();

        return lists != null ? lists : new ArrayList<>();
    }

    // 이미지를 byte 배열로 변환하는 메서드
    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
