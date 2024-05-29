package com.example.noteapp.Holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;

public class LoaiGhiChuHolder extends RecyclerView.ViewHolder {
    public RelativeLayout relativeLayout;
    public TextView txtID, txtNameLGC;
    public LoaiGhiChuHolder(@NonNull View itemView) {
        super(itemView);
        relativeLayout = itemView.findViewById(R.id.relativelayout_loaighichu);
        txtID = itemView.findViewById(R.id.txtIDLoaiGhiChu_layout);
        txtNameLGC = itemView.findViewById(R.id.txtTenLoaiGhiChu_layout);
    }
}
