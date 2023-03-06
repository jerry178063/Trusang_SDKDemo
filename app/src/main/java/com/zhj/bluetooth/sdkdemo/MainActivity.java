package com.zhj.bluetooth.sdkdemo;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_STATE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_NOTICE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_REALTIME_HEARTRATE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SEND_MESSAGE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SEND_MESSAGE_P;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_DEVICE_DATE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_DEVICE_SHOCK;
import static com.zhj.zhjsdkcustomized.ble.CmdHelper.CMD_GET_DEVICE_STATE;
import static com.zhj.zhjsdkcustomized.ble.bluetooth.BleManager.FLAT_START_GET_HEART_RATE;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.config.ForegroundNotification;
import com.zhj.bluetooth.sdkdemo.config.ForegroundNotificationClickListener;
import com.zhj.bluetooth.sdkdemo.sevice.ServiceWorker;
import com.zhj.bluetooth.sdkdemo.ui.AlarmActivity;
import com.zhj.bluetooth.sdkdemo.ui.DatumLineActivity;
import com.zhj.bluetooth.sdkdemo.ui.DeviceInfoActivity;
import com.zhj.bluetooth.sdkdemo.ui.DeviceStateActivity;
import com.zhj.bluetooth.sdkdemo.ui.DialCenterActivity;
import com.zhj.bluetooth.sdkdemo.ui.DisturbActivity;
import com.zhj.bluetooth.sdkdemo.ui.DrinkWaterActivity;
import com.zhj.bluetooth.sdkdemo.ui.FindPhoneActivity;
import com.zhj.bluetooth.sdkdemo.ui.FirmwareUpdateActivity;
import com.zhj.bluetooth.sdkdemo.ui.FirmwareUpdateOtaActivity;
import com.zhj.bluetooth.sdkdemo.ui.GoalActivity;
import com.zhj.bluetooth.sdkdemo.ui.HeartTestActivity;
import com.zhj.bluetooth.sdkdemo.ui.HeartWarnActivity;
import com.zhj.bluetooth.sdkdemo.ui.LongSitActivity;
import com.zhj.bluetooth.sdkdemo.ui.MessageContreActivity;
import com.zhj.bluetooth.sdkdemo.ui.NoticeActivity;
import com.zhj.bluetooth.sdkdemo.ui.NotificationActivity;
import com.zhj.bluetooth.sdkdemo.ui.RateHistoryActivity;
import com.zhj.bluetooth.sdkdemo.ui.RestingCalorieActivity;
import com.zhj.bluetooth.sdkdemo.ui.ScanDeviceReadyActivity;
import com.zhj.bluetooth.sdkdemo.ui.SportActivity;
import com.zhj.bluetooth.sdkdemo.ui.SportModeActivity;
import com.zhj.bluetooth.sdkdemo.ui.StepsDataActivity;
import com.zhj.bluetooth.sdkdemo.ui.TelephoAvtivity;
import com.zhj.bluetooth.sdkdemo.ui.TempDayDataActivity;
import com.zhj.bluetooth.sdkdemo.ui.TempHistoryActivity;
import com.zhj.bluetooth.sdkdemo.ui.TempTestActivity;
import com.zhj.bluetooth.sdkdemo.ui.TempWaringActivity;
import com.zhj.bluetooth.sdkdemo.ui.Test;
import com.zhj.bluetooth.sdkdemo.ui.UserInfoActivity;
import com.zhj.bluetooth.sdkdemo.ui.WeatherActivity;
import com.zhj.bluetooth.sdkdemo.util.IntentUtil;
import com.zhj.bluetooth.sdkdemo.util.PermissionUtil;
import com.zhj.zhjsdkcustomized.bean.AppNotice;
import com.zhj.zhjsdkcustomized.bean.BLEDevice;
import com.zhj.zhjsdkcustomized.bean.EcgHistoryData;
import com.zhj.zhjsdkcustomized.bean.HealthHeartRate;
import com.zhj.zhjsdkcustomized.bean.WarningInfo;
import com.zhj.zhjsdkcustomized.ble.BleCallbackWrapper;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.ByteDataConvertUtil;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.HealthHrDataHandler;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;
import com.zhj.zhjsdkcustomized.ble.bluetooth.DeviceCallback;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeConnectListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.ConnBleException;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.BaseCmdUtil;
import com.zhj.zhjsdkcustomized.util.Constants;
import com.zhj.zhjsdkcustomized.util.LogUtil;
import com.zhj.zhjsdkcustomized.util.SPHelper;
import com.zhj.zhjsdkcustomized.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements PermissionUtil.RequsetResult {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMac)
    TextView tvMac;
    private OneTimeWorkRequest mRequest;
    private Dialog mDialog;
    private String[] permissionsMsg = new String[]{Manifest.permission.READ_SMS};
    private String[] permissionsCon = new String[]{Manifest.permission.READ_CONTACTS};
    private String[] permissionsCall;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_home_page));
        BluetoothLe.getDefault().init(this, new BleCallbackWrapper() {

            @Override
            public void complete(int resultCode, Object data) {
                if (resultCode == Constants.BLE_RESULT_CODE.SUCCESS) {
                    runOnUiThread(() -> checkBind());
                    BluetoothLe.getDefault().setOnConnectListener(MyListenner);
                    //reconnect
                    BluetoothLe.getDefault().reconnect(MainActivity.this);
                    BluetoothLe.getDefault().addDeviceCallback(new DeviceCallback() {
                        @Override
                        public void enterCamare() {
                            LogUtil.d("Go to take photos");
                        }

                        @Override
                        public void camare() {
                            LogUtil.d("photograph");
                        }

                        @Override
                        public void exitCamare() {
                            LogUtil.d("Exit taking pictures");
                        }

                        @Override
                        public void musicControl() {
                        }

                        @Override
                        public void preMusic() {
                            LogUtil.d("Previous song");
                        }

                        @Override
                        public void nextMusic() {
                            LogUtil.d("Next song");
                        }

                        @Override
                        public void exitMusic() {
                            LogUtil.d("Exit music");
                        }

                        @Override
                        public void addVol() {
                            LogUtil.d("Volume increase");
                        }

                        @Override
                        public void subVol() {
                            LogUtil.d("Vol");
                        }

                        @Override
                        public void findPhone() {
                            LogUtil.d("Go to find mobile phone");
                        }

                        @Override
                        public void answerRingingCall() {
                        }

                        @Override
                        public void endRingingCall() {
                        }

                        @Override
                        public void sos() {
                        }

                        @Override
                        public void WarningInfo(WarningInfo warningInfo) {
                            LogUtil.d("Get alarm information");
                            LogUtil.d("Maximum value of heart rate alarm:" + warningInfo.getWarningHrMax());
                            LogUtil.d("Minimum value of heart rate alarm:" + warningInfo.getWarningHrMin());
                            LogUtil.d("Maximum value of diastolic blood pressure alarm:" + warningInfo.getWarningFzMax());
                            LogUtil.d("Minimum value of diastolic blood pressure alarm:" + warningInfo.getWarningFzMin());
                            LogUtil.d("Maximum systolic pressure alarm value:" + warningInfo.getWarningSsMax());
                            LogUtil.d("Minimum systolic pressure alarm value:" + warningInfo.getWarningSsMin());
                            LogUtil.d("Minimum value of blood oxygen alarm:" + warningInfo.getWarningOxygenMin());
                            LogUtil.d("Minimum temperature alarm value:" + warningInfo.getWarningTemp());
                        }
                    });
                } else {
                    LogUtil.d("SDK cannot be used");
                }
            }

            @Override
            public void setSuccess() {

            }
        });
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        mRequest = new OneTimeWorkRequest.Builder(ServiceWorker.class).setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(mRequest);


        //Call before sending a message
        BleSdkWrapper.getDeviceState(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_DEVICE_STATE){
                    //You can close a message channel in the NoticeActivity page
                    BleSdkWrapper.getNotice(new OnLeWriteCharacteristicListener() {
                        @Override
                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                            if(handlerBleDataResult.bluetooth_code == CODE_GET_NOTICE) {
                                Log.d("FFFfg4thf44","notice:" + handlerBleDataResult.data);
                                AppNotice appNotice = (AppNotice) handlerBleDataResult.data;
                            }
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

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });

    }

    private byte completeCheckCode(byte[] bytes) {
        byte sum = 0;
        for (byte b : bytes) {
            sum += b;
        }
        return (byte) (sum * 0x56 + 0x5A);
    }

    private OnLeConnectListener MyListenner = new OnLeConnectListener() {
        @Override
        public void onDeviceConnecting() {
            //Linking
        }

        @Override
        public void onDeviceConnected() {
            //Connection succeeded
        }

        @Override
        public void onDeviceDisconnected() {
            //Connection interrupted
            MyAppcation.getInstance().setConnected(false);
            BluetoothLe.getDefault().reconnect(MainActivity.this);
            checkBind();
            LogUtil.d("duankailianjie");
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt) {
            //Discover services and communicate
//            BluetoothLe.getDefault().enableNotification(true, Device.SERVICE_UUID,
//                    CHARACTERISTIC_UUID_WRITE);
            BluetoothLe.getDefault().setScanFaildCount(0);
            MyAppcation.getInstance().setConnected(true);
            //Clear Bluetooth cache
            BluetoothLe.getDefault().clearDeviceCache();
            checkBind();
        }

        @Override
        public void onDeviceConnectFail(ConnBleException e) {
            //Connection exception
            MyAppcation.getInstance().setConnected(false);
            BluetoothLe.getDefault().reconnect(MainActivity.this);
        }
    };
    private final int RESULT_CODE_BIND = 1000;

    private void checkBind() {
        if (SPHelper.getBindBLEDevice(this) != null) {
            rightText.setText(getResources().getString(R.string.main_unbind));
            if (MyAppcation.getInstance().isConnected()) {
                tvTitle.setText(getResources().getString(R.string.main_bind_device_connecr));
            } else {
                tvTitle.setText(getResources().getString(R.string.main_bind_device_unconnected));
            }
            BLEDevice bleDevice = SPHelper.getBindBLEDevice(this);
            tvMac.setText(getResources().getString(R.string.main_mac_address) + bleDevice.mDeviceAddress);
        } else {
            rightText.setText(getResources().getString(R.string.main_pairing));
            tvTitle.setText(getResources().getString(R.string.main_no_pairing));
            tvMac.setText("");
        }
        rightText.setOnClickListener(view -> {
            if (SPHelper.getBindBLEDevice(this) != null) {
                try {
                    BluetoothLe.getDefault().unBind(this);
                }catch (Exception e){

                }
                MyAppcation.getInstance().setConnected(false);
                tvTitle.setText(getResources().getString(R.string.main_no_pairing));
                tvMac.setText("");
                rightText.setText(getResources().getString(R.string.main_pairing));
            } else {
                if (BluetoothLe.getDefault().isBluetoothOpen()) {
                    IntentUtil.goToActivityForResult(MainActivity.this, ScanDeviceReadyActivity.class, RESULT_CODE_BIND);
                } else {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
//                        return;
                    }
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @OnClick(R.id.btnSendPairing)
    void toSendPairing(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, Test.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    @OnClick(R.id.btnGetSleep)
    void togGetSleep(){


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File outFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/000/" + System.currentTimeMillis() + ".jpg");
//        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", outFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        startActivity(intent);


        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(intent);

//        if(MyAppcation.getInstance().isConnected()){
//            IntentUtil.goToActivity(this, SleepDataActivity.class);
//        }else{
//            showToast(getResources().getString(R.string.main_device_unconnected));
//        }
    }


    @OnClick(R.id.btnGetSteps)
    void toGetSteps(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, StepsDataActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnSetHeartTest)
    void toSetHeartTest(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, HeartTestActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnReset)
    void resetDevice(){
        if(MyAppcation.getInstance().isConnected()){
            BleSdkWrapper.recoverSet(new OnLeWriteCharacteristicListener() {

                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {

                }

                @Override
                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                }

                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnSendMessage)
    void toSendMessage(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, MessageContreActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnDeviceInfo)
    void getDeviceInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DeviceInfoActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnDeviceState)
    void getDeviceState(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DeviceStateActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnUserInfo)
    void getUserInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, UserInfoActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnAlarmInfo)
    void getAlarmInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, AlarmActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnLongSitInfo)
    void getLongSitInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, LongSitActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnGoalInfo)
    void getGoalInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, GoalActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnNoticeInfo)
    void getNoticeInfo(){
//        if(MyAppcation.getInstance().isConnected()){
//            IntentUtil.goToActivity(this, NoticeActivity.class);
//        }else{
//            showToast(getResources().getString(R.string.main_device_unconnected));
//        }
        IntentUtil.goToActivity(this, NoticeActivity.class);
    }

    @OnClick(R.id.btnGetRateHistory)
    void getRateHistory(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, RateHistoryActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnHeartWarn)
    void toHeartWarn(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, HeartWarnActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnSportActivity)
    void toSport(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, SportActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvShowSleepsResting)
    void getRestingCal(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, RestingCalorieActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvCurrentTemp)
    void getCurrentTemp(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TempDayDataActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvHistoryTemp)
    void getHistoryTemp(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TempHistoryActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvTempTest)
    void setTempTest(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TempTestActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvDfu)
    void toFirmwareUpdate(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, FirmwareUpdateActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }


    @OnClick(R.id.tvDfuOta)
    void toFirmwareUpdateOta(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, FirmwareUpdateOtaActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvTempWaring)
    void toTempWaring(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TempWaringActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvWeather)
    void toWeather(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, WeatherActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvSenfDialCenter)
    void toSenfDialCenter(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DialCenterActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvDrinkWater)
    void toDrinkWater(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DrinkWaterActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    @OnClick(R.id.tvDisturb)
    void toDisturb(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DisturbActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    @OnClick(R.id.tvSportMode)
    void toSportMode(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, SportModeActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick({R.id.tvDatumLine})
    void toDatumLine(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DatumLineActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    @OnClick({R.id.tvTelepho})
    void tvTelepho(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TelephoAvtivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    //设置时间
    @OnClick({R.id.tvSetTime})
    void tvSetTime(){
        if(MyAppcation.getInstance().isConnected()){
            a2 = 0;
//            for (int k = 0; k < 20; k++) {
                    BleSdkWrapper.setDeviceDate(2023, 3, 3, 13, 42, 23, new OnLeWriteCharacteristicListener() {
                        @Override
                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                            if(handlerBleDataResult.bluetooth_code == CODE_SET_DEVICE_DATE) {
                                Log.d("FFFfg4thf44", "1111111:" + handlerBleDataResult.bluetooth_code);
                            }
                        }

                        @Override
                        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                        }

                        @Override
                        public void onFailed(WriteBleException e) {

                        }
                    });
//                    BleSdkWrapper.setDeviceshock(new OnLeWriteCharacteristicListener() {
//                        @Override
//                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                            if(handlerBleDataResult.bluetooth_code == CODE_SET_DEVICE_SHOCK) {
//                                Log.d("FFFfg4thf44", "222222:" + handlerBleDataResult.bluetooth_code);
////                                Log.d("FFFfg4thf44", "222222:" + a2 + " handlerBleDataResult:" + handlerBleDataResult.data);
//                                a2++;
//                            }
//                        }
//
//                        @Override
//                        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
//                            Log.d("FFFff44", "onSuccessCharac:");
//                        }
//
//                        @Override
//                        public void onFailed(WriteBleException e) {
//                            Log.d("FF45BH34", "e_veri:" + e);
//                        }
//                    });
//            }
//            BleSdkWrapper.sendMessage(1, "eee", "rrrr", new OnLeWriteCharacteristicListener() {
//                @Override
//                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                    Log.d("FFFff44","Sending succeeded");
//                }
//
//                @Override
//                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
//
//                }
//
//                @Override
//                public void onFailed(WriteBleException e) {
//
//                }
//            });
//            BleSdkWrapper.setDeviceshock(new OnLeWriteCharacteristicListener() {
//                @Override
//                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                    Log.d("FFFff44","Set vibration successfully");
//                }
//
//                @Override
//                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
//
//                }
//
//                @Override
//                public void onFailed(WriteBleException e) {
//
//                }
//            });
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    int j = 0;
    int a2 = 0;
    int a3 = 0;
    //Set vibration
    @OnClick({R.id.tvSetShock})
    void tvSetShock(){
        j = 0;
        a2 = 0;
        a3 = 0;
        for (int k = 0; k < 10; k++) {

            if (MyAppcation.getInstance().isConnected()) {
                if(k < 5) {
                    BleSdkWrapper.setDeviceshock(new OnLeWriteCharacteristicListener() {
                        @Override
                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                            if (handlerBleDataResult.bluetooth_code == CODE_SET_DEVICE_SHOCK) {


                                Log.d("FF45BH34", "Set vibration successfully:" + a2);
                                a2++;
                            }
                        }

                        @Override
                        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                            byte[] to = new byte[20];
                            BaseCmdUtil.copy(bluetoothGattCharacteristic.getValue(), to);
                            Log.d("FF45BH34", "onSuccessCharac:" + ByteDataConvertUtil.bytesToHexString(to));
                        }

                        @Override
                        public void onFailed(WriteBleException e) {
                            Log.d("FF45BH34", "e_veri:" + e);
                        }
                    });
                    BleSdkWrapper.getDeviceState(new OnLeWriteCharacteristicListener() {
                        @Override
                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                            if(handlerBleDataResult.bluetooth_code == CODE_GET_DEVICE_STATE) {
                                BLEDevice bleDevice = (BLEDevice) handlerBleDataResult.data;
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
                if(k >= 5) {
                    BleSdkWrapper.setDeviceDate(2022, 10, 27, 2, 12, 23, new OnLeWriteCharacteristicListener() {
                        @Override
                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                            if (handlerBleDataResult.bluetooth_code == CODE_SET_DEVICE_DATE) {

                                Log.d("FF45BH34", "setDeviceDate:" + a3);
                                a3++;
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
                if(k > 3 && k < 8){
                    BleSdkWrapper.getDeviceState(new OnLeWriteCharacteristicListener() {
                        @Override
                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                            if (handlerBleDataResult.bluetooth_code == CODE_GET_DEVICE_STATE) {

                                Log.d("FF45BH34", "setDeviceDate_222:" + a3);
                                a3++;
                            }
                        }

                        @Override
                        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                        }

                        @Override
                        public void onFailed(WriteBleException e) {

                        }
                    } );

                }
            } else {
                showToast(getResources().getString(R.string.main_device_unconnected));
            }

        }
    }
    int a = 0;
    //send message
    int b = 0;
    int i = 0;
    int h = 0;
    int hg = 0;

    @OnClick({R.id.tvSendMes})
    void tvSendMes(){
        a = 0;
            for (i = 0; i < 10; i++) {
                h = i;
                    if (MyAppcation.getInstance().isConnected()) {
                        BleSdkWrapper.sendMessage(1, "eee", "rrrr_a:" + a, new OnLeWriteCharacteristicListener() {
                            @Override
                            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                                BluetoothLe.getDefault().cancelTag("SETMESSAGE");
                                if(handlerBleDataResult.bluetooth_code == CODE_SEND_MESSAGE) {
                                    b = 0;
                                    a++;
                                    if(handlerBleDataResult.isComplete) {
                                        Log.d("FFG3232fr3", "sendMessage----a:" + a);
                                    }

                                }
                            }

                            @Override
                            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                            }

                            @Override
                            public void onFailed(WriteBleException e) {
//                                BluetoothLe.getDefault().cancelTag("SETMESSAGE");
                                Log.d("FF4d5fB54H34", "fail in send:" + e);
                                b = 0;
                                a++;
                            }
                        });
                            BleSdkWrapper.setDeviceshock(new OnLeWriteCharacteristicListener() {
                                @Override
                                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {

                                    if (handlerBleDataResult.bluetooth_code == CODE_SET_DEVICE_SHOCK) {
                                        Log.d("FFG323fd2fr3", "次数2:" + hg++);
                                        if(handlerBleDataResult.isComplete) {
                                            Log.d("FFG32g3243", "setDeviceshock:" + handlerBleDataResult.bluetooth_code);
                                        }
                                    }
                                }

                                @Override
                                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                                }

                                @Override
                                public void onFailed(WriteBleException e) {
                                }
                            });

                        BleSdkWrapper.getRealtimeHeartRate(new OnLeWriteCharacteristicListener() {
                            @Override
                            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {

                                if (handlerBleDataResult.bluetooth_code == CODE_GET_REALTIME_HEARTRATE) {
                                    Log.d("FFG323fd2fr3", "次数3:" + hg++);
                                    Log.d("FFG32g3243", "getRealtimeHeartRate:" + handlerBleDataResult.data);
                                    HealthHeartRate healthHeartRate = (HealthHeartRate) handlerBleDataResult.data;
//                                    Log.d("FFG3232ffr3", "heart_data_success:" + healthHeartRate.getSilentHeart());
                                    Log.d("FFG3232ffr3", "heart_data_success:" + TimeUtil.timeStamp2Date(healthHeartRate.getDate()));
                                }
                            }

                            @Override
                            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                                HealthHrDataHandler healthHrDataHandler=new HealthHrDataHandler();
                                byte[] to = new byte[20];
                                BaseCmdUtil.copy(bluetoothGattCharacteristic.getValue(), to);
                                int flag_start=to[0]&255;
                                if(flag_start == FLAT_START_GET_HEART_RATE) {
                                    HealthHeartRate healthHeartRate = healthHrDataHandler.handlerCurrent(to);
                                    //Get heart rate value
                                    a++;
                                    Log.d("FFG3232fr3", "heart_data:" + healthHeartRate.getSilentHeart() +" a:" + a);
                                }
                            }

                            @Override
                            public void onFailed(WriteBleException e) {
                                Log.d("FFG3232r3", "e:" + e);
                            }
                        });


                    } else {
                        showToast(getResources().getString(R.string.main_device_unconnected));
                    }

            }
    }

    @OnClick(R.id.tvEcg)
    void startEcg(){
    }
    @OnClick(R.id.tvFindPhone)
    void tvFindPhone(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, FindPhoneActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    private List<EcgHistoryData> ecgHistoryData = new ArrayList<>();
    private void synchEcg(){
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothLe.getDefault().close();
    }


}
