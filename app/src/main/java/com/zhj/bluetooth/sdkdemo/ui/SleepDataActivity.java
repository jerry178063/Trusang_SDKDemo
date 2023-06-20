package com.zhj.bluetooth.sdkdemo.ui;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.ui.adapter.SleepsHistoryAdapter;
import com.zhj.bluetooth.sdkdemo.ui.adapter.StepsHistoryAdapter;
import com.zhj.zhjsdkcustomized.bean.HealthActivity;
import com.zhj.zhjsdkcustomized.bean.HealthHeartRateItem;
import com.zhj.zhjsdkcustomized.bean.HealthSleepItem;
import com.zhj.zhjsdkcustomized.bean.HealthSportItem;
import com.zhj.zhjsdkcustomized.ble.BaseDataHandler;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.LogUtil;
import com.zhj.zhjsdkcustomized.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SleepDataActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_sleep;
    }

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.step_and_sleep_history_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        syncStepHistory(true);
    }
    List<HealthSportItem> sportItemsAll = new ArrayList<>();//Historical step data
    List<HealthSleepItem> sleepItemsAll = new ArrayList<>();//History sleep
    Calendar calendar;
    StepsHistoryAdapter stepsHistoryAdapter;
    SleepsHistoryAdapter sleepsHistoryAdapter;

    //Historical sleep data (calorie data is normally calculated)
    private void syncStepHistory(boolean isFirst){
        if (isFirst) {
            calendar = Calendar.getInstance();
        }
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH)+1;
        final int day=calendar.get(Calendar.DATE);
        BleSdkWrapper.getStepOrSleepHistory(year, month, day, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.isComplete){
                    if (handlerBleDataResult.hasNext){//是否还有更多的历史数据
                        List<HealthSportItem> sportItems = (List<HealthSportItem>) handlerBleDataResult.data;//历史步数数据
                        List<HealthSleepItem> sleepItems = handlerBleDataResult.sleepItems;//历史睡眠数数据
                        sportItemsAll.addAll(sportItems);
                        sleepItemsAll.addAll(sleepItems);
                        LogUtil.d("sportItems:"+sportItems.size()+",");
                        Log.d("fd333","sportItems:"+sportItems.size()+"," + "sleepItems:" + sleepItems.size());
                        calendar.add(Calendar.DATE,-1);
                        stepsHistoryAdapter = new StepsHistoryAdapter(SleepDataActivity.this,sportItemsAll);
                        mRecyclerView.setAdapter(stepsHistoryAdapter);
                        sleepsHistoryAdapter = new SleepsHistoryAdapter(SleepDataActivity.this,sleepItemsAll);
//                        syncStepHistory(false);
                    }else{
                        if(sportItemsAll != null){
                            stepsHistoryAdapter = new StepsHistoryAdapter(SleepDataActivity.this,sportItemsAll);
                            mRecyclerView.setAdapter(stepsHistoryAdapter);
                        }
                        if(sleepItemsAll != null){
                            sleepsHistoryAdapter = new SleepsHistoryAdapter(SleepDataActivity.this,sleepItemsAll);
                        }
                    }
                    ToastUtil.showToast(SleepDataActivity.this,"sportItemsAll:"+sportItemsAll.size()+"," + "sleepItemsAll:" + sleepItemsAll.size());
                    Log.d("fd333","sportItemsAll:"+sportItemsAll.size()+"," + "sleepItemsAll:" + sleepItemsAll.size());
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
    }

    @OnClick(R.id.tvShowSteps)
    void showHistorySteps(){
        if(stepsHistoryAdapter != null){
            mRecyclerView.setAdapter(stepsHistoryAdapter);
        }else{
            showToast(getResources().getString(R.string.step_history_no_data));
        }
    }

    @OnClick(R.id.tvShowSleeps)
    void showHistorySleeps(){
        if(sleepsHistoryAdapter != null){
            mRecyclerView.setAdapter(sleepsHistoryAdapter);
        }else{
            showToast(getResources().getString(R.string.sleep_history_no_data));
        }
    }

}
