package com.example.noteapp.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;

public class GhiChuDaXoaHolder extends RecyclerView.ViewHolder {
    public CardView cardView;
    public LinearLayout linearLayout;
    public TextView txtTitleLayout, txtNoiDungLayout, txtDeleteTime;
    public GhiChuDaXoaHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.cardView_itemghichu);
        linearLayout = itemView.findViewById(R.id.linerLayout_itemghichu);
        txtTitleLayout = itemView.findViewById(R.id.txtTitleLayout);
        txtNoiDungLayout = itemView.findViewById(R.id.txtNoiDungLayout);
        txtDeleteTime = itemView.findViewById(R.id.txtDeleteTime);
    }
}
