package com.woobo.crosswalkdemo.service.common;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * webkit包下
 * 定义一个常用的webviewclient，用于记录该类的一些方法，以作参考
 * 注：在项目中还是得自己定义一个client的子类，不建议直接使用该类，它只作参考使用
 * Created by sanji
 */
public class WebviewClientCommon extends WebViewClient {

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
        // 可以显示进度
        /*if (null != progressDialog){
            progressDialog.setMessage("loading。。。。。。。。。。。");
            progressDialog.show();
        }*/
    }

    /**
     * 页面结束的时候 回调此方法
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        // 结束进度
        //if (null != progressDialog) progressDialog.dismiss();
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
