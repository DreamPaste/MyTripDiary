package com.project.mytripdiary.ui.addtrip.placelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.project.mytripdiary.R;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder> {
    private static List<PlaceListData> placeListDataList;
    private final Context context;
    //===== [Click 이벤트 구현을 위해 추가된 코드] ==========================
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(int position, LatLng data);
    }

    // OnItemClickListener 참조 변수 선언
    private OnItemClickListener itemClickListener;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }
    //======================================================================

    public PlaceListAdapter(List<PlaceListData> placeListDataList, Context context) {
        this.placeListDataList = placeListDataList;
        this.context = context;
    }
    public void setLists(List<PlaceListData> placeListDataList){
        this.placeListDataList =placeListDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addtrip_place_item, parent, false);
        return new ViewHolder(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaceListData placeListData = placeListDataList.get(position);

        // PlaceListData에서 데이터를 가져와서 뷰에 설정
        holder.indexView.setText(String.valueOf(position+1));
        holder.placeName.setText(placeListData.getName());
        holder.placeAdr.setText(placeListData.getAddress());
    }

    @Override
    public int getItemCount() {
        return placeListDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView indexView;
        TextView placeName;
        TextView placeAdr;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // 뷰 초기화
            indexView = itemView.findViewById(R.id.addtrip_map_indexView);
            placeName = itemView.findViewById(R.id.addtrip_map_placeName);
            placeAdr = itemView.findViewById(R.id.addtrip_map_PlaceAdr);

            // 뷰 홀더 생성 시에 클릭 이벤트 처리
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                PlaceListData placeListData = placeListDataList.get(position);
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    LatLng data =new LatLng(placeListData.getX(),placeListData.getY());
                    listener.onItemClicked(position, data);

                }
            });
        }
    }
}
