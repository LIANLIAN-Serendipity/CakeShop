package cn.smxy.zhouxuelian1.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

//java集合类，用于存储键值对数据
import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.entity.LoginResponse;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {

    //定义成员变量
    private EditText etcakeUsername, etPassword;
    private Button btnLogin;
    private LinearLayout llBottom;
    private ProgressBar progressBar;
    private TextView tvProgressText, tvReset, tvRegister;
    private RadioGroup rgType;
    private RadioButton rbUser;
    private RadioButton rbAdmin;

    private String selectedRole = "2"; // 默认为普通用户
    private static final int CHANGE_PROGRESS = 1;//进度条更新
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setListeners();

        // 设置默认选中普通用户
        rbUser.setChecked(true);
        selectedRole = "2";

        //选择角色后更新selectedRole值
        rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_admin) {
                selectedRole = "1";
            } else {
                selectedRole = "2";
            }
        });
    }

    //根据findViewById方法获取布局文件中定义的UI组件，并赋值给类的成员变量
    private void initView() {
        btnLogin = findViewById(R.id.login_ok);
        etcakeUsername = findViewById(R.id.et_login_username);
        etPassword = findViewById(R.id.et_login_password);
        tvRegister = findViewById(R.id.tv_register);
        tvReset = findViewById(R.id.tv_mm);
        rgType = findViewById(R.id.rg_type);
        rbUser = findViewById(R.id.rb_user);
        rbAdmin = findViewById(R.id.rb_admin);
        llBottom = findViewById(R.id.ll_bottom);
        progressBar = findViewById(R.id.progressBar);
        tvProgressText = findViewById(R.id.tv_progress_text);
    }

    //登录按钮设置点击事件监听器，点击登录后调用checklogin方法，v表示点击事件发生时的视图对象，btnlogin
    private void setListeners() {
        btnLogin.setOnClickListener(v -> checkLogin());

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkLogin();
//            }
//        });

        tvReset.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RepasswordActivity.class);
            startActivity(intent);
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkLogin() {
        //获取用户输入的用户名和密码
        String cakeusername = etcakeUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 输入验证
        if (cakeusername.isEmpty()) {
            Toast.makeText(this, "账号不能为空!", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "密码不能为空!", Toast.LENGTH_LONG).show();
            return;
        }

        // 调用方法显示进度条
        showLoading();

        Map<String, String> map = new HashMap<>();
        map.put("cakeusername", cakeusername);
        map.put("password", password);
        map.put("role", selectedRole);

        OkHttpUtils.post().url(UrlConstants.LOGIN_URL).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败, 失败原因：" + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "登录失败，请检查网络连接", Toast.LENGTH_LONG).show();
                    hideLoading();
                });
            }

            //gson格式解析响应数据为loginresponse对象，根据返回的code值判断登录结果，并调用savelogininfo保存登录信息
            @Override
            public void onResponse(String response, int id) {
                try {
                    LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
                    if (loginResponse != null) {
                        if (loginResponse.getCode() == 2002) {
                            saveLoginInfo(loginResponse, "普通用户");
                        } else if (loginResponse.getCode() == 2001) {
                            saveLoginInfo(loginResponse, "管理员");
                        } else if (loginResponse.getCode() == 4040) {
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, "用户名密码或信息错误", Toast.LENGTH_LONG).show();
                                hideLoading();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, "登录失败，请重试", Toast.LENGTH_LONG).show();
                                hideLoading();
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "登录失败，服务器返回数据格式错误", Toast.LENGTH_LONG).show();
                            hideLoading();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "登录失败，解析数据出错", Toast.LENGTH_LONG).show();
                        hideLoading();
                    });
                }
            }
        });
    }

    private void saveLoginInfo(LoginResponse loginResponse, String roleName) {
        //获取用户信息
        String token = loginResponse.getToken();
        String cakeuserImage = loginResponse.getCakeuserImage();

        //使用SPUtil工具类将信息保存
        SPUtil.saveString(this, "token", token);
        SPUtil.saveString(this, "cakeusername", etcakeUsername.getText().toString());
        SPUtil.saveString(this, "cakeuserImage", cakeuserImage);
        SPUtil.saveString(this, "role", roleName);
        SPUtil.saveString(this, "cakeuserId", loginResponse.getCakeuserId().toString());

        Log.d(TAG, "登录成功，角色: " + roleName);

        runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this, roleName + "登录成功!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    //进度条
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == CHANGE_PROGRESS) {
                int num = msg.arg1;
                progressBar.setProgress(num);
                tvProgressText.setText(num + "%");

                // 进度完成后隐藏加载框
                if (num >= 100 && isLoading) {
                    hideLoading();
                }
            }
        }
    };

    private void showLoading() {
        isLoading = true;
        if (llBottom != null && progressBar != null && tvProgressText != null) {
            llBottom.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);

            // 启动进度条线程
            ChangeProgressThread changeProgressThread = new ChangeProgressThread();
            changeProgressThread.start();
        }
    }

    private void hideLoading() {
        isLoading = false;
        if (llBottom != null) {
            llBottom.setVisibility(View.GONE);
        }
    }

    class ChangeProgressThread extends Thread {
        @Override
        public void run() {
            for (int i = 1; i <= 100; i++) {
                if (!isLoading) { //加载已完成，退出循环
                    break;
                }
                Message message = handler.obtainMessage();
                message.what = CHANGE_PROGRESS;
                message.arg1 = i;
                handler.sendMessage(message);

                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}