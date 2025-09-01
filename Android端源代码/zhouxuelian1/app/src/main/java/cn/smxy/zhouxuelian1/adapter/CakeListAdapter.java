package cn.smxy.zhouxuelian1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.smxy.zhouxuelian1.R;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.smxy.zhouxuelian1.activity.CakeDetailActivity;
import cn.smxy.zhouxuelian1.dao.CakeCartDao;
import cn.smxy.zhouxuelian1.entity.Cake;

public class CakeListAdapter extends BaseAdapter {
    private static final String TAG = "CakeListAdapter";
    private Context context;
    private List<Cake> cakeListData;

    public CakeListAdapter(Context context, List<Cake> cakeListData) {
        this.context = context;
        this.cakeListData = cakeListData;
    }

    @Override
    public int getCount() {
        if (cakeListData != null) {
            return cakeListData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return cakeListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.cakelist_item, parent, false);
            viewHolder = new ViewHolder(itemView);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

        Cake cake = cakeListData.get(position);

        viewHolder.name.setText(cake.getCakeName());
        viewHolder.price.setText("价格：" + cake.getPrice() + "元");
        String intro = cake.getIntroduce();
        int len = intro.length();
        String substr;
        if (len >= 25) {
            substr = intro.substring(0, 25) + "...";
        } else {
            substr = intro;
        }
        viewHolder.introduce.setText(substr);
        Glide.with(context).load(cake.getCakePicture()).into(viewHolder.img);

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(viewHolder.num.getText().toString());
                n++;
                viewHolder.num.setText(n+"");
                CakeCartDao cakeCartDao = new CakeCartDao(context);
                cakeCartDao.insertOrIncreseNum(cake);
                cakeCartDao.close();
            }
        });
        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(viewHolder.num.getText().toString());
                if(n>0){
                    n--;
                    viewHolder.num.setText(n+"");
                    CakeCartDao cakeCartDao = new CakeCartDao(context);
                    cakeCartDao.decreseNumOrdelete(cake.getCakeId());
                    cakeCartDao.close();
                }
            }
        });
        // 单击列表项事件
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CakeDetailActivity.class);
                intent.putExtra("cake", cake); // 确保 Cake 类实现了 Serializable 接口
                context.startActivity(intent);
            }
        });

        CakeCartDao cakeCartDao = new CakeCartDao(context);
        int num = cakeCartDao.selectCakeNum(cake.getCakeId());
        viewHolder.num.setText(String.valueOf(num));

        return itemView;
    }

    // 内部类
    class ViewHolder {
        TextView name, price, introduce, num;
        ImageView img, add, minus;

        public ViewHolder(View itemView) {
            this.name = itemView.findViewById(R.id.cakelist_item_name);
            this.price = itemView.findViewById(R.id.cakelist_item_price);
            this.introduce = itemView.findViewById(R.id.cakelist_item_introduce);
            this.img = itemView.findViewById(R.id.cakelist_item_img);
            this.add = itemView.findViewById(R.id.cakelist_item_add);
            this.minus = itemView.findViewById(R.id.cakelist_item_minus);
            this.num = itemView.findViewById(R.id.cakelist_item_num);
        }
    }
}