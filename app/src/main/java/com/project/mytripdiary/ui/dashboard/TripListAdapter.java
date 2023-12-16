package com.project.mytripdiary.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mytripdiary.R;
import com.project.mytripdiary.database.DbCreate_Diary;
import com.project.mytripdiary.ui.addtrip.travellist.travelListAdapter;

import java.util.List;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    private List<TripsList> tripsList;
    static Context context;
    //===== [Click 이벤트 구현을 위해 추가된 코드] ==========================
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(int position, String data);
    }

    // OnItemClickListener 참조 변수 선언
    private TripListAdapter.OnItemClickListener itemClickListener;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (TripListAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }
    //======================================================================

    // 생성자
    public TripListAdapter(List<TripsList> tripsList, Context context) {
        this.tripsList = tripsList;
        this.context = context;
    }

    // ViewHolder 클래스
    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tripNameTextView;
        TextView DateTextView;
        ImageButton editButton;

        public TripViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            tripNameTextView = itemView.findViewById(R.id.diary_title);
            DateTextView = itemView.findViewById(R.id.diary_date);
            editButton = itemView.findViewById(R.id.diary_edit);




        }
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.triplist_item, parent, false);
        return new TripViewHolder(view, (OnItemClickListener) itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripsList currentTrip = tripsList.get(position);
        DbCreate_Diary  dbHelper = new DbCreate_Diary(context);

        // 데이터를 ViewHolder에 바인딩
        holder.tripNameTextView.setText(currentTrip.getTrip());
        holder.DateTextView.setText(currentTrip.getStartDate()+"~"+currentTrip.getEndDate());

        // editButton 클릭 이벤트 리스너 설정
        holder.editButton.setOnClickListener(view -> {
            int itemPosition = holder.getAdapterPosition();
            // 여기에 edit 버튼을 눌렀을 때의 동작을 추가
            if (itemPosition != RecyclerView.NO_POSITION && itemClickListener != null) {
                //전달하고 싶은 내용을 다이얼로그에 띄움
                DiaryInputDialog diaryInputDialog = new DiaryInputDialog(context);
                diaryInputDialog.show();
                diaryInputDialog.setDialogTitle(currentTrip.getTrip());
                diaryInputDialog.setDialogDates(holder.DateTextView.getText().toString());

                String  name =  currentTrip.getTrip()+","+
                        currentTrip.getStartDate()+","+
                        currentTrip.getEndDate();
                String content  = dbHelper.getTextByName(name);

                if(content!=null)
                    diaryInputDialog.setDialogText(content);

                diaryInputDialog.setOnSaveClickListener(inputText -> {
                    // inputText를 필요에 따라 처리하세요 (예: 변수에 저장)
                    // 여기서 inputText는 사용자가 입력한 텍스트입니다.
                    dbHelper.updateEntry(name,inputText);
                    itemClickListener.onItemClicked(itemPosition, inputText);
                    Log.d("dash", "content: "+name+"text: "+inputText);
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    // 데이터 업데이트 메서드 (필요한 경우)
    public void setTripsList(List<TripsList> tripsList) {
        this.tripsList = tripsList;
        notifyDataSetChanged(); // 데이터셋이 변경되었음을 알려 갱신
    }
}
