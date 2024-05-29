package com.example.noteapp.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.noteapp.Database.Database;
import com.example.noteapp.Models.GhiChu;
import com.example.noteapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class ChiTietGhiChu extends AppCompatActivity {
    EditText edtID, edtTitle, edtNoiDung, edtCreateDate, edtDueDate, edtIDLoaiGhiChu;
    ImageView imageViewTime, imageCamera, imageNote;
    Button btnSua;
    GhiChu ghiChu;
    Database database;
    int PICK_IMAGE_REQUEST = 1;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_ghi_chu);
        setControl();
        setEvent();

    }

    private void setEvent() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Chi tiết ghi chú");
        database = new Database(this);


        ghiChu = (GhiChu) getIntent().getSerializableExtra("noteItem");
        // Xử lý dữ liệu trong myObject
        edtID.setText(String.valueOf(ghiChu.getID()));
        edtTitle.setText(ghiChu.getTitle());
        edtNoiDung.setText(ghiChu.getNoiDung());
        edtCreateDate.setText(ghiChu.getCreateDate());
        edtDueDate.setText(ghiChu.getDueDate());
        byte[] hinhAnh = ghiChu.getHinhAnh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        imageNote.setImageBitmap(bitmap);
        edtIDLoaiGhiChu.setText(String.valueOf(ghiChu.getIDLoaiGhiChu()));

//        Intent intent = getIntent();
//        int id = Integer.parseInt(intent.getStringExtra("ID"));
//        String title = intent.getStringExtra("title");
//        String noiDung = intent.getStringExtra("noiDung");
//        String createDate = intent.getStringExtra("createDate");
//        String dueDate = intent.getStringExtra("dueDate");
//       String image = intent.getStringExtra("hinhAnh");
//        String idLoaiGhiChu = intent.getStringExtra("IDLoaiGhiChu");
//
//// Sau khi nhận dữ liệu, bạn có thể sử dụng chúng để hiển thị trong giao diện người dùng hoặc thực hiện các tác vụ khác.
//
//        edtID.setText(String.valueOf(id));
//        edtTitle.setText(title);
//        edtNoiDung.setText(noiDung);
//        edtCreateDate.setText(createDate);
//        edtDueDate.setText(dueDate);
////        byte[] hinhAnh = image.getBytes();
////        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
////        imageNote.setImageBitmap(bitmap);
//        edtIDLoaiGhiChu.setText(String.valueOf(idLoaiGhiChu));
//        System.out.println(id);

        DateTime();
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = o.getData();
                            uri = intent.getData();
                            imageNote.setImageURI(uri);
                        } else {
                            Toast.makeText(ChiTietGhiChu.this, "No Image Selected!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuaGhiChu();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            try {
                imageNote.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void DateTime() {
        imageViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                // DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ChiTietGhiChu.this,
                        (DatePicker datePicker, int year, int month, int day) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, day);
                            // TimePickerDialog
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    ChiTietGhiChu.this,
                                    (TimePicker timePicker, int hour, int minute) -> {
                                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                                        calendar.set(Calendar.MINUTE, minute);
                                        edtDueDate.setText(day + "/" + (month + 1) + "/" + year + " " + hour + ":" + minute);
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true);
                            timePickerDialog.show();
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(edtTitle.getText().toString())) {
            edtTitle.setError("Vui lòng nhập tiêu đề");
            return false;
        }
        if (TextUtils.isEmpty(edtNoiDung.getText().toString())) {
            edtNoiDung.setError("Vui lòng nhập nội dung");
            return false;
        }
        if (TextUtils.isEmpty(edtDueDate.getText().toString())) {
            edtDueDate.setError("Vui lòng ngày hết hạn");
            return false;
        }
        return true;
    }

    private void SuaGhiChu() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageNote.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageGhiChu = outputStream.toByteArray();

        if (checkInput()) {
            for (GhiChu item : GhiChuActivity.ghiChuList) {
                if (Objects.equals(item.getID(), ghiChu.getID())) {
                    item.setTitle(edtTitle.getText().toString());
                    item.setNoiDung(edtNoiDung.getText().toString());
                    item.setDueDate(edtDueDate.getText().toString());
                    item.setHinhAnh(imageGhiChu);
                    database.UpdateNote(item);
                    GhiChuActivity.ghiChuAdapter.notifyDataSetChanged();
                    //DanhSachGhiChuActivity.ghiChuAdapter.notifyDataSetChanged();
                }
            }
            Toast.makeText(this, "Đã sửa", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
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
        edtID = findViewById(R.id.TextInputEditTextSuaID);
        edtTitle = findViewById(R.id.TextInputEditTextSuaTitle);
        edtNoiDung = findViewById(R.id.TextInputEditTextSuaNoiDung);
        edtCreateDate = findViewById(R.id.TextInputEditTextCreateDate);
        edtDueDate = findViewById(R.id.TextInputEditTextSuaDueDate);
        edtIDLoaiGhiChu = findViewById(R.id.TextInputEditTextIDLoaiGhiChu);
        imageViewTime = findViewById(R.id.imgTime);
        imageCamera = findViewById(R.id.imageCamera);
        imageNote = findViewById(R.id.imageNote);
        btnSua = findViewById(R.id.btnSua);
    }
}