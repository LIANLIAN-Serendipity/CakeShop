package cn.smxy.zhouxuelian1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.CakeListManagerAdapter;
import cn.smxy.zhouxuelian1.adapter.UserListAdapter;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeUserInfo;
import cn.smxy.zhouxuelian1.entity.UserListResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;


public class ManageUserActivity extends AppCompatActivity {
    private static final String TAG = "ManageUserActivity";

    private UserListAdapter adapter;
    private ListView userListView;
    private Toolbar back;
    private View addButton;
    private List<CakeUserInfo> userListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
        initView();
        getData();
        setListeners();
    }

    private void initView() {
        userListView = findViewById(R.id.lv_user_list);
        back = findViewById(R.id.toolbar_user);
        addButton = findViewById(R.id.btn_user_add);
    }

    private void setListeners() {
        // 返回按钮点击事件
        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageUserActivity.this, MainActivity.class);
                intent.putExtra("intent_fragmentId", "4");
                startActivity(intent);
                finish();
            }
        });

        // 添加按钮点击事件处理
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageUserActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        String url = UrlConstants.USER_LIST_URL;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败，失败原因: " + e.getMessage());
                Toast.makeText(ManageUserActivity.this, "获取用户列表失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    UserListResponse userListResponse = new Gson().fromJson(response, UserListResponse.class);
                    if (userListResponse != null && userListResponse.getCode() == 2000) {
                        userListData = userListResponse.getDataobject();
                        adapter = new UserListAdapter(ManageUserActivity.this, userListData);
                        userListView.setAdapter(adapter);
                    } else {
                        Log.d(TAG, "获取用户列表失败: " + (userListResponse != null ? userListResponse.getMsg() : "未知错误"));
                        Toast.makeText(ManageUserActivity.this, "获取用户列表失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "解析用户列表数据失败: " + e.getMessage());
                    Toast.makeText(ManageUserActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}