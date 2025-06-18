package com.hyperbid.mcsdk.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mcsdk.core.api.AdSourceError;
import com.mcsdk.core.api.AdSourceEvent;
import com.mcsdk.core.api.MCAdInfo;
import com.mcsdk.core.api.NetworkAdSourceEventListener;

import java.lang.ref.WeakReference;

public abstract class BaseActivity extends Activity {
    private static WeakReference<TextView> mTVShowLogWR;
    protected TextView mTVShowLog;

    @SuppressLint("SetTextI18n")
    protected static void printLogOnUI(String msg) {
        if (mTVShowLogWR == null || mTVShowLogWR.get() == null || TextUtils.isEmpty(msg)) return;

        if (mTVShowLogWR.get() == null) return;

        mTVShowLogWR.get().setText(mTVShowLogWR.get().getText().toString() + msg + "\n");
        //let text view to move to the last line.
        int offset = mTVShowLogWR.get().getLineCount() * mTVShowLogWR.get().getLineHeight();
        if (offset > mTVShowLogWR.get().getHeight()) {
            mTVShowLogWR.get().scrollTo(0, offset - mTVShowLogWR.get().getHeight());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewId());

        initView();
        initListener();
        initAd();
    }

    protected abstract int getContentViewId();

    protected void initView() {
        initViewWithCommonView(getCommonViewBean());
    }

    protected void initListener() {
    }

    protected abstract void initAd();

    private void initViewWithCommonView(CommonViewBean commonViewBean) {
        if (commonViewBean != null) {
            TitleBar titleBar = commonViewBean.getTitleBar();
            if (titleBar != null) {
                setTitleBar(titleBar, commonViewBean.getTitleResId());
            }
            mTVShowLog = commonViewBean.getTvLogView();
            if (mTVShowLog != null) {
                mTVShowLogWR = new WeakReference<>(mTVShowLog);
                mTVShowLog.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
        }
    }

    protected CommonViewBean getCommonViewBean() {
        return null;
    }

    private void setTitleBar(TitleBar titleBar, int titleResId) {
        if (titleBar != null && titleResId != 0) {
            titleBar.setTitle(titleResId);
            titleBar.setListener(v -> {
                finish();
            });
        }
    }

    public static class BaseNetworkAdSourceEventListener implements NetworkAdSourceEventListener {

        @Override
        public void notifyAdSourceEvent(AdSourceEvent adSourceEvent, MCAdInfo adInfo, AdSourceError adSourceError) {
            Log.i(getClass().getSimpleName(), "notifyAdSourceEvent: " + adSourceEvent +
                    "\n, adInfo=" + adInfo +
                    "\n, adSourceError=" + adSourceError);
        }
    }

}
