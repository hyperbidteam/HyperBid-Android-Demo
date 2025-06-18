package com.hyperbid.mcsdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.mcsdk.core.api.MCAdExListener;
import com.mcsdk.core.api.MCAdInfo;
import com.mcsdk.core.api.MCAdRevenueListener;
import com.mcsdk.core.api.MCError;
import com.mcsdk.splash.api.MCAppOpenAd;
import com.mcsdk.splash.api.MCAppOpenAdListener;

public class AppOpenActivity extends AppCompatActivity {

    private FrameLayout mFlAdContainer;

    private MCAppOpenAd mAppOpenAd;

    private boolean isTimeout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_open);
        mFlAdContainer = findViewById(R.id.fl_ad_container);
        mAppOpenAd = new MCAppOpenAd(this, "kc78fe48d58a0751", 10 * 1000);
        mAppOpenAd.setListener(new MCAppOpenAdListener() {
            @Override
            public void onAdLoadTimeout() {
                // Loading timeout callback
                isTimeout = true;
                goToMain();
            }

            @Override
            public void onAdLoaded(MCAdInfo mcAdInfo) {
                // Ad load success callback
                if (isTimeout) return;
                mAppOpenAd.showAd(AppOpenActivity.this, mFlAdContainer, null);
            }

            @Override
            public void onAdDisplayed(MCAdInfo mcAdInfo) {
                // Ad display success callback
            }

            @Override
            public void onAdHidden(MCAdInfo mcAdInfo) {
                // Ad closed callback
                if (mFlAdContainer != null) {
                    mFlAdContainer.removeAllViews();
                }
                // pre-load  cold start can not do this (mAppOpenAd.loadAd();)
//                mAppOpenAd.loadAd();
                goToMain();
            }

            @Override
            public void onAdClicked(MCAdInfo mcAdInfo) {
                // Ad click callback
            }

            @Override
            public void onAdLoadFailed(MCError mcError) {
                // Ad load failure callback
                goToMain();
            }

            @Override
            public void onAdDisplayFailed(MCAdInfo mcAdInfo, MCError mcError) {
                // Ad display failure callback
                mAppOpenAd.loadAd();

                goToMain();
            }
        });

        mAppOpenAd.setRevenueListener(new MCAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(MCAdInfo adInfo) {
                // Revenue tracking callback
            }
        });

        mAppOpenAd.loadAd();
    }

    private void goToMain() {
        startActivity(new Intent(AppOpenActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mAppOpenAd != null) {
            mAppOpenAd.destroy();
        }
        super.onDestroy();
    }
}