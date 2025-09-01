package cn.smxy.zhouxuelian1.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;                  // 新增：解决权限符号问题
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;           // 新增：EditText 控件
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.entity.UpLoadImageResponse;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity {

    private TextView titleTextView; // 标题文本
    private TextView backTextView;  // 返回按钮文本
    private TextView BacktoLogin;
    private Button btn_register;
    private EditText et_register_username;
    private EditText et_register_phone;
    private EditText et_register_address;
    private EditText et_register_repassword;
    private EditText et_register_password;
    private Spinner et_register_sex;
    private ImageView register_userImg;
    private String userImgPath;
    private ActivityResultLauncher<String> resultLauncher;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Uri selectedImageUri;
    private boolean isLoggedIn = false; // 登录状态标记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 检查登录状态
        checkLoginStatus();

        BacktoLogin = findViewById(R.id.tv_BackLogin);
        BacktoLogin.setOnClickListener(new BacktoLoginListener());

        initView();
        setListeners();
        checkStoragePermission();

        // 根据登录状态更新界面
        updateUIAccordingToLoginStatus();
    }

    private void checkLoginStatus() {
        // 从SPUtil检查是否已登录
        String token = SPUtil.getString(this, "token");
        String username = SPUtil.getString(this, "cakeusername");
        isLoggedIn = !token.isEmpty() && !username.isEmpty();
        Log.d(TAG, "登录状态检查: " + (isLoggedIn ? "已登录" : "未登录"));
    }

    private void initView() {
        titleTextView = findViewById(R.id.tv_register_title); // 标题文本视图
        backTextView = findViewById(R.id.tv_BackLogin);       // 返回按钮文本视图
        register_userImg = findViewById(R.id.register_userImg);
        btn_register = findViewById(R.id.btn_register);
        et_register_username = findViewById(R.id.et_register_username);
        et_register_phone = findViewById(R.id.et_register_phone);
        et_register_address = findViewById(R.id.et_register_address);
        et_register_repassword = findViewById(R.id.et_register_repassword);
        et_register_password = findViewById(R.id.et_register_password);
        et_register_sex = findViewById(R.id.et_register_sex);
    }

    class BacktoLoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isLoggedIn) {
                // 已登录时返回MainActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 未登录时返回登录页面
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void setListeners() {
        // 注册按钮点击事件
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri == null) {
                    Toast.makeText(RegisterActivity.this, "请先选择头像", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadImage();
            }
        });

        // 头像选择事件
        resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            selectedImageUri = result;
                            register_userImg.setImageURI(result);
                        }
                    }
                });

        register_userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    resultLauncher.launch("image/*");
                }
            }
        });
    }

    // 检查存储权限
    private boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "已获得存储权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "需要存储权限才能选择图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 上传图片到服务器
    private void uploadImage() {
        if (selectedImageUri == null) {
            Toast.makeText(this, "请先选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = UrlConstants.UPLOAD_IMAGE_URL;
        Log.d(TAG, "上传图片URL: " + url);

        try {
            // 创建临时文件并保存图片
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            if (inputStream == null) {
                Toast.makeText(this, "无法读取图片", Toast.LENGTH_SHORT).show();
                return;
            }

            File tempFile = File.createTempFile("img_", System.currentTimeMillis() + ".png", getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            inputStream.close();

            // 获取原始文件名
            String fileName = getFileName(selectedImageUri);
            if (fileName == null || fileName.isEmpty()) {
                fileName = tempFile.getName();
            }

            // 上传文件
            OkHttpUtils.post()
                    .addFile("file", fileName, tempFile)
                    .url(url)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e(TAG, "图片上传失败: " + e.getMessage());
                            Toast.makeText(RegisterActivity.this, "图片上传失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d(TAG, "图片上传成功, 响应: " + response);
                            try {
                                UpLoadImageResponse upLoadImageResponse = new Gson().fromJson(response, UpLoadImageResponse.class);
                                if (upLoadImageResponse.getCode() == 2000) {
                                    userImgPath = upLoadImageResponse.getDataobject();
                                    register(); // 图片上传成功后执行注册
                                } else {
                                    Toast.makeText(RegisterActivity.this, "图片上传失败: " + upLoadImageResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "解析上传响应失败: " + e.getMessage());
                                Toast.makeText(RegisterActivity.this, "解析上传结果失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, "上传图片异常: " + e.getMessage());
            Toast.makeText(this, "上传图片异常: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // 获取图片文件名
    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int col = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                    if (col != -1) {
                        result = cursor.getString(col);
                    }
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    // 执行注册逻辑
    private void register() {
        String url = UrlConstants.REGISTER_URL;
        String username = et_register_username.getText().toString().trim();
        String phone = et_register_phone.getText().toString().trim();
        String address = et_register_address.getText().toString().trim();
        String password = et_register_password.getText().toString().trim();
        String repassword = et_register_repassword.getText().toString().trim();
        String sex = et_register_sex.getSelectedItem().toString();

        Log.d(TAG, "触发注册: " + username);

        // 表单验证
        if (username.isEmpty()) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (repassword.isEmpty()) {
            Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(repassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address.isEmpty()) {
            Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("cakeusername", username);    // 注意：参数名与后端实体类属性名一致
        params.put("password", password);
        params.put("sex", sex);
        params.put("address", address);
        params.put("phone", phone);
        params.put("role", "2");                // 普通用户角色
        params.put("cakeuserImage", userImgPath); // 头像URL

        // 注意：不传递cakeuserId参数，由后端自动生成

        Log.d(TAG, "注册参数: " + params.toString());

        // 发送注册请求
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "注册失败: " + e.getMessage());
                Toast.makeText(RegisterActivity.this, "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "注册响应: " + response);
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                // 注册成功后根据登录状态决定跳转
                if (isLoggedIn) {
                    // 已登录时返回MainActivity
                    Intent intent = new Intent(RegisterActivity.this, ManageUserActivity.class);
                    startActivity(intent);
                } else {
                    // 未登录时跳转到登录页面
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    // 根据登录状态更新界面
    private void updateUIAccordingToLoginStatus() {
        if (isLoggedIn) {
            // 已登录状态
            titleTextView.setText("添加用户");
            backTextView.setText("返回首页");
            btn_register.setText("添加");
        } else {
            // 未登录状态
            titleTextView.setText("新用户注册");
            backTextView.setText("返回登录");
            btn_register.setText("注册");
        }
    }
}
