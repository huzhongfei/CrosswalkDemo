package com.woobo.crosswalkdemo.common.utils;

import android.widget.Toast;

import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.common.config.APKRunConfig;

/**
 * Toast显示工具
 *
 * @author Administrator
 */
public class ToastUtil {

    public static boolean isShowTestToast = true;

    static {
        if (APKRunConfig.RUN_MODE == APKRunConfig.RUN_PRODUCT) {
            isShowTestToast = false;
        } else {
            isShowTestToast = true;
        }
    }

    /**
     * 显示short类型的Toast
     *
     * @param msg 显示内容
     */
    public static void showMsgShort(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Long类型的Toast
     *
     * @param msg 显示内容
     */
    public static void showMsgLong(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示自定义时间的Toast
     *
     * @param msg 显示内容
     * @param dur 显示时间，单位为毫秒
     */
    public static void showMsg(String msg, int dur) {
        Toast.makeText(MyApplication.getInstance(), msg, dur).show();
    }

    /**
     * 显示short类型的Toast  -- just for test
     *
     * @param msg 显示内容
     */
    public static void showTestMsgShort(String msg) {
        if (isShowTestToast) {
            Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
