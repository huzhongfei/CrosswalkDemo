package com.woobo.crosswalkdemo.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.app.MyApplication;
import com.woobo.crosswalkdemo.common.config.H5GameUrlConfig;

/**
 * @author sanji
 */
public class H5GameSelectPop extends PopupWindow implements RadioGroup.OnCheckedChangeListener {

    private final View view;
    private final RadioGroup game_group;
    private final EventObserver eventObserver;

    public H5GameSelectPop(Activity activity, EventObserver eventObserver) {
        this.eventObserver = eventObserver;
        LayoutInflater inflater = (LayoutInflater) MyApplication.getInstance().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_window_h5game_select, null);
        int h = activity.getWindowManager().getDefaultDisplay().getHeight();
        int w = activity.getWindowManager().getDefaultDisplay().getWidth();
        // 设置View
        this.setContentView(view);
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
        game_group = view.findViewById(R.id.game_group);
        game_group.setOnCheckedChangeListener(this);
    }

    // -------- 实现RadioGroup.OnCheckedChangeListener -----------------------------

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.game_mly: // 选择超级玛丽大逃跑游戏
                H5GameUrlConfig.H5_GAME = H5GameUrlConfig.MALIYA;
                break;
            case R.id.game_xhzxs: // 选择小盒子下山游戏
                H5GameUrlConfig.H5_GAME = H5GameUrlConfig.HEZIXIAS;
                break;
            case R.id.game_3dyyqc: // 选择3D汽车越野游戏
                H5GameUrlConfig.H5_GAME = H5GameUrlConfig.TDYUEYE;
                break;
            case R.id.game_kzqckp: // 选择空中球场酷跑游戏
                H5GameUrlConfig.H5_GAME = H5GameUrlConfig.QCKP;
                break;
        }
        if (null != eventObserver) eventObserver.onH5GameUpdate(true);
        dismiss();
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

    public interface EventObserver {
        void onH5GameUpdate(boolean isUpdate);
    }
}
