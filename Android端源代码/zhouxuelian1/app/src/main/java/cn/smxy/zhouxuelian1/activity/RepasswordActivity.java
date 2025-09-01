package cn.smxy.zhouxuelian1.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.entity.LoginResponse;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class RepasswordActivity extends AppCompatActivity {

    private Button btn_repwd;
    private EditText et_repwd_username;
    private EditText et_repwd_phone;
    private TextView tv_BacktoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repassword);

        tv_BacktoLogin = findViewById(R.id.tv_BacktoLogin);
        tv_BacktoLogin.setOnClickListener(new Repwd_BacktoLoginListener());

        initView();
        setListeners();
    }

    class Repwd_BacktoLoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 返回主页面
            Intent intent = new Intent(RepasswordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        btn_repwd = findViewById(R.id.btn_repwd);
        et_repwd_username = findViewById(R.id.et_repwd_username);
        et_repwd_phone = findViewById(R.id.et_repwd_phone);
    }

    private void setListeners() {
        btn_repwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String url = UrlConstants.REPASSWARD_URL;
        String cakeusername = et_repwd_username.getText().toString().trim();
        String phone = et_repwd_phone.getText().toString().trim();
        String newPassword = "123"; // 重置后的密码默认为123

        // 校验输入
        if (cakeusername.isEmpty()) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        // 构建请求参数
        Map<String, String> map = new HashMap<>();
        map.put("cakeusername", cakeusername);
        map.put("phone", phone);
        map.put("password", newPassword); // 使用服务端期望的参数名 "password"

        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败,失败原因：" + e.getMessage());
                Toast.makeText(RepasswordActivity.this, "重置失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "密码重置响应: " + response);
                try {
                    // 解析服务端响应
                    Map<String, Object> responseMap = new Gson().fromJson(response, Map.class);
                    if (responseMap != null && responseMap.get("code") != null &&
                            responseMap.get("code").equals(2000)) {
                        // 优化成功提示文案
                        Toast.makeText(RepasswordActivity.this, "密码重置成功，请返回登录页面", Toast.LENGTH_SHORT).show();
                        // 清除本地保存的用户信息
                        SPUtil.removeString(RepasswordActivity.this, "token");
                        SPUtil.removeString(RepasswordActivity.this, "cakeusername");
                        SPUtil.removeString(RepasswordActivity.this, "cakeuserId");
                        SPUtil.removeString(RepasswordActivity.this, "cakeuserImage");
                        // 跳转到登录页面
                        Intent intent = new Intent(RepasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = responseMap != null && responseMap.get("msg") != null
                                ? responseMap.get("msg").toString() : "重置失败";
                        Toast.makeText(RepasswordActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "解析响应失败: " + e.getMessage());
                    Toast.makeText(RepasswordActivity.this, "重置失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
