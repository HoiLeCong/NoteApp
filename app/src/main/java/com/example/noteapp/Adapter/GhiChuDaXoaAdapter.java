package com.example.noteapp.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.ClickListenner.GhiChuClickListener;
import com.example.noteapp.Holder.GhiChuDaXoaHolder;
import com.example.noteapp.Models.GhiChu;
import com.example.noteapp.R;

import java.util.List;

public class GhiChuDaXoaAdapter extends RecyclerView.Adapter<GhiChuDaXoaHolder> {
    Context context;
    List<GhiChu> ghiChuList;
    GhiChuClickListener listener;


    public GhiChuDaXoaAdapter(Context context, List<GhiChu> ghiChuList, GhiChuClickListener listener) {
        this.context = context;
        this.ghiChuList = ghiChuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GhiChuDaXoaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_ghichudaxoa, parent, false);
        return new GhiChuDaXoaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GhiChuDaXoaHolder holder, int position) {
        GhiChu ghiChu = ghiChuList.get(position);
        holder.txtTitleLayout.setText(ghiChu.getTitle());
        holder.txtNoiDungLayout.setText(ghiChu.getNoiDung());
        holder.txtDeleteTime.setText(ghiChu.getDueDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return ghiChuList.size();
    }
}
