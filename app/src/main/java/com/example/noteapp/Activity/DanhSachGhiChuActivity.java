package com.example.noteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.noteapp.Adapter.GhiChuAdapter;
import com.example.noteapp.ClickListenner.GhiChuClickListener;
import com.example.noteapp.Database.Database;
import com.example.noteapp.Holder.GhiChuHolder;
import com.example.noteapp.Models.GhiChu;
import com.example.noteapp.R;
import com.example.noteapp.TouchHelperListener.RecyclerViewItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class DanhSachGhiChuActivity extends AppCompatActivity implements GhiChuClickListener {
    SearchView searchView;
    RecyclerView recyclerView;
    static Database database;
    static List<GhiChu> ghiChuList = new ArrayList<>();
    static GhiChuAdapter ghiChuAdapter;
    Cursor data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_ghi_chu);
        setControl();
        setEvent();
    }

    private void setEvent() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Danh sách ghi chú");
        RecyclerViewDanhSach();
        GetDataGhiChu();
    }

    private void RecyclerViewDanhSach() {
        database = new Database(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));
        ghiChuAdapter = new GhiChuAdapter(this, ghiChuList, this);
        recyclerView.setAdapter(ghiChuAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchData(newText);
                return true;
            }
        });
    }

    private void SearchData(String newText) {
        ArrayList<GhiChu> ghiChuArrayList = new ArrayList<>();
        for (GhiChu ghiChu : ghiChuList) {
            if (ghiChu.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                ghiChuArrayList.add(ghiChu);
            }
        }
        ghiChuAdapter.SearchData(ghiChuArrayList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_danhsachghichu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.mnSortAZ:
                SortAZ();
                break;
            case R.id.mnSortZA:
                SortZA();
                break;
            case R.id.mnSortDueDate:
                SortDueDate();
                break;
            case R.id.mnSortDueDateL:
                SortDueDateL();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SortDueDateL() {
        data = database.GetData("SELECT * FROM GhiChu WHERE Complete = 0 and ListDelete = 0 ORDER BY DueDate DESC");
        CursorData();
    }

    private void SortDueDate() {
        data = database.GetData("SELECT * FROM GhiChu WHERE Complete = 0 and ListDelete = 0 ORDER BY DueDate ASC");
        CursorData();
    }

    public void CursorData() {
        ghiChuList.clear();
        while (data.moveToNext()) {
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
        }
        ghiChuAdapter.notifyDataSetChanged();
    }

    private void SortZA() {
        data = database.GetData("SELECT * FROM GhiChu WHERE Complete = 0 and ListDelete = 0 ORDER BY TenGhiChu DESC");
        CursorData();
    }

    private void SortAZ() {
        data = database.GetData("SELECT * FROM GhiChu WHERE Complete = 0 and ListDelete = 0 ORDER BY TenGhiChu ASC");
        CursorData();
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void GetDataGhiChu() {
        Cursor data = database.GetData("SELECT * FROM GhiChu WHERE Complete = 0 AND ListDelete = 0 ORDER BY IDGhiChu DESC");
        ghiChuList.clear();
        while (data.moveToNext()) {
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
        }
        ghiChuAdapter.notifyDataSetChanged();
    }

    private void setControl() {
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerViewGhiChu);
    }

    @Override
    public void OnItemClick(GhiChu ghiChu) {
        Intent intent = new Intent(DanhSachGhiChuActivity.this, ChiTietGhiChu.class);
        intent.putExtra("noteItem", ghiChu);
        startActivity(intent);
    }

    @Override
    public void OnItemLongClick(GhiChu ghiChu, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hoàn thành");
        builder.setMessage("Bạn đã hoàn thành ghi chú " + ghiChu.getTitle() );
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GhiChu ghiChuComplete = new GhiChu();
                ghiChuComplete.setID(id);
                ghiChuComplete.setComplete(1);
                database.NotesComplete(ghiChuComplete);
                GetDataGhiChu();
                dialogInterface.dismiss();
                Toast.makeText(DanhSachGhiChuActivity.this, "Đã hoàn thành", Toast.LENGTH_SHORT).show();
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
        if (holder instanceof GhiChuHolder) {
            int delete =  ghiChuList.get(holder.getAdapterPosition()).getID();
            String noteDelete = ghiChuList.get(holder.getAdapterPosition()).getTitle();

            final GhiChu ghiChuDelete = ghiChuList.get(holder.getAdapterPosition());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn có muốn xóa ghi chú " + noteDelete + " không!");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ghiChuDelete.setID(delete);
                    ghiChuDelete.setListDelete(1);
                    database.NoteListDelete(ghiChuDelete);
                    GetDataGhiChu();
                    dialogInterface.dismiss();
                    Toast.makeText(DanhSachGhiChuActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
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
    }
}