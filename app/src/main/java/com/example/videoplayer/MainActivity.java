package com.example.videoplayer;

import android.app.appsearch.SearchResult;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;

    public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentList;
    private Fragment localVideoFragment, onlineVideoFragment;
    private Boolean flag;
    private TextView localVideo;
    private Switch loop;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag=false;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 权限未被授予，请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
        }

        Switch loopSwitch = findViewById(R.id.loop);
        SharedPreferences sharedPreferences = getSharedPreferences("loopSwitch", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean isSwitchChecked = sharedPreferences.getBoolean("switch_state", false);

        loopSwitch.setChecked(isSwitchChecked);

        loopSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("switch_state", isChecked);
                editor.apply();

                if (isChecked) {
                    Toast.makeText(MainActivity.this, "循环播放已开启！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "循环播放已关闭！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boolean isFirstRun = sharedPreferences.getBoolean("first_run", true);
        if (isFirstRun) {
            editor.putBoolean("switch_state", false);
            editor.putBoolean("first_run", false);
            editor.apply();
        }

        viewPager = findViewById(R.id.viewPager);
        localVideo = findViewById(R.id.playermodepage);

        initView();

        localVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                localVideo.setTextColor(Color.parseColor(flag ? "#ffffff" : "#AAFFFFFF"));
                flag = !flag;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initView() {

        localVideoFragment = new LocalVideoFragment();
        setAdapter();
    }

    private void setAdapter() {

        fragmentList = new ArrayList<>();
        fragmentList.add(localVideoFragment);
        //建立Fragment页面适配器对象
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), fragmentList);

        //给viewPager设置上适配器
        viewPager.setAdapter(adapter);

    }
}

