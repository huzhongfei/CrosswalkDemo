package com.woobo.crosswalkdemo.common.app;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * @author sanji
 */
public class AppManager {

    // Activity栈
    private static Stack<Activity> activityStack;

    private AppManager() {}

    private static class SingleHolder {
        public static final AppManager INSTANCE = new AppManager();
    }

    public static AppManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 获得activity对象
     * @param cls
     * @return
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void exit(Context context) {
        // 如果开发者调用Process.kill或者System.exit之类的方法杀死进程，
        // 请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。
        //MobclickAgent.onKillProcess(context);

        finishAllActivity();
        System.exit(0);
    }

    /**
     * 强制退出应用
     * @param context
     */
    public void forceExit(Context context) {
        // 如果开发者调用Process.kill或者System.exit之类的方法杀死进程，
        // 请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。
        //MobclickAgent.onKillProcess(context);

        // 退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        //这个方法是用来结束当前正在运行中的java虚拟机。如果status是非零参数，那么表示是非正常退出。
        //0.标识正常退出，1表示非正常退出
        System.exit(1);
    }
}
