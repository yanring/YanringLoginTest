package com.example.yanring.login.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yanring on 2016/2/29.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_Name = "user";//创建数据库

    public DatabaseHelper(Context context) {
        super(context, "test.db", null,1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Database_Name + "(username varchar(20) not null , password varchar(60) not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}