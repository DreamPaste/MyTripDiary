package com.project.mytripdiary.ui.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.project.mytripdiary.R;


public class DiaryInputDialog extends Dialog{
    private TextView titleTextView;
    private TextView datesTextView;
    private EditText editText;
    private Button btnSave;

    private OnSaveClickListener onSaveClickListener;

    public DiaryInputDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_input_dialog);
        titleTextView = findViewById(R.id.dialog_title);
        datesTextView = findViewById(R.id.dialog_dates);
        editText = findViewById(R.id.dialog_editText);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = editText.getText().toString().trim();
                if (onSaveClickListener != null) {
                    onSaveClickListener.onSaveClick(inputText);
                }
                dismiss();
            }
        });
    }
    public void setDialogText(String text) {editText.setText(text);
    }
    public void setDialogTitle(String title) {
        titleTextView.setText(title);
    }

    public void setDialogDates(String dates) {
        datesTextView.setText(dates);
    }

    public void setOnSaveClickListener(OnSaveClickListener onSaveClickListener) {
        this.onSaveClickListener = onSaveClickListener;
    }

    public interface OnSaveClickListener {
        void onSaveClick(String inputText);
    }




}
