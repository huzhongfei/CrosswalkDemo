package com.woobo.crosswalkdemo.view.activity.tests;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.utils.LogUtils;
import com.woobo.crosswalkdemo.view.activity.common.CommonHeadActivity;

import java.util.HashMap;
import java.util.Set;

/** 测试JS 和 Android 交互的界面
 * Created by sanji on 2018/3/21.
 */
public class JsAndNativeActivity extends CommonHeadActivity {

    private LinearLayout mWebViewLay;
    private WebView mWebView;
    private Button loadJS;
    private Button evaluateJavas;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, JsAndNativeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public String getMiddleTitle() {
        return TAG;
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_js_and_native;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        register();
    }

    private void initView() {
        mWebViewLay = findViewById(R.id.webview_lay);
        loadJS = findViewById(R.id.loadJS);
        evaluateJavas = findViewById(R.id.evaluateJavas);
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

        mWebView.setWebChromeClient(new MyChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());

        // 使用addJavascriptInterface进行对象映射
        // 步骤1：定义一个与JS对象映射关系的Android类：AndroidtoJs
        // 步骤2：将需要调用的JS代码以.html格式放到src/main/assets文件夹里
        // 步骤3：在Android里通过WebView设置Android类与JS代码的映射
        // 特点:
        // 优点：使用简单，仅将Android对象和JS对象映射即可
        // 缺点：存在严重的漏洞问题，具体请看文章：http://blog.csdn.net/carson_ho/article/details/64904635
        // 通过addJavascriptInterface将java对象映射到JS对象
        // 参数1：JavaScript对象名
        // 参数2：Java对象名
        // 下面代码的意思：AndroidToJs类对象映射到JS的test对象
        mWebView.addJavascriptInterface(new AndroidToJS(), "test");

        // 先载入JS代码
        // 格式规定为：file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/js_and_native.html");
    }

    public void loadJS() {
        if (null == mWebView) return;
        // 注意调用的JS方法名要对应上
        // 调用javascript的callJS()方法
        mWebView.loadUrl("javascript:callJS()");
        LogUtils.d(TAG, "button.click");
    }

    public void evaluateJavas() {
        mWebView.evaluateJavascript("javascript:evaluateCallJs()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                LogUtils.d(TAG, "evaluateJavased.");
            }
        });
    }

    // 由于设置了弹窗检验调用结果,所以需要支持js对话框
    // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
    // 通过设置WebChromeClient对象处理JavaScript的对话框
    //设置响应js 的Alert()函数
    private class MyChromeClient extends WebChromeClient {

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Uri uri = Uri.parse(message);
            if (uri.getScheme().equals("js")) {
                if (uri.getAuthority().equals("prompt")) {
                    LogUtils.d(TAG, "js调用了Android的方法 prompt");
                    Set<String> set = uri.getQueryParameterNames();
                    String[] args = new String[2];
                    int i = 0;
                    for (String str : set) {
                        LogUtils.d(TAG, "js调用了Android的方法 promp 参数: " + str + " - " + uri.getQueryParameter(str));
                        args[i] = uri.getQueryParameter(str);
                        i ++;
                    }
                    jsToAnndroidByPrompt(args[0], args[1]);
                    result.confirm("js调用了Android的方法 prompt");
                }
                return true;
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(JsAndNativeActivity.this);
            b.setTitle("Alert");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setCancelable(false);
            b.create().show();
            return true;
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 步骤2：根据协议的参数，判断是否是所需要的url
            // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
            // [scheme:][//authority][path][?query][#fragment]
            // 其中authority,又可以分为host:port的形式，即再次划分后是这样的：
            // [scheme:][//host:port][path][?query][#fragment]
            //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
            Uri uri = Uri.parse(url);
            // 如果url的协议 = 预先约定的 js 协议
            // 就解析往下解析参数
            if (uri.getScheme().equals("js")) {
                // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                // 所以拦截url,下面JS开始调用Android需要的方法
                if (uri.getAuthority().equals("webview")) {
                    //  步骤3：
                    // 执行JS所需要调用的逻辑
                    LogUtils.d(TAG, "js调用了Android的方法 webview");
                    // 可以在协议上带有参数并传递到Android上
                    HashMap<String, String> hashMap = new HashMap<>();
                    Set<String> set = uri.getQueryParameterNames();

                    String[] args = new String[2];
                    int i = 0;
                    for (String str : set) {
                        LogUtils.d(TAG, "js调用了Android的方法 参数: " + str + " - " + uri.getQueryParameter(str));
                        args[i] = uri.getQueryParameter(str);
                        i ++;
                    }

                    jsToAndroidByUrl(args[0], args[1]);
                } else if (uri.getAuthority().equals("webview1")) {
                    LogUtils.d(TAG, "js调用了Android的方法 webview1");
                    HashMap<String, String> hashMap = new HashMap<>();
                    Set<String> set = uri.getQueryParameterNames();

                    String[] args = new String[2];
                    int i = 0;
                    for (String str : set) {
                        LogUtils.d(TAG, "js调用了Android的方法 参数: " + str + " - " + uri.getQueryParameter(str));
                        args[i] = uri.getQueryParameter(str);
                        i ++;
                    }

                    jsToAndroidByUrl1(args[0], args[1]);
                }
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private void register() {
        loadJS.setOnClickListener(this);
        evaluateJavas.setOnClickListener(this);
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.loadJS:
                loadJS();
                break;
            case R.id.evaluateJavas:
                evaluateJavas();
                break;
        }
    }

    public class AndroidToJS {
        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void hello(String msg) {
            LogUtils.d(TAG, "JS调用了Android的hello方法 msg: " + msg);
        }
    }

    private void jsToAndroidByUrl(String arg1, String arg2) {
        LogUtils.d(TAG, "jsToAndroidByUrl arg1: " + arg1 + " arg2: " + arg2);
    }

    private void jsToAndroidByUrl1(String arg1, String arg2) {
        LogUtils.d(TAG, "jsToAndroidByUrl1 arg1: " + arg1 + " arg2: " + arg2);
        // 回传结果
        mWebView.loadUrl("javascript:callAndroidByUrl1Back()");
    }

    private void jsToAnndroidByPrompt(String arg1, String arg2) {
        LogUtils.d(TAG, "jsToAnndroidByPrompt arg1: " + arg1 + " arg2: " + arg2);
    }
}
