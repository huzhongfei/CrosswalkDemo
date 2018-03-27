package com.woobo.crosswalkdemo.view.activity.common;

import android.os.Bundle;

import com.woobo.crosswalkdemo.common.app.AppManager;
import com.woobo.crosswalkdemo.common.utils.LogUtils;

import org.xwalk.core.XWalkActivity;

/**
 * 方便项目管理，在实际开发中可以自定义一个XWalkActivity，让它继承一个自己的BaseActivity
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

    /**
     * 在onCreate中调用
     */
    @Override
    protected void onXWalkReady() {
        LogUtils.d(TAG, "onXWalkReady");
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
