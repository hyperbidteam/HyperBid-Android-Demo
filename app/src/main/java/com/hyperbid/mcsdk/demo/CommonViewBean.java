package com.hyperbid.mcsdk.demo;

import android.widget.Spinner;
import android.widget.TextView;

public class CommonViewBean {
    private TitleBar titleBar;
    private TextView tvLogView;
    private Spinner spinnerSelectPlacement;
    private int titleResId;

    public CommonViewBean() {

    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public void setTitleBar(TitleBar titleBar) {
        this.titleBar = titleBar;
    }

    public Spinner getSpinnerSelectPlacement() {
        return spinnerSelectPlacement;
    }

    public void setSpinnerSelectPlacement(Spinner spinnerSelectPlacement) {
        this.spinnerSelectPlacement = spinnerSelectPlacement;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public void setTitleResId(int titleResId) {
        this.titleResId = titleResId;
    }

    public TextView getTvLogView() {
        return tvLogView;
    }

    public void setTvLogView(TextView tvLogView) {
        this.tvLogView = tvLogView;
    }
}
