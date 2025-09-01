package cn.smxy.zhouxuelian1.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.CakeListAdapter;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeListResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class CakeListActivity extends AppCompatActivity {
    private ListView cakeListView;
    private static final String TAG = "CakeListActivity";
    private List<Cake> cakeListData;
    private CakeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_list);
        initView();
        getDataFromNet();
    }

    private void getDataFromNet() {
        String url = UrlConstants.CAKE_LIST_URL;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败，失败原因：" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "网络请求成功，相应结果为：" + response);
                CakeListResponse cakeListResponse = new Gson().fromJson(response, CakeListResponse.class);
                if(cakeListResponse != null && cakeListResponse.getCode() == 2000) {
                    cakeListData = cakeListResponse.getDataobject();
                    adapter = new CakeListAdapter(CakeListActivity.this, cakeListData);
                    cakeListView.setAdapter(adapter);
                } else {
                    Toast.makeText(CakeListActivity.this, "没有查询到结果", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView(){
        cakeListView = this.findViewById(R.id.cake_list_list);
    }
}