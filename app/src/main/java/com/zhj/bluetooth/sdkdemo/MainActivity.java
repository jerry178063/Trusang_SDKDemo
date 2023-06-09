package com.zhj.bluetooth.sdkdemo;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_NOTICE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SEND_MESSAGE_P;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_DEVICE_DATE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_DEVICE_SHOCK;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
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
import com.zhj.bluetooth.sdkdemo.ui.RateHistoryActivity;
import com.zhj.bluetooth.sdkdemo.ui.RestingCalorieActivity;
import com.zhj.bluetooth.sdkdemo.ui.ScanDeviceReadyActivity;
import com.zhj.bluetooth.sdkdemo.ui.SleepDataActivity;
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
import com.zhj.zhjsdkcustomized.bean.BLEDevice;
import com.zhj.zhjsdkcustomized.bean.EcgHistoryData;
import com.zhj.zhjsdkcustomized.bean.WarningInfo;
import com.zhj.zhjsdkcustomized.ble.BleCallbackWrapper;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;
import com.zhj.zhjsdkcustomized.ble.bluetooth.DeviceCallback;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeConnectListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.ConnBleException;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.BaseMessage;
import com.zhj.zhjsdkcustomized.util.Constants;
import com.zhj.zhjsdkcustomized.util.LogUtil;
import com.zhj.zhjsdkcustomized.util.SPHelper;
import com.zhj.zhjsdkcustomized.util.SharePreferenceUtils;


