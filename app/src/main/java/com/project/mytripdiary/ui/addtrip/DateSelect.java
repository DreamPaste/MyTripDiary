package com.project.mytripdiary.ui.addtrip;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.mytripdiary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class DateSelect extends Fragment {

   private    ImageButton toolbarBackbtn;
    private TextView startDate;
    private TextView endDate;
    private AppCompatButton toolbarbtn;
    private String start,end;
    private CalendarView calendarView;
    private Calendar selectedStartDate;
    private Calendar selectedEndDate;
    private String nowTripPlace;
    public DateSelect() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomNavigation(true);
        Log.d("frag", "onCreate: addtrip_dateselect");
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AtomicReference<Bundle> bundle = new AtomicReference<>(getArguments());
        if (bundle.get() != null) {
             nowTripPlace = bundle.get().getString("nowTripPlace");
            // 이제 nowTripPlace 변수에 이전 프래그먼트에서 전달한 여행지 정보가 들어 있습니다.
            // TODO: 이전 프래그먼트에서 전달한 여행지 정보 사용하기

        }
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_date_select, container, false);
        toolbarBackbtn= view.findViewById(R.id.addtrip_toolbar_backbtn);
        startDate = view.findViewById(R.id.addtrip_startdate);
        endDate = view.findViewById(R.id.addtrip_endDate);
        calendarView = view.findViewById(R.id.date_picker);
        toolbarbtn = view.findViewById(R.id.addtrip_toolbarbtn);
        //nav를 숨김


        //백 버튼 세팅(toolbarBackbtn에 뒤로가기 이벤트를 처리합니다.)
        // 백 버튼 클릭 이벤트 처리
        toolbarBackbtn.setOnClickListener(view1 -> {
            // 뒤로가기 처리
            getParentFragmentManager().popBackStack();
        });
        //시작 날짜를 선택하면 출발 날짜에 표시되고 다음 날짜를 선택하면 도착 날짜에 표시되는 코드
        // CalendarView에서 날짜 선택 이벤트 처리
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            if (selectedStartDate == null) {
                // 출발 날짜 선택
                selectedStartDate = selectedDate;
                startDate.setText("출발 날짜: " + formatDate(selectedStartDate));
                endDate.setText("도착 날짜: ");
            }
            else if(selectedStartDate  != null && selectedEndDate != null){
                selectedStartDate = selectedDate;
                startDate.setText("출발 날짜: " + formatDate(selectedStartDate));
                endDate.setText("도착 날짜: ");
                selectedEndDate = null;
            }
            else {
                // 도착 날짜 선택
                selectedEndDate = selectedDate;
                if(selectedStartDate.after(selectedEndDate)){
                    Toast.makeText(getContext(), "이전 날짜는 선택할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    selectedEndDate = null;
                }
                else{
                    endDate.setText("도착 날짜: " + formatDate(selectedEndDate));
                }
            }

        });
        toolbarbtn.setText("여행 장소 찾아보기");
        toolbarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedStartDate  != null && selectedEndDate != null){
                    // Bundle 생성
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("nowTripPlace", nowTripPlace);
                    bundle1.putString("startDate", formatDate(selectedStartDate));
                    bundle1.putString("endDate",formatDate(selectedEndDate));
                    // 다음 프래그먼트 생성
                    place_map nextFragment = new place_map();
                    // Bundle을 프래그먼트에 전달
                    nextFragment.setArguments(bundle1);
                    // FragmentTransaction을 사용하여 다음 프래그먼트로 이동
                    getParentFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                            .setReorderingAllowed(true) // 이 부분을 추가
                            .replace(R.id.nav_host_fragment_activity_main, nextFragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss();

            } else{
                Toast.makeText(getContext(), "날짜 지정이 안되어있습니다.", Toast.LENGTH_SHORT).show();
            }
        }});


        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.nav_view);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("frag", "ondestroy: addtrip_dateselect");

    }

    }