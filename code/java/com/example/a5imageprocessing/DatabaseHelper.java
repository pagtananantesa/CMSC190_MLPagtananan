package com.example.a5imageprocessing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="plant99.db";
    public static final String TABLE_NAME="PLANT2";

    public static final String COL1="plantID";
    public static final String COL2="accessionID";
    public static final String COL3="plotNo";
    public static final String COL4="width";
    public static final String COL5="height";
    public static final String COL6="date";
    public static final String COL7="position";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(plantID TEXT PRIMARY KEY, accessionID TEXT, plotNo TEXT, width DOUBLE, height DOUBLE, date DATETIME DEFAULT CURRENT_TIMESTAMP, position INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db );
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    public boolean addData(String plantID, String accessionID, String plotNo, double width, double height, int position){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, plantID);
        contentValues.put(COL2, accessionID);
        contentValues.put(COL3, plotNo);
        contentValues.put(COL4, width);
        contentValues.put(COL5, height);
        contentValues.put(COL6,getCurrentTimeStamp());
        contentValues.put(COL7,position);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }


    public Cursor searchAnEntry(String plantID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + plantID + "' COLLATE NOCASE", null);
        return data;
    }

    public void deleteListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
        db.close();
    }

    public void deleteAnEntry(String plantID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME +" WHERE "+ COL1 +" = '" + plantID + "' COLLATE NOCASE" );
        db.close();
    }

    public void deleteAnEntry(int pos){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME +" WHERE "+ COL7 +" = " + pos );
        db.close();
    }

    public boolean updateEntry(String ogplantID, String accessionID, String plotNo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,ogplantID);
        contentValues.put(COL2,accessionID);
        contentValues.put(COL3,plotNo);

        db.update(TABLE_NAME, contentValues,"plantID = ?", new String[] {ogplantID});
        return true;
    }


    public Cursor raw() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME , new String[]{});

        return res;
    }


    public boolean isDBEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_NAME;
        Cursor res = db.rawQuery(count, null);
        res.moveToFirst();
        int c = res.getInt(0);
        if(c==0){
            return true;
        }else{
            return false;
        }
    }

    public int totalNumber(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_NAME;
        Cursor res = db.rawQuery(count, null);
        res.moveToFirst();
        int c = res.getInt(0);
        res.close();
        return c;
    }

    public boolean colExists(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT EXISTS (SELECT * FROM " +  TABLE_NAME + " WHERE " + COL1 + " = '" + value + "' COLLATE NOCASE)";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

}
