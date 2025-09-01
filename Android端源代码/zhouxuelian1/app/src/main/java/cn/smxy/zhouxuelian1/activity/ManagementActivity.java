package cn.smxy.zhouxuelian1.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.CakeListAdapter;
import cn.smxy.zhouxuelian1.adapter.CakeListManagerAdapter;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeListResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class ManagementActivity extends AppCompatActivity {

    private CakeListManagerAdapter adapter;
    private ListView cakeListView;
    private Toolbar back;
    private Button addButton;
    private List<Cake> cakeListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        initView();
        getData();
        setListeners();
    }

    private void initView() {
        cakeListView = findViewById(R.id.rv_search_commodity);
        back = findViewById(R.id.back);
        addButton = findViewById(R.id.btn_commodity_add);
    }

    private void setListeners() {
        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagementActivity.this, MainActivity.class);
                intent.putExtra("intent_fragmentId", "4");
                startActivity(intent);
                finish();
            }
        });

        // 添加按钮点击事件处理
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到添加蛋糕页面
                Intent intent = new Intent(ManagementActivity.this, AddCakeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getData() {
        String url = UrlConstants.CAKE_LIST_URL;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败，失败原因" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                CakeListResponse cakeListResponse = new Gson().fromJson(response, CakeListResponse.class);
                if (cakeListResponse != null && cakeListResponse.getCode() == 2000) {
                    cakeListData = cakeListResponse.getDataobject();
                    adapter = new CakeListManagerAdapter(ManagementActivity.this, cakeListData);
                    cakeListView.setAdapter(adapter);
                }
            }
        });
    }
}