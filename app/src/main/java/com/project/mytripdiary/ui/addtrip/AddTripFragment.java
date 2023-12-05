package com.project.mytripdiary.ui.addtrip;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.project.mytripdiary.MainActivity;
import com.project.mytripdiary.R;
import com.project.mytripdiary.database.Io_traveldata;
import com.project.mytripdiary.ui.addtrip.travellist.TravelListData;
import com.project.mytripdiary.ui.addtrip.travellist.travelListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddTripFragment extends Fragment {

    private AddTripViewModel mViewModel;
    private RecyclerView tripListView;
    private travelListAdapter adapter;
    private ArrayList<TravelListData> Samplelist;
    private ArrayList<TravelListData> dblist;
    private EditText search;
    private TextView triptitle;
    private String nowTripPlace =null;
    private Bitmap nowTripPlaceImg=null;
    private ImageButton toolbarBackbtn;
    private AppCompatButton toolbarbtn;
    ArrayList<TravelListData> search_list;
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        // 갤러리에서 선택한 이미지 처리
                        Uri selectedImageUri = data.getData();
                        nowTripPlaceImg = getImageBitmap(selectedImageUri);
                        nowTripPlace = search.getText().toString();
                        // 여기에서 선택한 이미지를 사용하거나 저장하세요.
                        //데이터베이스에 저장
                        Io_traveldata traveldata = new Io_traveldata();
                        traveldata.insertData(getContext(),nowTripPlace,nowTripPlaceImg);
                        //여행지 이름(nowTripPlace)를 가지고 다음 페이지로 이동합니다.

                        Bundle bundle = new Bundle();
                        bundle.putString("nowTripPlace", nowTripPlace);

                        // 실제로 이동하고 싶은 프래그먼트로 NextFragment를 대체하세요
                        DateSelect nextFragment = new DateSelect();
                        nextFragment.setArguments(bundle);

                        // 다음 프래그먼트로 이동합니다
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_main, nextFragment)
                                .addToBackStack(null)  // 백 버튼으로 뒤로가기 위해 백 스택에 추가
                                .commit();
                    }
                    }
                }

    );
    public static AddTripFragment newInstance() {
        return new AddTripFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        tripListView =view.findViewById(R.id.addtrip_trip_list);
        triptitle = view.findViewById(R.id.addtrip_trip_title);
        toolbarbtn = view.findViewById(R.id.addtrip_toolbarbtn);
        toolbarBackbtn= view.findViewById(R.id.addtrip_toolbar_backbtn);
        //백 버튼 세팅(toolbarBackbtn에 뒤로가기 이벤트를 처리합니다.)
        // 백 버튼 클릭 이벤트 처리
        toolbarBackbtn.setOnClickListener(view1 -> {
            // 뒤로가기 처리
            getParentFragmentManager().popBackStack();
        });
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
        Samplelist = new ArrayList<>();
        search_list = new ArrayList<>();
        String[] travelSample = {"제주","서울","부산","전주","뉴욕","양양"};

        for(int i=0;i<travelSample.length;i++){
            int id = getResources().getIdentifier("img_"+i,"drawable", getContext().getPackageName());
            Bitmap sampleImg = BitmapFactory.decodeResource(getResources(),id);
            Samplelist.add(new TravelListData(travelSample[i],sampleImg));
        }

        //데이터베이스에 저장된 리스트 불러오기
        dblist = (ArrayList<TravelListData>) new Io_traveldata().getAllData(getContext());

        Samplelist.addAll(dblist);
        //아뎁터 불러오기
        adapter = new travelListAdapter(getContext(), Samplelist);
        tripListView.setAdapter(adapter);
        Log.d("Sample",""+adapter.getItemCount());
        for (TravelListData data : Samplelist) {
            Log.d("Samplelist", "Name: " + data.getName());
            Log.d("Samplelist","img_"+data.getImage());
        }
        //아뎁터에 클릭 이벤트 연결
        adapter.setOnItemClickListener(new travelListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, String data) {
                //다음 페이지로 연결됩니다.
                Toast.makeText(getContext(), "여행을 출발합니다 :"+data, Toast.LENGTH_SHORT).show();
                nowTripPlace = data;
                //여행지 이름(nowTripPlace)를 가지고 다음 페이지로 이동합니다.
                Bundle bundle = new Bundle();
                bundle.putString("nowTripPlace", nowTripPlace);

                // 실제로 이동하고 싶은 프래그먼트로 NextFragment를 대체하세요
                DateSelect nextFragment = new DateSelect();
                nextFragment.setArguments(bundle);

                // 다음 프래그먼트로 이동합니다
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, nextFragment)
                        .addToBackStack(null)  // 백 버튼으로 뒤로가기 위해 백 스택에 추가
                        .commit();
            }
        });
        //툴바 버튼 이벤트 연결
        toolbarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toolbarbtn.getText().equals("여행지를 선택해 주세요")){
                    Toast.makeText(getContext(), "여행지를 선택해 주세요", Toast.LENGTH_SHORT).show();
                }
                else if(toolbarbtn.getText().equals("여행지 사진 추가하기")){
                    //갤러리에서 여행지 사진을 추가하는 이벤트를 설정
                    openGallery();
                }
            }
        });

        //검색 이벤트 연결
        search = view.findViewById(R.id.addtrip_trip_searchText);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = search.getText().toString();
                search_list.clear();

                if(searchText.equals("")){
                    adapter.setLists(Samplelist);
                    triptitle.setText("즐거운 여행을 \n시작해보세요");
                    triptitle.setTextColor(Color.BLACK);
                    toolbarbtn.setText("여행지를 선택해 주세요");
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    for (int a = 0; a < Samplelist.size(); a++) {
                        if (Samplelist.get(a).getName().toLowerCase().contains(searchText.toLowerCase())) {
                            search_list.add(Samplelist.get(a));
                        }
                        adapter.setLists(search_list);
                        if (search_list.isEmpty()){
                            //검색한 항목이 없을떄
                            triptitle.setText("새로운 여행지를\n 추가해보세요");
                            triptitle.setTextColor(Color.parseColor("#8DE8AB"));
                            toolbarbtn.setText("여행지 사진 추가하기");
                            toolbarbtn.setTextColor(Color.RED);

                        }
                        else{
                            triptitle.setText("즐거운 여행을 \n시작해보세요");
                            triptitle.setTextColor(Color.BLACK);
                            toolbarbtn.setText("여행지를 선택해 주세요");
                        }
                    }

                }
            }
        });
        return view;
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private Bitmap getImageBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddTripViewModel.class);
        // TODO: Use the ViewModel
    }
    // 갤러리 열기


}