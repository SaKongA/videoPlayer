package com.example.videoplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;
import android.content.SharedPreferences;


public class VideoPlayerActivity extends Activity implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {
    private static final String TAG = "=======================";
    private MediaPlayer mediaPlayer;
    public VideoView videoView;
    public int videoPosition = 0;
    public ProgressDialog dialog;
    private String uri;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_activity);
        videoView = findViewById(R.id.video_view);

        Intent intent = getIntent();
        uri = intent.getStringExtra("uri");

        dialog = new ProgressDialog(this);
        dialog.setTitle("安卓视频播放器");
        dialog.setMessage("正在加载...");
        dialog.setCancelable(false);

        videoView.setOnCompletionListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnErrorListener(this);
    }

    //开始播放
    @Override
    protected void onStart() {
        super.onStart();
        loadVideo();
    }

    private void loadVideo() {
        dialog.show();
        try {
            videoView.setVideoURI(Uri.parse(uri));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "Media onPrepared");

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        mp.seekTo(videoPosition);
        Log.d(TAG, "onPrepared: "+videoPosition);
        if (videoPosition == 0) {
            mp.start();
            // 初始化SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("loopSwitch", MODE_PRIVATE);
            // 从SharedPreferences中读取Switch状态
            boolean isSwitchChecked = sharedPreferences.getBoolean("switch_state", false);
            if (isSwitchChecked) {
                mp.setLooping(true);
            } else {
                mp.setLooping(false);
                Toast.makeText(VideoPlayerActivity.this, "循环播放已关闭！", Toast.LENGTH_SHORT).show();
            }
        } else {
            mp.pause();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "Media onCompletion");
        Toast.makeText(VideoPlayerActivity.this, "播放完成", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "Media onError");
        String err = "未知错误";
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                err = "媒体服务终止";
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                err = "未知错误";
                break;
            default:
                break;
        }
        Toast.makeText(VideoPlayerActivity.this, err, Toast.LENGTH_LONG).show();
        return true;
    }
}