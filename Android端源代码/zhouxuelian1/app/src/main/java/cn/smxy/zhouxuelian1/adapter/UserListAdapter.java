package cn.smxy.zhouxuelian1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.activity.ManageUserActivity;
import cn.smxy.zhouxuelian1.activity.UpdateUserActivity;
import cn.smxy.zhouxuelian1.entity.CakeUserInfo;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;


public class UserListAdapter extends BaseAdapter {

    private static final String TAG = "UserListAdapter";
    private List<CakeUserInfo> userListData;
    private Context context;
    private ManageUserActivity manageUserActivity;
    // 默认头像资源ID
    private final int DEFAULT_AVATAR = R.mipmap.ic_launcher;

    public UserListAdapter(Context context, List<CakeUserInfo> userList) {
        userListData = userList;
        this.context = context;
        this.manageUserActivity = (ManageUserActivity) context;
    }

    @Override
    public int getCount() {
        if (userListData != null) {
            return userListData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setListData(List<CakeUserInfo> listData) {
        this.userListData = listData;
        notifyDataSetChanged();
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
            viewHolder = new ViewHolder(itemView);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

        // 显示列表数据
        CakeUserInfo user = userListData.get(position);

        // 加载用户头像（修复版）
        loadImageFromUrl(user.getCakeuserImage(), viewHolder.img);

        viewHolder.username.setText(user.getCakeusername());
        viewHolder.userId.setText("ID: " + user.getCakeuserId());
        // 显示角色文字
        viewHolder.userRole.setText(getRoleText(user.getRole()));
        viewHolder.sex.setText(user.getSex());

        // 删除按钮点击事件
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示")
                        .setMessage("确定要删除该用户吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteUser(position);
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });

        // 修改按钮点击事件
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(position);
            }
        });

        return itemView;
    }

    // 修复版图片加载方法
    private void loadImageFromUrl(String imageUrl, ImageView imageView) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            InputStream input = null;
            try {
                // 处理空URL情况
                if (imageUrl == null || imageUrl.trim().isEmpty()) {
                    setDefaultAvatar(imageView);
                    return;
                }

                URL url = new URL(imageUrl);
                connection = (HttpURLConnection) url.openConnection();
                // 设置超时
                connection.setConnectTimeout(10000); // 10秒连接超时
                connection.setReadTimeout(15000);    // 15秒读取超时
                connection.setRequestMethod("GET");
                connection.connect();

                // 检查响应码
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);

                    if (bitmap != null) {
                        // 在主线程更新UI
                        manageUserActivity.runOnUiThread(() ->
                                imageView.setImageBitmap(bitmap)
                        );
                    } else {
                        setDefaultAvatar(imageView);
                    }
                } else {
                    Log.e(TAG, "图片请求失败，响应码: " + responseCode);
                    setDefaultAvatar(imageView);
                }
            } catch (Exception e) {
                Log.e(TAG, "图片加载异常: " + e.getMessage());
                setDefaultAvatar(imageView);
            } finally {
                // 关闭资源
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    // 设置默认头像
    private void setDefaultAvatar(ImageView imageView) {
        manageUserActivity.runOnUiThread(() ->
                imageView.setImageResource(DEFAULT_AVATAR)
        );
    }

    // 角色数字转文字
    private String getRoleText(String roleCode) {
        if ("1".equals(roleCode)) {
            return "管理员";
        } else if ("2".equals(roleCode)) {
            return "普通用户";
        } else {
            return "未知角色";
        }
    }

    private void deleteUser(int position) {
        CakeUserInfo user = userListData.get(position);
        String url = UrlConstants.DELETE_USER_URL + user.getCakeuserId();

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败，失败原因：" + e.getMessage());
                Toast.makeText(context, "删除失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "删除用户响应：" + response);
                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                // 重新加载用户列表
                Intent intent = new Intent(context, ManageUserActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void updateUser(int position) {
        CakeUserInfo user = userListData.get(position);
        Intent intent = new Intent(context, UpdateUserActivity.class);
        intent.putExtra("userId", user.getCakeuserId() + "");
        context.startActivity(intent);
    }

    class ViewHolder {
        TextView username, userId, userRole, sex;
        ImageView img;
        Button deleteBtn, editBtn;

        public ViewHolder(View itemView) {
            this.username = itemView.findViewById(R.id.item_username);
            this.userId = itemView.findViewById(R.id.item_user_id);
            this.userRole = itemView.findViewById(R.id.item_user_role);
            this.sex = itemView.findViewById(R.id.item_user_gender);
            this.img = itemView.findViewById(R.id.item_user_avatar);
            this.deleteBtn = itemView.findViewById(R.id.btn_user_delete);
            this.editBtn = itemView.findViewById(R.id.btn_user_edit);
        }
    }
}