package com.woobo.crosswalkdemo.service.common;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * webkit包下
 * 定义一个WebChromeClient类的子类。在其中对一些常用的方法做了注释说明。以参考为目的。
 * 不推荐在项目中直接使用。可以参考它来实现自己的WebChromeClient子类。
 * @author sanji
 */
public class WebChromeClientCommon extends WebChromeClient {

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
        //if (null != progressDialog) progressDialog.setMessage("load " + newProgress);
    }

    /**
     * 获取web页面中的标题
     * @param view
     * @param title
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        //if (null != eventInterface) eventInterface.onReceivedTitle(title);
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
