package cn.smxy.zhouxuelian1.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import cn.smxy.zhouxuelian1.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.smxy.zhouxuelian1.entity.ServerResponse;
import cn.smxy.zhouxuelian1.fragment.CartFragment;
import cn.smxy.zhouxuelian1.fragment.CakeHomeFragment;
import cn.smxy.zhouxuelian1.fragment.MultipleFragment;
import cn.smxy.zhouxuelian1.fragment.PersonCenterFragment;
import cn.smxy.zhouxuelian1.fragment.CakeUserOrderFragment;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private CakeHomeFragment cakeHomeFragment;
    private MultipleFragment multipleFragment;
    private CartFragment cartFragment;
    private CakeUserOrderFragment cakeUserOrderFragment;
    private PersonCenterFragment personCenterFragment;

    //Fragment
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            checkToken();

            initView();
            setListener();
            changeFragment(cakeHomeFragment);
        }

    private void checkToken() {
        String token = SPUtil.getString(this, "token");
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            String url = UrlConstants.CHECK_TOKEN_URL;
            Map<String, String> headersMap = new HashMap<>();
            headersMap.put("token", token);
            OkHttpUtils.get().url(url).headers(headersMap).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d(TAG,"网络请求错误，失败原因："+e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    ServerResponse serverResponse = new Gson().fromJson(response, ServerResponse.class);
                    if(serverResponse != null && serverResponse.getCode() !=2000){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void initView() {
            bottomNavigationView = findViewById(R.id.user_main_bottom_navigation);
            cakeHomeFragment = new CakeHomeFragment();
            multipleFragment = new MultipleFragment();
            cartFragment = new CartFragment();
            cakeUserOrderFragment = new CakeUserOrderFragment();
            personCenterFragment = new PersonCenterFragment();

//        tvReset = this.findViewById(R.id.reset);
/*        progressBar = this.findViewById(R.id.progressBar1);
        tvProgressText = this.findViewById(R.id.progress_text1);
        llBottom = this.findViewById(R.id.ll_Bottom1);*/
        }

        private void setListener() {
            //底部导航，选项发生改变时，触发setOnItemSelectedListener监听者
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                //MenuItem item当前菜单的单击项
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();//获得菜单项的Id
                    if(itemId == R.id.menu_home){
                        changeFragment(cakeHomeFragment);//分装方法，动态加载不同的片段，不同的片段作为参数
                    } else if (itemId == R.id.menu_multiple){
                        changeFragment(multipleFragment);
                    } else if (itemId == R.id.menu_cart){
                        changeFragment(cartFragment);
                    } else if (itemId == R.id.menu_order){
                        changeFragment(cakeUserOrderFragment);
                    } else if (itemId == R.id.menu_person_center){
                        changeFragment(personCenterFragment);
                    }
                    return true;
                }
            });
        }

        //动态加载片段，根据参数选择加载的片段，如首页、购物车
        private void changeFragment(Fragment fragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.user_main_content, fragment);
            transaction.commit();
        }
    }
