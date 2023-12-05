package com.project.mytripdiary.ui.addtrip.travellist;

import android.graphics.Bitmap;

public class TravelListData {

//item_layout에 들어갈 데이터

    String name;     //item에 넣어질 여행지 이름
    Bitmap image;        //item에 넣어질 사진



    ///getter setter 만들기


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


    //constructor 만들기
    public TravelListData( String name,Bitmap image) {
        this.name = name;
        this.image = image;
    }
}