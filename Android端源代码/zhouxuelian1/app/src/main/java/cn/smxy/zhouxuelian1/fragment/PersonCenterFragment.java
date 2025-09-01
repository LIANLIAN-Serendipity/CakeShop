package cn.smxy.zhouxuelian1.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.activity.LoginActivity;
import cn.smxy.zhouxuelian1.activity.ManagOrderActivity;
import cn.smxy.zhouxuelian1.activity.ManageUserActivity;
import cn.smxy.zhouxuelian1.activity.ManagementActivity;
import cn.smxy.zhouxuelian1.activity.PlayActivity;
import cn.smxy.zhouxuelian1.activity.UpdateUserActivity;
import cn.smxy.zhouxuelian1.activity.UserOrderActivity;
import cn.smxy.zhouxuelian1.utils.SPUtil;

public class PersonCenterFragment extends Fragment {
    private static final String TAG = "PersonCenterFragment";
    private View rootView;
    private ImageView personcenter_userImg;
    private TextView personcenter1, personcenter2, personcenter3;
    private String role;
    private TextView tvUsername, tvLogout;

    public PersonCenterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_person_center, container, false);
        initView();
        setListeners();
        return rootView;
    }

    private void initView() {
        tvUsername = rootView.findViewById(R.id.person_center_username);
        tvLogout = rootView.findViewById(R.id.person_center_logout);
        personcenter_userImg = rootView.findViewById(R.id.personcenter_userImg);
        personcenter1 = rootView.findViewById(R.id.personcenter1);
        personcenter2 = rootView.findViewById(R.id.personcenter2);
        personcenter3 = rootView.findViewById(R.id.personcenter3);

        String username = SPUtil.getString(getActivity(), "cakeusername");
        String userImage = SPUtil.getString(getActivity(), "cakeuserImage");
        role = SPUtil.getString(getActivity(), "role");

        // 打印调试信息
        Log.d(TAG, "用户名: " + username);
        Log.d(TAG, "头像URL: " + userImage);
        Log.d(TAG, "用户角色: " + role);

        // 使用Glide加载头像，添加错误处理和默认占位图
        Glide.with(this)
                .load(userImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 禁用缓存，确保每次获取最新图
                .error(R.drawable.emptyimage)          // 加载失败时显示的默认图
                .into(personcenter_userImg);

        tvUsername.setText(username + "，欢迎登录！");

        setupMenuItemsByRole();
    }

    /**
     * 根据用户角色设置菜单项内容和图标
     */
    private void setupMenuItemsByRole() {
        if ("管理员".equals(role)) {
            // 管理员菜单设置
            personcenter1.setText("管理用户");
            personcenter2.setText("管理订单");
            personcenter3.setText("管理商品");

            // 设置图标
            setMenuItemIcon(personcenter1, R.drawable.guanliuser, R.drawable.right);
            setMenuItemIcon(personcenter2, R.drawable.guanlidingdan, R.drawable.right);
            setMenuItemIcon(personcenter3, R.drawable.guanlicake, R.drawable.right);
        } else {
            // 普通用户菜单设置
            personcenter1.setText("修改个人信息");
            personcenter2.setText("我的订单");
            personcenter3.setText("小工具-音乐播放器");

            // 设置图标
            setMenuItemIcon(personcenter1, R.drawable.mima, R.drawable.right);
            setMenuItemIcon(personcenter2, R.drawable.order3, R.drawable.right);
            setMenuItemIcon(personcenter3, R.drawable.yinyue, R.drawable.right);
        }
    }

    private void setMenuItemIcon(TextView textView, int leftIconRes, int rightIconRes) {
        Drawable leftDrawable = ContextCompat.getDrawable(this.getContext(), leftIconRes);
        Drawable rightDrawable = ContextCompat.getDrawable(this.getContext(), rightIconRes);

        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());
        }
        if (rightDrawable != null) {
            rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
        }

        textView.setCompoundDrawables(leftDrawable, null, rightDrawable, null);
        textView.setCompoundDrawablePadding(10);
    }

    private void setListeners() {
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.removeString(getActivity(), "token");
                SPUtil.removeString(getActivity(), "cakeusername");
                SPUtil.removeString(getActivity(), "cakeuserId");
                SPUtil.removeString(getActivity(), "cakeuserImage");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        personcenter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("管理员".equals(role)) {
                    adminManageUser();
                } else {
                    commonUserChangePassword();
                }
            }
        });

        personcenter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("管理员".equals(role)) {
                    adminManageOrder();
                } else {
                    commonUserMyOrder();
                }
            }
        });

        personcenter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("管理员".equals(role)) {
                    adminManageCake();
                } else {
                    commonUserMusicPlayer();
                }
            }
        });
    }

    // 管理员功能点击事件
    private void adminManageUser() {
        Intent intent = new Intent(getActivity(), ManageUserActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void adminManageOrder() {
        Intent intent = new Intent(getActivity(), ManagOrderActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void adminManageCake() {
        Intent intent = new Intent(getActivity(), ManagementActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    // 普通用户功能点击事件
    private void commonUserChangePassword() {
        // 普通用户跳转到个人信息修改页面
        Intent intent = new Intent(getActivity(), UpdateUserActivity.class);
        // 传递当前用户ID（从SPUtil获取）
        String userId = SPUtil.getString(getActivity(), "cakeuserId");
        if (userId != null && !userId.isEmpty()) {
            intent.putExtra("cakeuserId", userId);
        }
        startActivity(intent);
        getActivity().finish();
    }

    private void commonUserMyOrder() {
        // 假设已有我的订单页面
        Intent intent = new Intent(getActivity(), UserOrderActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void commonUserMusicPlayer() {
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}