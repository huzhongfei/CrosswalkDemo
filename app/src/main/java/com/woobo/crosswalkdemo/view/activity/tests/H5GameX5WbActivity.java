package com.woobo.crosswalkdemo.view.activity.tests;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.config.X5Config;
import com.woobo.crosswalkdemo.common.utils.LogUtils;
import com.woobo.crosswalkdemo.view.activity.common.CommonHeadActivity;
import com.woobo.crosswalkdemo.view.custom.X5WebView;

/**
 * 加载本地H5游戏界面 tbs x5引擎
 * Created by sanji on 2018/3/27.
 */
public class H5GameX5WbActivity extends CommonHeadActivity {

    private LinearLayout mWebViewLay;
    private WebView mWebView;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, H5GameX5WbActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public String getMiddleTitle() {
        // 为了避免woobo看不到右上角的情况，将刷新功能入口放到了中间标题
        return "点我刷新";
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_webview_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();
    }

    private void initView() {
        mWebViewLay = findViewById(R.id.webview_lay);
        initWebView();
    }

    private void initWebView() {
        // 动态添加webview
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 注：传入的参数不是activity，这里不会显示弹窗的
        mWebView = new X5WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        if (null == mWebViewLay) return;
        mWebViewLay.addView(mWebView);

        WebSettings webSettings = mWebView.getSettings();
        // 设置与JS交互的权限  不设置的话，H5游戏加载的时候会黑屏
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(true);
        // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // 加载切水果游戏
        //mWebView.loadUrl(H5GameUrlConfig.FRUIT_LOCAL);
        // 加载X5测试反馈界面
        mWebView.loadUrl(X5Config.FEED_BACK);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 打印是否使用了X5内核
        LogUtils.d(TAG, "getX5WebViewExtension: " + mWebView.getX5WebViewExtension());
        LogUtils.d(TAG, "isTbsCoreInited: " + QbSdk.isTbsCoreInited());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWebView) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup)mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    public void onClickMiddle(ViewGroup head) {
        if (null != mWebView) mWebView.reload();
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
