package cn.smxy.zhouxuelian1.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyUtil {
    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}
