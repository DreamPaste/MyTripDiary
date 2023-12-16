package com.project.mytripdiary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.project.mytripdiary.ui.addtrip.travellist.TravelListData;

import java.util.ArrayList;

public interface Setting {
    //국내 여행지입니다. 순서대로 사진도 추가해야하며, 사진 이름은 dome_1..2..3입니다.
    String[] domestic_name = {"제주","서울","부산","전주","양양"};
    //해외 여행지입니다. 순서대로 사진도 추가해야하며, 사진 이름은 over_1..over_2..3입니다.
    String[] oversea_name = {"뉴욕"};

    //구글맵 api 키는 menifest->metadata | res->values->string-> API_KEY 도 함께 수정해야 합니다.
    String API_KEY  ="AIzaSyDsBvulnayrpcOHyjPSFBkRvTWzS7mt3i4";
}
