package cn.smxy.zhouxuelian1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.OrderListAdapter;
import cn.smxy.zhouxuelian1.entity.CakeOrderInfo;
import cn.smxy.zhouxuelian1.entity.CakeOrderListResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class ManagOrderActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private OrderListAdapter adapter;
    private List<CakeOrderInfo> orderInfoList;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manag_order);

        initView();
        setListeners();
        getOrdersFromNet();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar_user);
        orderRecyclerView = findViewById(R.id.order_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        orderRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListeners() {
        // 返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ManagOrderActivity.this, MainActivity.class);
            intent.putExtra("intent_fragmentId", "4"); // 根据管理用户页面的实现添加这一行
            startActivity(intent);
            finish();
        });
    }

    private void getOrdersFromNet() {
        String url = UrlConstants.FIND_ORDER_URL; // 获取所有订单的接口
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("ManagOrderActivity", "网络请求失败，失败原因：" + e.getMessage());
                Toast.makeText(ManagOrderActivity.this, "网络请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("ManagOrderActivity", "网络请求成功，相应数据为：" + response);
                CakeOrderListResponse cakeOrderListResponse = new Gson().fromJson(response, CakeOrderListResponse.class);
                if (cakeOrderListResponse != null && cakeOrderListResponse.getCode() == 2000) {
                    orderInfoList = cakeOrderListResponse.getDataobject();
                    if (adapter == null) {
                        adapter = new OrderListAdapter(ManagOrderActivity.this, orderInfoList, ManagOrderActivity.this);
                        orderRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateData(orderInfoList);
                    }
                } else {
                    Toast.makeText(ManagOrderActivity.this, "没有查询到订单列表", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 刷新订单列表
    public void refreshOrderList() {
        getOrdersFromNet();
    }
}