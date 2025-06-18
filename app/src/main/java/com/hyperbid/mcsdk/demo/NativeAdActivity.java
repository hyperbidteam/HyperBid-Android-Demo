package com.hyperbid.mcsdk.demo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mcsdk.core.api.MCAdConst;
import com.mcsdk.core.api.MCAdExListener;
import com.mcsdk.core.api.MCAdInfo;
import com.mcsdk.core.api.MCAdRevenueListener;
import com.mcsdk.core.api.MCError;
import com.mcsdk.core.api.MCNativeAd;
import com.mcsdk.core.api.MCNativeAdView;
import com.mcsdk.core.api.MCNativeAdViewBinder;
import com.mcsdk.nativead.api.MCNativeAdListener;
import com.mcsdk.nativead.api.MCNativeAdLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NativeAdActivity extends BaseActivity implements View.OnClickListener {

    public static final String NATIVE_SELF_RENDER_TYPE = "1";
    public static final String NATIVE_EXPRESS_TYPE = "2";
    private MCNativeAdLoader mNativeAdLoader;
    private MCNativeAd mMCNativeAd;
    private MCAdInfo mMCAdInfo;
    private MCNativeAdView mMCNativeAdView;
    private FrameLayout mNativeAdViewContainer;

    private TextView mTVLoadAdBtn;
    private TextView mTVShowAdBtn;
    private View mPanel;

    private String mMediationUnitId;

    private int retryAttempt;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_native;
    }

    @Override
    protected void initView() {
        super.initView();
        mTVLoadAdBtn = findViewById(R.id.load_ad_btn);
        findViewById(R.id.is_ad_ready_btn).setVisibility(View.GONE);
        mTVShowAdBtn = findViewById(R.id.show_ad_btn);

        // Parent container for NativeAdView
        mNativeAdViewContainer = findViewById(R.id.adview_container);

        mPanel = findViewById(R.id.rl_panel);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTVLoadAdBtn.setOnClickListener(this);
        mTVShowAdBtn.setOnClickListener(this);
    }

    @Override
    protected void initAd() {
        initNative(mMediationUnitId);
    }

    private void initNative(String placementId) {
        mNativeAdLoader = new MCNativeAdLoader(this, placementId);

        mNativeAdLoader.setNativeAdListener(new MCNativeAdListener() {
            @Override
            public void onAdLoaded(MCNativeAdView mcNativeAdView, MCAdInfo mcAdInfo) {
                // Ad load success callback
                // Clean up any pre-existing native ad to prevent memory leaks.
                if (mMCNativeAd != null) {
                    mNativeAdLoader.destroy(mMCNativeAd);
                }

                // Save ad for cleanup.
                mMCAdInfo = mcAdInfo;
                mMCNativeAd = mcAdInfo.getNativeAd();
                mMCNativeAdView = mcNativeAdView;
                printLogOnUI("onAdLoaded");

                // Reset retry attempt
                retryAttempt = 0;
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
                        mNativeAdLoader.loadAd();
                    }
                }, delayMillis);
            }

            @Override
            public void onAdClicked(MCAdInfo mcAdInfo) {
                // Ad click callback
                printLogOnUI("onAdClicked");
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
                mNativeAdLoader.loadAd();
            }
        });

        mNativeAdLoader.setRevenueListener(new MCAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(MCAdInfo adInfo) {
                // Revenue tracking callback
                printLogOnUI("onAdRevenuePaid");
            }
        });

    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        final CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));

        String nativeType = getIntent().getStringExtra("native_type");
        if (nativeType.equals(NATIVE_SELF_RENDER_TYPE)) {
            commonViewBean.setTitleResId(R.string.hyperbid_native_self);
            mMediationUnitId = "kfc98a6c32d23a57";
        } else {
            commonViewBean.setTitleResId(R.string.hyperbid_native_express);
            mMediationUnitId = "kfc98a6c32d23a57";
        }
        return commonViewBean;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAd();
    }

    private void destroyAd() {
        if (mNativeAdLoader != null) {
            mNativeAdLoader.destroy();

            if (mMCNativeAd != null) {
                mNativeAdLoader.destroy(mMCNativeAd);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mPanel.getVisibility() == View.VISIBLE) {
            exitNativePanel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitNativePanel() {
        if (mMCNativeAd != null) {
            mNativeAdLoader.destroy(mMCNativeAd);
        }

        mPanel.setVisibility(View.GONE);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null) return;

        int id = v.getId();
        if (id == R.id.load_ad_btn) {
            loadAd();
        } else if (id == R.id.show_ad_btn) {
            showAd();
        }
    }

    private void loadAd() {
        printLogOnUI(getString(R.string.hyperbid_ad_status_loading));

        mNativeAdLoader.setPlacement("test_placement_scenario");


        Map<String, Object> loadExtraParameter = new HashMap<>();
        loadExtraParameter.put("test_load_extra_key", "test_load_extra_value");

        int adWidth = getResources().getDisplayMetrics().widthPixels;
        int adHeight = adWidth * 3 / 4;
        loadExtraParameter.put(MCAdConst.KEY.AD_WIDTH, adWidth);
        loadExtraParameter.put(MCAdConst.KEY.AD_HEIGHT, adHeight);
        mNativeAdLoader.setLoadExtraParameter(loadExtraParameter);

        Map<String, String> extraParameter = new HashMap<>();
        extraParameter.put("test_extra_key", "test_extra_value");
        mNativeAdLoader.setExtraParameter(extraParameter);

        mNativeAdLoader.loadAd();
    }

    private void showAd() {
        mNativeAdViewContainer.removeAllViews();

        if (mMCNativeAd != null) {
            if (mMCNativeAd.isTemplateNativeAd()) {
                // Template rendering
                mNativeAdViewContainer.addView(mMCNativeAdView);
            } else {
                // Self-rendering
                mMCNativeAdView = createNativeAdView();
                mNativeAdViewContainer.addView(mMCNativeAdView);
                mNativeAdLoader.render(mMCNativeAdView, mMCAdInfo);
            }
            mPanel.setVisibility(View.VISIBLE);
        } else {
            // No cached ads available, reload recommended
            mNativeAdLoader.loadAd();
        }
    }

    private MCNativeAdView createNativeAdView() {
        // Custom XML components must match the following identifiers
        MCNativeAdViewBinder binder = new MCNativeAdViewBinder.Builder(R.layout.native_custom_ad_view)
                .setTitleTextViewId(R.id.title_text_view)               //TextView
                .setBodyTextViewId(R.id.body_text_view)                 //TextView
                .setStarRatingContentViewGroupId(R.id.star_rating_view) //ViewGroup
                .setAdvertiserTextViewId(R.id.advertiser_text_view)     //TextView
                .setIconImageViewId(R.id.icon_image_view)               //ImageView
                .setMediaContentViewGroupId(R.id.media_view_container)  //ViewGroup
                .setOptionsContentViewGroupId(R.id.options_view)        //ViewGroup
                .setCallToActionButtonId(R.id.cta_button)               //Button
                .build();
        return new MCNativeAdView(binder, this);
    }
}
