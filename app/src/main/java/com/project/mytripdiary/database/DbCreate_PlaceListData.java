package com.project.mytripdiary.database;

import static android.content.ContentValues.TAG;
import static com.project.mytripdiary.Setting.API_KEY;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.google.android.libraries.places.api.net.PlacesClient;
import com.project.mytripdiary.ui.addtrip.placelist.PlaceListData;
import com.project.mytripdiary.ui.dashboard.TripsList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DbCreate_PlaceListData extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "place_list_database";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "place_list";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TRIP = "trip";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_D_DAY = "d_day";
    public static final String COLUMN_PLACE_NAME = "place_name";
    public static final String COLUMN_PLACE_ADR = "place_adr";
    public static final String COLUMN_PLACE_X = "place_x";
    public static final String COLUMN_PLACE_Y = "place_y";


    private static final String CREATE_TABLE_PLACE_LIST =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TRIP + " TEXT," +
                    COLUMN_START_DATE + " TEXT," +
                    COLUMN_END_DATE + " TEXT," +
                    COLUMN_D_DAY + " INTEGER," +
                    COLUMN_PLACE_NAME + " TEXT," +
                    COLUMN_PLACE_ADR + " TEXT," +
                    COLUMN_PLACE_X + " DOUBLE," +
                    COLUMN_PLACE_Y + " DOUBLE);";

    public DbCreate_PlaceListData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLACE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 메서드를 추가하여 PlaceListData를 데이터베이스에 추가하는 기능
    public void addPlaceListData(PlaceListData placeListData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRIP, placeListData.getTrip());
        values.put(COLUMN_START_DATE, placeListData.getStartDate());
        values.put(COLUMN_END_DATE, placeListData.getEndDate());
        values.put(COLUMN_D_DAY, placeListData.getDday());
        values.put(COLUMN_PLACE_NAME, placeListData.getName());
        values.put(COLUMN_PLACE_ADR, placeListData.getAddress());
        values.put(COLUMN_PLACE_X,placeListData.getX());
        values.put(COLUMN_PLACE_Y,placeListData.getY());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // 메서드를 추가하여 모든 PlaceListData를 얻어오는 기능
    public Cursor getAllPlaceListData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    //...

    public List<PlaceListData> getPlaceListDataByTripDate(SQLiteDatabase db, String trip, String startDate, String endDate) {

        List<PlaceListData> resultList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_TRIP + " = ?" +
                " AND " + COLUMN_START_DATE + " = ?" +
                " AND " + COLUMN_END_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{trip, startDate, endDate});

        if (cursor.moveToFirst()) {
            do {

                String Name = cursor.getString(5);
                String Address = cursor.getString(6);
                Log.d("db", "getPlaceListDataByTripDate2: "+Name+Address);
                double x = cursor.getDouble(7);
                double y = cursor.getDouble(8);

                PlaceListData placeListData = new PlaceListData(trip, startDate, endDate, cursor.getInt(4), Name, Address, x, y);

                Log.d("db", "getPlaceListDataByTripDate: list :" + placeListData.getName());
                resultList.add(placeListData);
            } while (cursor.moveToNext());
        }

        Log.d("db", "getPlaceListDataByTripDate: ___resultListSize" + resultList.size());
        cursor.close();

        return resultList;
    }



//...

    // 가장 최근에 추가된 데이터의 trip, startdate, enddate를 반환하는 함수
    public ArrayList<String> getLatestPlaceListData(SQLiteDatabase  db) {
        ArrayList<String> data= new ArrayList<>();
        // 테이블의 모든 데이터를 날짜 기준으로 내림차순으로 정렬하여 최상단의 데이터를 가져옵니다.
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_START_DATE + " DESC",  // 내림차순 정렬
                "1"  // 최상위 데이터 1개만 가져옴
        );
        if (cursor.moveToFirst()) {
            // 최상위 데이터에서 필요한 정보 추출
            String trip = cursor.getString(1);
            String startDate = cursor.getString(2);
            String endDate = cursor.getString(3);
            data.add(trip);
            data.add(startDate);
            data.add(endDate);
            Log.d("db", "getLatestPlaceListData: "+trip+startDate+endDate);
        }
        cursor.close();
        return data;
    }
    public List<TripsList> getRecentTripsList(SQLiteDatabase db) {
        List<TripsList> tripsList = new ArrayList<>();

        // 테이블의 모든 데이터를 날짜 기준으로 내림차순으로 정렬하여 가져옵니다.
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_TRIP, COLUMN_START_DATE, COLUMN_END_DATE},
                null,
                null,
                COLUMN_TRIP,  // GROUP BY trip
                null,
                COLUMN_START_DATE + " DESC"  // 내림차순 정렬
        );
        int columnCount = cursor.getColumnCount();
        Log.d("dash", "열 개수: " + columnCount);

        for (int i = 0; i < columnCount; i++) {
            Log.d("dash", "열 " + i + ": " + cursor.getColumnName(i));
        }
        if (cursor.moveToFirst()) {
            do {
                // 데이터에서 trip, startDate, endDate 추출
                String trip = cursor.getString(0);

                String startDate = cursor.getString(1);
                String endDate = cursor.getString(2);
                Log.d("dash", "getRecentTripsList: "+trip+startDate+endDate);
                // TripsList 객체 생성
                TripsList tripsListItem = new TripsList(trip, startDate, endDate);
                // 리스트에 추가 (중복되는 데이터는 추가하지 않음)
                if (!tripsList.contains(tripsListItem)) {
                    tripsList.add(tripsListItem);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();


        return tripsList;
    }


}
