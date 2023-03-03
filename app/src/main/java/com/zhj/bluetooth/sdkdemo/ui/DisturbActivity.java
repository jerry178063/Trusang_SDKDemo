package com.zhj.bluetooth.sdkdemo.ui;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DISTURB;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_DISTURB;

import android.app.TimePickerDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.TextView;


import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.util.CustomToggleButton;
import com.zhj.zhjsdkcustomized.bean.SleepBean;
import com.zhj.zhjsdkcustomized.bean.SleepBeans;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.ByteDataConvertUtil;
import com.zhj.zhjsdkcustomized.ble.CmdHelper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.BaseCmdUtil;
import com.zhj.zhjsdkcustomized.util.LogUtil;
import com.zhj.zhjsdkcustomized.util.StringUtils;

import butterknife.BindView;


public class DisturbActivity extends BaseActivity {
    @BindView(R.id.mToggleButton)
    CustomToggleButton mToggleButton;

    @BindView(R.id.tvRemindStartTime)
    TextView tvRemindStartTime;
    @BindView(R.id.tvRemindEndTime)
    TextView tvRemindEndTime;
    @Override
    protected int getContentView() {
        return R.layout.activity_disturb;
    }
    private SleepBean sleepBean;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.do_not_disturb_mode));
        rightText.setText(getResources().getString(R.string.scan_device_set));
        rightText.setOnClickListener(v -> {
            sleepBean.setSwitch(mToggleButton.getSwitchState());
            sleepBean.setStartHour(10);
            sleepBean.setStartMin(10);
            sleepBean.setEndHour(15);
            sleepBean.setEndMin(15);
            BleSdkWrapper.setDisturbSetting(sleepBean, new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    if(handlerBleDataResult.bluetooth_code == CODE_SET_DISTURB) {
                        showToast(getResources().getString(R.string.toast_set_success));
                        DisturbActivity.this.finish();
                    }
                }

                @Override
                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                }

                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        });

        BleSdkWrapper.getDisturbSetting(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_DISTURB) {
                    SleepBeans sleepBeans = (SleepBeans) handlerBleDataResult.data;
                    sleepBean = sleepBeans.getSleepBeans().get(0);
                    mToggleButton.setSwitchState(sleepBean.isSwitch());
                    tvRemindStartTime.setText(StringUtils.format("%02d", sleepBean.getStartHour()) + ":" + StringUtils.format("%02d", sleepBean.getStartMin()));
                    tvRemindEndTime.setText(StringUtils.format("%02d", sleepBean.getEndHour()) + ":" + StringUtils.format("%02d", sleepBean.getEndMin()));
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

}
