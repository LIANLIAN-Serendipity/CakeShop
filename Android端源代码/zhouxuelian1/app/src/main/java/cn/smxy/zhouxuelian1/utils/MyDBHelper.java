package cn.smxy.zhouxuelian1.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;

public class MyDBHelper extends SQLiteOpenHelper { // 确保继承了SQLiteOpenHelper
    public MyDBHelper(@NonNull Context context) {
        super(context, "cartinfo.db", null, 1); // 确保调用了SQLiteOpenHelper的构造函数
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table cart(" +
                "cakeId integer primary key," +
                "cakeName varchar(100)," + // 修改字段名以匹配表结构
                "introduce text," +
                "price float," +
                "cakePicture varchar(255)," + // 修改字段名以匹配表结构
                "typeId integer," +
                "num integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 这里可以添加数据库升级逻辑
        // 例如：db.execSQL("DROP TABLE IF EXISTS cart");
        // db.execSQL("CREATE TABLE cart ...");
    }
}