import java.util.ArrayList;
import java.util.List;

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
    private String TAG = "MainActivity";

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
            Log.d("gff33","onDeviceConnected");

        }

        @Override
        public void onDeviceDisconnected() {
            //Connection interrupted
            MyAppcation.getInstance().setConnected(false);
            BluetoothLe.getDefault().reconnect(MainActivity.this);
            checkBind();
            LogUtil.d("duankailianjie");
            Log.d("gff33","onDeviceDisconnected");
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt) {
            //Discover services and communicate
//            BluetoothLe.getDefault().enableNotification(true, Device.SERVICE_UUID,
//                    CHARACTERISTIC_UUID_WRITE);
            Log.d("gfderfe3","onServicesDiscovered");
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
            Log.d("gff33","onDeviceConnectFail");
        }
    };
    private final int RESULT_CODE_BIND = 1000;

    private void checkBind() {
        if (SPHelper.getBindBLEDevice(this) != null) {
            rightText.setText(getResources().getString(R.string.main_unbind));
            Log.d("asasa5fsds","MyAppcation.getInstance().isConnected():" + MyAppcation.getInstance().isConnected());
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
                } catch (Exception e) {

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
    void toSendPairing() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, Test.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnGetSleep)
    void togGetSleep() {



//        File outFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/000/" + System.currentTimeMillis() + ".jpg");
//        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", outFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        startActivity(intent);

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        startActivity(intent);

        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, SleepDataActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }


    @OnClick(R.id.btnGetSteps)
    void toGetSteps() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, StepsDataActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnSetHeartTest)
    void toSetHeartTest() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, HeartTestActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnReset)
    void resetDevice() {
        if (MyAppcation.getInstance().isConnected()) {
            BleSdkWrapper.recoverSet(new OnLeWriteCharacteristicListener() {

                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {

                }


                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnSendMessage)
    void toSendMessage() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, MessageContreActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnDeviceInfo)
    void getDeviceInfo() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, DeviceInfoActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnDeviceState)
    void getDeviceState() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, DeviceStateActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnUserInfo)
    void getUserInfo() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, UserInfoActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnAlarmInfo)
    void getAlarmInfo() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, AlarmActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnLongSitInfo)
    void getLongSitInfo() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, LongSitActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnGoalInfo)
    void getGoalInfo() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, GoalActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnNoticeInfo)
    void getNoticeInfo() {
//        if(MyAppcation.getInstance().isConnected()){
//            IntentUtil.goToActivity(this, NoticeActivity.class);
//        }else{
//            showToast(getResources().getString(R.string.main_device_unconnected));
//        }
        IntentUtil.goToActivity(this, NoticeActivity.class);
    }

    @OnClick(R.id.btnGetRateHistory)
    void getRateHistory() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, RateHistoryActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnHeartWarn)
    void toHeartWarn() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, HeartWarnActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnSportActivity)
    void toSport() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, SportActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvShowSleepsResting)
    void getRestingCal() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, RestingCalorieActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvCurrentTemp)
    void getCurrentTemp() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, TempDayDataActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvHistoryTemp)
    void getHistoryTemp() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, TempHistoryActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvTempTest)
    void setTempTest() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, TempTestActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvDfu)
    void toFirmwareUpdate() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, FirmwareUpdateActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }


    @OnClick(R.id.tvDfuOta)
    void toFirmwareUpdateOta() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, FirmwareUpdateOtaActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvTempWaring)
    void toTempWaring() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, TempWaringActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvWeather)
    void toWeather() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, WeatherActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvSenfDialCenter)
    void toSenfDialCenter() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, DialCenterActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvDrinkWater)
    void toDrinkWater() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, DrinkWaterActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvDisturb)
    void toDisturb() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, DisturbActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvSportMode)
    void toSportMode() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, SportModeActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick({R.id.tvDatumLine})
    void toDatumLine() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, DatumLineActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick({R.id.tvTelepho})
    void tvTelepho() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, TelephoAvtivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    //设置时间
    @OnClick({R.id.tvSetTime})
    void tvSetTime() {
        if (MyAppcation.getInstance().isConnected()) {
            for (int i = 0; i < 1; i++) {
                a = i;
                BleSdkWrapper.setDeviceDate(2023, 3, 8, 8, 42, 23, new OnLeWriteCharacteristicListener() {
                    @Override
                    public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                        if (handlerBleDataResult.bluetooth_code == CODE_SET_DEVICE_DATE) {
                            int nState = handlerBleDataResult.bluetooth_code.ordinal();
                            Log.d("hhhff3", "setTime:" + a);

                        }
                    }


                    @Override
                    public void onFailed(WriteBleException e) {

                    }
                });
            }
          } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }


    //Set vibration
    private int a;
    @OnClick({R.id.tvSetShock})
    void tvSetShock() {
            if (MyAppcation.getInstance().isConnected()) {
                for (int i = 0; i < 5; i++) {
                    a = i;
                    BleSdkWrapper.setDeviceshock(new OnLeWriteCharacteristicListener() {
                        @Override
                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                            if (handlerBleDataResult.bluetooth_code == CODE_SET_DEVICE_SHOCK) {
                                final String tagId = handlerBleDataResult.tagId;
                                Log.d("hhhff3", "shock: "  + handlerBleDataResult.bluetooth_code + "  tagId:" +tagId);
                            }
                        }

                        @Override
                        public void onFailed(WriteBleException e) {
                            Log.d(TAG, "e_veri:" + e.getTagId());
                        }
                    });

                }

            } else {
                showToast(getResources().getString(R.string.main_device_unconnected));
            }
    }

    //send message
    private String startMsg;
    private int start_i;

    @OnClick({R.id.tvSendMes})
    void tvSendMes() {

        //If you cannot receive a message, please set the message notification master switch and set the switch status of the specific message
        //BleSdkWrapper.setDeviceState This interface
        startMsg = SharePreferenceUtils.getString(MainActivity.this,"message");
        if (MyAppcation.getInstance().isConnected()) {
            if(TextUtils.isEmpty(startMsg)){
                  BleSdkWrapper.getNotice(new OnLeWriteCharacteristicListener() {
                  @Override
                  public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                     if (handlerBleDataResult.bluetooth_code == CODE_GET_NOTICE) {
                            Log.d(TAG, "notice:" + handlerBleDataResult.data);
                           SharePreferenceUtils.putString(MainActivity.this,"message","message");
                       }
                 }


                  @Override
                 public void onFailed(WriteBleException e) {
                 }
              });
            }else {
                for (int i = 0; i < 1; i++) {
                    startSendMess("title555555555555555555", "message66666666666666666666");
                }
            }
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }

    }
    private void startSendMess(String title ,String message) {
        BleSdkWrapper.sendMessage(title, message, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                                BluetoothLe.getDefault().cancelTag("SETMESSAGE");
                if (handlerBleDataResult.bluetooth_code == CODE_SEND_MESSAGE_P) {
                    Log.d("hhhff3", "sendMessage: "  + handlerBleDataResult.bluetooth_code + "  tagId:" +handlerBleDataResult.tagId);
                }
            }
            @Override
            public void onFailed(WriteBleException e) {
//                                BluetoothLe.getDefault().cancelTag("SETMESSAGE");
            }
        });

    }

    @OnClick(R.id.tvEcg)
    void startEcg() {
    }


    @OnClick(R.id.tvFindPhone)
    void tvFindPhone() {
        if (MyAppcation.getInstance().isConnected()) {
            IntentUtil.goToActivity(this, FindPhoneActivity.class);
        } else {
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    private List<EcgHistoryData> ecgHistoryData = new ArrayList<>();

    private void synchEcg() {
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
