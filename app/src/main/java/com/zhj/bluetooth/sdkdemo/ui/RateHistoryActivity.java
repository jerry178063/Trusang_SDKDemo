package com.zhj.bluetooth.sdkdemo.ui;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_HISTROY_SENSOR;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.ui.adapter.RateHistoryAdapter;
import com.zhj.zhjsdkcustomized.bean.HealthActivity;
import com.zhj.zhjsdkcustomized.bean.HealthHeartRateItem;
import com.zhj.zhjsdkcustomized.bean.SensorHistoryBean;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class RateHistoryActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Override
    protected int getContentView() {
        return R.layout.activity_rate_history;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.heart_history_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        syncHeartHistory(true);

        BleSdkWrapper.getHistroySensor(2023, 3, 10, 1, 10, 10, 5260, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == CODE_GET_HISTROY_SENSOR) {
                    SensorHistoryBean sensorHistoryBean = (SensorHistoryBean) handlerBleDataResult.data;
                    Log.d(TAG, "sensorHistoryBean:" + new Gson().toJson(sensorHistoryBean));
                    List<SensorHistoryBean.HeartList> samplingList = sensorHistoryBean.getHeartList();
                    Log.d(TAG, "samplingList_size:" + samplingList.size());
                }
            }

            @Override
            public void onFailed(WriteBleException e) {
                Log.d(TAG, "e:" + e);
            }
        });

    }
    List<HealthHeartRateItem> healthHeartRateItemsAll = new ArrayList<>();
    Calendar calendar;
    //Historical heart rate data
    private void syncHeartHistory(boolean isFirst){
        if (isFirst) {
            calendar = Calendar.getInstance();
        }
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH)+1;
        final int day=calendar.get(Calendar.DATE);
    }
}
