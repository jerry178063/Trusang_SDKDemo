package com.zhj.bluetooth.sdkdemo.ui;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_USER_INFO;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_USER_INFO;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.View;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.bean.DeviceState;
import com.zhj.zhjsdkcustomized.bean.UserBean;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvHight)
    TextView tvHight;
    @BindView(R.id.tvWeight)
    TextView tvWeight;
    @BindView(R.id.tvStepDistance)
    TextView tvStepDistance;
    @Override
    protected int getContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.user_info_title));
        BleSdkWrapper.getUserInfo(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_USER_INFO) {
                    UserBean userBean = (UserBean) handlerBleDataResult.data;
                    //0x00: male 0x01: female 0x02: others
                    tvSex.setText(getResources().getString(R.string.user_info_sex) + userBean.getGender());
                    tvAge.setText(getResources().getString(R.string.user_info_age) + userBean.getAge());
                    tvHight.setText(getResources().getString(R.string.user_info_height) + userBean.getHeight()); //Unit CM
                    tvWeight.setText(getResources().getString(R.string.user_info_weight) + userBean.getWeight());//The unit is 0.1KG. If 600 is returned, the corresponding KG * 10 is also required when 60KG is set
                    tvStepDistance.setText(getResources().getString(R.string.user_info_stride) + userBean.getStepDistance());//Unit CM
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        rightText.setText(getResources().getString(R.string.preservation));
        rightText.setOnClickListener(view -> {
            UserBean userBean = new UserBean();
            userBean.setAge(25);
            userBean.setWeight(700);
            userBean.setHeight(200);
            userBean.setGender(1);
            BleSdkWrapper.setUserInfo(userBean, new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    if(handlerBleDataResult.bluetooth_code == CODE_SET_USER_INFO) {
                        showToast(getResources().getString(R.string.saving_succeeded));
                        UserInfoActivity.this.finish();
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
    }
}
