package cn.smxy.zhouxuelian1.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.CakeListAdapter;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeListResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;



public class CakeHomeFragment extends Fragment {
    private View rootView;
    private ListView cakeListView;
    private static final String TAG = "CakeHomeFragment";
    private List<Cake> cakeListData;
    private CakeListAdapter adapter;
    private LinearLayout llBottom;
    private ProgressBar progressBar;
    private TextView tvProgressText;
    private static final int CHANGE_PROGRESS = 1;
    private static final int SHOW_DATA = 2;
    private boolean isFinish = false;

    public CakeHomeFragment() {
        // Required empty public constructor
    }

    //Looper.getMainLooper()获取主线程的looper对象，handleMessage回调方法，处理队列中的消息
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case CHANGE_PROGRESS:
                    int progress = msg.arg1;
                    progressBar.setProgress(progress);//更新进度条进度
                    tvProgressText.setText(progress + "%");//更新进度文本
                    // 进度达到100%时显示数据
                    if (progress >= 100) {
                        handler.sendEmptyMessageDelayed(SHOW_DATA, 600); // 延迟600ms切换UI
                    }
                    break;
                case SHOW_DATA:
                    // 隐藏进度条，显示列表
                    llBottom.setVisibility(View.GONE);
                    cakeListView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    class ChangeProgressThread extends Thread {
        private int progressNum = 0;

        @Override
        public void run() {
            while (progressNum < 100) {
                if (isFinish && progressNum > 90) {
                    // 当请求完成且进度超过90%时直接跳到100%
                    progressNum = 100;
                    sendProgressMessage(progressNum);
                    break;
                } else if (isFinish) {
                    // 请求完成但进度不足，加速进度
                    progressNum += 5;
                } else {
                    // 正常进度
                    progressNum++;
                }

                sendProgressMessage(progressNum);

                try {
                    Thread.sleep(isFinish ? 50 : 100); // 完成时加速
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Log.e(TAG, "进度线程被中断", e);
                }
            }
        }

        //发送消息到handler，更新进度条
        private void sendProgressMessage(int progress) {
            Message msg = handler.obtainMessage();
            msg.what = CHANGE_PROGRESS;
            msg.arg1 = progress;
            handler.sendMessage(msg);
        }
    }


    //初始化视图
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();

        // 启动进度条线程
        new ChangeProgressThread().start();
        // 获取网络数据，蛋糕数据
        getDataFromNet();

        return rootView;
    }

    //初始化ui组件
    private void initView() {
        cakeListView = rootView.findViewById(R.id.cake_list_list);
        llBottom = rootView.findViewById(R.id.ll_Bottom2);
        progressBar = rootView.findViewById(R.id.progressBar2);
        tvProgressText = rootView.findViewById(R.id.progress_text2);
        cakeListView.setVisibility(View.GONE);
        llBottom.setVisibility(View.VISIBLE);
    }

    private void getDataFromNet() {
        String url = UrlConstants.CAKE_LIST_URL;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "请求错误: " + e.getMessage());
                isFinish = true;

                // 显示错误提示
                handler.post(() -> {
                    Toast.makeText(getContext(), "加载失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void onResponse(String response, int id) {
                isFinish = true; // 标记完成

                try {
                    CakeListResponse cakeListResponse = new Gson().fromJson(response, CakeListResponse.class);
                    if (cakeListResponse != null && cakeListResponse.getCode() == 2000) {
                        cakeListData = cakeListResponse.getDataobject();
                        //主程序执行代码，更新ui
                        handler.post(() -> {
                            adapter = new CakeListAdapter(getContext(), cakeListData);
                            cakeListView.setAdapter(adapter);
                        });
                    } else {
                        Log.d(TAG, "数据为空或响应码不是2000");
                        handler.post(() -> {
                            Toast.makeText(getContext(), "没有获取到数据", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "解析数据错误", e);
                    handler.post(() -> {
                        Toast.makeText(getContext(), "数据解析错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}