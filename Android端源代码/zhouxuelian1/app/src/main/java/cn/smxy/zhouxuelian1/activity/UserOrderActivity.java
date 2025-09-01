package cn.smxy.zhouxuelian1.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.Collections;
import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.UserOrderListAdapter;
import cn.smxy.zhouxuelian1.entity.CakeOrderInfo;
import cn.smxy.zhouxuelian1.entity.CakeOrderListResponse;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;



public class UserOrderActivity extends AppCompatActivity {
    private RecyclerView orderRecyclerView;
    private static final String TAG = "UserOrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        initView();
        getdataFromNet();
    }

    private void initView() {
        orderRecyclerView = findViewById(R.id.user_order_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        orderRecyclerView.setLayoutManager(layoutManager);
    }

    private void getdataFromNet() {
        String url = UrlConstants.USER_ORDER_LIST_URL; // 确保这个 URL 是正确的
        String userId = SPUtil.getString(this, "cakeuserId");

        // 检查用户 ID 是否存在
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "未登录，请先登录！", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 发起请求，仅使用用户 ID
        OkHttpUtils.get().url(url)
                .addParams("cakeuserId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "网络请求失败，失败原因：" + e.getMessage());
                        Toast.makeText(UserOrderActivity.this, "网络请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "网络请求成功，相应数据为：" + response);
                        CakeOrderListResponse cakeOrderListResponse = new Gson().fromJson(response, CakeOrderListResponse.class);
                        if (cakeOrderListResponse != null) {
                            if (cakeOrderListResponse.getCode() == 2000) {
                                List<CakeOrderInfo> orderInfoList = cakeOrderListResponse.getDataobject();
                                UserOrderListAdapter adapter = new UserOrderListAdapter(UserOrderActivity.this, orderInfoList);
                                orderRecyclerView.setAdapter(adapter);
                            } else if (cakeOrderListResponse.getCode() == 4040) {
                                Toast.makeText(UserOrderActivity.this, "没有查询到该用户的订单列表", Toast.LENGTH_SHORT).show();
                                // 设置空适配器
                                UserOrderListAdapter adapter = new UserOrderListAdapter(UserOrderActivity.this, Collections.emptyList());
                                orderRecyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(UserOrderActivity.this, "服务器返回未知错误", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UserOrderActivity.this, "服务器返回数据格式错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}