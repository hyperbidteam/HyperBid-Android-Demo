package com.hyperbid.mcsdk.demo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.mcsdk.core.api.MCInitListener;
import com.mcsdk.core.api.MCInitResult;
import com.mcsdk.core.api.MCSDK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalApplication extends Application {

    private static final String TAG = "+++++++++++++++";

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (!getPackageName().equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }

        if (!isMainProcess(this)) {
            return;
        }

        MCSDK.setLogDebug(true);

        MCSDK.setUserId("test_user_id");
        MCSDK.setChannel("test_channel");
        MCSDK.setSubChannel("test_sub_channel");

//        MCSDK.setDoNotSell(false);

        Map<String, Object> customRule = new HashMap<>();
        customRule.put("test_custom_rule_key", "test_custom_rule_value");
        MCSDK.setCustomRule(customRule);

        // Set 2-second strategy grace period
        MCSDK.setTimeoutForWaitingSetting(2000);
        MCSDK.setLocalStrategyAssetPath(this, "localStrategy");

        // Init SDK with UMP
//        initSdkWithUMP();

        initSDK(this);
    }

    public boolean isMainProcess(Context context) {
        try {
            if (null != context) {
                return context.getPackageName().equals(getProcessName(context));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void initSdkWithUMP() {
        // Please refer to the AppOpenWithUMPActivity
        // and you need to modify the launcher activity in AndroidManifest.xml to AppOpenWithUMPActivity
    }

    public void initSDK(Context context) {
        MCSDK.setPrivacySettingEnable(false);

        MCSDK.init(context, "j87336cfc5a7e4d3", "j2680e56630c3e2fd8857807bc7ac72cefea0879e", new MCInitListener() {
            @Override
            public void onMediationInitFinished(MCInitResult mcInitResult) {
                Log.i(TAG, "onMediationInitFinished: error msg: " + mcInitResult.getErrorMsg());
                Log.i(TAG, "onMediationInitFinished: init success mediation: " + mcInitResult.getSuccessMediationIdList());
                Log.e(TAG, "onMediationInitFinished: init failed mediation: " + mcInitResult.getFailedMediationIdMap());
                String mediationConfig = MCSDK.getMediationConfig();
                Log.i(TAG, "onMediationInitFinished: mediationConfig: " + mediationConfig);
            }
        });

    }

    public String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

}
