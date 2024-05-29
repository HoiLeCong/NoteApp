package com.example.noteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.Adapter.GhiChuAdapter;
import com.example.noteapp.ClickListenner.GhiChuClickListener;
import com.example.noteapp.Database.Database;
import com.example.noteapp.Holder.GhiChuHolder;
import com.example.noteapp.Models.GhiChu;
import com.example.noteapp.R;
import com.example.noteapp.TouchHelperListener.RecyclerViewItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GhiChuActivity extends AppCompatActivity implements GhiChuClickListener {

    public static TextView txtTenLoaiGhiChu, txtIDLGC;

    SearchView searchView;
    RecyclerView recyclerViewGhiChu;
    FloatingActionButton actionButtonAdd;
    static GhiChuAdapter ghiChuAdapter;
    static List<GhiChu> ghiChuList = new ArrayList<>();
    static Database database;
    Bundle bundle;
    RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghi_chu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        setControl();
        setEvent();
    }

    private void setEvent() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Danh sách ghi chú");

        database = new Database(this);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            txtIDLGC.setText(String.valueOf(bundle.getInt("IDLoaiGhiChu")));
            txtTenLoaiGhiChu.setText(bundle.getString("tenLoaiGhiChu"));

        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchNote(newText);
                return true;
            }
        });


        actionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GhiChuActivity.this, AddGhiChuActivity.class);
                startActivity(intent);
            }
        });

        RecyclerViewGhiChuLayoutManager(ghiChuList);
        GetDataNoteNotifi();
    }

    private void SearchNote(String newText) {
        ArrayList<GhiChu> ghiChuArrayList = new ArrayList<>();
        for (GhiChu ghiChu : ghiChuList) {
            if (ghiChu.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                ghiChuArrayList.add(ghiChu);
            }
        }
        ghiChuAdapter.SearchData(ghiChuArrayList);
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void GetDataGhiChu() {
        ghiChuList.clear();
        //Cursor data = database.GetData("SELECT GhiChu.IDGhiChu, GhiChu.TenGhiChu, GhiChu.NoiDung, GhiChu.CreateDate, GhiChu.DueDate, GhiChu.IDLoaiGhiChu" +
        //   " FROM GhiChu JOIN LoaiGhiChu ON GhiChu.IDLoaiGhiChu ='" + txtIDLGC.getText().toString() + "'");
        Cursor data = database.GetData("SELECT *" +
                " FROM GhiChu WHERE GhiChu.IDLoaiGhiChu ='" + txtIDLGC.getText().toString() + "' and Complete = 0 and ListDelete = 0");
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

    @SuppressLint("NotifyDataSetChanged")
    public void GetDataNoteNotifi() {
        ghiChuList.clear();
        Cursor data = database.GetData("SELECT * FROM GhiChu WHERE GhiChu.IDLoaiGhiChu ='" + txtIDLGC.getText().toString() + "' and Complete = 0 and ListDelete= 0");
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


                Calendar current = Calendar.getInstance();
                int yearNow = current.get(Calendar.YEAR);
                int monthNow = current.get(Calendar.MONTH) + 1;  // Tháng bắt đầu từ 0
                int dayNow = current.get(Calendar.DAY_OF_MONTH);
                int hourNow = current.get(Calendar.HOUR_OF_DAY); // 24 giờ
                int minuteNow = current.get(Calendar.MINUTE);
                String currentNotifi = dayNow + "/" + monthNow + "/" + yearNow + " " + hourNow + ":" + minuteNow;
                System.out.println(dueDate);
                System.out.println(currentNotifi);
                if (currentNotifi.equals(dueDate)) {
                    showNotification("NoteApp", "Ghi chú đã đến hạn");
                }
            } while (data.moveToNext());
        }
        ghiChuAdapter.notifyDataSetChanged();
    }

    @SuppressLint("MissingPermission")
    private void showNotification(String title, String message) {
        // Tạo Intent để mở NotificationActivity khi thông báo được nhấn
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Nhận NotificationManager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Hiển thị thông báo
        notificationManager.notify(1, builder.build());
    }

    private void RecyclerViewGhiChuLayoutManager(List<GhiChu> ghiChuList) {
        LinearLayoutManager linearLayoutManagerLGC = new LinearLayoutManager(this);
        recyclerViewGhiChu.setLayoutManager(linearLayoutManagerLGC);
        ghiChuAdapter = new GhiChuAdapter(this, ghiChuList, this);
        recyclerViewGhiChu.setAdapter(ghiChuAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerViewGhiChu);
    }

    private void setControl() {
        txtTenLoaiGhiChu = findViewById(R.id.txtTenLoaiGhiChu);
        txtIDLGC = findViewById(R.id.txtIDLGC);
        searchView = findViewById(R.id.searchGhiChu);
        recyclerViewGhiChu = findViewById(R.id.recyclerViewGhiChu);
        actionButtonAdd = findViewById(R.id.floatingAdd);
        rootView = findViewById(R.id.rootView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_ghichu, menu);
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

    @Override
    public void OnItemClick(GhiChu ghiChu) {
        Intent intent = new Intent(GhiChuActivity.this, ChiTietGhiChu.class);
        intent.putExtra("noteItem", ghiChu);
//        intent.putExtra("ID", ghiChu.getID());
//        intent.putExtra("title", ghiChu.getTitle());
//        intent.putExtra("noiDung", ghiChu.getNoiDung());
//        intent.putExtra("createDate", ghiChu.getCreateDate());
//        intent.putExtra("dueDate", ghiChu.getDueDate());
//        intent.putExtra("hinhAnh", ghiChu.getHinhAnh());
//
//        intent.putExtra("IDLoaiGhiChu", ghiChu.getIDLoaiGhiChu());

        startActivity(intent);

    }

    @Override
    public void OnItemLongClick(GhiChu ghiChu, int ID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hoàn thành");
        builder.setMessage("Bạn đã hoàn thành ghi chú " + ghiChu.getTitle() );
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GhiChu ghiChuComplete = new GhiChu();
                ghiChuComplete.setID(ID);
                ghiChuComplete.setComplete(1);
                database.NotesComplete(ghiChuComplete);
                GetDataGhiChu();
                dialogInterface.dismiss();
                Toast.makeText(GhiChuActivity.this, "Đã hoàn thành", Toast.LENGTH_SHORT).show();
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
        if (holder instanceof GhiChuHolder){
            int delete =  ghiChuList.get(holder.getAdapterPosition()).getID();
            String noteDelete = ghiChuList.get(holder.getAdapterPosition()).getTitle();

            final GhiChu ghiChuDelete =ghiChuList.get(holder.getAdapterPosition());

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
                    Toast.makeText(GhiChuActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();

//            Snackbar snackbar = Snackbar.make(rootView, noteDelete + " remove success!",Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ghiChuAdapter.undoItem(ghiChuDelete, indexDelete);
//                    if (indexDelete ==0 || indexDelete == ghiChuList.size() -1){
//                        recyclerViewGhiChu.scrollToPosition(indexDelete);
//                    }
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }


}