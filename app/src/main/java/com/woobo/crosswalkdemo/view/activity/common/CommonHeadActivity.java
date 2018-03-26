package com.woobo.crosswalkdemo.view.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.common.utils.ToastUtil;

/**
 * Created by sanji on 2018/3/21.
 */
public abstract class CommonHeadActivity extends BaseActivity
        implements HeadUIHelper.EvenObserver, View.OnClickListener {

    private HeadUIHelper headUIHelper;
    private FrameLayout contentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_head);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.showMsgShort(TAG);
    }

    private void initView() {
        initHeadView();
        contentContainer = findViewById(R.id.content_lay);

        // 这里只要inflate出来后加入一次即可
        if (contentContainer.getChildCount() == 0) {
            View.inflate(MyApplication.getInstance(), getContentResId(), contentContainer);
        }
    }

    private void initHeadView() {
        headUIHelper = new HeadUIHelper();
        headUIHelper.initHead(getWindow().getDecorView(), this);
        headUIHelper.setMiddleText(getMiddleTitle());
    }

    // -------- 实现HeadUIHelper.EvenObserver ------------------------------

    @Override
    public void onClickLeft(ViewGroup head) {
        finish();
    }

    @Override
    public void onClicRight(ViewGroup head) {

    }

    // -------- 实现View.OnClickListener------------------------------------

    @Override
    public void onClick(View v) {

    }

    abstract public String getMiddleTitle();

    abstract protected int getContentResId();
}
