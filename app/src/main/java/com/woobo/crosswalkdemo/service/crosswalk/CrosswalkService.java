package com.woobo.crosswalkdemo.service.crosswalk;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;

import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.common.config.H5GameUrlConfig;
import com.woobo.crosswalkdemo.common.utils.ToastUtil;

import org.xwalk.core.ClientCertRequest;
import org.xwalk.core.XWalkHttpAuthHandler;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

/**
 * 封装crosswalk下集成webview的一些功能
 * 集成Crosswalk的服务类
 * @author sanji
 */
public class CrosswalkService {

    // crosswal的webview
    private XWalkView mWebView;

    private EventInterface eventInterface;

    /**
     * 动态添加webview 避免内存泄漏
     * @param parent
     */
    public void showWebview(LinearLayout parent) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new XWalkView(MyApplication.getInstance());
        mWebView.setLayoutParams(params);

        if (null == parent) return;

        parent.addView(mWebView);
        mWebView.loadUrl(H5GameUrlConfig.H5_GAME);

        XWalkSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);//支持缩放
        mWebSettings.setBuiltInZoomControls(true);//可以任意缩放
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);////将图片调整到适合webview的大小
        //mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);
        //mWebSettings.setMixedContentMode();
        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.setJavaScriptEnabled(true);//支持JS

        //mWebView.clearCache(true);//清除缓存
        //mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存(使用：ebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK))
        saveData(mWebSettings);
        newWin(mWebSettings);

        mWebView.setResourceClient(new MyXWalkResourceClient(mWebView));
        mWebView.setUIClient(new MyXWalkUIClient(mWebView));
    }

    /**
     * 多窗口的问题
     */
    private void newWin(XWalkSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下Myerain69
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
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
            ApplicationInfo ai = pm.getApplicationInfo("com.mytest", PackageManager.GET_ACTIVITIES);
            Log.d("!!", "!!" + ai.uid);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    class MyXWalkResourceClient extends XWalkResourceClient {

        public MyXWalkResourceClient(XWalkView view) {
            super(view);
        }

        //=========HTML5定位==========================================================
        //需要先加入权限
        //<uses-permission android:name="android.permission.INTERNET"/>
        //<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        //<uses-permission android:name=
        // "android.permission.ACCESS_COARSE_LOCATION"/>

        @Override
        public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
            //callback.onReceiveValue(false);
            //super.onReceivedSslError(view, callback, error);
            ToastUtil.showMsgShort("证书不合法");
        }

        @Override
        public void onLoadFinished(XWalkView view, String url) {
            super.onLoadFinished(view, url);
        }

        @Override
        public void onLoadStarted(XWalkView view, String url) {
            super.onLoadStarted(view, url);
        }

        @Override
        public void onProgressChanged(XWalkView view, int progressInPercent) {
            super.onProgressChanged(view, progressInPercent);
        }

        @Override
        public void onReceivedClientCertRequest(XWalkView view, ClientCertRequest handler) {
            super.onReceivedClientCertRequest(view, handler);
        }

        @Override
        public void onDocumentLoadedInFrame(XWalkView view, long frameId) {
            super.onDocumentLoadedInFrame(view, frameId);
        }

        @Override
        public void onReceivedHttpAuthRequest(XWalkView view, XWalkHttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
            super.onReceivedLoadError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedResponseHeaders(XWalkView view, XWalkWebResourceRequest request, XWalkWebResourceResponse response) {
            super.onReceivedResponseHeaders(view, request, response);
        }

        //=========HTML5定位==========================================================

    }

    class MyXWalkUIClient extends XWalkUIClient {

        public MyXWalkUIClient(XWalkView view) {
            super(view);
        }

        @Override
        public void onPageLoadStarted(XWalkView view, String url) {
            super.onPageLoadStarted(view, url);
        }

        @Override
        public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
            ToastUtil.showMsgShort(message);
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onScaleChanged(XWalkView view, float oldScale, float newScale) {
            if (view != null) {
                view.invalidate();
            }
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
            super.onPageLoadStopped(view, url, status);
        }
    }

    public boolean canGoBack() {
        return false;
    }

    public void goBack() {
        
    }

    public void updateLoadUrl() {
        if (null != mWebView) mWebView.loadUrl(H5GameUrlConfig.H5_GAME);
    }

    public void onResume() {
        if (mWebView != null) {
            mWebView.resumeTimers();
            mWebView.onShow();
        }
    }

    public void onPause() {
        if (mWebView != null) {
            mWebView.pauseTimers();
            mWebView.onHide();
        }
    }

    public void onNewIntent(Intent intent) {
        if (mWebView != null) {
            mWebView.onNewIntent(intent);
        }
    }

    public void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //mWebView.clearHistory();
            ((ViewGroup)mWebView.getParent()).removeView(mWebView);
            mWebView.onDestroy();
            mWebView = null;
            System.gc();
        }
    }

    public interface EventInterface {
        void onReceivedTitle(String title);
    }

    public void setEventListener(EventInterface eventInterface) {
        this.eventInterface = eventInterface;
    }
}
