package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.ui.adapter.AlarmListAdapter;
import com.zhj.zhjsdkcustomized.bean.Alarm;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import java.util.List;

import butterknife.BindView;

public class AlarmActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private AlarmListAdapter mAdapter;

    private List<Alarm> alarmList;
    @Override
    protected int getContentView() {
        return R.layout.activity_alarm;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.alarm_info_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Get the alarm clock set in the bracelet
        BleSdkWrapper.getAlarmList(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.isComplete){
                    alarmList = (List<Alarm>) handlerBleDataResult.data;
                    mAdapter = new AlarmListAdapter(AlarmActivity.this,alarmList);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
        //Set a new alarm clock to modify the alarmList
//        BleSdkWrapper.setAlarm(alarmList, new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
    }
}
