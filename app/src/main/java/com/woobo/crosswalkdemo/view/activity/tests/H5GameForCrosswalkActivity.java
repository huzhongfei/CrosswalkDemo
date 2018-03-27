package com.woobo.crosswalkdemo.view.activity.tests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.view.activity.common.CommonHeadActivity;
import com.woobo.crosswalkdemo.view.activity.common.CommonHeadCrosswalkActivity;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * 加载本地H5游戏界面 crosswalk引擎
 * Created by sanji on 2018/3/27.
 */
public class H5GameForCrosswalkActivity extends CommonHeadCrosswalkActivity {

    private LinearLayout mWebViewLay;
    private XWalkView mWebView;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, H5GameForCrosswalkActivity.class);
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

    @Override
    protected void onXWalkReady() {
        super.onXWalkReady();
        initWebView();
    }

    private void initView() {
        mWebViewLay = findViewById(R.id.webview_lay);
    }

    private void initWebView() {
        // 动态添加webview
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 注：传入的参数不是activity，这里不会显示弹窗的
        mWebView = new XWalkView(MyApplication.getInstance());
        mWebView.setLayoutParams(params);
        if (null == mWebViewLay) return;
        mWebViewLay.addView(mWebView);

        XWalkSettings webSettings = mWebView.getSettings();


        // 设置与JS交互的权限
        webSettings.setJavaScriptEnabled(true);

        // 支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(true);
        // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);////将图片调整到适合webview的大小
        //mWebSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadsImagesAutomatically(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(XWalkSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        saveData(webSettings);

        newWin(webSettings, true);

        mWebView.setResourceClient(new XWalkResourceClient(mWebView));
        mWebView.setUIClient(new XWalkUIClient(mWebView));


        mWebView.loadUrl("file:///android_asset/html5-fruit-ninja/index.html");
    }

    /**
     * HTML5数据存储
     */
    private void saveData(XWalkSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        try {
            PackageManager pm = MyApplication.getInstance().getPackageManager();
            @SuppressLint("WrongConstant") ApplicationInfo ai = pm.getApplicationInfo("com.mytest", PackageManager.GET_ACTIVITIES);
            Log.d("!!", "!!" + ai.uid);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多窗口的问题
     */
    private void newWin(XWalkSettings mWebSettings, boolean isMulti) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下Myerain69
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(isMulti);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(isMulti);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWebView) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            // TODO: 2018/3/27 缓存管理
            //mWebView.clearCache(true);
            ((ViewGroup)mWebView.getParent()).removeView(mWebView);
            mWebView.onDestroy();
            mWebView = null;
        }
    }

    @Override
    public void onClickMiddle(ViewGroup head) {
        // TODO: 2018/3/27 mode
        if (null != mWebView) mWebView.reload(0);
    }
}
