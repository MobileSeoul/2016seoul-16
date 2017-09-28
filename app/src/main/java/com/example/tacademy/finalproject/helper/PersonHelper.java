package com.example.tacademy.finalproject.helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tacademy.finalproject.LM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tacademy on 2016-10-18.
 */
public class PersonHelper extends SQLiteOpenHelper {
    public PersonHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {/*두번쨰 인자 DB이름, 세번째 null , 네번째 버전 이게 한번 올라가면 onUpgrade이 한번 실행됨*/
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*고유번호 START*/
        String fileName;
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        fileName = String.valueOf(sdf.format(day));
        /*고유번호 END*/
        String sql = "create table person (_id integer primary key autoincrement,name text);";/*컬럼의 이름은 _id이 들어간다*/

        String insertSQL = "insert into person(name)  values('"+fileName+"')";
        try{
            sqLiteDatabase.execSQL(sql);

            sqLiteDatabase.execSQL(insertSQL);/*sql문 실행하는 메소드 select를 뺀 나머지*/
            LM.v("create success ");
        }catch (SQLException e){
            LM.v("create e :" + e);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        LM.v("SqlLiteDatabase success!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        LM.v("onUpgrade  oldVersion : " + oldVersion);
        LM.v("onUpgrade  newVersion : " + newVersion);
        try{
            sqLiteDatabase.execSQL("drop table person");
            onCreate(sqLiteDatabase);
        }catch (SQLException e){}
    }
}
