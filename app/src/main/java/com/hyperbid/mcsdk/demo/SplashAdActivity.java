/*
 * Copyright © 2018-2020 TopOn. All rights reserved.
 * https://www.toponad.com
 * Licensed under the TopOn SDK License Agreement
 * https://github.com/toponteam/TopOn-Android-SDK/blob/master/LICENSE
 */

package com.hyperbid.mcsdk.demo;

import android.view.View;
import android.widget.FrameLayout;

import com.mcsdk.core.api.MCAdExListener;
import com.mcsdk.core.api.MCAdInfo;
import com.mcsdk.core.api.MCAdRevenueListener;
import com.mcsdk.core.api.MCError;
import com.mcsdk.splash.api.MCAppOpenAd;
import com.mcsdk.splash.api.MCAppOpenAdListener;

import java.util.HashMap;
import java.util.Map;

public class SplashAdActivity extends BaseActivity implements View.OnClickListener {
    private MCAppOpenAd mSplashAd;
    private FrameLayout mSplashAdContainer;

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        super.initView();
        mSplashAdContainer = findViewById(R.id.splash_ad_container);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.is_ad_ready_btn).setOnClickListener(this);
        findViewById(R.id.load_ad_btn).setOnClickListener(this);
        findViewById(R.id.show_ad_btn).setOnClickListener(this);
    }

    @Override
    protected void initAd() {
        initSplashAd("kc78fe48d58a0751");
    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));
        commonViewBean.setTitleResId(R.string.hyperbid_title_splash);
        return commonViewBean;
    }

    private void initSplashAd(String placementId) {
        mSplashAd = new MCAppOpenAd(this, placementId, 10 * 1000);
        mSplashAd.setListener(new MCAppOpenAdListener() {
            @Override
            public void onAdLoadTimeout() {
                // Loading timeout callback
                printLogOnUI("onAdLoadTimeout");
            }

            @Override
            public void onAdLoaded(MCAdInfo mcAdInfo) {
                // Ad load success callback
                printLogOnUI("onAdLoaded");
            }

            @Override
            public void onAdDisplayed(MCAdInfo mcAdInfo) {
                // Ad display success callback
                printLogOnUI("onAdDisplayed");
            }

            @Override
            public void onAdHidden(MCAdInfo mcAdInfo) {
                // Ad closed callback
                printLogOnUI("onAdHidden");
                if (mSplashAdContainer != null) {
                    mSplashAdContainer.removeAllViews();
                }
                // pre-load ,soft launch need use call loadAd after ad hidden
                mSplashAd.loadAd();
            }

            @Override
            public void onAdClicked(MCAdInfo mcAdInfo) {
                // Ad click callback
                printLogOnUI("onAdClicked");
            }

            @Override
            public void onAdLoadFailed(MCError mcError) {
                // Ad load failure callback
                printLogOnUI("onAdLoadFailed");
            }

            @Override
            public void onAdDisplayFailed(MCAdInfo mcAdInfo, MCError mcError) {
                // Ad display failure callback
                printLogOnUI("onAdDisplayFailed");

                mSplashAd.loadAd();
            }
        });

        mSplashAd.setRevenueListener(new MCAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(MCAdInfo adInfo) {
                // Revenue tracking callback
                printLogOnUI("onAdRevenuePaid");
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (mSplashAd != null) {
            mSplashAd.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;
        int id = v.getId();
        if (id == R.id.is_ad_ready_btn) {
            isAdReady();
        } else if (id == R.id.load_ad_btn) {
            loadAd();
        } else if (id == R.id.show_ad_btn) {
            if (mSplashAd != null && mSplashAd.isReady()) {
                showAd();
            } else {
                loadAd();
                printLogOnUI(getString(R.string.hyperbid_ad_status_not_load));
            }
        }
    }

    private void isAdReady() {
        if (mSplashAd == null) return;
        if (mSplashAd.isReady()) {
            printLogOnUI("SplashAd is ready to show.");
        } else {
            printLogOnUI("SplashAd isn't ready to show.");
        }
    }

    private void loadAd() {
        printLogOnUI(getString(R.string.hyperbid_ad_status_loading));

        if (mSplashAd != null) {

            Map<String, Object> loadExtraParameter = new HashMap<>();
            loadExtraParameter.put("test_load_extra_key", "test_load_extra_value");
            mSplashAd.setLoadExtraParameter(loadExtraParameter);

            Map<String, String> extraParameter = new HashMap<>();
            extraParameter.put("test_extra_key", "test_extra_value");
            mSplashAd.setExtraParameter(extraParameter);

            mSplashAd.loadAd();
        }
    }

    private void showAd() {
        if (mSplashAd == null) {
            return;
        }
        if (mSplashAd.isReady()) {
            // mSplashAdContainer: Parent container for ad display
            mSplashAd.showAd(SplashAdActivity.this, mSplashAdContainer, null);
        } else {
            mSplashAd.loadAd();
        }
    }

}
