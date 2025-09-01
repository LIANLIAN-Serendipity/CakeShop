package cn.smxy.zhouxuelian1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.entity.CakeOrderDetail;
import cn.smxy.zhouxuelian1.entity.CakeOrderInfo;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class UpdateOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etCustomerName, etCustomerPhone, etDeliveryAddress, etOrderRemark, etOrderTime;
    private Spinner spOrderStatus;
    private Button btnSave;
    private TextView tvOrderId;
    private int orderId;
    private String userRole;
    private int orderStatus;
    private CakeOrderInfo orderInfo;
    private List<CakeOrderDetail> cakeOrderDetails = new ArrayList<>();
    private boolean isOrderInfoLoaded = false;
    private boolean isOrderDetailsLoaded = false;

    // 订单状态选项，对应数据库：1未发货、2已发货、3已取消
    private String[] statusOptions = {"未发货", "已发货", "已取消"};
    private int[] statusValues = {1, 2, 3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order);

        // 获取订单ID和用户角色
        if (getIntent() != null) {
            orderId = getIntent().getIntExtra("orderId", 0);
            userRole = SPUtil.getString(this, "role");
            if (TextUtils.isEmpty(userRole)) {
                userRole = "普通用户";
            }
            orderStatus = getIntent().getIntExtra("orderStatus", 1);
        }

        Log.d("UpdateOrderActivity", "当前用户角色: " + userRole + ", 订单ID: " + orderId);

        if (orderId <= 0) {
            Toast.makeText(this, "订单ID无效", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initView();
        loadOrderInfo();
        loadOrderDetails();
        setupSpinner();
        setListeners();
        getWindow().getDecorView().post(this::setupEditability);
    }

    private void initView() {
        toolbar = findViewById(R.id.update_order_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("修改订单");

        tvOrderId = findViewById(R.id.tv_order_id);
        etOrderTime = findViewById(R.id.et_order_time);
        spOrderStatus = findViewById(R.id.sp_order_status);
        etCustomerName = findViewById(R.id.et_customer_name);
        etCustomerPhone = findViewById(R.id.et_customer_phone);
        etDeliveryAddress = findViewById(R.id.et_delivery_address);
        etOrderRemark = findViewById(R.id.et_order_remark);
        btnSave = findViewById(R.id.btn_save);
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
                        Log.e("UpdateOrderActivity", "获取订单信息失败: " + e.getMessage());
                        Toast.makeText(UpdateOrderActivity.this, "获取订单信息失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Map<String, Object> responseMap = new Gson().fromJson(response,
                                    new TypeToken<Map<String, Object>>() {}.getType());

                            if (responseMap != null && responseMap.get("code") != null &&
                                    responseMap.get("code").equals(2000.0)) {

                                Object dataObject = responseMap.get("dataobject");
                                if (dataObject instanceof List) {
                                    List<?> dataList = (List<?>) dataObject;
                                    if (!dataList.isEmpty() && dataList.get(0) instanceof Map) {
                                        Map<String, Object> orderMap = (Map<String, Object>) dataList.get(0);
                                        orderInfo = new Gson().fromJson(new Gson().toJson(orderMap), CakeOrderInfo.class);
                                        updateOrderInfoUI();
                                        isOrderInfoLoaded = true;
                                        setupEditability();
                                    }
                                } else if (dataObject instanceof Map) {
                                    orderInfo = new Gson().fromJson(new Gson().toJson(dataObject), CakeOrderInfo.class);
                                    updateOrderInfoUI();
                                    isOrderInfoLoaded = true;
                                    setupEditability();
                                }
                            } else {
                                String msg = responseMap != null && responseMap.get("msg") != null
                                        ? responseMap.get("msg").toString() : "获取订单信息失败";
                                Toast.makeText(UpdateOrderActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("UpdateOrderActivity", "解析订单信息失败", e);
                            Toast.makeText(UpdateOrderActivity.this, "解析订单信息失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void updateOrderInfoUI() {
                        runOnUiThread(() -> {
                            if (orderInfo != null) {
                                tvOrderId.setText("订单编号：" + orderInfo.getCakeorderId());
                                etOrderTime.setText(orderInfo.getCakeorderTime());
                                orderStatus = orderInfo.getCakeorderStatus();

                                for (int i = 0; i < statusValues.length; i++) {
                                    if (statusValues[i] == orderStatus) {
                                        spOrderStatus.setSelection(i);
                                        break;
                                    }
                                }

                                etCustomerName.setText(orderInfo.getCustomerName());
                                etCustomerPhone.setText(orderInfo.getCustomerPhone());
                                etDeliveryAddress.setText(orderInfo.getDeliveryAddress());
                            }
                        });
                    }
                });
    }

    private void loadOrderDetails() {
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
                        Log.e("UpdateOrderActivity", "获取订单详情失败: " + e.getMessage());
                        Toast.makeText(UpdateOrderActivity.this, "获取订单详情失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Map<String, Object> responseMap = new Gson().fromJson(response,
                                    new TypeToken<Map<String, Object>>() {}.getType());

                            if (responseMap != null && responseMap.get("code") != null &&
                                    responseMap.get("code").equals(2000.0)) {

                                Object dataObject = responseMap.get("dataobject");
                                if (dataObject instanceof List) {
                                    cakeOrderDetails = new Gson().fromJson(
                                            new Gson().toJson(dataObject),
                                            new TypeToken<List<CakeOrderDetail>>() {}.getType());
                                    isOrderDetailsLoaded = true;
                                    updateRemarkIfLoaded();
                                }
                            } else {
                                String msg = responseMap != null && responseMap.get("msg") != null
                                        ? responseMap.get("msg").toString() : "获取订单详情失败";
                                Toast.makeText(UpdateOrderActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("UpdateOrderActivity", "解析订单详情失败", e);
                            Toast.makeText(UpdateOrderActivity.this, "解析订单详情失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateRemarkIfLoaded() {
        if (isOrderDetailsLoaded) {
            runOnUiThread(() -> {
                if (!cakeOrderDetails.isEmpty() && cakeOrderDetails.get(0).getRemark() != null) {
                    etOrderRemark.setText(cakeOrderDetails.get(0).getRemark());
                } else {
                    etOrderRemark.setText("");
                }
            });
        }
    }

    private void setupSpinner() {
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrderStatus.setAdapter(statusAdapter);
    }

    private String getOrderStatusText(int status) {
        switch (status) {
            case 1: return "未发货";
            case 2: return "已发货";
            case 3: return "已取消";
            default: return "未知状态";
        }
    }

    private void setListeners() {
        toolbar.setNavigationOnClickListener(v -> goToMainActivity()); // 修改返回按钮点击事件

        btnSave.setOnClickListener(v -> {
            String orderTime = etOrderTime.getText().toString().trim();
            String customerName = etCustomerName.getText().toString().trim();
            String customerPhone = etCustomerPhone.getText().toString().trim();
            String deliveryAddress = etDeliveryAddress.getText().toString().trim();
            String orderRemark = etOrderRemark.getText().toString().trim();
            int selectedStatus = statusValues[spOrderStatus.getSelectedItemPosition()];

            if (TextUtils.isEmpty(customerName) || TextUtils.isEmpty(customerPhone) || TextUtils.isEmpty(deliveryAddress)) {
                Toast.makeText(this, "请填写必要信息", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidOrderTime(orderTime)) {
                Toast.makeText(this, "请输入正确的订单时间格式(yyyy-MM-dd HH:mm:ss)", Toast.LENGTH_SHORT).show();
                return;
            }

            updateOrderInfo(orderTime, customerName, customerPhone, deliveryAddress, orderRemark, selectedStatus);
        });
    }

    private boolean isValidOrderTime(String time) {
        if (TextUtils.isEmpty(time)) return false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        try {
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void updateOrderInfo(String orderTime, String customerName, String customerPhone,
                                 String deliveryAddress, String orderRemark, int orderStatus) {
        String url = UrlConstants.UPDATE_ORDER_URL;
        Map<String, String> headersMap = new HashMap<>();
        String token = SPUtil.getString(this, "token");
        if (!token.isEmpty()) {
            headersMap.put("token", token);
        }

        // 构建完整的订单参数
        Map<String, Object> params = new HashMap<>();
        params.put("cakeorderId", orderId);
        params.put("cakeorderTime", orderTime);
        params.put("cakeorderStatus", orderStatus);
        params.put("customerName", customerName);
        params.put("customerPhone", customerPhone);
        params.put("deliveryAddress", deliveryAddress);
        params.put("orderRemark", orderRemark);

        // 如果有订单详情，更新详情备注
        if (!cakeOrderDetails.isEmpty()) {
            CakeOrderDetail detail = cakeOrderDetails.get(0);
            detail.setRemark(orderRemark);
            params.put("detail", detail);
        }

        // 打印JSON参数用于调试
        String jsonParams = new Gson().toJson(params);
        Log.d("UpdateOrderActivity", "提交的订单参数: " + jsonParams);

        // 发送POST请求更新订单
        OkHttpUtils.postString()
                .url(url)
                .headers(headersMap)
                .content(jsonParams)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("UpdateOrderActivity", "更新订单失败: " + e.getMessage());
                        Toast.makeText(UpdateOrderActivity.this, "更新订单失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("UpdateOrderActivity", "服务器响应: " + response);
                        try {
                            Map<String, Object> responseMap = new Gson().fromJson(response,
                                    new TypeToken<Map<String, Object>>() {}.getType());

                            if (responseMap != null && responseMap.get("code") != null &&
                                    responseMap.get("code").equals(2000.0)) {

                                runOnUiThread(() -> {
                                    Toast.makeText(UpdateOrderActivity.this, "订单更新成功", Toast.LENGTH_SHORT).show();
                                    goToMainActivity(); // 订单更新成功后返回MainActivity
                                });
                            } else {
                                String msg = responseMap != null && responseMap.get("msg") != null
                                        ? responseMap.get("msg").toString() : "更新订单失败";
                                Toast.makeText(UpdateOrderActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("UpdateOrderActivity", "解析响应失败", e);
                            Toast.makeText(UpdateOrderActivity.this, "解析响应失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setupEditability() {
        if (etOrderTime == null) return;

        Log.d("UpdateOrderActivity", "设置编辑权限 - 角色: " + userRole + ", 状态: " + orderStatus);

        // 管理员拥有完全编辑权限
        if ("管理员".equals(userRole)) {
            etOrderTime.setEnabled(true);
            spOrderStatus.setEnabled(true);
            etCustomerName.setEnabled(true);
            etCustomerPhone.setEnabled(true);
            etDeliveryAddress.setEnabled(true);
            etOrderRemark.setEnabled(true);

            if (orderInfo == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                etOrderTime.setText(sdf.format(new Date()));
            }

        } else {
            // 普通用户根据订单状态限制编辑
            if (orderStatus == 1) { // 未发货状态
                etCustomerName.setEnabled(true);
                etCustomerPhone.setEnabled(true);
                etDeliveryAddress.setEnabled(true);
                etOrderRemark.setEnabled(true);
                etOrderTime.setEnabled(false);
                spOrderStatus.setEnabled(false);
            } else {
                etCustomerName.setEnabled(false);
                etCustomerPhone.setEnabled(false);
                etDeliveryAddress.setEnabled(false);
                etOrderRemark.setEnabled(true);
                etOrderTime.setEnabled(false);
                spOrderStatus.setEnabled(false);

                runOnUiThread(() ->
                        Toast.makeText(this, "订单已" + getOrderStatusText(orderStatus) + "，仅可修改备注", Toast.LENGTH_SHORT).show()
                );
            }
        }
    }

    /**
     * 返回到MainActivity的方法
     */
    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        // 设置标志位确保回到MainActivity时清除中间活动（如果需要）
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}