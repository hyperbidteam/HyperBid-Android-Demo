package com.hyperbid.mcsdk.demo;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.mcsdk.core.api.MCAdConst;
import com.mcsdk.core.api.MCAdExListener;
import com.mcsdk.core.api.MCAdInfo;
import com.mcsdk.core.api.MCAdRevenueListener;
import com.mcsdk.core.api.MCError;
import com.mcsdk.core.api.MCReward;
import com.mcsdk.reward.api.MCRewardedAd;
import com.mcsdk.reward.api.MCRewardedAdListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RewardVideoAdActivity extends BaseActivity implements View.OnClickListener {
    private MCRewardedAd mRewardVideoAd;
    private TextView mTvLoadAdBtn;
    private TextView mTvIsAdReadyBtn;
    private TextView mTvShowAdBtn;

    private int retryAttempt;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rewarded_video;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvLoadAdBtn = findViewById(R.id.load_ad_btn);
        mTvIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn);
        mTvShowAdBtn = findViewById(R.id.show_ad_btn);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTvLoadAdBtn.setOnClickListener(this);
        mTvIsAdReadyBtn.setOnClickListener(this);
        mTvShowAdBtn.setOnClickListener(this);
    }

    @Override
    protected void initAd() {
        initRewardVideoAd("kfcbe7867652599a");
    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        final CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));
        commonViewBean.setTitleResId(R.string.hyperbid_title_rewarded_video);
        return commonViewBean;
    }

    private void initRewardVideoAd(String placementId) {
        mRewardVideoAd = new MCRewardedAd(this, placementId);

        mRewardVideoAd.setListener(new MCRewardedAdListener() {
            @Override
            public void onUserRewarded(MCAdInfo mcAdInfo, MCReward mcReward) {
                // Reward triggered callback
                printLogOnUI("onUserRewarded ");
            }

            @Override
            public void onUserRewardFailed(MCAdInfo mcAdInfo) {
                // Reward failure callback
                printLogOnUI("onUserRewardFailed");
            }

            @Override
            public void onRewardedVideoStarted(MCAdInfo mcAdInfo) {
                printLogOnUI("onAdVideoStarted");
            }

            @Override
            public void onRewardedVideoCompleted(MCAdInfo mcAdInfo) {
                printLogOnUI("onAdVideoCompleted");
            }

            @Override
            public void onAdLoaded(MCAdInfo mcAdInfo) {
                // Ad load success callback
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
                mRewardVideoAd.loadAd();
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
                        mRewardVideoAd.loadAd();
                    }
                }, delayMillis);

            }

            @Override
            public void onAdDisplayFailed(MCAdInfo mcAdInfo, MCError mcError) {
                // Ad display failure callback
                printLogOnUI("onAdDisplayFailed");

                // pre-load
                mRewardVideoAd.loadAd();
            }

        });

        mRewardVideoAd.setRevenueListener(new MCAdRevenueListener() {
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
        if (mRewardVideoAd != null) {
            mRewardVideoAd.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.load_ad_btn) {
            loadAd();
        } else if (id == R.id.is_ad_ready_btn) {
            isAdReady();
        } else if (id == R.id.show_ad_btn) {
            if (mRewardVideoAd != null && mRewardVideoAd.isReady()) {
                showAd();
            } else {
                loadAd();
                printLogOnUI(getString(R.string.hyperbid_ad_status_not_load));
            }
        }
    }

    private void loadAd() {
        if (mRewardVideoAd == null) {
            printLogOnUI("MCRewardedAd is not init.");
            return;
        }
        printLogOnUI(getString(R.string.hyperbid_ad_status_loading));

        String userid = "test_userid_001";
        String userdata = "test_userdata_001";
        Map<String, Object> localMap = new HashMap<>();
        localMap.put(MCAdConst.KEY.USER_ID, userid);
        localMap.put(MCAdConst.KEY.USER_CUSTOM_DATA, userdata);
        mRewardVideoAd.setLoadExtraParameter(localMap);

        Map<String, String> extraParameter = new HashMap<>();
        extraParameter.put("test_extra_key", "test_extra_value");
        mRewardVideoAd.setExtraParameter(extraParameter);

        mRewardVideoAd.loadAd();
    }

    private void isAdReady() {
        if (mRewardVideoAd == null) {
            return;
        }
        if (mRewardVideoAd.isReady()) {
            printLogOnUI("rewarded ad is ready to show.");
        } else {
            printLogOnUI("rewarded ad isn't ready to show.");
        }
    }

    private void showAd() {
        if (mRewardVideoAd == null) return;
        if (mRewardVideoAd.isReady()) {
            mRewardVideoAd.showAd(RewardVideoAdActivity.this, null);
        } else {
            mRewardVideoAd.loadAd();
        }
    }
}

