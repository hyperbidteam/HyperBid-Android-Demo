package com.hyperbid.mcsdk.demo;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mcsdk.banner.api.MCAdView;
import com.mcsdk.banner.api.MCAdViewAdListener;
import com.mcsdk.core.api.MCAdConst;
import com.mcsdk.core.api.MCAdExListener;
import com.mcsdk.core.api.MCAdInfo;
import com.mcsdk.core.api.MCAdRevenueListener;
import com.mcsdk.core.api.MCError;
import com.mcsdk.core.api.MCSDKUtil;

import java.util.HashMap;
import java.util.Map;

public class BannerAdActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = BannerAdActivity.class.getSimpleName();

    private MCAdView mBannerView;
    private TextView tvLoadAdBtn;
    private FrameLayout mBannerViewContainer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_banner;
    }

    @Override
    protected void initView() {
        super.initView();
        tvLoadAdBtn = findViewById(R.id.banner_load_ad_btn);
        mBannerViewContainer = findViewById(R.id.adview_container);

        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerViewContainer.setVisibility(View.VISIBLE);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        super.initListener();
        tvLoadAdBtn.setOnClickListener(this);
    }

    @Override
    protected void initAd() {
        initBannerView("kd945c8f112cd7b8");
        addBannerViewToContainer();
    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));
        commonViewBean.setTitleResId(R.string.hyperbid_title_banner);
        return commonViewBean;
    }

    private void initBannerView(String placementId) {
        mBannerView = new MCAdView(this, placementId);
        mBannerView.setListener(new MCAdViewAdListener() {
            @Override
            public void onAdExpanded(MCAdInfo adInfo) {
                // Triggered when collapsible banner expands (if supported by ad network)
                printLogOnUI("onAdExpanded");
            }

            @Override
            public void onAdCollapsed(MCAdInfo adInfo) {
                // Triggered when collapsible banner collapses (if supported by ad network)
                printLogOnUI("onAdCollapsed");
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
            }

        });

        mBannerView.setRevenueListener(new MCAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(MCAdInfo adInfo) {
                // Revenue tracking callback
                printLogOnUI("onAdRevenuePaid");
            }
        });

        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView.setVisibility(View.VISIBLE);
    }

    private void addBannerViewToContainer() {
        mBannerViewContainer.removeAllViews();
        if (mBannerViewContainer != null && mBannerView != null) {
            mBannerViewContainer.addView(mBannerView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mBannerViewContainer.getLayoutParams().height));
        }
    }

    @Override
    protected void onDestroy() {
        if (mBannerViewContainer != null) {
            mBannerViewContainer.removeAllViews();
        }
        if (mBannerView != null) {
            mBannerView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.banner_load_ad_btn) {
            loadAd();
        }
    }

    private void loadAd() {
        printLogOnUI(getString(R.string.hyperbid_ad_status_loading));

        mBannerView.setPlacement("test_placement_scenario");

        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView.setVisibility(View.VISIBLE);
        mBannerViewContainer.setVisibility(View.VISIBLE);

        Map<String, Object> loadExtraParameter = new HashMap<>();
        loadExtraParameter.put("test_load_extra_key", "test_load_extra_value");
        loadExtraParameter.put("adaptive_banner_width", 400);
        //示例：加载的广告宽高为320x50
        loadExtraParameter.put(MCAdConst.KEY.AD_WIDTH, MCSDKUtil.dpToPx(this, 320));
        loadExtraParameter.put(MCAdConst.KEY.AD_HEIGHT, MCSDKUtil.dpToPx(this, 50));
        mBannerView.setLoadExtraParameter(loadExtraParameter);

        Map<String, String> extraParameter = new HashMap<>();
        extraParameter.put("test_extra_key", "test_extra_value");
        extraParameter.put("adaptive_banner", "true");
        mBannerView.setExtraParameter(extraParameter);

        mBannerView.loadAd();

    }
}
