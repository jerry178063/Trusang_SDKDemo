package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.bean.DeviceState;
import com.zhj.zhjsdkcustomized.bean.Goal;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class GoalActivity extends BaseActivity {

    @BindView(R.id.tvSleepTimeState)
    TextView tvSleepTimeState;
    @BindView(R.id.tvSleepTime)
    TextView tvSleepTime;
    @BindView(R.id.tvStepsState)
    TextView tvStepsState;
    @BindView(R.id.tvSteps)
    TextView tvSteps;
    @BindView(R.id.tvCalState)
    TextView tvCalState;
    @BindView(R.id.tvCal)
    TextView tvCal;
    @BindView(R.id.tvDistanceState)
    TextView tvDistanceState;
    @BindView(R.id.tvDistance)
    TextView tvDistance;

    @Override
    protected int getContentView() {
        return R.layout.activity_goal;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.target_info_title));
        BleSdkWrapper.getTarget(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                Goal goal = (Goal) handlerBleDataResult.data;
                tvSleepTimeState.setText(getResources().getString(R.string.target_info_sleep_swith)+goal.sleepstate);//0x01: ON 0x00: OFF
                tvSleepTime.setText(getResources().getString(R.string.target_info_sleep_time)+goal.goalSleep);//Unit hour
                tvStepsState.setText(getResources().getString(R.string.target_info_steps_swith)+goal.stepstate);//0x01: ON 0x00: OFF
                tvSteps.setText(getResources().getString(R.string.target_info_steps)+goal.goalStep);//Unit step
                tvCalState.setText(getResources().getString(R.string.target_info_cal_swith)+goal.calstate);//0x01: ON 0x00: OFF
                tvCal.setText(getResources().getString(R.string.target_info_cal)+goal.goalCal);//Unit kilocalorie
                tvDistanceState.setText(getResources().getString(R.string.target_info_distance_swith)+goal.distancestate);//0x01: ON 0x00: OFF
                tvDistance.setText(getResources().getString(R.string.target_info_distance)+goal.goalDistanceKm+"KM");//Unit kilometer
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
    }
}
