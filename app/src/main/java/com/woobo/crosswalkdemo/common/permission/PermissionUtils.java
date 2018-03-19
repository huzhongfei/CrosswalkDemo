package com.woobo.crosswalkdemo.common.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;

import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanji
 */
public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();

    /**
     * 检查权限是否被允许
     * 这里涉及到一个API，ContextCompat.checkSelfPermission，
     * 主要用于检测某个权限是否已经被授予，方法返回值
     * 为PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED。
     * 当返回DENIED就需要进行申请授权了。
     *
     * @param permission
     * @return
     */
    public static boolean selfPermissionGranted(String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        // 当版本号为23及其以上时，才会去检查
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getSdkVersion() >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = MyApplication.getInstance().checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(MyApplication.getInstance(), permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }

    /**
     * 获取targetSdkVersion
     *
     * @return
     */
    public static int getSdkVersion() {
        try {
            final PackageInfo info = MyApplication.getInstance().getPackageManager().getPackageInfo(
                    MyApplication.getInstance().getPackageName(), 0);
            return info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 请求权限
     *
     * @param object
     * @param requestCode
     * @param permissions
     * @return
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static boolean requestPermissions(Object object, int requestCode, String[] permissions) {
        LogUtils.d(TAG, "requestPermissions.");
        if (!isOverMarshmallow()) {
            //doExecuteSuccess(object, requestCode);
            LogUtils.d(TAG, "requestPermissions. !isOverMarshmallow()");
            return true;
        }
        List<String> deniedPermissions = findDeniedPermissions(getActivity(object), permissions);

        if (deniedPermissions.size() > 0) {
            LogUtils.d(TAG, "requestPermissions. deniedPermissions.size() > 0 " + deniedPermissions.size());

            if (object instanceof Activity) {
                LogUtils.d(TAG, "requestPermissions. object instanceof Activity");
                ((Activity) object).requestPermissions(
                        deniedPermissions.toArray(
                                new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(
                        deniedPermissions.toArray(
                                new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName()
                        + " is not supported");
            }

        } else {
            //doExecuteSuccess(object, requestCode);
            LogUtils.d(TAG, "requestPermissions. deniedPermissions.size() <= 0");
            return true;
        }
        return false;
    }

    /**
     * 请求权限
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @return
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static boolean requestPermissions(
            Activity activity, int requestCode, String[] permissions) {
        if (!isOverMarshmallow()) {
            //doExecuteSuccess(object, requestCode);
            return true;
        }

        List<String> deniedPermissions = findDeniedPermissions(activity, permissions);

        if (deniedPermissions.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else {
            //doExecuteSuccess(object, requestCode);
            return true;
        }
        return false;
    }

    /**
     * 获得activity
     *
     * @param object
     * @return
     */
    private static Activity getActivity(Object object) {
        Activity activity = null;
        if (object instanceof Activity) {
            activity = (Activity) object;
        } else if (object instanceof Fragment) {
            activity = ((Fragment) object).getActivity();
        }
        return activity;
    }

    /**
     * 检查系统版本是否大于23
     *
     * @return
     */
    private static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 遍历待检查的权限，获得需要申请的权限集合
     *
     * @param activity
     * @param permission
     * @return
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    private static List<String> findDeniedPermissions(Activity activity, String... permission) {
        LogUtils.d(TAG, "findDeniedPermissions. permission.length: " + permission.length);
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            LogUtils.d(TAG, "findDeniedPermissions. permission.value: " + value);
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                LogUtils.d(TAG, "findDeniedPermissions. permission.value-added" + value);
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    /**
     * 检查请求权限结果，检查是否有被拒绝的权限
     * @param obj
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static boolean checkReqPermisResult(Object obj, int requestCode, String[] permissions,
                                               int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            //doExecuteFail(obj, requestCode);
            return false;
        } else {
            //doExecuteSuccess(obj, requestCode);
            return true;
        }
    }

    /**
     * 检查请求权限结果，检查是否有被拒绝的权限
     * @param permissions
     * @param grantResults
     */
    public static boolean checkReqPermisResult(String[] permissions, int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            //doExecuteFail(obj, requestCode);
            return false;
        } else {
            //doExecuteSuccess(obj, requestCode);
            return true;
        }
    }
}
