package cn.smxy.zhouxuelian1.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.utils.MyDBHelper;

public class CakeCartDao1 {
    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;

    // 构造函数需要一个Context参数
    public CakeCartDao1(Context context) {
        myDBHelper = new MyDBHelper(context);
        db = myDBHelper.getWritableDatabase();
    }

    public void close() {
        myDBHelper.close();
    }

    // dish为从未添加到购物车的菜品，将其添加到购物车
    public void insert(Cake cake) {
        ContentValues values = new ContentValues();
        values.put("cakeId", cake.getCakeId()); // 修改字段名以匹配数据库表结构
        values.put("cakeName", cake.getCakeName());
        values.put("introduce", cake.getIntroduce());
        values.put("price", cake.getPrice());
        values.put("cakePicture", cake.getCakePicture());
        values.put("typeId", cake.getCaketypeId()); // 修改字段名以匹配数据库表结构
        values.put("num", 1);

        db.insert("cart", null, values);
    }

    public List<Cake> selectAll() {
        List<Cake> cakeList = new ArrayList<>();
        Cursor cursor = db.query("cart", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Cake cake = new Cake(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getFloat(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));

            cakeList.add(cake);
        }
        if (cursor != null) {
            cursor.close();
        }

        return cakeList;
    }

    public void increseNum(int cakeId) {
        String sql = "update cart set num = num + 1 where cakeId =" + cakeId; // 修改字段名以匹配数据库表结构
        db.execSQL(sql);
    }

    public void decreseNum(int cakeId) {
        String sql = "update cart set num = num - 1 where cakeId = " + cakeId; // 修改字段名以匹配数据库表结构
        db.execSQL(sql);
    }

    public void delete(int cakeId) {
        db.delete("cart", "cakeid=?", new String[]{String.valueOf(cakeId)}); // 修改字段名以匹配数据库表结构
    }

    public boolean isExit(int cakeId) {
        boolean result;
        Cursor cursor = db.query("cart",
                null, "cakeid=?", new String[]{String.valueOf(cakeId)}, null, null, null); // 修改字段名以匹配数据库表结构

        if (cursor != null && cursor.getCount() > 0) {
            result = true;
        } else {
            result = false;
        }

        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

    public void insertOrIncreseNum(Cake cake) {
        boolean exit = isExit(cake.getCakeId());
        if (exit) {
            increseNum(cake.getCakeId());
        } else {
            insert(cake);
        }
    }

    public int selectCakeNum(int cakeId) {
        int num = 0;
        Cursor cursor = db.query("cart",
                null, "cakeid=?", new String[]{String.valueOf(cakeId)}, null, null, null); // 修改字段名以匹配数据库表结构

        if (cursor.moveToNext()) {
            num = cursor.getInt(6);
        } else {
            num = 0;
        }

        if (cursor != null) {
            cursor.close();
        }

        return num;
    }

    public void decreseNumOrdelete(int cakeId) {
        int num = selectCakeNum(cakeId);
        if (num == 1) {
            delete(cakeId);
        } else {
            decreseNum(cakeId);
        }
    }

    public boolean isEmpty() {
        boolean result = true;
        Cursor cursor = db.query("cart", null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            result = false;
        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    public float getTotalPrice() {
        Cursor cursor = db.query("cart", null, null, null, null, null, null);

        float totalPrice = 0;
        while (cursor.moveToNext()) {
            Cake cake = new Cake(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getFloat(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));

            totalPrice = totalPrice + cake.getPrice() * cake.getNum();
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalPrice;
    }
}