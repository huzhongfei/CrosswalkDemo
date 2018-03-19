package com.woobo.crosswalkdemo.view.activity.common;

import android.os.Bundle;

import com.woobo.crosswalkdemo.common.app.AppManager;
import com.woobo.crosswalkdemo.common.utils.LogUtils;

import org.xwalk.core.XWalkActivity;

/**
 * Created by sanji on 2018/3/15.
 */

public abstract class BaseCrosswalkActivity extends XWalkActivity {

    public final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将该Activity加入集合中以便管理
        AppManager.getInstance().addActivity(this);
        LogUtils.d(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        // Activity移除
        AppManager.getInstance().finishActivity(this);
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
    }
}
