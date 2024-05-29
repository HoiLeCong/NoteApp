package com.example.noteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.Adapter.LoaiGhiChuAdapter;
import com.example.noteapp.ClickListenner.LoaiGhiChuClickListener;
import com.example.noteapp.Database.Database;
import com.example.noteapp.Models.LoaiGhiChu;
import com.example.noteapp.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaiGhiChuClickListener {
    TextView txtDanhSachGhiChu;
    RecyclerView recyclerViewLoaiGhiChu;
    List<LoaiGhiChu> loaiGhiChuList = new ArrayList<>();
    LoaiGhiChuAdapter loaiGhiChuAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContol();
        setEvent();
    }

    private void setEvent() {
        database = new Database(this);
        RecyclerViewLoaiGhiChuLayoutManager(loaiGhiChuList);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8893F8"))); // Đặt màu sắc

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnThemLoaiGhiChu:
                        AddLoaiGhiChu();
                        break;
                    case R.id.mnComplete:
                        Intent intentCompelte = new Intent(MainActivity.this, GhiChuHoanThanh.class);
                        startActivity(intentCompelte);
                        break;
                    case R.id.mnGhiChuDaXoa:
                        Intent intent = new Intent(MainActivity.this, DaXoaGanDayActivity.class);
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
        DanhSachGhiChu();
        GetDataCV();
    }

    private void DanhSachGhiChu() {
        txtDanhSachGhiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DanhSachGhiChuActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    private void RecyclerViewLoaiGhiChuLayoutManager(List<LoaiGhiChu> loaiGhiChuList) {
        LinearLayoutManager linearLayoutManagerLGC = new LinearLayoutManager(this);
        recyclerViewLoaiGhiChu.setLayoutManager(linearLayoutManagerLGC);
        loaiGhiChuAdapter = new LoaiGhiChuAdapter(this, loaiGhiChuList, this);
        recyclerViewLoaiGhiChu.setAdapter(loaiGhiChuAdapter);
        //set gạch dưới
//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerViewLoaiGhiChu.addItemDecoration(itemDecoration);
    }

    public static EditText edtID, edtName;

    private void GetDataCV() {
        Cursor data = database.GetData("SELECT * FROM LoaiGhiChu");
        loaiGhiChuList.clear();
        while (data.moveToNext()) {
            String name = data.getString(1);
            int id = data.getInt(0);
            loaiGhiChuList.add(new LoaiGhiChu(id, name));
        }
        loaiGhiChuAdapter.notifyDataSetChanged();
    }

    public void AddLoaiGhiChu() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addloaighichu);

        edtID = dialog.findViewById(R.id.txtInpuetEdtID);
        edtName = dialog.findViewById(R.id.txtInpuetEdtName);
        edtName.requestFocus();
        ImageView imageViewHuy = dialog.findViewById(R.id.imgViewHuy);
        Button btnThem;
        btnThem = dialog.findViewById(R.id.btnThem);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edtName.getText().toString())) {
                    LoaiGhiChu loaiGhiChu = new LoaiGhiChu();
                    loaiGhiChu.setTenLoaiGhiChu(edtName.getText().toString());
                    database.InsertNoteType(loaiGhiChu);
                    GetDataCV();
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Đã thêm " + edtName.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    edtName.setError("Vui lòng nhập loại ghi chú");
                }
            }
        });


        imageViewHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void setContol() {
        txtDanhSachGhiChu = findViewById(R.id.txtDanhSachGhiChu);
        recyclerViewLoaiGhiChu = findViewById(R.id.recyclerViewLoaiGhiChu);
        navigationView = findViewById(R.id.NavigationViewMain);
        drawerLayout = findViewById(R.id.drawerlayout);
    }

    @Override
    public void onItemClick(LoaiGhiChu loaiGhiChu) {
        Intent intent = new Intent(MainActivity.this, GhiChuActivity.class);
        startActivity(intent);

    }

    @Override
    public void onItemLongClick(LoaiGhiChu loaiGhiChu) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sualoaighichu);
        EditText edtSuaID, edtSuaName;
        edtSuaID = dialog.findViewById(R.id.txtSuaInputEdtID);
        edtSuaName = dialog.findViewById(R.id.txtSuaInputEdtName);

        edtSuaName.requestFocus();
        ImageView imageViewHuy = dialog.findViewById(R.id.imgViewHuySua);
        Button btnSua, btnXoa;

        btnSua = dialog.findViewById(R.id.btnSua);
        btnXoa = dialog.findViewById(R.id.btnXoa);

        edtSuaID.setText(String.valueOf(loaiGhiChu.getID()));
        edtSuaName.setText(loaiGhiChu.getTenLoaiGhiChu());

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.DeleteNoteType(loaiGhiChu);
                loaiGhiChuAdapter.notifyDataSetChanged();
                dialog.dismiss();
                GetDataCV();
                Toast.makeText(MainActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTen = edtSuaName.getText().toString();
                loaiGhiChu.setTenLoaiGhiChu(newTen);
                database.UpdateNoteType(loaiGhiChu);
                loaiGhiChuAdapter.notifyDataSetChanged();
                dialog.dismiss();
                GetDataCV();
                Toast.makeText(MainActivity.this, "Đã sửa", Toast.LENGTH_SHORT).show();
            }
        });
        imageViewHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}