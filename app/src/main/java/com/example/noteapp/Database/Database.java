package com.example.noteapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.noteapp.Models.GhiChu;
import com.example.noteapp.Models.LoaiGhiChu;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context) {
        super(context, "NoteApp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlLoaiGhiChu = "CREATE TABLE IF NOT EXISTS LoaiGhiChu(IDLoaiGhiChu INTEGER PRIMARY KEY AUTOINCREMENT, TenLoaiGhiChu TEXT)";
        String sqlGhiChu = "CREATE TABLE IF NOT EXISTS GhiChu(IDGhiChu INTEGER PRIMARY KEY AUTOINCREMENT, TenGhiChu TEXT, NoiDung TEXT," +
                " CreateDate TEXT, DueDate TEXT,HinhAnh BLOB, Complete INTEGER DEFAULT 0,ListDelete INTEGER DEFAULT 0, IDLoaiGhiChu INTEGER," +
                " FOREIGN KEY (IDLoaiGhiChu)  REFERENCES LoaiGhiChu(IDLoaiGhiChu))";
        sqLiteDatabase.execSQL(sqlLoaiGhiChu);
        sqLiteDatabase.execSQL(sqlGhiChu);
    }

    public void InsertNote(GhiChu ghiChu) {
        String sql = "INSERT INTO GhiChu(TenGhiChu, NoiDung, CreateDate ,DueDate, HinhAnh, IDLoaiGhiChu) Values(?,?,?,?,?,?)";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new Object[]{ghiChu.getTitle(), ghiChu.getNoiDung(), ghiChu.getCreateDate(), ghiChu.getDueDate(), ghiChu.getHinhAnh(), ghiChu.getIDLoaiGhiChu()});
    }

    public void DeleteNote(GhiChu ghiChu) {
        String sql = "DELETE FROM GhiChu WHERE IDGhiChu =?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new Object[]{ghiChu.getID()});
    }

    public void NotesComplete(GhiChu ghiChu) {
        String sql = "UPDATE GhiChu SET Complete = ? WHERE IDGhiChu =?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new Object[]{ghiChu.getComplete(), ghiChu.getID()});
    }

    public void NoteListDelete(GhiChu ghiChu){
        String sql = "UPDATE GhiChu SET ListDelete = ? WHERE IDGhiChu =?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new Object[]{ghiChu.getListDelete(), ghiChu.getID()});
    }
    public void UpdateNote(GhiChu ghiChu) {
        String sql = "UPDATE GhiChu SET TenGhiChu = ?, NoiDung =?, DueDate=?, HinhAnh = ?WHERE IDGhiChu =?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new Object[]{ghiChu.getTitle(), ghiChu.getNoiDung(), ghiChu.getDueDate(), ghiChu.getHinhAnh(), ghiChu.getID()});
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void InsertNoteType(LoaiGhiChu loaiGhiChu) {
        String sql = "INSERT INTO LoaiGhiChu(TenLoaiGhiChu) Values(?)";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new String[]{loaiGhiChu.getTenLoaiGhiChu()});
    }

    public void DeleteNoteType(LoaiGhiChu loaiGhiChu) {
        String sql = "DELETE FROM LoaiGhiChu WHERE IDLoaiGhiChu =?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new Number[]{loaiGhiChu.getID()});
    }

    public void UpdateNoteType(LoaiGhiChu loaiGhiChu) {
        String sql = "UPDATE LoaiGhiChu SET TenLoaiGhiChu =? WHERE IDLoaiGhiChu =?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new Object[]{loaiGhiChu.getTenLoaiGhiChu(), loaiGhiChu.getID()});
    }

    public Cursor GetData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }
}
