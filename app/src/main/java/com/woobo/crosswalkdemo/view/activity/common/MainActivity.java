package com.woobo.crosswalkdemo.view.activity.common;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.config.H5GameUrlConfig;
import com.woobo.crosswalkdemo.view.activity.crosswalk.CrossWebviewActivity;
import com.woobo.crosswalkdemo.view.activity.system.SysWebviewActivity;
import com.woobo.crosswalkdemo.view.activity.tbs.TbsWebviewActivity;

/**
 * @author sanji
 * 程序主界面（也是入口界面）
 */
public class MainActivity extends BaseActivity implements
        RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    // 表示原生webview
    private static final int WB_NATIVE = 0;

    // 表示crosswalk webview
    private static final int WB_CROSSWALK = 1;

    // 表示tbs webview
    private static final int WB_TBS = 2;

    // 标记当前应该显示的webview策略，默认为原生native
    private static int WB_FLAG = WB_NATIVE;

    private RadioGroup webview_group;
    private RadioGroup game_group;
    private Button confirm_bn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewObject();
        register();
    }

    private void initViewObject() {
        webview_group = findViewById(R.id.webview_group);
        game_group = findViewById(R.id.game_group);
        confirm_bn = findViewById(R.id.confirm_bn);
    }

    private void register() {
        webview_group.setOnCheckedChangeListener(this);
        game_group.setOnCheckedChangeListener(this);
        confirm_bn.setOnClickListener(this);
    }

    // ------- help function -----------------------------------------------

    private void switchWebview() {
        switch (WB_FLAG) {
            case WB_NATIVE:
                goSysWebview();
                break;
            case WB_CROSSWALK:
                goCrosswalkWebview();
                break;
            case WB_TBS:
                goTbsWebview();
                break;
        }
    }

    /**
     * 进入原生webview测试界面
     */
    public void goSysWebview() {
        SysWebviewActivity.start(this);
    }

    /**
     * 进入crosswalk webview测试界面
     */
    public void goCrosswalkWebview() {
        CrossWebviewActivity.start(this);
    }

    /**
     * 进入tbs webview测试界面
     */
    public void goTbsWebview() {
        TbsWebviewActivity.start(this);
    }

    // -------- 实现RadioGroup.OnCheckedChangeListener ---------------------

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.native_wb: // 选择原生webview
                    WB_FLAG = WB_NATIVE;
                    break;
                case R.id.cw_wb: // 选择crosswalk webview
                    WB_FLAG = WB_CROSSWALK;
                    break;
                case R.id.tbs_wb: // 选择腾讯TBS webview
                    WB_FLAG = WB_TBS;
                    break;
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
    }

    // -------- 实现View.OnClickListener -------------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_bn: // 确认选择，并进入webview界面
                switchWebview();
                break;
        }
    }
}