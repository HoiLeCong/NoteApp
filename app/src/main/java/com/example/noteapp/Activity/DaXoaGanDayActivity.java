package com.example.noteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.noteapp.Adapter.GhiChuAdapter;
import com.example.noteapp.Adapter.GhiChuDaXoaAdapter;
import com.example.noteapp.ClickListenner.GhiChuClickListener;
import com.example.noteapp.Database.Database;
import com.example.noteapp.Models.GhiChu;
import com.example.noteapp.R;

import java.util.ArrayList;
import java.util.List;

public class DaXoaGanDayActivity extends AppCompatActivity implements GhiChuClickListener{
    RecyclerView recyclerView;
    GhiChuAdapter ghiChuAdapter;
    List<GhiChu> ghiChuList = new ArrayList<>();
    Database database;
    GhiChu ghiChu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_xoa_gan_day);

        setControl();
        setEvent();
    }

    private void setEvent() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ghi chú đã xóa");
        database = new Database(this);
        RecyclerViewListGhiChu();
        GetDataGhiChu();
    }

    private void RecyclerViewListGhiChu() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ghiChuAdapter = new GhiChuAdapter(this, ghiChuList, this);
        recyclerView.setAdapter(ghiChuAdapter);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void GetDataGhiChu() {
        ghiChuList.clear();
        Cursor data = database.GetData("SELECT * FROM GhiChu WHERE ListDelete = 1");
        if (data.moveToFirst()) {
            do {
                int id = data.getInt(0);
                String title = data.getString(1);
                String noiDung = data.getString(2);
                String createDate = data.getString(3);
                String dueDate = data.getString(4);
                byte[] hinhAnh = data.getBlob(5);
                int complete = data.getInt(6);
                int listDelete = data.getInt(7);
                int idLGC = data.getInt(8);
                ghiChuList.add(new GhiChu(id, title, noiDung, createDate, dueDate, hinhAnh, complete,listDelete, idLGC));
            } while (data.moveToNext());
        }
        ghiChuAdapter.notifyDataSetChanged();
    }
    @Override
    public void OnItemClick(GhiChu ghiChu) {

    }

    @Override
    public void OnItemLongClick(GhiChu ghiChu, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa ghi chú " + ghiChu.getTitle() + " không!");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GhiChu ghiChuComplete = new GhiChu();
                ghiChuComplete.setID(id);
                database.DeleteNote(ghiChuComplete);
                GetDataGhiChu();
                dialogInterface.dismiss();
                Toast.makeText(DaXoaGanDayActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder holder) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclerViewGhiChu);
    }


}