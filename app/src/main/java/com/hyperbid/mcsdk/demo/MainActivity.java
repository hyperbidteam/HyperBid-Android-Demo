package com.hyperbid.mcsdk.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mcsdk.core.api.MCSDK;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_version)).setText(getResources().getString(R.string.hyperbid_sdk_version, MCSDK.getSDKVersionName()));
        ((TextView) findViewById(R.id.tv_sdk_demo)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        findViewById(R.id.tv_native).setOnClickListener(this);
        findViewById(R.id.tv_splash).setOnClickListener(this);
        findViewById(R.id.tv_banner).setOnClickListener(this);
        findViewById(R.id.tv_inter).setOnClickListener(this);
        findViewById(R.id.tv_rv).setOnClickListener(this);
        findViewById(R.id.tv_debugger).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Class<?> adPageClass = null;
        int id = v.getId();
        if (id == R.id.tv_native) {
            adPageClass = NativeMainActivity.class;
        } else if (id == R.id.tv_splash) {
            adPageClass = SplashAdActivity.class;
        } else if (id == R.id.tv_inter) {
            adPageClass = InterstitialAdActivity.class;
        } else if (id == R.id.tv_banner) {
            adPageClass = BannerAdActivity.class;
        } else if (id == R.id.tv_rv) {
            adPageClass = RewardVideoAdActivity.class;
        } else if (id == R.id.tv_debugger) {
            adPageClass = MediationDebuggerActivity.class;
        }
        startAdPage(adPageClass);
    }

    private void startAdPage(Class<?> adPageClass) {
        startActivity(new Intent(MainActivity.this, adPageClass));
    }
}