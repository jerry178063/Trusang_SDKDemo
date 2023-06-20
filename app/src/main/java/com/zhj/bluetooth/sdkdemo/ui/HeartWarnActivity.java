package com.zhj.bluetooth.sdkdemo.ui;

import android.widget.EditText;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.views.ItemToggleLayout;
import com.zhj.zhjsdkcustomized.bean.HeartRateInterval;
import com.zhj.zhjsdkcustomized.bean.HeartRateIntervalGJ;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class HeartWarnActivity extends BaseActivity {
    @BindView(R.id.itHeartWarn)
    ItemToggleLayout itHeartWarn;
    @BindView(R.id.etVauleHrMax)
    EditText etVauleHrMax;
    @BindView(R.id.etVauleHrMin)
    EditText etVauleHrMin;
    @BindView(R.id.itPressWarn)
    ItemToggleLayout itPressWarn;
    @BindView(R.id.etVauleFzMax)
    EditText etVauleFzMax;
    @BindView(R.id.etVauleFzMin)
    EditText etVauleFzMin;
    @BindView(R.id.etVauleSsMax)
    EditText etVauleSsMax;
    @BindView(R.id.etVauleSsMin)
    EditText etVauleSsMin;
    @BindView(R.id.itOxygenWarn)
    ItemToggleLayout itOxygenWarn;
    @BindView(R.id.etVauleOxygenMin)
    EditText etVauleOxygenMin;

    @Override
    protected int getContentView() {
        return R.layout.activity_heart_warn;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.heart_warm_title));
    }
}
