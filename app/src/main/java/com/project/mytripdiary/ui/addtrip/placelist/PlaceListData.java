package com.project.mytripdiary.ui.addtrip.placelist;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

public class PlaceListData {
    // 여행지 이름
    private String trip;
    // 여행 시작 날짜
    private String startDate;
    // 여행 종료 날짜
    private String endDate;
    // 방문 날짜(몇일차인지)
    private int Dday;

    private String name;
    private String address;
    private double x;
    private double y;
    // 방문 장소 이름


    // 생성자
    public PlaceListData(String trip, String startDate, String endDate,  int Dday, String name,  String address, double  x, double y) {
        this.trip = trip;
        this.startDate = startDate;
        this.endDate = endDate;
        this.Dday = Dday;
        this.name  =  name;
        this. address = address;
        this.x = x;
        this.y = y;

    }

    // Getter 및 Setter 메서드
    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }



    public int getDday() {
        return Dday;
    }

    public void setDday(int Dday){this.Dday = Dday;}

    public String getName(){return name;}
    public void setName(String  name){this.name =name;}
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
