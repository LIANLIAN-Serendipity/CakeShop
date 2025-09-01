package cn.smxy.zhouxuelian1.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import cn.smxy.zhouxuelian1.R;

public class HActivity extends AppCompatActivity {
    private TextView tvNumber;
    private MyBatteryLevelReceiver myBatteryLevelReceiver;  // 修正变量名与类名一致

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hactivity);

        register();
        initView();
    }

    private void initView() {
        tvNumber = this.findViewById(R.id.number);
    }

    private void register() {
        myBatteryLevelReceiver = new MyBatteryLevelReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);  // 添加new关键字
        registerReceiver(myBatteryLevelReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {  // 修正方法名拼写
        super.onDestroy();
        unregisterReceiver(myBatteryLevelReceiver);
    }

    public class MyBatteryLevelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int number = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            tvNumber.setText("当前电量：" + number + "%");  // 添加百分号显示更直观
        }
    }
}