package cn.smxy.zhouxuelian1.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.utils.SPUtil;

public class FActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factivity);
    }

    public void saveData(View view){
        //首先获得sp对象,上下文,获得sp成员方法
        SharedPreferences sp = getSharedPreferences("sp_info",MODE_PRIVATE);
        //放入sp编辑器
        sp.edit().putString("username","张三").commit();
    }
    public void getData(View view){
        SharedPreferences sp = getSharedPreferences("sp_info",MODE_PRIVATE);
        String username = sp.getString("username","");
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
    }
    public void delData(View view){
        SharedPreferences sp = getSharedPreferences("sp_info",MODE_PRIVATE);
        sp.edit().remove("username").commit();
    }
}