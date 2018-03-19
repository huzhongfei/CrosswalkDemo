package com.woobo.crosswalkdemo.service.system;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.common.config.H5GameUrlConfig;
import com.woobo.crosswalkdemo.common.utils.LogUtils;

/**
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
        settings.setSupportZoom(true);
        //设置浏览器支持javaScript
        settings.setJavaScriptEnabled(true);
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
        //获取websetings 设置
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true);
        //设置浏览器支持javaScript
        settings.setJavaScriptEnabled(true);
        //设置打开自带缩放按钮
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

        /**
         * 当网页调用alert()来弹出alert弹出框前回调，用以拦截alert()函数
         * @param view
         * @param url
         * @param message
         * @param result
         * @return 默认返回false 当返回true时表示上层拦截了该事件
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        /**
         * 当网页调用confirm()来弹出confirm弹出框前回调，用以拦截confirm()函数
         * @param view
         * @param url
         * @param message
         * @param result
         * @return 默认返回false 当返回true时表示上层拦截了该事件
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        /**
         * 当网页调用prompt()来弹出prompt弹出框前回调，用以拦截prompt()函数
         * @param view
         * @param url
         * @param message
         * @param defaultValue
         * @param result
         * @return 默认返回false 当返回true时表示上层拦截了该事件
         */
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        /**
         * 当html中调用console相关输出的时候，就会通过onConsoleMessage进行通知
         * @param consoleMessage
         * @return 如果返回true时，就表示拦截了console输出，系统就不再通过console输出出来了，
         * 如果返回false则表示没有拦截console输出，调用系统默认处理。
         */
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }

        /**
         * 通知主程序当前页面将要显示指定方向的View，该方法用来全屏播放视频。
         * @param view
         * @param callback
         */
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        /**
         * 与onShowCustomView对应，通知主程序当前页面将要关闭Custom View
         */
        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }

        /**
         *  请求主程序创建一个新的Window，如果主程序接收请求，
         *  返回true并创建一个新的WebView来装载Window，然后添加到View中，
         *  发送带有创建的WebView作为参数的resultMsg的给Target。如果主程序拒绝接收请求，
         *  则方法返回false。默认不做任何处理，返回false
         * @param view
         * @param isDialog
         * @param isUserGesture
         * @param resultMsg
         * @return
         */
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        /**
         * 显示当前WebView，为当前WebView获取焦点。
         * @param view
         */
        @Override
        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
        }


        /**
         * 通知主程序关闭WebView，并从View中移除，WebCore停止任何的进行中的加载和JS功能。
         * @param window
         */
        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        /**
         *  告诉客户端显示离开当前页面的导航提示框。如果返回true，由客户端处理确认提示框，
         *  调用合适的JsResult方法。如果返回false，则返回默认值true给javascript接受离开当前页面的导航。
         *  默认：false。JsResult设置false，当前页面取消导航提示，否则离开当前页面。
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        /**
         *  通知程序有定位权限请求。如果onGeolocationPermissionsShowPrompt权限申请操作被取消，则隐藏相关的UI界面
         */
        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        /**
         * 通知主程序web内容尝试使用定位API，但是没有相关的权限。主程序需要调用调用指定的定位权限申请的回调。
         * 更多说明查看GeolocationPermissions相关API。
         * @param origin
         * @param callback
         */
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }


        /**
         *通知主程序web内容尝试申请指定资源的权限（权限没有授权或已拒绝），
         * 主程序必须调用PermissionRequest#grant(String[])或PermissionRequest#deny()。如果没有覆写该方法，默认拒绝。
         * @param request
         */
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            super.onPermissionRequest(request);
        }

        /**
         * 通知主程序相关权限被取消。任何相关UI都应该隐藏掉。
         * @param request
         */
        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            super.onPermissionRequestCanceled(request);
        }

        /**
         *当停止播放，Video显示为一张图片。
         * 默认图片可以通过HTML的Video的poster属性标签来指定。
         * 如果poster属性不存在，则使用默认的poster。该方法允许ChromeClient提供默认图片。
         */
        @Override
        public Bitmap getDefaultVideoPoster() {
            return super.getDefaultVideoPoster();
        }

        /**
         * 当用户重放视频，在渲染第一帧前需要花费时间去缓冲足够的数据。
         * 在缓冲期间，ChromeClient可以提供一个显示的View。如：可以显示一个加载动画。
         */
        @Override
        public View getVideoLoadingProgressView() {
            return super.getVideoLoadingProgressView();
        }

        /**
         * 获取访问历史Item，用于链接颜色。
         */
        @Override
        public void getVisitedHistory(ValueCallback<String[]> callback) {
            super.getVisitedHistory(callback);
        }

        /**
         * 通知客户端显示文件选择器。用来处理file类型的HTML标签，响应用户点击选择文件的按钮操作。调用filePathCallback.onReceiveValue(null)并返回true取消请求操作。
         * FileChooserParams参数的枚举列表：
         MODE_OPEN 打开
         MODE_OPEN_MULTIPLE 选中多个文件打开
         MODE_OPEN_FOLDER 打开文件夹（暂不支持）
         MODE_SAVE 保存
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    }

    /**
     * 当影响View的事件到来时，会通过WebviewClient中的方法回调通知用户
     */
    class MyWebViewClient extends WebViewClient {

        /**
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
        // 显示读渠道的内容
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

        /**
         * 当接收到https错误时，会回调此函数，在其中可以做错误处理，比如自定义一个错误界
         * 默认加载SSL出错的网站——出现空白页面
         * 备注:
         * 1.当出现SSL错误时，WebView默认是取消加载当前页面，只有去掉onReceivedSslError的默认操作，然后添加SslErrorHandler.proceed()才能继续加载出错页面
         * 2.当HTTPS传输出现SSL错误时，错误会只通过onReceivedSslError回调传过来，不会执行onReceivedError
         * @param view 当前的WebView实例
         * @param handler 当前处理错误的Handler，它只有两个函数SslErrorHandler.proceed()和SslErrorHandler.cancel()，
         *                SslErrorHandler.proceed()表示忽略错误继续加载，SslErrorHandler.cancel()表示取消加载。
         *                在onReceivedSslError的默认实现中是使用的SslErrorHandler.cancel()来取消加载，
         *                所以一旦出来SSL错误，HTTPS网站就会被取消加载了，如果想忽略错误继续加载就只有
         *                重写onReceivedSslError，并在其中调用SslErrorHandler.proceed()
         * @param error 当前的的错误对象，SslError包含了当前SSL错误的基本所有信息
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            // 如果这里修改了默认行为，则需要注释掉上面super的调用。因为从源码来看，onReceivedSslError里面
            // 的默认实现是handler.cancel();即取消继续加载
            // handler.proceed();
        }

        /**
         * 在每一次请求资源时，都会通过这个函数来回调，比如超链接、JS文件、CSS文件、图片等，
         * 也就是说浏览器中每一次请求资源时，都会回调回来，无论任何资源！
         * 但是必须注意的是shouldInterceptRequest函数是在非UI线程中执行的，
         * 在其中不能直接做UI操作，如果需要做UI操作，则需要利用Handler（或其他线程转换策略）来实现。
         * 该函数会在请求资源前调用，我们可以通过返回WebResourceResponse的处理结果来让WebView直接使用我们的处理结果。
         * 如果我们不想处理，则直接返回null，系统会继续加载该资源。
         * 利用这个特性，我们可以解决一个需求：假如网页中需要加载本地的图片，我们就可以通过拦截shouldInterceptRequest，并返回结果即可 。
         * @param view
         * @param request
         * @return
         */
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        /**
         * 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
         * @param view
         * @param url
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        /**
         * WebView发生改变时调用)
         * 可以参考http://www.it1352.com/191180.html的用法
         * @param view
         * @param oldScale
         * @param newScale
         */
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        /**
         * 重写此方法才能够处理在浏览器中的按键事件。
         * 是否让主程序同步处理Key Event事件，如过滤菜单快捷键的Key Event事件。
         * 如果返回true，WebView不会处理Key Event，
         * 如果返回false，Key Event总是由WebView处理。默认：false
         * @param view
         * @param event
         * @return
         */
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        /**
         * 是否重发POST请求数据，默认不重发。
         * @param view
         * @param dontResend
         * @param resend
         */
        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            super.onFormResubmission(view, dontResend, resend);
        }

        /**
         * 更新访问历史
         * @param view
         * @param url
         * @param isReload
         */
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        /**
         * 通知主程序输入事件不是由WebView调用。是否让主程序处理WebView未处理的Input Event。
         * 除了系统按键，WebView总是消耗掉输入事件或shouldOverrideKeyEvent返回true。
         * 该方法由event 分发异步调用。注意：如果事件为MotionEvent，则事件的生命周期只存在方法调用过程中，
         * 如果WebViewClient想要使用这个Event，则需要复制Event对象。
         */
        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
        }

        /**
         * 通知主程序执行了自动登录请求。
         */
        @Override
        public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
            super.onReceivedLoginRequest(view, realm, account, args);
        }

        /**
         * 通知主程序：WebView接收HTTP认证请求，主程序可以使用HttpAuthHandler为请求设置WebView响应。默认取消请求
         * @param view
         * @param handler
         * @param host
         * @param realm
         */
        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        /**
         * 通知主程序处理SSL客户端认证请求。如果需要提供密钥，主程序负责显示UI界面。
         * 有三个响应方法：proceed(), cancel() 和 ignore()。
         * 如果调用proceed()和cancel()，webview将会记住response，
         * 对相同的host和port地址不再调用onReceivedClientCertRequest方法。
         * 如果调用ignore()方法，webview则不会记住response。该方法在UI线程中执行，
         * 在回调期间，连接被挂起。默认cancel()，即无客户端认证
         */
        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            super.onReceivedClientCertRequest(view, request);
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

    }

    public void onPause() {

    }

    public void onNewIntent(Intent intent) {

    }

    public void onDestroy() {
        if (null != mWebView) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup)mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    public interface EventInterface {
        void onReceivedTitle(String title);
    }

    public void setEventListener(EventInterface eventInterface) {
        this.eventInterface = eventInterface;
    }
}
