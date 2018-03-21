package com.woobo.crosswalkdemo.view.activity.tests;

import android.app.Activity;
import android.content.Intent;
import android.view.ViewGroup;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.view.activity.common.BaseActivity;
import com.woobo.crosswalkdemo.view.activity.common.CommonHeadActivity;

/**
 * @author sanji
 */
public class EvaluateJsActivity extends CommonHeadActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, EvaluateJsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public String getMiddleTitle() {
        return TAG;
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_evaluate_js;
    }

}
