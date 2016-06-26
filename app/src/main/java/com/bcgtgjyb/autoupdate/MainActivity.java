package com.bcgtgjyb.autoupdate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bcgtgjyb.updatelib.UpdateUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateUtil.getInstance(this).checkUpdate(this);
        UpdateUtil.getInstance(this).registerReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateUtil.getInstance(this).unRegisterReceiver(this);
    }
}
