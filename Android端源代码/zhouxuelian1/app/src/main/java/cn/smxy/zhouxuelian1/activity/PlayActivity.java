package cn.smxy.zhouxuelian1.activity;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.Service.MusicService;

public class PlayActivity extends AppCompatActivity {
    private static final String TAG = "music";
    private ImageView ivPan, ivPlayPause;
    private SeekBar seekBar;
    private TextView tvCurrentTime, tvTotalTime;
    private MusicService.MyBinder myBinder;
    private ActivityResultLauncher<String> resultLauncher;
    private Toolbar toolbar;
    private ObjectAnimator objectAnimator;

    // 控制进度更新线程：true=运行，false=停止
    private boolean isProgressUpdating = false;
    // 标记是否已选择音乐（未选择时，播放按钮点击不触发动画/进度）
    private boolean isMusicSelected = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, android.os.IBinder binder) {
            Log.d(TAG, "onServiceConnected: ");
            myBinder = (MusicService.MyBinder) binder;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // 注册文件选择回调
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null && myBinder != null) {
                            myBinder.changeMusic(uri);
                            updateDuration();
                            isMusicSelected = true;
                        }
                    }
                }
        );

        initView();
        setListener();
    }

    // 更新歌曲总时长到 UI
    private void updateDuration() {
        if (myBinder != null && myBinder.isMediaPlayerInitialized()) {
            int duration = myBinder.getDuration();
            seekBar.setMax(duration);
            tvTotalTime.setText(msFormat(duration));
        }
    }

    // 毫秒转 分:秒 格式
    public static String msFormat(int ms) {
        int second = ms / 1000;
        int minute = second / 60;
        second = second - minute * 60;
        return String.format("%02d:%02d", minute, second);
    }

    private void setListener() {
        // 播放/暂停按钮逻辑
        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myBinder == null || !myBinder.isMediaPlayerInitialized()) {
                    return; // 未初始化，不处理
                }

                if (myBinder.isPlaying()) {
                    // 暂停逻辑：音乐、进度更新、转盘动画 均暂停
                    myBinder.pause();
                    ivPlayPause.setImageResource(R.drawable.zanting1);
                    pauseProgressUpdate();
                    objectAnimator.pause();
                } else {
                    // 播放逻辑：音乐播放 + 进度更新启动 + 转盘动画启动/恢复
                    myBinder.play();
                    ivPlayPause.setImageResource(R.drawable.bofang);

                    if (isMusicSelected) {
                        // 首次播放（刚选完音乐）：启动所有流程
                        objectAnimator.start();
                        startProgressUpdate();
                        isMusicSelected = false;
                    } else {
                        // 暂停后继续播放：恢复动画和进度
                        objectAnimator.resume();
                        resumeProgressUpdate();
                    }
                }
            }
        });

        // 选择音乐逻辑
        ivPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultLauncher.launch("audio/*");
            }
        });

        // 进度条拖动逻辑
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && myBinder != null && myBinder.isMediaPlayerInitialized()) {
                    myBinder.seekTo(progress);
                    updateCurrentProgressUI(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseProgressUpdate();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                resumeProgressUpdate();
            }
        });

        // 返回按钮逻辑
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBinder != null) {
                    myBinder.stop();
                }
                startActivity(new Intent(PlayActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void initView() {
        ivPan = findViewById(R.id.player_pan);
        ivPlayPause = findViewById(R.id.player_pause);
        seekBar = findViewById(R.id.player_seekbar);
        tvCurrentTime = findViewById(R.id.player_current_time);
        tvTotalTime = findViewById(R.id.player_total_time);
        toolbar = findViewById(R.id.back);

        // 绑定音乐服务
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        // 初始化转盘动画（不自动启动）
        objectAnimator = ObjectAnimator.ofFloat(ivPan, "rotation", 0, 360);
        objectAnimator.setDuration(5000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "PlayerActivity的onDestroy: ");
        isProgressUpdating = false;
        if (myBinder != null) {
            myBinder.stop();
        }
        unbindService(serviceConnection);
    }

    // 手动更新当前进度 UI（用于进度条拖动时同步显示）
    private void updateCurrentProgressUI(int progress) {
        tvCurrentTime.setText(msFormat(progress));
    }

    // 启动进度更新线程
    private void startProgressUpdate() {
        if (!isProgressUpdating && myBinder != null && myBinder.isMediaPlayerInitialized()) {
            isProgressUpdating = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isProgressUpdating && myBinder != null) {
                        if (myBinder.isMediaPlayerInitialized()) {
                            int currentPosition = myBinder.getCurrentPosition();
                            Message msg = handler.obtainMessage();
                            msg.arg1 = currentPosition;
                            handler.sendMessage(msg);
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    // 暂停进度更新（暂停音乐或拖动进度条时调用）
    private void pauseProgressUpdate() {
        isProgressUpdating = false;
    }

    // 恢复进度更新（播放恢复或拖动结束时调用）
    private void resumeProgressUpdate() {
        startProgressUpdate();
    }

    // 主线程 Handler，更新进度条和时间显示
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int currentPosition = msg.arg1;
            seekBar.setProgress(currentPosition);
            tvCurrentTime.setText(msFormat(currentPosition));
        }
    };
}