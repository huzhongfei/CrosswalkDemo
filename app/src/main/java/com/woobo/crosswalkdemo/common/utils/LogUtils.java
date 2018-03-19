package com.woobo.crosswalkdemo.common.utils;

import android.util.Log;

import com.woobo.crosswalkdemo.common.config.APKRunConfig;

/**
 * 打印日志的工具类
 *
 * @author sanjin
 */
public class LogUtils {

    // 增加一个log过滤tag
    private static final String INNER_TAG = "CrosswalkDemo : ";

    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    public static final int NOTHING = 6;

    //  LEVEL等于VEROBOSE打印所有日志，
    //  LEVEL等于NOTHING不打印所有日志（项目上线就改成这种的）
    public static int level = VERBOSE;

    static {
        if (APKRunConfig.RUN_MODE == APKRunConfig.RUN_PRODUCT) {
            level = NOTHING;
        } else {
            level = VERBOSE;
        }
    }

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(INNER_TAG + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.d(INNER_TAG + tag, msg);
        }
    }


    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(INNER_TAG + tag, msg);
        }
    }


    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.w(INNER_TAG + tag, msg);
        }
    }

    /**
     * 错误需要上报
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Log.e(INNER_TAG + tag, msg);
        }
    }
}
