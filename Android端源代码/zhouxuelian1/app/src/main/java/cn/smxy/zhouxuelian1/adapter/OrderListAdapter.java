package cn.smxy.zhouxuelian1.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.activity.ManagOrderActivity;
import cn.smxy.zhouxuelian1.activity.OrderDetailActivity;
import cn.smxy.zhouxuelian1.entity.CakeOrderInfo;
import cn.smxy.zhouxuelian1.entity.CakeOrderListResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    private List<CakeOrderInfo> orderListData;
    private Context context;
    private ManagOrderActivity activity;

    public OrderListAdapter(Context context, List<CakeOrderInfo> orderListData, ManagOrderActivity activity) {
        this.orderListData = orderListData;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_order_management, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        CakeOrderInfo order = orderListData.get(position);
        viewHolder.itemOrderId.setText("订单ID: " + order.getCakeorderId());
        viewHolder.itemOrderUser.setText("用户: " + order.getCustomerName());
        viewHolder.itemOrderDate.setText("下单日期: " + order.getCakeorderTime());
        viewHolder.itemOrderAmount.setText("订单金额: ￥" + order.getCaketotalPrice());
        viewHolder.itemOrderStatus.setText("订单状态: " + getOrderStatus(order.getCakeorderStatus()));

        // 设置修改按钮点击事件
        viewHolder.btnOrderUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getCakeorderId());
            context.startActivity(intent);
        });

        // 设置删除按钮点击事件
        viewHolder.btnOrderDel.setOnClickListener(v -> {
            showDeleteDialog(order.getCakeorderId(), position);
        });
    }

    // 显示删除确认对话框
    private void showDeleteDialog(final int orderId, final int position) {
        new AlertDialog.Builder(context)
                .setTitle("确认删除")
                .setMessage("确定要删除该订单吗？此操作不可恢复")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOrder(orderId, position);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    // 删除订单
    private void deleteOrder(int orderId, final int position) {
        // 修正URL拼接方式，使用占位符替换
        String url = UrlConstants.DELETE_ORDER_URL.replace("{orderId}", String.valueOf(orderId));
        Log.d("OrderListAdapter", "删除订单URL: " + url);

        // 改用DELETE请求方式
        OkHttpUtils.delete()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("OrderListAdapter", "删除订单失败，失败原因：" + e.getMessage());
                        Toast.makeText(context, "删除订单失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("OrderListAdapter", "删除订单响应：" + response);
                        CakeOrderListResponse commonResponse = new Gson().fromJson(response, CakeOrderListResponse.class);
                        if (commonResponse != null && commonResponse.getCode() == 2000) {
                            // 从列表中移除该订单
                            orderListData.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "订单删除成功", Toast.LENGTH_SHORT).show();
                            // 刷新订单列表
                            if (activity != null) {
                                activity.refreshOrderList();
                            }
                        } else {
                            Toast.makeText(context, "删除订单失败：" + (commonResponse != null ? commonResponse.getMsg() : "未知错误"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return orderListData == null ? 0 : orderListData.size();
    }

    // 更新适配器数据
    public void updateData(List<CakeOrderInfo> newData) {
        orderListData.clear();
        orderListData.addAll(newData);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemOrderId, itemOrderUser, itemOrderDate, itemOrderAmount, itemOrderStatus;
        Button btnOrderUpdate, btnOrderDel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemOrderId = itemView.findViewById(R.id.item_order_id);
            itemOrderUser = itemView.findViewById(R.id.item_order_user);
            itemOrderDate = itemView.findViewById(R.id.item_order_date);
            itemOrderAmount = itemView.findViewById(R.id.item_order_amount);
            itemOrderStatus = itemView.findViewById(R.id.item_order_status);
            btnOrderUpdate = itemView.findViewById(R.id.btn_order_update);
            btnOrderDel = itemView.findViewById(R.id.btn_order_del);
        }
    }

    private String getOrderStatus(int status) {
        switch (status) {
            case 1:
                return "进行中";
            case 2:
                return "已完成";
            case 3:
                return "已取消";
            default:
                return "未知状态";
        }
    }
}