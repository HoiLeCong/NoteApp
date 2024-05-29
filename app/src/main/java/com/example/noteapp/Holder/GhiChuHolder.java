package com.example.noteapp.Holder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;

public class GhiChuHolder extends RecyclerView.ViewHolder {
    public FrameLayout frameLayout;
    public LinearLayout linearLayout;
    public TextView txtTitleLayout, txtNoiDungLayout, txtDueDateLayout;
    public ImageView imageView;
    public GhiChuHolder(@NonNull View itemView) {
        super(itemView);
        frameLayout = itemView.findViewById(R.id.cardView_itemghichu);
        linearLayout = itemView.findViewById(R.id.linerLayout_itemghichu);
        txtTitleLayout = itemView.findViewById(R.id.txtTitleLayout);
        txtNoiDungLayout = itemView.findViewById(R.id.txtNoiDungLayout);
        txtDueDateLayout = itemView.findViewById(R.id.txtCreateDateLayout);
        imageView = itemView.findViewById(R.id.imageNoteLayout);
    }
}
