package cn.smxy.zhouxuelian1.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.dao.CakeCartDao;
import cn.smxy.zhouxuelian1.entity.Cake;

public class CakeDetailActivity extends AppCompatActivity {
    private static final String TAG = "CakeDetailActivity";

    private ImageView img, addBtn, minusBtn;
    private TextView name, price, introduce, numText;
    private Cake cake;
    private Toolbar toolbar;
    private Button addTocart;
    private int currentNum = 0; // 当前选择的数量
    private CakeCartDao cakeCartDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_detail);

        // 初始化数据库操作类
        cakeCartDao = new CakeCartDao(this);

        initView();
        setListener();
        loadCartData(); // 加载购物车中已有的数量
    }

    private void loadCartData() {
        if (cake != null) {
            // 从数据库获取该蛋糕的当前数量
            currentNum = cakeCartDao.selectCakeNum(cake.getCakeId());
            if (currentNum <= 0) currentNum =0; // 默认为0
            numText.setText(String.valueOf(currentNum));
        }
    }

    private void setListener() {
        // 返回按钮
        toolbar.setNavigationOnClickListener(v -> finish());

        // 数量增加按钮
        addBtn.setOnClickListener(v -> {
            currentNum++;
            numText.setText(String.valueOf(currentNum));
            // 更新数据库中的数量
            cakeCartDao.increseNum(cake.getCakeId());
            Toast.makeText(this, "已增加数量", Toast.LENGTH_SHORT).show();
        });

        // 数量减少按钮
        minusBtn.setOnClickListener(v -> {
            if (currentNum > 1) {
                currentNum--;
                numText.setText(String.valueOf(currentNum));
                // 更新数据库中的数量
                cakeCartDao.decreseNum(cake.getCakeId());
                Toast.makeText(this, "已减少数量", Toast.LENGTH_SHORT).show();
            } else if (currentNum == 1) {
                // 数量为1时，提示用户是否删除
                showDeleteDialog();
            }
        });

        // 添加到购物车按钮（或更新购物车数量）
        addTocart.setOnClickListener(v -> {
            if (cake != null) {
                // 根据当前数量更新购物车
                if (currentNum > 0) {
                    if (cakeCartDao.isExit(cake.getCakeId())) {
                        // 如果已存在，更新数量
                        cakeCartDao.updateCakeNum(cake.getCakeId(), currentNum);
                    } else {
                        // 如果不存在，插入新记录
                        cakeCartDao.insertOrIncreseNum(cake);
                    }
                    Toast.makeText(this, "已更新购物车数量", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "数量不能为0", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 显示删除确认对话框（当数量为1时）
     */
    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要从购物车中删除该蛋糕吗？")
                .setPositiveButton("确认", (dialog, which) -> {
                    // 从购物车删除该蛋糕
                    cakeCartDao.delete(cake.getCakeId());
                    currentNum = 0;
                    numText.setText("0");
                    Toast.makeText(this, "已从购物车删除", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void initView() {
        name = findViewById(R.id.cake_detail_name);
        img = findViewById(R.id.cake_detail_img);
        price = findViewById(R.id.cake_detail_price);
        introduce = findViewById(R.id.cake_detail_intro);
        toolbar = findViewById(R.id.cake_detail_toolbar);
        addTocart = findViewById(R.id.cake_detail_addtocart);
        addBtn = findViewById(R.id.cake_detail_add);
        minusBtn = findViewById(R.id.cake_detail_minus);
        numText = findViewById(R.id.cake_detail_num);

        Intent intent = getIntent();
        cake = (Cake) intent.getSerializableExtra("cake");

        if (cake != null) {
            name.setText(cake.getCakeName());
            price.setText("价格：" + cake.getPrice() + "元");
            introduce.setText("介绍：" + cake.getIntroduce());
            Glide.with(this).load(cake.getCakePicture()).into(img);
        } else {
            Toast.makeText(this, "蛋糕信息加载失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭数据库连接
        if (cakeCartDao != null) {
            cakeCartDao.close();
        }
    }
}