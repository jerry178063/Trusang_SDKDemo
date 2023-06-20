package com.zhj.bluetooth.sdkdemo.ui;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_TEMPWARING;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_TEMPWARING;

import android.view.View;
import android.widget.EditText;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.views.ItemToggleLayout;
import com.zhj.zhjsdkcustomized.bean.HeartRateInterval;
import com.zhj.zhjsdkcustomized.bean.TempWaring;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class TempWaringActivity extends BaseActivity {
    @BindView(R.id.itTempWarn)
    ItemToggleLayout itTempWarn;
    @BindView(R.id.etVaule)
    EditText etVaule;

    @Override
    protected int getContentView() {
        return R.layout.activity_temp_waring;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_temp_waring));
        //Get temperature alarm switch
        rightText.setText(getResources().getString(R.string.temp_warm_set));
        //获取体温报警开关
        BleSdkWrapper.getTempWaring(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_TEMPWARING) {
                    TempWaring interval = (TempWaring) handlerBleDataResult.data;
                    itTempWarn.setOpen(interval.isCustomTemp);
                    etVaule.setText(String.valueOf(interval.maxTemp));
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
        rightText.setOnClickListener(v -> BleSdkWrapper.setTempWaring(itTempWarn.isOpen(),Integer.parseInt(etVaule.getText().toString()), new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_SET_TEMPWARING) {
                    showToast(getResources().getString(R.string.toast_set_success));
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        }));
    }
}
