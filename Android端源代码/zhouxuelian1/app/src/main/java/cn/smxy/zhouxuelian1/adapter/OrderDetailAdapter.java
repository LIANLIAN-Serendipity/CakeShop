package cn.smxy.zhouxuelian1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeOrderDetail;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private Context context;
    private List<CakeOrderDetail> orderDetails;
    private Map<Integer, Cake> cakeMap = new HashMap<>();

    // 简化构造，适配初始空数据
    public OrderDetailAdapter(Context context, List<CakeOrderDetail> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    public void setCakeList(List<Cake> cakeList) {
        cakeMap.clear();
        if (cakeList != null) {
            for (Cake cake : cakeList) {
                cakeMap.put(cake.getCakeId(), cake);
            }
        }
        notifyDataSetChanged();
    }

    public void setOrderDetails(List<CakeOrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (orderDetails == null || position >= orderDetails.size()) {
            Log.w("OrderDetailAdapter", "Invalid position: " + position);
            return;
        }

        CakeOrderDetail detail = orderDetails.get(position);
        if (detail == null) {
            Log.w("OrderDetailAdapter", "Null detail at position: " + position);
            return;
        }

        Cake cake = cakeMap.get(detail.getCakeId());
        if (cake != null) {
            holder.tvCakeName.setText(cake.getCakeName());
            holder.tvCakePrice.setText("单价: ￥" + String.format("%.2f", cake.getPrice()));

            // 从 Cake 中获取 introduce 作为描述
            if (cake.getIntroduce() != null) {
                holder.tvCakeInfo.setText(cake.getIntroduce());
            } else {
                holder.tvCakeInfo.setText("无蛋糕描述");
            }

            if (cake.getCakePicture() != null && !cake.getCakePicture().isEmpty()) {
                Glide.with(context)
                        .load(cake.getCakePicture())
                        .placeholder(R.drawable.emptyimage)
                        .error(R.drawable.emptyimage)
                        .into(holder.ivCakeImage);
            } else {
                holder.ivCakeImage.setImageResource(R.drawable.emptyimage);
            }
        } else {
            Log.w("OrderDetailAdapter", "Cake not found for cakeId: " + detail.getCakeId());
            holder.tvCakeName.setText("未知蛋糕");
            holder.tvCakePrice.setText("单价: ￥0.00");
            holder.tvCakeInfo.setText("无蛋糕描述");
            holder.ivCakeImage.setImageResource(R.drawable.emptyimage);
        }

        holder.tvCakeId.setText("蛋糕ID: " + detail.getCakeId());
//        holder.tvSubtotal.setText("小计: ￥" + String.format("%.2f", detail.getSubtotal()));
        holder.tvCakeQuantity.setText("x " + detail.getNum());
    }

    @Override
    public int getItemCount() {
        return orderDetails != null ? orderDetails.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCakeImage;
        TextView tvCakeName, tvCakeId, tvCakePrice, tvSubtotal, tvCakeQuantity, tvCakeInfo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCakeImage = itemView.findViewById(R.id.iv_cake_image);
            tvCakeName = itemView.findViewById(R.id.tv_cake_name);
            tvCakeId = itemView.findViewById(R.id.tv_cake_id);
            tvCakePrice = itemView.findViewById(R.id.tv_cake_price);
            tvSubtotal = itemView.findViewById(R.id.tv_total_price);
            tvCakeQuantity = itemView.findViewById(R.id.tv_cake_quantity);
            tvCakeInfo = itemView.findViewById(R.id.tv_cake_info);
        }
    }
}