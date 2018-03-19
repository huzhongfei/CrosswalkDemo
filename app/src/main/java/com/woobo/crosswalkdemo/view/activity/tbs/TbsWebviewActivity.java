package com.woobo.crosswalkdemo.view.activity.tbs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.utils.ToastUtil;

/**
 * @author sanji
 * 显示腾讯TBS内核webview的界面
 */
public class TbsWebviewActivity extends AppCompatActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, TbsWebviewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tbs_webview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.showMsgShort("tbs webview");
    }
}
