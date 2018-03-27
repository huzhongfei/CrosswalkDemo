package com.woobo.crosswalkdemo.view.activity.tests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.view.activity.common.CommonHeadActivity;

/**
 * 加载本地H5游戏界面
 * Created by sanji on 2018/3/27.
 */
public class H5GameActivity extends CommonHeadActivity {

    private LinearLayout mWebViewLay;
    private WebView mWebView;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, H5GameActivity.class);
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
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        if (null == mWebViewLay) return;
        mWebViewLay.addView(mWebView);

        WebSettings webSettings = mWebView.getSettings();
        // 设置与JS交互的权限
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
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("file:///android_asset/html5-fruit-ninja/index.html");
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
}
