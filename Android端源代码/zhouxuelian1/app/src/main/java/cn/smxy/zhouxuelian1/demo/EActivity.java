package cn.smxy.zhouxuelian1.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.smxy.zhouxuelian1.R;

public class EActivity extends AppCompatActivity {

    private EditText etContent;
    private Button btnSend;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eactivity);

        register();
        initView();
        setListeners();
    }

    private void setListeners() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("content", content);
                intent.setAction("cn.smxy.zhouxuelian1.mycontentbroadcast");
                sendBroadcast(intent);  // 添加发送广播的调用
            }
        });
    }

    private void initView() {
        etContent = this.findViewById(R.id.content);
        btnSend = this.findViewById(R.id.send);
    }

    private void register() {
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("cn.smxy.zhouxuelian1.mycontentbroadcast"); // 修正拼写错误
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {  // 修正方法名拼写
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}