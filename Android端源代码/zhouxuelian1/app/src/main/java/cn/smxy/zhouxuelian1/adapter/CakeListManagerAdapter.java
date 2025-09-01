package cn.smxy.zhouxuelian1.adapter;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import cn.smxy.zhouxuelian1.R;

import java.util.List;

import cn.smxy.zhouxuelian1.activity.ManagementActivity;
import cn.smxy.zhouxuelian1.activity.UpdateCakeActivity;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;


public class CakeListManagerAdapter extends BaseAdapter {
    private List<Cake> cakeListData;
    private Context context;
    private MyOnClickItemListener myOnClickItemListener;
    private ManagementActivity ManagementActivity;
    public CakeListManagerAdapter(Context context, List<Cake> cakeList){
        cakeListData = cakeList;
        this.context = context;
    }
    public int getCount(){
        if (cakeListData !=null){
            return cakeListData.size();
        }
        return 0;
    }
    @Override
    public Object getItem(int position){
        return position;
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    public void setListData(List<Cake> ListData) {
        this.cakeListData = ListData;
        notifyDataSetChanged();
    }

    public void setMyOnClickItemListener(MyOnClickItemListener myOnClickItemListener) {
        this.myOnClickItemListener = myOnClickItemListener;
    }

    public View getView(int position, View itemView, ViewGroup parent) {
       ViewHolder viewHolder;
        if (itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_commodity_management, parent, false);
            viewHolder = new ViewHolder(itemView) ;
            itemView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) itemView.getTag();
        }
        //显示列表
        Cake cake = cakeListData.get(position);
        double price = cake.getPrice();
        Glide.with(context).load(cake.getCakePicture()).into(viewHolder.ivcommodityImage);
        viewHolder.tvcommodityPrice.setText(cake.getPrice()+"");
        viewHolder.tvcommodityName.setText(cake.getCakeName());
        //删除
        viewHolder.btncommoditydel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示")
                        .setMessage("确定要将该蛋糕删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete(position);
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();
            }
        });
        //修改
        viewHolder.btncommodityupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(position);
            }
        });


        return itemView;
    }

    private void delete(int position){
        Cake cake = cakeListData.get(position);
        String url = UrlConstants.DELETE_CAKE_URL+cake.getCakeId();
            OkHttpUtils.get().url(url).build().execute(new StringCallback(){
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d(TAG,"网络请求失败，失败原因："+e.getMessage());
                }
                public void onResponse(String response, int id) {
                    Log.d(TAG, "删除成功，响应：" + response);
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
//                    commodityManagementActivity.getData();
                    Intent intent=new Intent(context, ManagementActivity.class);
                    context.startActivity(intent);
//                    finish();

                }
            });
    }
    private void update(int position){
        Cake cake = cakeListData.get(position);
                Intent intent=new Intent(context, UpdateCakeActivity.class);
                Log.d(TAG,"TETETE:"+cake.getCakeId()+"  "+cake.getCakeId()+"");
                intent.putExtra("cakeId", cake.getCakeId()+"");
                context.startActivity(intent);

//                    finish();

    }

    class ViewHolder {
        TextView tvcommodityName,tvcommodityPrice,timedetail;
        ImageView ivcommodityImage;
        private Button btncommoditydel,btncommodityupdate;
        public ViewHolder(View itemView) {
//            super(itemView);
            this.tvcommodityName = itemView.findViewById(R.id.item_commodity_name);
            this.tvcommodityPrice = itemView.findViewById(R.id.item_commodity_price);
            this.ivcommodityImage = itemView.findViewById(R.id.item_commodity_img);
            this.btncommoditydel = itemView.findViewById(R.id.btn_commodity_del);
            this.btncommodityupdate= itemView.findViewById(R.id.btn_commodity_update);
            this.timedetail= itemView.findViewById(R.id.item_timedetail);
        }
    }


}
