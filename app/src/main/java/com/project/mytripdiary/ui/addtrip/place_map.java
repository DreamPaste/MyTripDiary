package com.project.mytripdiary.ui.addtrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.mytripdiary.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class place_map extends Fragment implements OnMapReadyCallback {


    private String nowTripPlace;
    private String startDate;
    private String endDate;
    private GoogleMap googleMap;
    private MapView mapView =null;
    private RecyclerView placeListRecyclerView;
    private TextView listTitle;
    private PlacesClient placesClient;
    private RelativeLayout searchBtn;
    private ImageButton toolbarBackBtn;
    private AppCompatButton toolbarBtn;
    private  FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_place_map, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Bundle에서 여행지 정보, 출발 날짜, 도착 날짜를 가져옴
            nowTripPlace = bundle.getString("nowTripPlace");
            startDate = bundle.getString("startDate");
            endDate = bundle.getString("endDate");
        }
        hideBottomNavigation(true);
        //id찾기-------------------------
        placeListRecyclerView = view.findViewById(R.id.addtrip_map_placeList);
        listTitle = view.findViewById(R.id.addtrip_map_listTitle);
        searchBtn = view.findViewById(R.id.addtrip_map_searchWrap);
        toolbarBackBtn =view.findViewById(R.id.addtrip_toolbar_backbtn);
        toolbarBtn =view.findViewById(R.id.addtrip_toolbarbtn);
        mapView =view.findViewById(R.id.addtrip_map_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        //클릭 리스너 연결-----------------
        //Place Autocomplete 인텐드 추가
        searchBtn.setOnClickListener(this::startSearch);
        //백 버튼 세팅(toolbarBackbtn에 뒤로가기 이벤트를 처리합니다.)
        // 백 버튼 클릭 이벤트 처리
        toolbarBackBtn.setOnClickListener(view1 ->
            getParentFragmentManager().popBackStack());
        //틀바 버튼 클릭 이벤트
        toolbarBtn.setOnClickListener(view1 ->{
            //저장 완료 기능 구현

        });
        // Google Places API 초기화
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyDsBvulnayrpcOHyjPSFBkRvTWzS7mt3i4");
        }
        placesClient = Places.createClient(getContext());
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        // return after the user has made a selection.


        return view;

    }
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        // 맵이 준비되었을 때 호출되는 콜백에서 기본 위치를 서울로 지정합니다.
        LatLng seoul = new LatLng(37.5665, 126.9780);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10));  // 줌 레벨을 조절합니다.
    }

    // 메서드 내에서 검색한 위치를 받아와서 지도에 표시하는 코드 추가
    private void showLocationOnMap(Place place) {
        Log.i("maps","ff0");
        if (googleMap != null && place != null && place.getLatLng()!=null) {
            LatLng location = place.getLatLng();
             
            // 기존 마커 제거
            googleMap.clear();

            // 선택한 위치에 새로운 마커 표시

            googleMap.addMarker(new MarkerOptions().position(location).title(place.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    // Autocomplete를 통해 Place를 가져오고 이를 mapView에 마커로 표시하는 메서드
    private void handlePlaceSelection(Intent data) {
        Place place = Autocomplete.getPlaceFromIntent(data);
        Log.i("maps", "Place: " + place.getName() + ": " + place.getAddress()+":"+place.getLatLng());

        // 검색한 위치를 지도에 표시
        showLocationOnMap(place);
    }

    // Autocomplete 인텐트를 실행하는 메서드
    public void startSearch(View view) {
        // Start the autocomplete intent.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getContext());

        startAutocomplete.launch(intent);
    }

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        // Autocomplete 결과를 처리하는 메서드 호출
                        handlePlaceSelection(data);
                    }
                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(result.getData());
                    Log.i("maps", status.getStatusMessage());
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Log.i("maps", "User canceled autocomplete");
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideBottomNavigation(false);
    }
    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.nav_view);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}

