package com.woobo.crosswalkdemo.common.app;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;
import com.woobo.crosswalkdemo.common.utils.LogUtils;

/**
 * @author sanji
 */
public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MySCrashHandler.getInstance().init(this);

        initX5WebView();
    }

    /**
     * 第一个参数传入 context，第二个参数传入 callback，不需要 callback 的可以传入 null，
     * initX5Environment 内部会创建一个线程向后台查询当前可用内核版本号，
     * 这个函数内是异步执行所以不会阻塞 App 主线程，这个函数内是轻量级执行所以对 App
     * 启动性能没有影响，当 App 后续创建 webview 时就可以首次加载 x5 内核了
     */
    private void initX5WebView() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            /**
             * x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
             */
            @Override
            public void onCoreInitFinished() {
                LogUtils.d(TAG, "initX5WebView - onCoreInitFinished");
            }

            /**
             * x5内核初始化完成回调接口，可通过参数判断是否加载起来了x5内核
             * isX5Core为true表示x5内核加载成功；false表示加载失败，此时会自动切换到系统内核。
             * 如果在此回调前创建webview会导致使用系统内核 -return:void
             * @param isX5Core
             */
            @Override
            public void onViewInitFinished(boolean isX5Core) {
                LogUtils.d(TAG, "initX5WebView - onViewInitFinished " + isX5Core);
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb);
        // 非wifi网络条件下是否允许下载内核，默认为false。
        // 当本地无可用内核会去主动下载内核，会产生24M左右的数据流量，为了节省用户流量，默认
        // 只在wifi条件下才会去下载。开发者可以通过调用该接口设置为true,允许用户在非wifi条件下也下载内核。
        QbSdk.setDownloadWithoutWifi(true);
    }


}
