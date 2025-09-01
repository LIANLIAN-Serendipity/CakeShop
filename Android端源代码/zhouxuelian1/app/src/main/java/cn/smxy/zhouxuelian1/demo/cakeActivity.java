package cn.smxy.zhouxuelian1.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeListResponse;
import okhttp3.Call;

public class cakeActivity extends AppCompatActivity {

    private static final String TAG = "cakeactivity";
    private Button btnNet;
    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake);


        btnNet = findViewById(R.id.d_net);


        btnNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                netRequest();
            }
        });
    }

    private void netRequest(){
        String url="http://192.168.182.230:8089/zhouxuelian9/cake/findAll";
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG,"网络请求失败，失败原因："+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG,"网络请求成功，相应数据为："+response);
                CakeListResponse cakeListResponse= new Gson().fromJson(response,CakeListResponse.class);
                if(cakeListResponse !=null && cakeListResponse.getCode() == 2000){
                    List<Cake> cakeList = cakeListResponse.getDataobject();
                    Log.d(TAG,"caleList:"+cakeList.toString());
                }else{
                    Toast.makeText(cakeActivity.this,"没有查询到蛋糕列表",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}