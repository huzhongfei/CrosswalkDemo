package com.woobo.crosswalkdemo.view.activity.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.utils.ToastUtil;
import com.woobo.crosswalkdemo.service.system.SystemService;
import com.woobo.crosswalkdemo.view.activity.common.H5Activity;
import com.woobo.crosswalkdemo.view.activity.common.HeadUIHelper;
import com.woobo.crosswalkdemo.view.dialog.H5GameSelectPop;

/**
 * @author sanji
 * 显示系统原生webview的界面
 */
public class SysWebviewActivity extends H5Activity implements SystemService.EventInterface,
        HeadUIHelper.EvenObserver, H5GameSelectPop.EventObserver {

    private SystemService systemService;

    private HeadUIHelper headUIHelper;

    private H5GameSelectPop h5GameSelectPop;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, SysWebviewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_webview);
        initView();
        initData();
    }

    @Override
    public boolean canGoBack() {
        if (null != systemService) {
            return systemService.canGoBack();
        }
        return false;
    }

    @Override
    public void goBack() {
        if (null != systemService) {
            systemService.goBack();
        }
    }

    private void initView() {
        initHeadView();
    }

    private void initHeadView() {
        headUIHelper = new HeadUIHelper();
        headUIHelper.initHead(getWindow().getDecorView(), this);
        headUIHelper.setRightImage(R.mipmap.more);
    }

    private void initData() {
        systemService = new SystemService();
        systemService.showWebview((LinearLayout) findViewById(R.id.webview_lay), this);
        systemService.setEventListener(this);
        h5GameSelectPop = new H5GameSelectPop(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.showMsgShort("native webview");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != systemService) systemService.onDestroy();
    }

    // -------- S= 实现HeadUIHelper.EvenObserver ----------------------------------

    @Override
    public void onClickLeft(ViewGroup head) {
        finish();
    }

    @Override
    public void onClickMiddle(ViewGroup head) {

    }

    @Override
    public void onClicRight(ViewGroup head) {
        if (null != h5GameSelectPop) h5GameSelectPop.showPopupWindow(head);
    }

    // --------E= 实现HeadUIHelper.EvenObserver -----------------------------------

    // --------S=  实现H5GameSelectPop.EventObserver -------------------------------

    @Override
    public void onH5GameUpdate(boolean isUpdate) {
        if (null != systemService) systemService.updateLoadUrl();
    }

    // --------E=  实现H5GameSelectPop.EventObserver -------------------------------

    // --------S= 实现SystemService.EventInterface --------------------------------

    @Override
    public void onReceivedTitle(String title) {
        headUIHelper.setMiddleText(title);
    }

    // --------E= 实现SystemService.EventInterface --------------------------------
}
