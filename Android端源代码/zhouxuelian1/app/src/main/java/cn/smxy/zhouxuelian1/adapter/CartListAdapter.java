package cn.smxy.zhouxuelian1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.dao.CakeCartDao;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.fragment.CartFragment;


public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {

    private List<Cake> cakeListData;
    private Context context;

    public CartListAdapter(Context context, List<Cake> cakeListData) {
        this.cakeListData = cakeListData;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cakelist_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        Cake cake = cakeListData.get(position);
        if (cake != null) {
            viewHolder.name.setText(cake.getCakeName());
            viewHolder.price.setText(String.valueOf(cake.getPrice()));
            viewHolder.num.setText(String.valueOf(cake.getNum()));
            String intro = cake.getIntroduce();
            int len = intro.length();
            String substr;
            if (len >= 25) {
                substr = intro.substring(0, 25) + "......";
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
                    viewHolder.num.setText(String.valueOf(n));
                    CakeCartDao cakeCartDao = new CakeCartDao(context);
                    cakeCartDao.addToCart(cake);
                    cakeCartDao.close();

                    changeTotalPrice();
                }
            });

            viewHolder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = Integer.parseInt(viewHolder.num.getText().toString());
                    n--;
                    viewHolder.num.setText(String.valueOf(n));
                    CakeCartDao cakeCartDao = new CakeCartDao(context);
                    cakeCartDao.decreseNumOrdelete(cake.getCakeId());
                    cakeCartDao.close();

                    if (n == 0) {
                        cakeListData.remove(cake);
                        notifyDataSetChanged();
                    }
                    changeTotalPrice();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (cakeListData == null) {
            return 0;
        }
        return cakeListData.size();
    }

    //实时更新总价
    private void changeTotalPrice(){
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment instanceof CartFragment){
                CartFragment cartFragment = (CartFragment) fragment;
                cartFragment.getDataFromDB();
                break;
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, introduce, num;
        ImageView img, add, minus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cakelist_item_name);
            price = itemView.findViewById(R.id.cakelist_item_price);
            introduce = itemView.findViewById(R.id.cakelist_item_introduce);
            img = itemView.findViewById(R.id.cakelist_item_img);
            add = itemView.findViewById(R.id.cakelist_item_add);
            minus = itemView.findViewById(R.id.cakelist_item_minus);
            num = itemView.findViewById(R.id.cakelist_item_num);
        }
    }
}