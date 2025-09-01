package cn.smxy.zhouxuelian1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeListResponse;
import cn.smxy.zhouxuelian1.entity.CakeType;
import cn.smxy.zhouxuelian1.entity.CakeTypeListResponse;
import cn.smxy.zhouxuelian1.entity.CakeUserInfo;
import cn.smxy.zhouxuelian1.entity.UpLoadImageResponse;
import cn.smxy.zhouxuelian1.entity.UserListResponse;
import cn.smxy.zhouxuelian1.entity.UserResponse;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class UpdateUserActivity extends AppCompatActivity {

    private static final String TAG = "UpdateUserActivity";
    private String userId;
    private ImageView avatar;
    private ActivityResultLauncher<String> resultLauncher;
    private Button updateButton;
    private EditText accountEditText, phoneEditText, passwordEditText;
    private EditText addressEditText;
    private TextView genderTextView, roleTextView;
    private Toolbar backToolbar;
    private String avatarUrl;
    private CakeUserInfo userInfo;
    private String uploadedImageUrl;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        userId = getIntent().getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            userId = SPUtil.getString(this, "cakeuserId");
        }
        userRole = SPUtil.getString(this, "role");
        Log.d(TAG, "用户ID: " + userId + ", 角色: " + userRole);

        initView();

        if (userId != null && !userId.isEmpty()) {
            loadUserInfo();
        } else {
            Toast.makeText(this, "用户ID无效", Toast.LENGTH_SHORT).show();
        }

        setupListeners();
    }

    private void initView() {
        backToolbar = findViewById(R.id.back);
        avatar = findViewById(R.id.update_user_avatar);
        updateButton = findViewById(R.id.update_user_button);
        accountEditText = findViewById(R.id.update_user_account);
        phoneEditText = findViewById(R.id.update_user_phone);
        passwordEditText = findViewById(R.id.update_user_password);
        addressEditText = findViewById(R.id.update_user_address);
        genderTextView = findViewById(R.id.update_user_gender);
        roleTextView = findViewById(R.id.update_user_role);

        accountEditText.setText("加载中...");
        phoneEditText.setText("加载中...");
        passwordEditText.setText("");
        addressEditText.setText("加载中...");
        genderTextView.setText("加载中...");
        roleTextView.setText("加载中...");
    }

    private void loadUserInfo() {
        String url = UrlConstants.USER_FIND_BY_ID_URL + userId;
        Log.d(TAG, "加载用户信息URL: " + url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "获取用户信息失败: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(UpdateUserActivity.this, "获取用户信息失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    setDefaultValues();
                });
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "用户信息响应: " + response);
                try {
                    UserResponse userResponse = new Gson().fromJson(response, UserResponse.class);
                    if (userResponse != null && userResponse.getCode() == 2000) {
                        userInfo = userResponse.getUser();
                        if (userInfo != null) {
                            Log.d(TAG, "解析后的用户信息: " + userInfo.toString());
                            runOnUiThread(() -> updateUIWithUserInfo(userInfo));
                        } else {
                            Log.e(TAG, "用户数据为空");
                            runOnUiThread(() -> {
                                Toast.makeText(UpdateUserActivity.this, "用户数据为空", Toast.LENGTH_SHORT).show();
                                setDefaultValues();
                            });
                        }
                    } else {
                        String errorMsg = userResponse != null ? userResponse.getMsg() : "获取用户信息失败";
                        Log.e(TAG, errorMsg);
                        runOnUiThread(() -> {
                            Toast.makeText(UpdateUserActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                            setDefaultValues();
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "解析用户信息失败: " + e.getMessage(), e);
                    runOnUiThread(() -> {
                        Toast.makeText(UpdateUserActivity.this, "解析用户信息失败", Toast.LENGTH_SHORT).show();
                        setDefaultValues();
                    });
                }
            }
        });
    }

    private void setDefaultValues() {
        accountEditText.setText("无数据");
        phoneEditText.setText("无数据");
        passwordEditText.setText("");
        addressEditText.setText("无数据");
        genderTextView.setText("无数据");
        roleTextView.setText("无数据");
        avatar.setImageResource(R.mipmap.ic_launcher);
    }

    private void updateUIWithUserInfo(CakeUserInfo user) {
        if (user == null) {
            Log.e(TAG, "用户信息为空");
            setDefaultValues();
            return;
        }

        accountEditText.setText(user.getCakeusername());
        phoneEditText.setText(user.getPhone());
        passwordEditText.setText(user.getPassword() != null ? user.getPassword() : "");
        addressEditText.setText(user.getAddress());
        genderTextView.setText(user.getSex());
        roleTextView.setText(user.getRoleText());

        if (user.getCakeuserImage() != null && !user.getCakeuserImage().isEmpty()) {
            Glide.with(this)
                    .load(user.getCakeuserImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    .into(avatar);
            avatarUrl = user.getCakeuserImage();
            uploadedImageUrl = user.getCakeuserImage();
        } else {
            avatar.setImageResource(R.mipmap.ic_launcher);
            uploadedImageUrl = "";
        }

        if (!"管理员".equals(userRole)) {
            roleTextView.setEnabled(false);
            roleTextView.setClickable(false);
            roleTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void setupListeners() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Log.d(TAG, "选择的头像URI: " + result);
                        if (result != null) {
                            avatar.setImageURI(result);
                            uploadedImageUrl = "";
                        }
                    }
                });

        avatar.setOnClickListener(v -> resultLauncher.launch("image/*"));

        genderTextView.setOnClickListener(v -> showGenderSelectionDialog());

        if ("管理员".equals(userRole)) {
            roleTextView.setOnClickListener(v -> showRoleSelectionDialog());
        } else {
            roleTextView.setOnClickListener(null);
        }

        backToolbar.setNavigationOnClickListener(v -> {
            Intent intent = "管理员".equals(userRole)
                    ? new Intent(UpdateUserActivity.this, ManageUserActivity.class)
                    : new Intent(UpdateUserActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        updateButton.setOnClickListener(v -> updateUserInfo());
    }

    private void showGenderSelectionDialog() {
        String[] genders = {"男", "女"};
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("选择性别");
        dlg.setItems(genders, (dialog, which) -> genderTextView.setText(genders[which]));
        dlg.show();
    }

    private void showRoleSelectionDialog() {
        String[] roles = {"普通用户", "管理员"};
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("选择角色");
        dlg.setItems(roles, (dialog, which) -> {
            roleTextView.setText(roles[which]);
            if (userInfo != null) {
                userInfo.setRole(String.valueOf(which + 1));
            }
        });
        dlg.show();
    }

    private void updateUserInfo() {
        if (userInfo == null) {
            Toast.makeText(this, "用户信息未加载，请重试", Toast.LENGTH_SHORT).show();
            return;
        }

        String account = accountEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String gender = genderTextView.getText().toString().trim();
        String roleText = roleTextView.getText().toString().trim();

        if (account.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "账户和手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("cakeuserId", userId);
        params.put("cakeusername", account);
        params.put("phone", phone);
        params.put("address", address);
        params.put("sex", gender);

        if ("管理员".equals(userRole)) {
            params.put("role", "管理员".equals(roleText) ? "1" : "2");
        } else {
            params.put("role", userInfo.getRole());
        }

        if (!password.isEmpty()) {
            params.put("password", password);
        }

        if (hasNewAvatar()) {
            uploadAvatar(params);
        } else {
            if (uploadedImageUrl != null && !uploadedImageUrl.isEmpty()) {
                params.put("cakeuserImage", uploadedImageUrl);
            }
            updateUser(params);
        }
    }

    private boolean hasNewAvatar() {
        Drawable drawable = avatar.getDrawable();
        if (drawable == null) {
            return false;
        }

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap() != null;
        }

        String uriString = drawable.toString();
        return uriString.startsWith("content://");
    }

    private void uploadAvatar(Map<String, String> baseParams) {
        String url = UrlConstants.UPLOAD_IMAGE_URL;
        OkHttpUtils.post()
                .addFile("file", "avatar.png", toImageFile("avatar.png"))
                .url(url).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "上传头像失败: " + e.getMessage());
                        Toast.makeText(UpdateUserActivity.this, "上传头像失败，将使用原头像", Toast.LENGTH_SHORT).show();
                        if (uploadedImageUrl != null && !uploadedImageUrl.isEmpty()) {
                            baseParams.put("cakeuserImage", uploadedImageUrl);
                        }
                        updateUser(baseParams);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "上传头像响应: " + response);
                        try {
                            UpLoadImageResponse upLoadImageResponse = new Gson().fromJson(response, UpLoadImageResponse.class);
                            if (upLoadImageResponse != null && upLoadImageResponse.getCode() == 2000) {
                                String avatarUrl = upLoadImageResponse.getDataobject();
                                baseParams.put("cakeuserImage", avatarUrl);
                                uploadedImageUrl = avatarUrl;
                            } else {
                                String errorMsg = upLoadImageResponse != null ? upLoadImageResponse.getMsg() : "头像上传失败";
                                Log.e(TAG, errorMsg);
                                Toast.makeText(UpdateUserActivity.this, errorMsg + "，将使用原头像", Toast.LENGTH_SHORT).show();
                                if (uploadedImageUrl != null && !uploadedImageUrl.isEmpty()) {
                                    baseParams.put("cakeuserImage", uploadedImageUrl);
                                }
                            }
                            updateUser(baseParams);
                        } catch (Exception e) {
                            Log.e(TAG, "解析头像响应失败: " + e.getMessage());
                            Toast.makeText(UpdateUserActivity.this, "解析头像响应失败，将使用原头像", Toast.LENGTH_SHORT).show();
                            if (uploadedImageUrl != null && !uploadedImageUrl.isEmpty()) {
                                baseParams.put("cakeuserImage", uploadedImageUrl);
                            }
                            updateUser(baseParams);
                        }
                    }
                });
    }

    private File toImageFile(String fileName) {
        Drawable drawable = avatar.getDrawable();
        if (drawable == null) {
            Log.e(TAG, "头像Drawable为空，无法创建文件");
            return null;
        }

        Bitmap bitmap = null;
        try {
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            } else {
                String uriString = drawable.toString();
                if (uriString.startsWith("content://")) {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uriString));
                } else {
                    Log.e(TAG, "不支持的Drawable类型: " + uriString);
                    return null;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "获取Bitmap失败: " + e.getMessage(), e);
            return null;
        }

        if (bitmap == null) {
            Log.e(TAG, "获取的Bitmap为空");
            return null;
        }

        File file = new File(getCacheDir(), fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            Log.d(TAG, "成功创建临时图片文件: " + file.getAbsolutePath());
            return file;
        } catch (IOException e) {
            Log.e(TAG, "保存头像文件失败: " + e.getMessage(), e);
            return null;
        }
    }

    private void updateUser(Map<String, String> params) {
        String url = UrlConstants.USER_UPDATE_URL;
        Log.d(TAG, "更新用户信息URL: " + url);
        Log.d(TAG, "更新用户信息参数: " + params);

        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "更新用户信息失败: " + e.getMessage());
                Toast.makeText(UpdateUserActivity.this, "更新失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "更新用户信息响应: " + response);
                try {
                    Map<String, Object> responseMap = new Gson().fromJson(response, Map.class);
                    if (responseMap != null && responseMap.get("code") != null && responseMap.get("code").equals(2000)) {
                        Toast.makeText(UpdateUserActivity.this, "更新用户信息成功", Toast.LENGTH_SHORT).show();

                        Intent intent;
                        if ("管理员".equals(userRole)) {
                            intent = new Intent(UpdateUserActivity.this, ManageUserActivity.class);
                        } else {
                            intent = new Intent(UpdateUserActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = responseMap != null ? (String) responseMap.get("msg") : "更新失败";
                        Log.e(TAG, errorMsg);
                        Toast.makeText(UpdateUserActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "解析更新响应失败: " + e.getMessage());
                    Toast.makeText(UpdateUserActivity.this, "解析更新响应失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}