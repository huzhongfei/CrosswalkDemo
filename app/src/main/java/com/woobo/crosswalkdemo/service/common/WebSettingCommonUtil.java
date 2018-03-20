package com.woobo.crosswalkdemo.service.common;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.common.config.CacheConfig;
import com.woobo.crosswalkdemo.common.utils.NetWorkUtils;

/**
 * webkit包下WebSetting属性的设置
 * 注：本类设计的目的仅限于学习WebSetting，不推荐直接使用
 * @author sanji
 */
public class WebSettingCommonUtil {

    /**
     * 定义一个set WebSetting的方法
     * 设置WebSetting的属性 仅供学习参考 不推荐直接调用该方法
     * @param webView
     */
    public static void setWebSettingCommon(WebView webView) {
        // 获取WebSettings子类
        WebSettings webSettings = webView.getSettings();

        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        // 支持插件；该方法已经过时，不再使用
        // Plugins will not be supported in future, and should not be used.
        //webSettings.setPluginState(WebSettings.PluginState.ON);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    /**
     * 设置缓存模式。缓存模式有如下几种：
     * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
     * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
     * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
     * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据
     * note:
     * 当加载 html 页面时，WebView会在/data/data/包名目录下生成 database 与 cache 两个文件夹
     * 请求的 URL记录保存在 WebViewCache.db，而 URL的内容是保存在 WebViewCache 文件夹下
     * @param webSettings
     */
    public static void setCacheMode(WebSettings webSettings, int mode) {
        webSettings.setCacheMode(mode);
    }

    /**
     * 离线加载
     * note: 每个 Application 只调用一次 WebSettings.setAppCachePath()，WebSettings.setAppCacheMaxSize()
     * @param webSettings
     */
    public static void loadingOffLine(WebSettings webSettings) {
        if (NetWorkUtils.isAvailable()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }

        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能

        String cacheDirPath = MyApplication.getInstance().getFilesDir().getAbsolutePath() + CacheConfig.APP_CACAHE_DIRNAME;
        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录
    }



}
