package cn.smxy.zhouxuelian1.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {

    // 保存字符串到SharedPreferences
    public static void saveString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("sp_info", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    // 从SharedPreferences中获取字符串
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("sp_info", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    // 从SharedPreferences中移除字符串
    public static void removeString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("sp_info", Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    // 新增：保存int类型数据到SharedPreferences
    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences("sp_info", Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    // 新增：从SharedPreferences中获取int类型数据
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences("sp_info", Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }
}