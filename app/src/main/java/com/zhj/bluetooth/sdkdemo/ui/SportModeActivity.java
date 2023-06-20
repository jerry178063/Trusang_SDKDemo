package com.zhj.bluetooth.sdkdemo.ui;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_SPORT_MODE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_SPORT_MODE;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.ui.adapter.SportModeAdapter;
import com.zhj.zhjsdkcustomized.bean.SportModeBean;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class SportModeActivity extends BaseActivity implements SportModeAdapter.OnStatusClickListener {

    @BindView(R.id.mRecyclerViewShow)
    RecyclerView mRecyclerViewShow;
    @BindView(R.id.mRecyclerViewHide)
    RecyclerView mRecyclerViewHide;
    private List<SportModeBean> openSport = new ArrayList<>();
    private List<SportModeBean> closeSport = new ArrayList<>();
    private SportModeAdapter showAdapter;
    private SportModeAdapter hideAdapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_sport_mode;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.sport_mode));
        mRecyclerViewShow.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewHide.setLayoutManager(new LinearLayoutManager(this));
        rightText.setText(getResources().getString(R.string.scan_device_set));
        mRecyclerViewShow.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewHide.setLayoutManager(new LinearLayoutManager(this));
        rightText.setOnClickListener(v -> {
            List<SportModeBean> sportModeBeans = new ArrayList<>();
            sportModeBeans.addAll(openSport);
            sportModeBeans.addAll(closeSport);
            BleSdkWrapper.setSportMode(sportModeBeans, new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    if(handlerBleDataResult.bluetooth_code == CODE_SET_SPORT_MODE) {
                        showToast("set success");
                        SportModeActivity.this.finish();
                    }
                }

                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        });
        BleSdkWrapper.getSportMode(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_SPORT_MODE) {
                    List<SportModeBean> sportModeBeans = (List<SportModeBean>) handlerBleDataResult.data;
                    for (SportModeBean sportModeBean : sportModeBeans) {
                        if (sportModeBean.isSportStaus()) {
                            openSport.add(sportModeBean);
                        } else {
                            closeSport.add(sportModeBean);
                        }
                    }
                    showAdapter = new SportModeAdapter(SportModeActivity.this, openSport);
                    mRecyclerViewShow.setAdapter(showAdapter);
                    showAdapter.setmClickListener(SportModeActivity.this);
                    hideAdapter = new SportModeAdapter(SportModeActivity.this, closeSport);
                    mRecyclerViewHide.setAdapter(hideAdapter);
                    hideAdapter.setmClickListener(SportModeActivity.this);
                    Log.d("ffe232","openSport:" + openSport.size());
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }



    @Override
    public void onStatusClick(View view, int position, boolean status) {
        if(status){
            if(openSport.size() <= 1){
                showToast(getResources().getString(R.string.keep_at_least_displayed_motion_mode));
            }else{
                SportModeBean sportModeBean = openSport.get(position);
                sportModeBean.setSportStaus(false);
                openSport.remove(position);
                showAdapter.notifyDataSetChanged();
                hideAdapter.addItem(0,sportModeBean);
            }
        }else{
            if(openSport.size() >= 4){
                showToast(getResources().getString(R.string.keep_up_four_displayed_modes));
            }else{
                SportModeBean sportModeBean = closeSport.get(position);
                sportModeBean.setSportStaus(true);
                closeSport.remove(position);
                hideAdapter.notifyDataSetChanged();
                showAdapter.addItem(openSport.size(),sportModeBean);
                showAdapter.notifyDataSetChanged();
            }
        }
    }
}
