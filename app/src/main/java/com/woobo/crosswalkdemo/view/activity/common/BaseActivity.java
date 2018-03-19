package com.woobo.crosswalkdemo.view.activity.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.woobo.crosswalkdemo.common.app.AppManager;
import com.woobo.crosswalkdemo.common.utils.LogUtils;

/**
 * @author sanji
 * 所有Activity的基类
 */
public class BaseActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate");
        // 将该Activity加入集合中以便管理
        AppManager.getInstance().addActivity(this);
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
