package com.example.noteapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Activity.GhiChuActivity;
import com.example.noteapp.ClickListenner.LoaiGhiChuClickListener;
import com.example.noteapp.Holder.LoaiGhiChuHolder;
import com.example.noteapp.Models.LoaiGhiChu;
import com.example.noteapp.R;

import java.util.List;

public class LoaiGhiChuAdapter extends RecyclerView.Adapter<LoaiGhiChuHolder> {
    Context context;
    List<LoaiGhiChu> loaiGhiChuList;
    LoaiGhiChuClickListener loaiGhiChuClickListener;

    public LoaiGhiChuAdapter(Context context, List<LoaiGhiChu> loaiGhiChuList, LoaiGhiChuClickListener loaiGhiChuClickListener) {
        this.context = context;
        this.loaiGhiChuList = loaiGhiChuList;
        this.loaiGhiChuClickListener = loaiGhiChuClickListener;
    }

    @NonNull
    @Override
    public LoaiGhiChuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_loaighichu, parent, false);
        return new LoaiGhiChuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiGhiChuHolder holder, @SuppressLint("RecyclerView") int position) {
        LoaiGhiChu loaiGhiChu = loaiGhiChuList.get(position);
        holder.txtID.setText(String.valueOf(loaiGhiChu.getID()));
        holder.txtNameLGC.setText(loaiGhiChu.getTenLoaiGhiChu());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GhiChuActivity.class);
                intent.putExtra("IDLoaiGhiChu", loaiGhiChuList.get(holder.getAdapterPosition()).getID());
                intent.putExtra("tenLoaiGhiChu", loaiGhiChuList.get(holder.getAdapterPosition()).getTenLoaiGhiChu());
                context.startActivity(intent);
            }
        });
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                loaiGhiChuClickListener.onItemLongClick(loaiGhiChuList.get(position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return loaiGhiChuList.size();
    }
}
