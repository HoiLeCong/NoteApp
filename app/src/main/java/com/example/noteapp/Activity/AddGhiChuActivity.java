package com.example.noteapp.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.noteapp.Database.Database;
import com.example.noteapp.Models.GhiChu;
import com.example.noteapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddGhiChuActivity extends AppCompatActivity {

    static EditText edtIDGhiChu, edtTitle, edtNoiDung, edtCreateDate, edtDueDate, edtIDLoaiGhiChu;
    ImageView imgTime;
    Database database;
    String idLoaiGhiChu = "";
    ImageView imageCamera, imageNote;
    int PICK_IMAGE_REQUEST = 1;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ghi_chu);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Thêm ghi chú");
        database = new Database(this);

        DateTime();

        idLoaiGhiChu = GhiChuActivity.txtIDLGC.getText().toString();
        edtIDLoaiGhiChu.setText(String.valueOf(Integer.parseInt(idLoaiGhiChu)));
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = o.getData();
                            uri = intent.getData();
                            imageNote.setImageURI(uri);
                        } else {
                            Toast.makeText(AddGhiChuActivity.this, "No Image Selected!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageNote.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void KiemTraPhienBan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(AddGhiChuActivity.this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED)

                ActivityCompat.requestPermissions(AddGhiChuActivity.this, new String[]
                        {Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
    }

    public void DateTime() {
        imgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                // DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddGhiChuActivity.this,
                        (DatePicker datePicker, int year, int month, int day) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, day);
                            // TimePickerDialog
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    AddGhiChuActivity.this,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addghichu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BanNhap();
                break;
            case R.id.menuXong:
                ThemGhiChu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void BanNhap() {
        if (!TextUtils.isEmpty(edtTitle.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bản nháp");
            builder.setMessage("Bạn có muốn lưu bản nháp này không");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageNote.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] imageGhiChu = outputStream.toByteArray();
                    GhiChu ghiChu = new GhiChu();
                    ghiChu.setTitle(edtTitle.getText().toString());
                    ghiChu.setNoiDung(edtNoiDung.getText().toString());
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM  yyyy HH:mm a");
                    Date date = new Date();
                    ghiChu.setCreateDate(formatter.format(date));
                    ghiChu.setDueDate(edtDueDate.getText().toString());
                    ghiChu.setHinhAnh(imageGhiChu);
                    ghiChu.setIDLoaiGhiChu(Integer.parseInt(idLoaiGhiChu));
                    database.InsertNote(ghiChu);
                    GhiChuActivity.GetDataGhiChu();
                    dialogInterface.dismiss();
                    onBackPressed();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    onBackPressed();
                }
            });
            builder.show();
        } else {
            onBackPressed();
        }
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

    private void ThemGhiChu() {
        if (checkInput()) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageNote.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] imageGhiChu = outputStream.toByteArray();


            GhiChu ghiChu = new GhiChu();
            ghiChu.setTitle(edtTitle.getText().toString());
            ghiChu.setNoiDung(edtNoiDung.getText().toString());
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM  yyyy HH:mm a");
            Date date = new Date();
            ghiChu.setCreateDate(formatter.format(date));
            ghiChu.setDueDate(edtDueDate.getText().toString());
            ghiChu.setHinhAnh(imageGhiChu);
            ghiChu.setIDLoaiGhiChu(Integer.parseInt(idLoaiGhiChu));
            database.InsertNote(ghiChu);
            GhiChuActivity.GetDataGhiChu();
            Toast.makeText(this, "Đã thêm", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    private void setControl() {
        edtIDGhiChu = findViewById(R.id.TextInputEditTextID);
        edtTitle = findViewById(R.id.TextInputEditTextTitle);
        edtNoiDung = findViewById(R.id.TextInputEditTextNoiDung);
        edtCreateDate = findViewById(R.id.TextInputEditTextCreateDate);
        edtDueDate = findViewById(R.id.TextInputEditTextDueDate);
        edtIDLoaiGhiChu = findViewById(R.id.TextInputEditTextIDLoaiGhiChu);
        imgTime = findViewById(R.id.imgTime);
        imageCamera = findViewById(R.id.imageCamera);
        imageNote = findViewById(R.id.imageNote);
    }
}