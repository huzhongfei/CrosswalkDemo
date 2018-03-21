package com.woobo.crosswalkdemo.view.activity.tests;

import android.app.Activity;
import android.content.Intent;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.view.activity.common.CommonHeadActivity;

/**
 * Created by sanji on 2018/3/21.
 */
public class JsByLoadActivity extends CommonHeadActivity {


    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, JsByLoadActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public String getMiddleTitle() {
        return TAG;
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_js_by_load;
    }
}
