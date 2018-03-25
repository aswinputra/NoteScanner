package com.mad.notescanners.db;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mad.notescanners.CONSTANT;

import java.util.ArrayList;


/**
 * Created by aswinhartono on 4/6/17.
 */

public class NoteDatabaseHelper extends SQLiteOpenHelper{


    public NoteDatabaseHelper(Context context) {
        super(context, CONSTANT.DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table "+ CONSTANT.TABLE_NAME + "("+CONSTANT.COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ CONSTANT.COL_2+" TEXT, "+ CONSTANT.COL_3 +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ CONSTANT.TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNote(String title, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONSTANT.COL_2, title);
        contentValues.put(CONSTANT.COL_3, note);
        long result = db.insert(CONSTANT.TABLE_NAME, null, contentValues);
        if (result ==-1)
            return false;
        else
            return true;
    }

    public Cursor getAllNote(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from "+ CONSTANT.TABLE_NAME, null);
        return res;
    }

    public boolean updateNote(String id, String title, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONSTANT.COL_1,id);
        contentValues.put(CONSTANT.COL_2,title);
        contentValues.put(CONSTANT.COL_3,note);
        db.update(CONSTANT.TABLE_NAME,contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteNote(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(CONSTANT.TABLE_NAME,"ID = ?", new String[]{id});
        return rows;
    }
}
