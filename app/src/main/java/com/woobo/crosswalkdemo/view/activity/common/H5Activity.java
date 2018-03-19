package com.woobo.crosswalkdemo.view.activity.common;

import android.os.Bundle;
import android.view.KeyEvent;

/**
 * @author sanji
 */
public abstract class H5Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract boolean canGoBack();

    public abstract void goBack();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断点击back后能否执行后退操作
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
