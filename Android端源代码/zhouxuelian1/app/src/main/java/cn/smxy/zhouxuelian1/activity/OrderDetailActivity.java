package cn.smxy.zhouxuelian1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.OrderDetailAdapter;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeOrderDetail;
import cn.smxy.zhouxuelian1.entity.CakeOrderInfo;
import cn.smxy.zhouxuelian1.entity.OrderDetailResponse;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;


public class OrderDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvOrderId, tvOrderTime, tvOrderStatus, tvTotalPrice,
            tvCustomerName, tvCustomerPhone, tvDeliveryAddress, tvOrderRemark;
    private Button btnReorder, btnModify;
    private RecyclerView rvOrderDetails;
    private OrderDetailAdapter adapter;

    private int orderId;
    private List<CakeOrderDetail> orderDetails = new ArrayList<>();
    private List<Cake> cakeList = new ArrayList<>();
    private CakeOrderInfo orderInfo;

    // 用户角色常量
    private static final String ROLE_ADMIN = "管理员"; // 管理员
    private static final String ROLE_NORMAL = "普通用户"; // 普通用户
    private String userRole = ROLE_NORMAL; // 默认普通用户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // 获取订单ID
        if (getIntent() != null && getIntent().hasExtra("orderId")) {
            orderId = getIntent().getIntExtra("orderId", 0);
        } else {
            Toast.makeText(this, "订单ID无效", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 从 SP 中获取用户角色，修正为getString
        userRole = SPUtil.getString(this, "role");
        if (TextUtils.isEmpty(userRole)) {
            userRole = ROLE_NORMAL;
        }
        Log.d("OrderDetailActivity", "用户角色: " + userRole);

        initView();

        loadOrderInfo();
        loadOrderDetailData();
        loadCakeList();

        setListeners();
    }

    private void initView() {
        toolbar = findViewById(R.id.cake_detail_toolbar);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvOrderTime = findViewById(R.id.tv_order_time);
        tvOrderStatus = findViewById(R.id.tv_order_status);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvCustomerPhone = findViewById(R.id.tv_customer_phone);
        tvDeliveryAddress = findViewById(R.id.tv_delivery_address);
        tvOrderRemark = findViewById(R.id.tv_order_remark);

        btnReorder = findViewById(R.id.btn_reorder);
        btnModify = findViewById(R.id.btn_modify);

        rvOrderDetails = findViewById(R.id.rv_order_details);
        rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDetailAdapter(this, new ArrayList<>());
        rvOrderDetails.setAdapter(adapter);
    }

    private void loadOrderInfo() {
        String url = UrlConstants.FIND_ORDER_URL;
        Map<String, String> headersMap = new HashMap<>();
        String token = SPUtil.getString(this, "token");
        if (!token.isEmpty()) {
            headersMap.put("token", token);
        }

        OkHttpUtils.get()
                .url(url)
                .headers(headersMap)
                .addParams("cakeorderId", String.valueOf(orderId))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(OrderDetailActivity.this, "获取订单基本信息失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.d("OrderDetailActivity", "订单基本信息响应: " + response);

                            Map<String, Object> responseMap = new Gson().fromJson(response,
                                    new TypeToken<Map<String, Object>>() {}.getType());

                            if (responseMap != null) {
                                Object codeObj = responseMap.get("code");
                                if (codeObj instanceof Double && ((Double) codeObj).intValue() == 2000) {
                                    Object dataObject = responseMap.get("dataobject");

                                    if (dataObject instanceof List) {
                                        List<Map<String, Object>> orderList = (List<Map<String, Object>>) dataObject;
                                        CakeOrderInfo matchedOrder = null;

                                        for (Map<String, Object> orderMap : orderList) {
                                            Object idObj = orderMap.get("cakeorderId");
                                            if (idObj instanceof Double) {
                                                int currentOrderId = ((Double) idObj).intValue();
                                                if (currentOrderId == orderId) {
                                                    matchedOrder = new Gson().fromJson(new Gson().toJson(orderMap), CakeOrderInfo.class);
                                                    break;
                                                }
                                            }
                                        }

                                        if (matchedOrder != null) {
                                            orderInfo = matchedOrder;
                                            runOnUiThread(() -> updateOrderInfoUI());
                                        } else {
                                            Toast.makeText(OrderDetailActivity.this, "未找到匹配的订单信息", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (dataObject instanceof Map) {
                                        orderInfo = new Gson().fromJson(new Gson().toJson(dataObject), CakeOrderInfo.class);
                                        runOnUiThread(() -> updateOrderInfoUI());
                                    } else {
                                        Toast.makeText(OrderDetailActivity.this, "订单信息格式错误", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Object msgObj = responseMap.get("msg");
                                    String errorMsg = msgObj instanceof String ?
                                            (String) msgObj : "获取订单基本信息失败";
                                    Toast.makeText(OrderDetailActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("OrderDetailActivity", "解析订单基本信息失败: " + e.getMessage(), e);
                            Toast.makeText(OrderDetailActivity.this, "解析订单基本信息失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateOrderInfoUI() {
        if (orderInfo != null) {
            tvOrderId.setText(String.valueOf(orderInfo.getCakeorderId()));
            tvOrderTime.setText(orderInfo.getCakeorderTime());

            String statusText = getOrderStatusText(orderInfo.getCakeorderStatus());
            tvOrderStatus.setText(statusText);

            tvTotalPrice.setText(String.format("¥%.2f", orderInfo.getCaketotalPrice()));
            tvCustomerName.setText(orderInfo.getCustomerName());
            tvCustomerPhone.setText(orderInfo.getCustomerPhone());
            tvDeliveryAddress.setText(orderInfo.getDeliveryAddress());

            if (!orderDetails.isEmpty()) {
                String remark = orderDetails.get(0).getRemark();
                tvOrderRemark.setText(remark);
            }

            // 根据用户角色显示/隐藏修改按钮
            if (ROLE_ADMIN.equals(userRole) ||
                    (ROLE_NORMAL.equals(userRole) && orderInfo.getCakeorderStatus() == 0)) {
                btnModify.setVisibility(View.VISIBLE);
            } else {
                btnModify.setVisibility(View.GONE);
            }
        }
    }

    private String getOrderStatusText(int status) {
        String statusText;
        switch (status) {
            case 0:
                statusText = "未发货";
                break;
            case 1:
                statusText = "已发货";
                break;
            case 2:
                statusText = "已取消";
                break;
            default:
                statusText = "未知状态";
        }
        return statusText;
    }

    private void loadOrderDetailData() {
        String url = UrlConstants.ORDER_DETAIL_URL;
        Map<String, String> headersMap = new HashMap<>();
        String token = SPUtil.getString(this, "token");
        if (!token.isEmpty()) {
            headersMap.put("token", token);
        }

        OkHttpUtils.get()
                .url(url)
                .headers(headersMap)
                .addParams("cakeorderId", String.valueOf(orderId))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(OrderDetailActivity.this, "获取订单详情失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.d("OrderDetail", "原始响应: " + response);
                            Map<String, Object> responseMap = new Gson().fromJson(response,
                                    new TypeToken<Map<String, Object>>() {}.getType());

                            if (responseMap != null) {
                                Object codeObj = responseMap.get("code");
                                if (codeObj instanceof Double) {
                                    double codeDouble = (Double) codeObj;
                                    if (codeDouble == 2000) {
                                        List<Map<String, Object>> detailList = (List<Map<String, Object>>)
                                                responseMap.get("dataobject");

                                        if (detailList != null && !detailList.isEmpty()) {
                                            orderDetails = new Gson().fromJson(new Gson().toJson(detailList),
                                                    new TypeToken<List<CakeOrderDetail>>() {}.getType());

                                            runOnUiThread(() -> {
                                                adapter.setOrderDetails(orderDetails);
                                                adapter.notifyDataSetChanged();
                                            });
                                        } else {
                                            Toast.makeText(OrderDetailActivity.this, "没有查询到订单详情", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Object msgObj = responseMap.get("msg");
                                        if (msgObj instanceof String) {
                                            Toast.makeText(OrderDetailActivity.this, (String) msgObj, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e("OrderDetailActivity", "解析数据失败", e);
                            Toast.makeText(OrderDetailActivity.this, "解析数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadCakeList() {
        String url = UrlConstants.CAKE_LIST_URL;
        Map<String, String> headersMap = new HashMap<>();
        String token = SPUtil.getString(this, "token");
        if (!token.isEmpty()) {
            headersMap.put("token", token);
        }

        OkHttpUtils.get()
                .url(url)
                .headers(headersMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(OrderDetailActivity.this, "获取蛋糕列表失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.d("OrderDetailActivity", "蛋糕列表响应: " + response);

                            Map<String, Object> responseMap = new Gson().fromJson(response,
                                    new TypeToken<Map<String, Object>>() {}.getType());

                            if (responseMap != null) {
                                Object codeObj = responseMap.get("code");
                                if (codeObj instanceof Double && ((Double) codeObj).intValue() == 2000) {
                                    List<Map<String, Object>> cakeListData = (List<Map<String, Object>>)
                                            responseMap.get("dataobject");

                                    if (cakeListData != null && !cakeListData.isEmpty()) {
                                        cakeList = new Gson().fromJson(new Gson().toJson(cakeListData),
                                                new TypeToken<List<Cake>>() {}.getType());

                                        runOnUiThread(() -> {
                                            adapter.setCakeList(cakeList);
                                            Log.d("OrderDetailActivity", "蛋糕列表加载完成，共 " + cakeList.size() + " 个蛋糕");
                                        });
                                    } else {
                                        Log.w("OrderDetailActivity", "没有查询到蛋糕信息");
                                    }
                                } else {
                                    Object msgObj = responseMap.get("msg");
                                    String errorMsg = msgObj instanceof String ?
                                            (String) msgObj : "获取蛋糕列表失败";
                                    Toast.makeText(OrderDetailActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("OrderDetailActivity", "解析蛋糕列表失败: " + e.getMessage(), e);
                            Toast.makeText(OrderDetailActivity.this, "解析蛋糕列表失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setListeners() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        btnReorder.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        btnModify.setOnClickListener(v -> {
            if (orderInfo != null) {
                Intent intent = new Intent(OrderDetailActivity.this, UpdateOrderActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("orderStatus", orderInfo.getCakeorderStatus());
                startActivity(intent);
            } else {
                Toast.makeText(this, "订单信息加载中，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}