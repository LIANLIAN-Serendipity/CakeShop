package cn.smxy.zhouxuelian1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.activity.OrderDetailActivity;
import cn.smxy.zhouxuelian1.entity.CakeOrderInfo;

public class UserOrderListAdapter extends RecyclerView.Adapter<UserOrderListAdapter.MyViewHolder> {
    private List<CakeOrderInfo> orderListData;
    private Context context;

    public UserOrderListAdapter(Context context, List<CakeOrderInfo> orderListData) {
        this.orderListData = orderListData;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.user_order_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        CakeOrderInfo order = orderListData.get(position);
        viewHolder.cakeorderId.setText("订单ID：" + order.getCakeorderId());
        viewHolder.cakeorderTime.setText("下单时间：" + order.getCakeorderTime());

        // 设置订单状态
        if (order.getCakeorderStatus() == 1) {
            viewHolder.cakeorderStatus.setText("订单状态：未发货");
        } else if (order.getCakeorderStatus() == 2) {
            viewHolder.cakeorderStatus.setText("订单状态：已完成");
        } else {
            viewHolder.cakeorderStatus.setText("订单状态：已取消");
        }

        // 关键修改：设置详情按钮的点击事件并传递订单ID
        viewHolder.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getCakeorderId()); // 传递订单ID
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (orderListData == null) {
            return 0;
        } else {
            return orderListData.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cakeorderId, cakeorderTime, cakeorderStatus;
        Button btnDetail; // 订单详情按钮

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cakeorderTime = itemView.findViewById(R.id.order_list_item_time);
            cakeorderStatus = itemView.findViewById(R.id.order_list_item_status);
            cakeorderId = itemView.findViewById(R.id.order_list_item_id);
            btnDetail = itemView.findViewById(R.id.btn_order_detail); // 获取详情按钮
        }
    }
}












