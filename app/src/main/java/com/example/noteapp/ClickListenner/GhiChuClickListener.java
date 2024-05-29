package com.example.noteapp.ClickListenner;

import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Models.GhiChu;

public interface GhiChuClickListener {
    void OnItemClick(GhiChu ghiChu);
    void OnItemLongClick(GhiChu ghiChu, int id);
    void onSwipe(RecyclerView.ViewHolder holder);
}
