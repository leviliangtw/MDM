package org.ncsist.mdm;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MDMDBHelper extends SQLiteOpenHelper {

    public MDMDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE record " +
                "(_id INTEGER PRIMARY KEY  NOT NULL, " +
                "event VARCHAR NOT NULL, " +
                "record_date DATETIME NOT NULL, " +
                "upload_date DATETIME NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertRecord(SQLiteDatabase db, String event, String recordtime, String uploadtime) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("event", event);
        contentValues.put("record_date", recordtime);
        contentValues.put("upload_date", uploadtime);
        db.insert("record", null, contentValues);
    }

    public void updateRecord(SQLiteDatabase db, String id, String event, String recordtime, String uploadtime) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("event", event);
        contentValues.put("record_date", recordtime);
        contentValues.put("upload_date", uploadtime);

        //問號對應到後面陣列的資料
        db.update("record", contentValues, "_id=?", new String[] {id});
    }

    public void removeRecord(SQLiteDatabase db, String id) {
        db.delete("record", "_id=?", new String[]{id});
    }

    public void removeAllRecord(SQLiteDatabase db) {
        db.execSQL("delete from 'record'");
    }

    public List<Record> selectRecordBytime(SQLiteDatabase db, String start_time, String end_time) {
        List<Record> result = new ArrayList<>();
        System.out.println(System.out + start_time + ", " + end_time);
        String[] timeSpan = {start_time, end_time};
        Cursor cursor = db.query(
                "record",
                null,
                "record_date BETWEEN ? AND ?",
                timeSpan,
                null,
                null,
                "record_date",
                null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public List<Record> selectAllRecord(SQLiteDatabase db) {
        List<Record> result = new ArrayList<>();
        Cursor cursor = db.query(
                "record",
                null,
                null,
                null,
                null,
                null,
                "record_date",
                null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 把Cursor目前的資料包裝為物件
    public Record getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Record result = new Record();

        result.setType(0);
        result.setEvent(cursor.getString(1));
        result.setRecordtime(cursor.getString(2));
        result.setUploadtime(cursor.getString(3));
        // 回傳結果
        return result;
    }
}