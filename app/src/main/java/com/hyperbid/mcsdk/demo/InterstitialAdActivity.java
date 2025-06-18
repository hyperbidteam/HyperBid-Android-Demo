package com.hyperbid.mcsdk.demo;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.mcsdk.core.api.MCAdInfo;
import com.mcsdk.core.api.MCAdListener;
import com.mcsdk.core.api.MCAdRevenueListener;
import com.mcsdk.core.api.MCError;
import com.mcsdk.interstitial.api.MCInterstitialAd;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InterstitialAdActivity extends BaseActivity implements View.OnClickListener {
    private MCInterstitialAd mInterstitialAd;
    private TextView mTVLoadAdBtn;
    private TextView mTVIsAdReadyBtn;
    private TextView mTVShowAdBtn;

    private int retryAttempt;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_interstitial;
    }

    @Override
    protected void initView() {
        super.initView();
        mTVLoadAdBtn = findViewById(R.id.load_ad_btn);
        mTVIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn);
        mTVShowAdBtn = findViewById(R.id.show_ad_btn);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTVLoadAdBtn.setOnClickListener(this);
        mTVIsAdReadyBtn.setOnClickListener(this);
        mTVShowAdBtn.setOnClickListener(this);
    }

    @Override
    protected void initAd() {
        initInterstitialAd("k3f32c004b923390");
    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        final CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));
        commonViewBean.setTitleResId(R.string.hyperbid_title_interstitial);

        return commonViewBean;
    }

    private void initInterstitialAd(String placementId) {
        mInterstitialAd = new MCInterstitialAd(this, placementId);

        mInterstitialAd.setListener(new MCAdListener() {

            @Override
            public void onAdLoaded(MCAdInfo mcAdInfo) {
                // Ad load success callback
                // The ad is ready to be displayed. mInterstitialAd.isReady() will now return 'true'
                printLogOnUI("onAdLoaded");
                // Reset retry attempt
                retryAttempt = 0;
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
                // pre-load
                mInterstitialAd.loadAd();
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
                // We recommend that you retry with exponentially higher delays up to a maximum delay (in this case 8 seconds) and a maximum number of attempts (in this case 3).
                if (retryAttempt >= 3) return;
                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(3, retryAttempt)));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mInterstitialAd.loadAd();
                    }
                }, delayMillis);
            }

            @Override
            public void onAdDisplayFailed(MCAdInfo mcAdInfo, MCError mcError) {
                // Ad display failure callback
                printLogOnUI("onAdDisplayFailed");
                // pre-load
                mInterstitialAd.loadAd();
            }
        });

        mInterstitialAd.setRevenueListener(new MCAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(MCAdInfo adInfo) {
                // Revenue tracking callback
                printLogOnUI("onAdRevenuePaid");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;
        int id = v.getId();
        if (id == R.id.load_ad_btn) {
            loadAd();
        } else if (id == R.id.is_ad_ready_btn) {
            isAdReady();
        } else if (id == R.id.show_ad_btn) {
            if (mInterstitialAd != null && mInterstitialAd.isReady()) {
                showAd();
            } else {
                loadAd();
                printLogOnUI(getString(R.string.hyperbid_ad_status_not_load));
            }
        }
    }

    private void loadAd() {
        if (mInterstitialAd == null) {
            printLogOnUI("MCInterstitialAd is not init.");
            return;
        }
        printLogOnUI(getString(R.string.hyperbid_ad_status_loading));

        Map<String, Object> loadExtraParameter = new HashMap<>();
        loadExtraParameter.put("test_load_extra_key", "test_load_extra_value");
        mInterstitialAd.setLoadExtraParameter(loadExtraParameter);

        Map<String, String> extraParameter = new HashMap<>();
        extraParameter.put("test_extra_key", "test_extra_value");
        mInterstitialAd.setExtraParameter(extraParameter);

        mInterstitialAd.loadAd();
    }

    private void isAdReady() {
        if (mInterstitialAd == null) {
            return;
        }
        if (mInterstitialAd.isReady()) {
            printLogOnUI("interstitial ad is ready to show.");
        } else {
            printLogOnUI("interstitial ad isn't ready to show.");
        }
    }

    private void showAd() {
        if (mInterstitialAd == null) return;
        if (mInterstitialAd.isReady()) {
            mInterstitialAd.showAd(InterstitialAdActivity.this, null);
        } else {
            mInterstitialAd.loadAd();
        }
    }
}

