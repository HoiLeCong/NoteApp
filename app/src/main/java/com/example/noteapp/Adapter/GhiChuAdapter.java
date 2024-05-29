package com.example.noteapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Activity.ChiTietGhiChu;
import com.example.noteapp.ClickListenner.GhiChuClickListener;
import com.example.noteapp.Holder.GhiChuHolder;
import com.example.noteapp.Models.GhiChu;
import com.example.noteapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GhiChuAdapter extends RecyclerView.Adapter<GhiChuHolder> {
    Context context;
    List<GhiChu> ghiChuList;
    GhiChuClickListener ghiChuClickListener;


    public GhiChuAdapter(Context context, List<GhiChu> ghiChuList, GhiChuClickListener ghiChuClickListener) {
        this.context = context;
        this.ghiChuList = ghiChuList;
        this.ghiChuClickListener = ghiChuClickListener;
    }

    @NonNull
    @Override
    public GhiChuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_ghichu, parent, false);
        return new GhiChuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GhiChuHolder holder, @SuppressLint("RecyclerView") int position) {
        GhiChu ghiChu = ghiChuList.get(position);
        holder.txtTitleLayout.setText(ghiChu.getTitle());
        holder.txtTitleLayout.setSelected(true);
        holder.txtNoiDungLayout.setText(ghiChu.getNoiDung());
        holder.txtDueDateLayout.setText(ghiChu.getDueDate());
        holder.txtDueDateLayout.setSelected(true);
        byte[] hinhAnh = ghiChu.getHinhAnh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        holder.imageView.setImageBitmap(bitmap);


        int color_code = getRamdomColor();
        holder.linearLayout.setBackgroundColor(holder.itemView.getResources().getColor(color_code, null));

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              ghiChuClickListener.OnItemClick(ghiChuList.get(position));
//                Intent intent = new Intent(context, ChiTietGhiChu.class);
//                intent.putExtra("ID", ghiChuList.get(holder.getAdapterPosition()).getID());
//                intent.putExtra("title", ghiChuList.get(holder.getAdapterPosition()).getTitle());
//                intent.putExtra("noiDung", ghiChuList.get(holder.getAdapterPosition()).getNoiDung());
//                intent.putExtra("createDate", ghiChuList.get(holder.getAdapterPosition()).getCreateDate());
//                intent.putExtra("dueDate", ghiChuList.get(holder.getAdapterPosition()).getDueDate());
//                intent.putExtra("hinhAnh", ghiChuList.get(holder.getAdapterPosition()).getHinhAnh());
//                intent.putExtra("IDLoaiGhiChu", ghiChuList.get(holder.getAdapterPosition()).getIDLoaiGhiChu());
//                context.startActivity(intent);
            }
        });
        holder.frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ghiChuClickListener.OnItemLongClick(ghiChuList.get(position), ghiChu.getID());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return ghiChuList.size();
    }

    private int getRamdomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }

    public void SearchData(ArrayList<GhiChu> ghiChuArrayList){
        ghiChuList = ghiChuArrayList;
        notifyDataSetChanged();
    }
    public void removeItem(int index){
        ghiChuList.remove(index);
        notifyItemRemoved(index);
    }
    public void  undoItem(GhiChu ghiChu, int index){
        ghiChuList.add(index, ghiChu);
        notifyItemInserted(index);
    }
}
