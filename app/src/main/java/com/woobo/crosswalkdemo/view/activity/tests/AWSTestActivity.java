package com.woobo.crosswalkdemo.view.activity.tests;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.utils.LogUtils;
import com.woobo.crosswalkdemo.common.utils.ToastUtil;
import com.woobo.crosswalkdemo.view.activity.common.CommonHeadActivity;

/**
 * Created by sanji on 2018/3/23.
 */

public class AWSTestActivity extends CommonHeadActivity implements
        RadioGroup.OnCheckedChangeListener {

    private static final String usa_url = "https://s3-us-west-1.amazonaws.com/woobo-media/Dance+Party/05-07-Giraffe.mp4";

    private static final String japan_url = "https://s3-ap-northeast-1.amazonaws.com/test-tokyo-latency-woobo/05-07-Giraffe.mp4";

    private static String aws_url = usa_url;

    private LinearLayout mWebViewLay;
    private WebView mWebView;
    private RadioGroup radioGroup;
    private Button loadBn;
    private Button releaseBn;
    private TextView startTimeView;
    private long startDuration = 0;
    private long startTime = 0;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AWSTestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public String getMiddleTitle() {
        return TAG;
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_aws_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        register();
    }

    private void initView() {
        mWebViewLay = findViewById(R.id.webview_lay);
        radioGroup = findViewById(R.id.radioGroup);
        loadBn = findViewById(R.id.loadBn);
        releaseBn = findViewById(R.id.releaseBn);
        startTimeView = findViewById(R.id.startTime);
    }

    private void initWebView() {
        // 动态添加webview
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 注：传入的参数不是activity，这里不会显示弹窗的
        //mWebView = new WebView(getApplicationContext());
        mWebView = new WebView(this);
        mWebView.setLayoutParams(params);
        if (null == mWebViewLay) return;
        mWebViewLay.addView(mWebView);

        WebSettings webSettings = mWebView.getSettings();
        // 设置与JS交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new MyWebChromeClient());

        // 先载入JS代码
        // 格式规定为：file:///android_asset/文件名.html
        mWebView.loadUrl(aws_url);
    }

    private void register() {
        radioGroup.setOnCheckedChangeListener(this);
        loadBn.setOnClickListener(this);
        releaseBn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseWebView();
    }

    private void releaseWebView() {
        if (null != mWebView) {
            ToastUtil.showMsgShort("开始释放资源");
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.freeMemory();
            mWebView.pauseTimers();
            //mWebViewLay.removeAllViews();
            mWebViewLay.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
            ToastUtil.showMsgShort("释放资源结束");
        }
    }

    // -------- 实现RadioGroup.OnCheckedChangeListener -------------------

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.usa:
                aws_url = usa_url;
                ToastUtil.showMsgShort("选择USA");
                break;
            case R.id.japan:
                aws_url = japan_url;
                ToastUtil.showMsgShort("选择JAPAN");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.loadBn:
                loadUrl();
                break;
            case R.id.releaseBn:
                release();
                break;
        }
    }

    private void loadUrl() {
        ToastUtil.showMsgShort("开始加载");
        initWebView();
    }

    public void release() {
        LogUtils.d(TAG, "releaseing.");
        releaseWebView();
    }

    private class MyWebChromeClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            startTime = System.currentTimeMillis();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            startDuration = System.currentTimeMillis() - startTime;
            startDuration = startDuration / 1000;
            startTimeView.setText("" + startDuration);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断点击back后能否执行后退操作
        if (keyCode == KeyEvent.KEYCODE_BACK && null != mWebView && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
