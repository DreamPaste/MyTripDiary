package com.project.mytripdiary.ui.addtrip;

import static com.project.mytripdiary.Setting.domestic_name;
import static com.project.mytripdiary.Setting.oversea_name;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.mytripdiary.MainActivity;
import com.project.mytripdiary.R;
import com.project.mytripdiary.ui.addtrip.travellist.TravelListData;
import com.project.mytripdiary.ui.addtrip.travellist.travelListAdapter;

import java.util.ArrayList;

public class AddTripFragment extends Fragment {

    private RecyclerView tripListView;
    private travelListAdapter adapter;
    private ArrayList<TravelListData> DomesticList;
    private ArrayList<TravelListData> OverSeaList;
    private ArrayList<TravelListData> AllList;
    private EditText search;
    private TextView triptitle;
    private String nowTripPlace =null;
    private Bitmap nowTripPlaceImg=null;
    private AppCompatButton domesitc;
    private AppCompatButton overseas;
    private AppCompatButton newcity;
    private String searchText ;
    ArrayList<TravelListData> search_list;
//
    public static AddTripFragment newInstance() {
        return new AddTripFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.nav_view);
        tripListView =view.findViewById(R.id.addtrip_trip_list);
        triptitle = view.findViewById(R.id.addtrip_trip_title);

        domesitc = view.findViewById(R.id.addtrip_trip_searchtitle1);
        overseas = view.findViewById(R.id.addtrip_trip_searchtitle2);
        newcity = view.findViewById(R.id.addtrip_trip_searchtitle3);
        search = view.findViewById(R.id.addtrip_trip_searchText);
        //------상단 바 세팅----
        // Toolbar 대신에 ActionBar를 사용하도록 변경
        ActionBar actionBar = ((MainActivity) requireActivity()).getSupportActionBar();
        // ActionBar 설정
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        }
        //----리사이클러 뷰 세팅-----

        //리스트에 샘플 여행 데이터 삽입.
        AllList = new ArrayList<>();
        DomesticList = new ArrayList<>();
        OverSeaList = new ArrayList<>();
        search_list = new ArrayList<>();
        for(int i=0;i<domestic_name.length;i++){
            int id = getResources().getIdentifier("dome_"+i,"drawable", getContext().getPackageName());
            Bitmap sampleImg = BitmapFactory.decodeResource(getResources(),id);
            DomesticList.add(new TravelListData(domestic_name[i],sampleImg));
        }
        for(int i=0;i<oversea_name.length;i++){
            int id = getResources().getIdentifier("over_"+i,"drawable", getContext().getPackageName());
            Bitmap sampleImg = BitmapFactory.decodeResource(getResources(),id);
            OverSeaList.add(new TravelListData(oversea_name[i],sampleImg));
        }
        AllList.addAll(DomesticList);
        AllList.addAll(OverSeaList);

        //아뎁터 불러오기
        adapter = new travelListAdapter(getContext(), AllList);
        tripListView.setAdapter(adapter);
        Log.d("Sample",""+adapter.getItemCount());
        for (TravelListData data : AllList) {
            Log.d("Samplelist", "Name: " + data.getName());
            Log.d("Samplelist","img_"+data.getImage());
        }
        //아뎁터에 클릭 이벤트 연결
        adapter.setOnItemClickListener((position, data) -> {
            //다음 페이지로 연결됩니다.
            nowTripPlace = data;
            //여행지 이름(nowTripPlace)를 가지고 다음 페이지로 이동합니다.
            Bundle bundle = new Bundle();
            bundle.putString("nowTripPlace", nowTripPlace);

            // 실제로 이동하고 싶은 프래그먼트로 NextFragment를 대체하세요
            DateSelect nextFragment = new DateSelect();
            nextFragment.setArguments(bundle);

            // 다음 프래그먼트로 이동합니다
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                    .setReorderingAllowed(true) // 이 부분을 추가
                    .replace(R.id.nav_host_fragment_activity_main, nextFragment)
                    .addToBackStack(null)  // 백 버튼으로 뒤로가기 위해 백 스택에 추가
                    .commit();
        });
        //여행지 선택 이벤트 연결
        domesitc.setOnClickListener(view1 -> adapter.setLists(DomesticList));
        overseas.setOnClickListener(view1 -> adapter.setLists(OverSeaList));
        newcity.setOnClickListener(view1 -> {
            if(!searchText.isEmpty()){
            //다음 페이지로 연결됩니다.
            nowTripPlace = searchText;
            //여행지 이름(nowTripPlace)를 가지고 다음 페이지로 이동합니다.
            Bundle bundle = new Bundle();
            bundle.putString("nowTripPlace", nowTripPlace);

            // 실제로 이동하고 싶은 프래그먼트로 NextFragment를 대체하세요
            DateSelect nextFragment = new DateSelect();
            nextFragment.setArguments(bundle);

            // 다음 프래그먼트로 이동합니다
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                    .replace(R.id.nav_host_fragment_activity_main, nextFragment)
                    .addToBackStack(null)  // 백 버튼으로 뒤로가기 위해 백 스택에 추가
                    .commitAllowingStateLoss();
        }});
        //검색 이벤트 연결
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchText = search.getText().toString();
                search_list.clear();

                if(searchText.equals("")){
                    adapter.setLists(AllList);
                    //아무 내용도 치지 않았을때
                    newcity.setVisibility(View.GONE);
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    for (int a = 0; a < AllList.size(); a++) {
                        if (AllList.get(a).getName().toLowerCase().contains(searchText.toLowerCase())) {
                            search_list.add(AllList.get(a));
                        }
                        adapter.setLists(search_list);
                        if (search_list.isEmpty()){
                            //검색한 항목이 없을떄
                            newcity.setText(searchText+"으로 여행을 떠나볼까요? ");
                            newcity.setVisibility(View.VISIBLE);
                        }
                        else{
                            //검색된 항목이 있을떄
                            newcity.setVisibility(View.GONE);
                        }
                    }

                }
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("frag", "onCreate: addtrip_addtrip");

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("frag", "onDestroy: addtrip_addtrip");
    }



}