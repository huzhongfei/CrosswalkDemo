package com.woobo.crosswalkdemo.view.activity.crosswalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.utils.ToastUtil;
import com.woobo.crosswalkdemo.service.crosswalk.CrosswalkService;
import com.woobo.crosswalkdemo.view.activity.common.BaseCrosswalkActivity;
import com.woobo.crosswalkdemo.view.activity.common.HeadUIHelper;
import com.woobo.crosswalkdemo.view.dialog.H5GameSelectPop;

/**
 * @author sanji
 * 显示Crosswalk webview的界面
 */
public class CrossWebviewActivity extends BaseCrosswalkActivity implements
        HeadUIHelper.EvenObserver, H5GameSelectPop.EventObserver  {

    private CrosswalkService crosswalkService;

    private HeadUIHelper headUIHelper;

    private H5GameSelectPop h5GameSelectPop;

    private LinearLayout webview_lay;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, CrossWebviewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使屏幕保持常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_cross_webview);

        initView();
        initData();
    }

    private void initView() {
        webview_lay = findViewById(R.id.webview_lay);
        initHeadView();
    }

    private void initHeadView() {
        headUIHelper = new HeadUIHelper();
        headUIHelper.initHead(getWindow().getDecorView(), this);
        headUIHelper.setRightImage(R.mipmap.more);
        headUIHelper.setMiddleText("CROSSWALK");
    }

    private void initData() {
        crosswalkService = new CrosswalkService();
        h5GameSelectPop = new H5GameSelectPop(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.showMsgShort("crosswalk webview");
        //if (null != crosswalkService) crosswalkService.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //if (null != crosswalkService) crosswalkService.onPause();
    }

    /**
     * 加载网页和配置在此方法中进行
     * 说明：XWalkview的配置和webview基本相同，可以参考webview对XWalkView进行配置
     */
    @Override
    protected void onXWalkReady() {
        if (null != crosswalkService) {
            crosswalkService.showWebview(webview_lay);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (null != crosswalkService) crosswalkService.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        if (null != crosswalkService) crosswalkService.onDestroy();
        super.onDestroy();
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
        if (null != crosswalkService) crosswalkService.updateLoadUrl();
    }

    // --------E=  实现H5GameSelectPop.EventObserver -------------------------------

}
