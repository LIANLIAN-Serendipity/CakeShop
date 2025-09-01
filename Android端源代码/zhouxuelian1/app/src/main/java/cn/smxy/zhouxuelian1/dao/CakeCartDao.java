package cn.smxy.zhouxuelian1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.utils.MyDBHelper;

public class CakeCartDao {
    private SQLiteDatabase db;
    private MyDBHelper myDBHelper;

    public CakeCartDao(Context context) {
        myDBHelper = new MyDBHelper(context);
        db = myDBHelper.getWritableDatabase();
    }

    public void close() {
        myDBHelper.close();
    }

    public boolean isExit(Cake cake) {
        boolean result;
        Cursor cursor = db.query("cart", new String[]{"cakeId"}, "cakeId=?", new String[]{cake.getCakeId()+""}, null, null, null, null);
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

    public void insert(Cake cake) {
        ContentValues values = new ContentValues();
        values.put("cakeId", cake.getCakeId());
        values.put("cakeName", cake.getCakeName());
        values.put("introduce", cake.getIntroduce());
        values.put("price", cake.getPrice());
        values.put("cakePicture", cake.getCakePicture());
        values.put("typeId", cake.getCaketypeId());
        values.put("num", 1);

        long result = db.insert("cart", null, values);
        if (result == -1) {
            Log.e("CakeCartDao", "Insert failed");
        }
    }

    public void increseNum(int cakeId) {
        String sql = "update cart set num = num + 1 where cakeId = ?";
        db.execSQL(sql, new String[]{cakeId+""});
    }

    public void decreseNum(int cakeId) {
        String sql = "update cart set num = num - 1 where cakeId = ?";
        db.execSQL(sql, new String[]{cakeId+""});
    }

    public void addToCart(Cake cake) {
        boolean exit = isExit(cake);
        if (exit) {
            increseNum(cake.getCakeId());
        } else {
            insert(cake);
        }
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

    public void delete(int cakeId) {
        db.delete("cart", "cakeId=?", new String[]{cakeId+""});
    }

    public boolean isExit(int cakeId) {
        boolean result;
        Cursor cursor = db.query("cart", new String[]{"cakeId"}, "cakeId=?", new String[]{cakeId+ ""}, null, null, null, null);
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
        Cursor cursor = db.query("cart", null, "cakeId=?", new String[]{cakeId+""}, null, null, null);
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
            Cake cake = new Cake(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            totalPrice = totalPrice + cake.getPrice() * cake.getNum();
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalPrice;
    }

    public void clearCart() {
        db.delete("cart", null, null);
    }

    /**
     * 更新购物车中蛋糕的数量
     */
    public void updateCakeNum(int cakeId, int num) {
        ContentValues values = new ContentValues();
        values.put("num", num);
        db.update("cart", values, "cakeId = ?", new String[]{String.valueOf(cakeId)});
    }
}