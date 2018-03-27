package com.woobo.crosswalkdemo.service.system;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.common.config.H5GameUrlConfig;
import com.woobo.crosswalkdemo.common.utils.LogUtils;

/**
 * 封装webkit下集成webview的一些功能
 * @author sanji
 */
public class SystemService {

    private final String TAG = SystemService.class.getSimpleName();

    private WebView mWebView;

    private ProgressDialog progressDialog;

    private EventInterface eventInterface;

    /**
     * 点击进行跳转 适用于动态添加webview的情况
     */
    public void showWebview(LinearLayout parent, Activity activity){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new WebView(MyApplication.getInstance());
        mWebView.setLayoutParams(params);
        if (null == parent) return;
        parent.addView(mWebView);

        //获取websetings 设置
        WebSettings settings = mWebView.getSettings();
        //设置支持javaScript交互
        settings.setJavaScriptEnabled(true);
        //settings.setPluginState(); Plugins will not be supported in future, and should not be used.
        settings.setSupportZoom(true);
        //设置打开自带缩放按钮
        settings.setBuiltInZoomControls(true);
        // 进行跳转用户输入的url地址
        mWebView.loadUrl(H5GameUrlConfig.H5_GAME);

        progressDialog = new ProgressDialog(activity);

        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.setWebViewClient(new MyWebViewClient());
    }

    /**
     * 点击进行跳转 适用于webview写在布局文件的情况
     */
    public void showWebview(WebView webView, Activity activity){
        mWebView = webView;
        // 获取websetings对象
        WebSettings settings = mWebView.getSettings();
        // 设置支持javaScript交互
        settings.setJavaScriptEnabled(true);
        // 支持缩放，默认为true。是下面那个的前提。
        settings.setSupportZoom(true);
        // 设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setBuiltInZoomControls(true);

        // 进行跳转用户输入的url地址
        mWebView.loadUrl(H5GameUrlConfig.H5_GAME);

        progressDialog = new ProgressDialog(activity);
        // 设置加载网页时不调用系统浏览器，而是在本地webview显示
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    /**
     * 当影响浏览器的事件到来时，就会通过WebChromeClient中的方法回调通知用户
     */
    class MyWebChromeClient extends WebChromeClient {


        /**
         * 获得网页的加载进度
         * 底层实现时，是利用handler来定时轮循当前进度的，每隔一定时间查询一次，
         * 所以每次拿到的进度数据是不一样的，也就是说如果页面较简单，可能会直接返回100，
         * 而跳过中间的各个数据。也就是说，除了100，其它任何一个数值不是一定返回的，
         * 所以大家如果要用到进度除了数值100可以用等号来判断，其它一定要用大于号或小于号，如果用了等号，可能永远也不会执行到！
         * @param view
         * @param newProgress 当前的加载进度，值从0到100
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (null != progressDialog)
            progressDialog.setMessage("加载" + newProgress);
        }

        /**
         * 获取web页面中的标题
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (null != eventInterface) eventInterface.onReceivedTitle(title);
        }
    }

    /**
     * 当影响View的事件到来时，会通过WebviewClient中的方法回调通知用户
     */
    class MyWebViewClient extends WebViewClient {

        /**
         * 显示读渠道的内容
         * 由于每次超链接在加载前都会先走shouldOverrideUrlLoading回调，所以我们如果想拦截某个URL，
         * 将其转换成其它URL可以在这里做。
         * @param view
         * @param url
         * @return 返回值是boolean类型，表示是否屏蔽WebView继续加载URL的默认行为。
         * 因为这个函数是WebView加载URL前回调的，所以如果我们return true，则WebView接下来就不会再加载这个URL了。
         * 如果我们return false，则系统就认为上层没有做处理，接下来还是会继续加载这个URL的。
         * WebViewClient默认就是return false的。
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        /**
         * 页面开始的时候 回调此方法
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (null != progressDialog){
                progressDialog.setMessage("loading。。。。。。。。。。。");
                progressDialog.show();
            }
        }

        /**
         * 页面结束的时候 回调此方法
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            if (null != progressDialog)
            progressDialog.dismiss();
        }

        /**
         * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
         * 加载本地错误界面的步骤：
         * 1、写一个html文件（error_handle.html），用于出错时展示给用户看的提示页面
         * 2、将该html文件放置到代码根目录的assets文件夹下
         * 3、复写WebViewClient的onRecievedError方法
         * 4、该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
         * @param view 当前的WebView实例
         * @param request 本次加载对应的request对象
         * @param error 错误描述
         */
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            // 重新定义了而一个错误界面（for test）
            view.loadUrl("file:///androi_asset/error.html");
        }

    }

    public boolean canGoBack() {
        if (null != mWebView) {
            LogUtils.d(TAG, "canGoBack: " + mWebView.canGoBack());
            return mWebView.canGoBack();
        }
        return false;
    }

    public void goBack() {
        if (null != mWebView) mWebView.goBack();
    }

    public void updateLoadUrl() {
        if (null != mWebView) mWebView.loadUrl(H5GameUrlConfig.H5_GAME);
    }

    public void onResume() {
        if (null != mWebView) {
            //激活WebView为活跃状态，能正常执行网页的响应
            mWebView.onResume();
            //恢复pauseTimers状态
            mWebView.resumeTimers();
        }
    }

    public void onPause() {
        if (null != mWebView) {
            //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
            //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
            mWebView.onPause();
            //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
            //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
            mWebView.pauseTimers();
        }
    }

    public void onNewIntent(Intent intent) {

    }

    /**
     * 销毁Webview
     */
    public void onDestroy() {
        if (null != mWebView) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            clearHistory(mWebView);
            removeWebview(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    /**
     * 清除网页访问留下的缓存
     * 由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
     */
    private void clearCache(boolean includeDiskFiles) {
        if (null != mWebView) mWebView.clearCache(includeDiskFiles);
    }

    /**
     * 清除当前webview访问的历史记录
     * 只会webview访问历史记录里的所有记录除了当前访问记录
     */
    private void clearHistory(WebView webview) {
        if (null != webview) webview.clearHistory();
    }

    /**
     * 这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
     */
    private void clearFormData(WebView webview) {
        if (null != webview) webview.clearFormData();
    }

    /**
     * 从webview的父容器中移除该控件 适用于动态添加webview的情形
     * @param webview
     */
    private void removeWebview(WebView webview) {
        //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
        //但是注意：webview调用destory时,webview仍绑定在Activity上
        //这是由于自定义webview构建时传入了该Activity的context对象
        //因此需要先从父容器中移除webview,然后再销毁webview:
        if (null != webview) ((ViewGroup)webview.getParent()).removeView(webview);
    }

    public interface EventInterface {
        void onReceivedTitle(String title);
    }

    public void setEventListener(EventInterface eventInterface) {
        this.eventInterface = eventInterface;
    }
}
