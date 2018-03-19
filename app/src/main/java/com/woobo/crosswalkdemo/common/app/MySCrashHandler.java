package com.woobo.crosswalkdemo.common.app;

import android.content.Context;
import android.os.Environment;

import com.woobo.crosswalkdemo.common.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 未捕捉异常的处理类
 * 当程序发生未捕捉异常的时候，该类负责接管程序，并负责发送错误报告
 *
 * @author sanjin
 */
public class MySCrashHandler implements UncaughtExceptionHandler {

    /**
     * 未处理异常打印的日志标识
     */
    private static final String TAG = MySCrashHandler.class.getSimpleName();

    /**
     * MySCrashHandler的实例
     */
    private static MySCrashHandler instance = new MySCrashHandler();

    /**
     * context对象
     */
    private Context mContext;

    /**
     * 系统默认的未处理异常的处理器
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * 构造器私有化，保证只会有一个实例，即单例
     */
    private MySCrashHandler() {}

    /**
     * 得到MySCrashHandler类的实例
     *
     * @return
     */
    public static MySCrashHandler getInstance() {
        return instance;
    }

    /**
     * 初始化MySCrashHandler类
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置CoSCrashHandler类为程序DefaultUncaughtExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(this);
        //Thread.currentThread().setUncaughtExceptionHandler(new MyApplication.MyUncaughtExceptionHandler
        // ((MyApplication.MyUncaughtExceptionHandler) null));
    }

    /**
     * UncaughtException发生时，调用该方法
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        // 将异常记录到本地
        recordLogLocal(thread, ex);

        // 把异常打印到日志中去
        LogUtils.d(TAG, "error: " + ex);

        // 将异常发送给后台
        if (!handlerException(ex) && mDefaultHandler != null) {
            // 如果用于没有处理未捕捉异常/即异常内容为空的时候，则让系统自己处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
                // 强制退出程序
                AppManager.getInstance().forceExit(mContext);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送异常日志到远程服务器
     * 自定义错误处理、收集错误信息、发送错误报告等操作均在此方法完成
     * @param ex 异常对象
     * @return 如果在该方法中处理了异常则返回true，否则返回false，往往当异常对象为null的时候，会令其返回false
     */
    private boolean handlerException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 打印异常
        ex.printStackTrace();
        // 发送错误报告
        // send to server
        // 把为捕捉异常发给统计平台
        // TODO: 2018/3/15  发送错误报告
        return true;
    }

    /**
     * 将异常日志写到本地
     * @param thread
     * @param ex
     */
    private void recordLogLocal(Thread thread, Throwable ex){
        ex.printStackTrace();
        System.out.println(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "crosswalk_log.txt");
        LogUtils.d(TAG, Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "crosswalk_log.txt");
        try {
            ex.printStackTrace(new PrintStream(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "crosswalk_log.txt")));
        } catch (FileNotFoundException var4) {
            var4.printStackTrace();
        }
    }
}
