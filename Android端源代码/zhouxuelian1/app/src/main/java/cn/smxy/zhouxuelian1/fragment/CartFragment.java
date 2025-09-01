package cn.smxy.zhouxuelian1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.activity.UserOrderActivity;
import cn.smxy.zhouxuelian1.adapter.CartListAdapter;
import cn.smxy.zhouxuelian1.dao.CakeCartDao;
import cn.smxy.zhouxuelian1.dialog.DeliveryInfoDialog;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeOrderDetail;
import cn.smxy.zhouxuelian1.entity.CakeOrderInfo;
import cn.smxy.zhouxuelian1.utils.MyUtil;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;
import okhttp3.MediaType;

public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";
    private View rootView;
    private RecyclerView recyclerView;
    private List<Cake> cakeListData;
    private CartListAdapter cartListAdapter;
    private Button settlement;
    private ImageView img;
    private TextView tip, tvTotalPrice;
    private float totalPrice;
    private DeliveryInfoDialog deliveryInfoDialog;

    // 新增JSON相关常量
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Gson gson = new Gson();

    public CartFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        initView();
        getDataFromDB();
        setListeners();
        return rootView;
    }

    private void setListeners() {
        settlement.setOnClickListener(v -> {
            if (cakeListData != null && !cakeListData.isEmpty()) {
                showDeliveryInfoDialog();
            } else {
                Toast.makeText(getContext(), "购物车为空，无法结算", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeliveryInfoDialog() {
        if (deliveryInfoDialog == null) {
            deliveryInfoDialog = new DeliveryInfoDialog();
        }
        deliveryInfoDialog.setCartFragment(this);
        deliveryInfoDialog.show(getChildFragmentManager(), "delivery_info_dialog");
    }

    public void onDeliveryInfoConfirmed(String name, String phone, String address, String remark) {
        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(getContext(), "收货人姓名、电话和地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查是否已登录
        String token = SPUtil.getString(getActivity(), "token");
        if (token == null || token.isEmpty()) {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 生成订单并提交到服务器
        submitOrderToServer(name, phone, address, remark);
    }

    private void submitOrderToServer(String consigneeName, String consigneePhone,
                                     String consigneeAddress, String remark) {
        String userId = SPUtil.getString(getActivity(), "cakeuserId");
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(getContext(), "用户信息获取失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // 生成订单ID - 使用时间戳+随机数确保唯一性且为整数
        long timestamp = System.currentTimeMillis();
        int randomNum = new Random().nextInt(1000);
        int orderId = (int) (timestamp % 100000000 + randomNum); // 生成8-9位随机整数

        String orderTime = MyUtil.getCurrentTime();

        // 创建订单对象
        CakeOrderInfo order = new CakeOrderInfo();
        order.setCakeorderId(orderId);
        order.setCakeuserId(Integer.parseInt(userId));
        order.setCakeorderTime(orderTime);
        order.setCaketotalPrice(totalPrice);
        order.setCakeorderStatus(1); // 1: 待付款
        order.setDeliveryAddress(consigneeAddress);
        order.setCustomerName(consigneeName);
        order.setCustomerPhone(consigneePhone);

        // 创建订单详情列表（不设置cakedetailId，由数据库自动生成）
        List<CakeOrderDetail> orderDetails = new ArrayList<>();
        for (Cake cake : cakeListData) {
            CakeOrderDetail orderDetail = new CakeOrderDetail();
            orderDetail.setCakeorderId(order.getCakeorderId());
            orderDetail.setCakeId(cake.getCakeId());
            orderDetail.setNum(cake.getNum());
            orderDetail.setSubtotal(cake.getPrice() * cake.getNum());
            orderDetail.setRemark(remark);
            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);

        // 提交订单到服务器
        submitOrderInfoToServer(order);
    }

    private void submitOrderInfoToServer(CakeOrderInfo order) {
        String orderUrl = UrlConstants.ADD_ORDER_URL;
        Map<String, String> headers = new HashMap<>();
        String token = SPUtil.getString(getActivity(), "token");
        if (token != null && !token.isEmpty()) {
            headers.put("token", token);
        }

        String orderJson = gson.toJson(order);

        OkHttpUtils.postString()
                .url(orderUrl)
                .headers(headers)
                .content(orderJson)
                .mediaType(JSON) // 显式设置 JSON 媒体类型
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "提交订单主信息失败: " + e.getMessage());
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "订单提交失败，请重试", Toast.LENGTH_SHORT).show()
                        );
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "订单主信息响应: " + response);
                        try {
                            Map<String, Object> respMap = gson.fromJson(response, Map.class);
                            if (respMap != null) {
                                if (respMap.get("code").equals(2000)) {
                                    // 提交订单详情（确保不传递cakedetailId）
                                    submitOrderDetailsToServer(order.getCakeorderId(), order.getOrderDetails());
                                } else {
                                    getActivity().runOnUiThread(() ->
                                            Toast.makeText(getContext(), " " + respMap.get("msg"), Toast.LENGTH_SHORT).show()
                                    );
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "解析订单响应失败: " + e.getMessage());
                            getActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "订单提交失败，请重试", Toast.LENGTH_SHORT).show()
                            );
                        }
                    }
                });
    }

    private void submitOrderDetailsToServer(int orderId, List<CakeOrderDetail> orderDetails) {
        String detailUrl = UrlConstants.ADD_ORDER_DETAIL_URL;

        // 添加Token到请求头
        Map<String, String> headers = new HashMap<>();
        String token = SPUtil.getString(getActivity(), "token");
        if (token != null && !token.isEmpty()) {
            headers.put("token", token);
        }

        // 构建请求参数（确保不包含cakedetailId）
        Map<String, String> params = new HashMap<>();
        params.put("cakeorderId", String.valueOf(orderId));
        params.put("cakeList", gson.toJson(orderDetails));

        OkHttpUtils.post()
                .url(detailUrl)
                .params(params)
                .headers(headers)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "提交订单详情失败: " + e.getMessage());
                        getActivity().runOnUiThread(() -> handleOrderSubmissionResult(false));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "订单详情响应: " + response);
                        try {
                            Map<String, Object> respMap = gson.fromJson(response, Map.class);
                            if (respMap != null && respMap.get("code").equals(2000)) {
                                getActivity().runOnUiThread(() -> handleOrderSubmissionResult(true));
                            } else {
                                getActivity().runOnUiThread(() -> handleOrderSubmissionResult(false));
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "解析订单详情响应失败: " + e.getMessage());
                            getActivity().runOnUiThread(() -> handleOrderSubmissionResult(false));
                        }
                    }
                });
    }

    private void handleOrderSubmissionResult(boolean success) {
        if (success) {
            // 订单提交成功，清空购物车
            CakeCartDao cakeCartDao = new CakeCartDao(getActivity());
            cakeCartDao.clearCart();
            cakeCartDao.close();

            // 刷新购物车页面
            refreshCart();

            // 跳转到订单列表页
            Intent intent = new Intent(getActivity(), UserOrderActivity.class);
            startActivity(intent);
            getActivity().finish();

            Toast.makeText(getContext(), "订单创建成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "订单提交失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshCart() {
        getDataFromDB();
    }

    public void getDataFromDB() {
        CakeCartDao cakeCartDao = new CakeCartDao(getActivity());
        if (cakeCartDao.isEmpty()) {
            img.setVisibility(View.VISIBLE);
            tip.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            settlement.setVisibility(View.GONE);
            tvTotalPrice.setVisibility(View.GONE);
        } else {
            img.setVisibility(View.GONE);
            tip.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            settlement.setVisibility(View.VISIBLE);
            tvTotalPrice.setVisibility(View.VISIBLE);

            cakeListData = cakeCartDao.selectAll();
            cartListAdapter = new CartListAdapter(getActivity(), cakeListData);
            recyclerView.setAdapter(cartListAdapter);
            cartListAdapter.notifyDataSetChanged();

            totalPrice = cakeCartDao.getTotalPrice();
            tvTotalPrice.setText("总价：" + String.valueOf(totalPrice) + "元");
        }
        cakeCartDao.close();
    }

    private void initView() {
        recyclerView = rootView.findViewById(R.id.cart_list);
        img = rootView.findViewById(R.id.cart_img);
        tip = rootView.findViewById(R.id.cart_tip);
        tvTotalPrice = rootView.findViewById(R.id.cart_total_price);
        settlement = rootView.findViewById(R.id.cart_settlement);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}