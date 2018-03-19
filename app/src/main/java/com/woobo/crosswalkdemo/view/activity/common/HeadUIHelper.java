package com.woobo.crosswalkdemo.view.activity.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woobo.crosswalkdemo.R;

/**
 * 通用标题栏的集成辅助类
 * @author sanji
 */
public class HeadUIHelper implements View.OnClickListener {

    /**
     * 标题栏头部左侧文字
     */
    private TextView leftText;

    /**
     * 标题栏头部左侧图标
     */
    private ImageView leftImage;

    /**
     * 标题栏头部左侧布局
     */
    private ViewGroup leftView;

    /**
     * 标题栏头部中间文字
     */
    private TextView middleText;

    /**
     * 标题栏头部中间布局
     */
    private ViewGroup middleView;

    /**
     * 标题栏头部右侧文字
     */
    private TextView rightText;

    /**
     * 标题栏头部右侧图标
     */
    private ImageView rightImage;

    /**
     * 标题栏头部右侧布局
     */
    private ViewGroup rightView;

    /**
     * 标题栏整体布局
     */
    private ViewGroup headGlobalLayout;

    private EvenObserver evenObserver;

    public void initHead(View parent, EvenObserver evenObserver) {
        this.evenObserver = evenObserver;
        // 这些都是该头容器的【头属性】
        leftText = (TextView) parent.findViewById(R.id.leftText);
        leftImage = (ImageView) parent.findViewById(R.id.leftImage);
        leftView = (ViewGroup) parent.findViewById(R.id.headLeftView);
        middleText = (TextView) parent.findViewById(R.id.middleText);
        middleView = (ViewGroup) parent.findViewById(R.id.headMiddleView);
        rightImage = (ImageView) parent.findViewById(R.id.rightImage);
        rightText = (TextView) parent.findViewById(R.id.rightText);
        rightView = (ViewGroup) parent.findViewById(R.id.headRightView);
        headGlobalLayout = (ViewGroup) parent.findViewById(R.id.headLayout);

        register();
    }

    private void register() {
        leftView.setOnClickListener(this);
        middleView.setOnClickListener(this);
        rightView.setOnClickListener(this);
    }

    public void setLeftText(String leftTextStr) {
        if (null == leftText) return;
        leftText.setVisibility(View.VISIBLE);
        leftText.setText(leftTextStr);
    }

    public void setMiddleText(String middleTextStr) {
        if (null == middleText) return;
        middleText.setVisibility(View.VISIBLE);
        middleText.setText(middleTextStr);
    }

    public void setRightText(String rightTextStr) {
        if (null == rightText) return;
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(rightTextStr);
    }

    public void setRightImage(int resId) {
        if (null == rightImage) return;
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setImageResource(resId);
    }

    // --------- 实现View.OnClickListener -------------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headLeftView: // 点击左侧控件
                if (null != evenObserver)
                evenObserver.onClickLeft(headGlobalLayout);
                break;
            case R.id.headRightView: // 点击右侧控件
                if (null != evenObserver)
                evenObserver.onClicRight(headGlobalLayout);
                break;
        }
    }

    public interface EvenObserver {
        void onClickLeft(ViewGroup head);
        void onClicRight(ViewGroup head);
    }
}
