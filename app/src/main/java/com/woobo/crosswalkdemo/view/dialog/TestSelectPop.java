package com.woobo.crosswalkdemo.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.app.AppManager;
import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.view.activity.tests.AWSTestActivity;
import com.woobo.crosswalkdemo.view.activity.tests.H5GameActivity;
import com.woobo.crosswalkdemo.view.activity.tests.H5GameForCrosswalkActivity;
import com.woobo.crosswalkdemo.view.activity.tests.H5GameX5WbActivity;
import com.woobo.crosswalkdemo.view.activity.tests.JsAndNativeActivity;
import com.woobo.crosswalkdemo.view.adapter.TestSelectAdapter;

/**
 * @author sanji
 */
public class TestSelectPop extends PopupWindow implements TestSelectAdapter.OnItemClickListener {

    private static final String[] testItems = {"JS And Native", "AWS TEST", "H5GAME Fruits", "H5GAME Fruits FOR CROSSW", "H5GAME Fruits FOR X5"};
    private final View view;
    private RecyclerView recyclerview;
    private TestSelectAdapter testSelectAdapter;

    public TestSelectPop(Activity activity) {

        LayoutInflater inflater = (LayoutInflater) MyApplication.getInstance().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_window_test_select, null);

        this.setContentView(view);
        setWindow(activity);
        initView();
    }

    private void initView() {
        recyclerview = view.findViewById(R.id.recyclerview);
        setTestList();
    }

    private void setTestList() {
        testSelectAdapter = new TestSelectAdapter(testItems);
        testSelectAdapter.setOnItemClickListener(this);
        LinearLayoutManager ll = new LinearLayoutManager(MyApplication.getInstance());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(ll);
        recyclerview.setAdapter(testSelectAdapter);
    }

    private void setWindow(Activity activity) {
        int h = activity.getWindowManager().getDefaultDisplay().getHeight();
        int w = activity.getWindowManager().getDefaultDisplay().getWidth();
        // 设置弹出窗体的宽
        this.setWidth(w);
        // 设置弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        //设置非PopupWindow区域是否可触摸
        this.setOutsideTouchable(true);
        // 刷新状态
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.update();
    }

    /**
     * 显示popupWindow
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing() && null != parent) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    // --------实现TestSelectAdapter.OnItemClickListener ------------------------

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                JsAndNativeActivity.start(AppManager.getInstance().currentActivity());
                break;
            case 1:
                AWSTestActivity.start(AppManager.getInstance().currentActivity());
                break;
            case 2:
                H5GameActivity.start(AppManager.getInstance().currentActivity());
                break;
            case 3:
                H5GameForCrosswalkActivity.start(AppManager.getInstance().currentActivity());
                break;
            case 4:
                H5GameX5WbActivity.start(AppManager.getInstance().currentActivity());
                break;
        }
    }
}
