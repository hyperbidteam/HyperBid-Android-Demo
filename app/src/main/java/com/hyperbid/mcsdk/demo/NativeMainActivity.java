package com.hyperbid.mcsdk.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NativeMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_native_main);

        initView();
    }

    private void initView() {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.setTitle(R.string.hyperbid_title_native);
        titleBar.setListener(new TitleBarClickListener() {
            @Override
            public void onBackClick(View v) {
                finish();
            }
        });

        TextView tvSelfRender = findViewById(R.id.tv_self_render);
        TextView tvExpress = findViewById(R.id.tv_express);

        tvSelfRender.setOnClickListener(this);
        tvExpress.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_self_render) {
            Intent intent1 = new Intent(NativeMainActivity.this, NativeAdActivity.class);
            intent1.putExtra("native_type", NativeAdActivity.NATIVE_SELF_RENDER_TYPE);
            startActivity(intent1);
        } else if (id == R.id.tv_express) {
            Intent intent2 = new Intent(NativeMainActivity.this, NativeAdActivity.class);
            intent2.putExtra("native_type", NativeAdActivity.NATIVE_EXPRESS_TYPE);
            startActivity(intent2);
        }
    }

}
