package com.hyperbid.mcsdk.demo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mcsdk.core.api.MCAdConst;
import com.mcsdk.core.api.MCSDK;

public class MediationDebuggerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediation_debugger);

        initView();
    }

    private void initView() {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.setTitle(R.string.hyperbid_title_debugger);
        titleBar.setListener(v -> finish());

        findViewById(R.id.tv_topon_debugger).setOnClickListener(v -> MCSDK.showMediationDebugger(MCAdConst.MediationId.MEDIATION_ID_TOPON));

        findViewById(R.id.tv_max_debugger).setOnClickListener(v -> MCSDK.showMediationDebugger(MCAdConst.MediationId.MEDIATION_ID_MAX));
    }
}