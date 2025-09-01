package cn.smxy.zhouxuelian1.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        String content = intent.getStringExtra("content");
        Toast.makeText(context,"接收到了广播，content= "+content,Toast.LENGTH_SHORT).show();
    }
}
