package com.harsimranksaran.tripplanner.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "places.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE_FAVORITES = "CREATE TABLE "+
                DatabaseContract.DatabaseEntry.TABLE_NAME + " (" +
                DatabaseContract.DatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DatabaseContract.DatabaseEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                DatabaseContract.DatabaseEntry.COLUMN_PLACE_NAME + " TEXT NOT NULL, " +
                DatabaseContract.DatabaseEntry.COLUMN_PLACE_ADDRESS + " TEXT " +
                " );";

        final String SQL_CREATE_TABLE_MYLIST = "CREATE TABLE "+
                DatabaseContract.DatabaseEntry.TABLE_NAME_TWO + " (" +
                DatabaseContract.DatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DatabaseContract.DatabaseEntry.COLUMN_P_ID + " TEXT NOT NULL, " +
                DatabaseContract.DatabaseEntry.COLUMN_P_NAME + " TEXT NOT NULL, " +
                DatabaseContract.DatabaseEntry.COLUMN_P_ADDRESS + " TEXT " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITES);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.DatabaseEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DatabaseContract.DatabaseEntry.TABLE_NAME_TWO);
    }

    public Cursor viewallData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+ DatabaseContract.DatabaseEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + DatabaseContract.DatabaseEntry.TABLE_NAME_TWO;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

}
