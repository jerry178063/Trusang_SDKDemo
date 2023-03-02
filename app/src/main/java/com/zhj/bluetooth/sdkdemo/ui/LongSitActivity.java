package com.zhj.bluetooth.sdkdemo.ui;

import android.view.View;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.bean.LongSit;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.CmdHelper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class LongSitActivity extends BaseActivity {

    @BindView(R.id.tvOpenState)
    TextView tvOpenState;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.tvRepeti)
    TextView tvRepeti;
    @BindView(R.id.tvInterval)
    TextView tvInterval;
    @Override
    protected int getContentView() {
        return R.layout.activity_long_sit;
    }
    private LongSit longSit;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.sendentary_info_title));

    }
}
