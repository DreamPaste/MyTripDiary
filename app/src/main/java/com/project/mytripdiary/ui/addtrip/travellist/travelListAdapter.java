package com.project.mytripdiary.ui.addtrip.travellist;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mytripdiary.R;

import java.util.ArrayList;

public class travelListAdapter extends RecyclerView.Adapter<travelListAdapter.ViewHolder> {
    private ArrayList<TravelListData> lists;
    Context context;
    //===== [Click 이벤트 구현을 위해 추가된 코드] ==========================
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(int position, String data);
    }

    // OnItemClickListener 참조 변수 선언
    private OnItemClickListener itemClickListener;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }
    //======================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;


        public ViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            image = itemView.findViewById(R.id.addtrip_travel_imgs);
            name = itemView.findViewById(R.id.addtrip_travel_text);

            // 뷰 홀더 생성 시에 클릭 이벤트 처리
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    String data = name.getText().toString();
                    listener.onItemClicked(position, data);
                }
            });
        }
        public TextView getTextView() {
            return name;
        }

        void onBind(TravelListData item){
            image.setImageBitmap(item.getImage());
            name.setText(item.getName());

        }
    }
    @NonNull
    @Override
    public travelListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addtrip_travel_item, parent, false);

        return new ViewHolder(view,itemClickListener);
    }
    @Override
    public void onBindViewHolder(@NonNull travelListAdapter.ViewHolder holder, int position) {
        String name = lists.get(position).name;
        Bitmap img = lists.get(position).image;
        holder.name.setText(name);
        holder.image.setImageBitmap(img);
    }

    @Override
    public int getItemCount() {

        return lists.size();
    }
    public travelListAdapter(Context context, ArrayList<TravelListData> lists) {
        this.context = context;
        this.lists = lists;

    }
    public void setLists(ArrayList<TravelListData> list){
        this.lists =list;
        notifyDataSetChanged();
    }




}
