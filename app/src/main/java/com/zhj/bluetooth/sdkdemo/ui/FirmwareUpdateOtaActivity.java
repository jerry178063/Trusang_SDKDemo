package com.zhj.bluetooth.sdkdemo.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.nfc.Tag;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.util.Device;
import com.zhj.bluetooth.sdkdemo.util.TelinkLog;
import com.zhj.zhjsdkcustomized.bean.BLEDevice;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.ByteDataConvertUtil;
import com.zhj.zhjsdkcustomized.ble.CmdHelper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothUUID;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeConnectListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeScanListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.ConnBleException;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.ScanBleException;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.ble.bluetooth.scanner.ScanRecord;
import com.zhj.zhjsdkcustomized.ble.bluetooth.scanner.ScanResult;
import com.zhj.zhjsdkcustomized.util.LogUtil;
import com.zhj.zhjsdkcustomized.util.SPHelper;
import com.zhj.zhjsdkcustomized.util.ThreadUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceController;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

public class FirmwareUpdateOtaActivity extends BaseActivity {

    private final String saveFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SDKDemo/Device_update/";
    private String[] pers = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=200;
    private final String downUrl = "http://47.75.143.120/file_v2/images/1648537589437.bin";
    private BluetoothDevice mDfuDevice;
    @BindView(R.id.pb_update_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.progressRate)
    TextView progressRate;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTips2)
    TextView tvTips2;
    @BindView(R.id.text_path)
    TextView text_path;
    @Override
    protected int getContentView() {
        return R.layout.activity_firmware;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_update));
        text_path.setText("../app/src/main/assets/c60_dreamit_v1.15.zip");
        if (!checkSelfPermission(pers)){
            requestPermissions(WRITE_EXTERNAL_STORAGE_REQUEST_CODE,pers);
        } else{
//          String  downUrl = "http://47.75.143.120/file_v2/images/1669021180958.bin";

//            downLoadFile(downUrl);
            updateDfu();
        }
    }


    private void downLoadFile(String filrUrl) {
        File file = new File(saveFileName);
        if (!file.exists()) file.mkdirs();
        final File apkFile = new File(saveFileName + "deviceUpdateOta.zip");
        if(apkFile.exists()) apkFile.delete();
        new Thread() {
            @Override
            public void run() {
                try {
                    downloadUpdateFile(filrUrl, apkFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private long downloadUpdateFile(String downloadUrl, File saveFile) throws IOException {
        int downloadCount = 0;
        int currentSize = 0;
        int totalSize = 0;
        int updateTotalSize = 0;

        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            updateTotalSize = httpConnection.getContentLength();
            if (httpConnection.getResponseCode() == 404) {
                mHandler.sendEmptyMessage(DOWN_FAILD);
                throw new Exception("fail!");
            }
            is = httpConnection.getInputStream();
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[2048];
            int readSize = 0;
            while ((readSize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readSize);
                totalSize += readSize;
                if ((downloadCount == 0) || (int) (totalSize * 100 / updateTotalSize) - 1 > downloadCount) {
                    downloadCount += 1;
                    Message msg = mHandler.obtainMessage();
                    msg.what = DOWN_UPDATE;
                    msg.arg1 = totalSize;
                    msg.arg2 = updateTotalSize;
                    mHandler.sendMessage(msg);
                }
            }
        }catch (Exception e){
            mHandler.sendEmptyMessage(DOWN_FAILD);
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if(is != null){
                is.close();
            }
            if(fos != null){
                fos.close();
            }
        }
        return totalSize;
    }

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_FAILD = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    downLoadRate(msg.arg1,msg.arg2);
                    break;
                case DOWN_FAILD:
                    showToast(getResources().getString(R.string.failed_to_download_file));
                    FirmwareUpdateOtaActivity.this.finish();
                    break;
            }
        }
    };


    private void downLoadRate(long totalSize,long updateTotalSize) {
        if(totalSize >= updateTotalSize){
            mProgressBar.setProgress((int) (totalSize * 100/updateTotalSize));
            progressRate.setText((int) (totalSize * 100 /updateTotalSize)+"%");
            tvTips.setText(getResources().getString(R.string.upgrade_package_size)+updateTotalSize/1024+"KB");
            tvTips2.setText(getResources().getString(R.string.download_complete));
            ThreadUtil.delayTask(() -> {
                tvTips.setText(getResources().getString(R.string.upgrade_package_size)+updateTotalSize/1024+"KB");
                updateDfu();},500);
        }else{
            mProgressBar.setProgress((int) (totalSize * 100 /updateTotalSize));
            progressRate.setText((int) (totalSize  * 100  /updateTotalSize)+"%");
            tvTips.setText(getResources().getString(R.string.upgrade_package_size)+updateTotalSize/1024+"KB");
            tvTips2.setText(getResources().getString(R.string.the_upgrade_file_is_downloading));
        }
    }


    //手环进入空升模式
    private void updateDfu() {
        mProgressBar.setProgress(0);
        progressRate.setText("0%");
        tvTips2.setText(getResources().getString(R.string.dfu_connecting));
        BleSdkWrapper.enterOta(new OnLeWriteCharacteristicListener() {

            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                ThreadUtil.delayTask(() -> {
                    startDfu();},1000);
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }
    private Device mdevice;
    BLEDevice bleDevice;
    private void startDfu() {
        //Instantiate air lift tool class Device
        bleDevice = SPHelper.getBindBLEDevice(this);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(bleDevice.mDeviceAddress);
        mdevice = new Device(bluetoothDevice, bleDevice.getmBytes(), bleDevice.mRssi);
        mdevice.setDeviceStateCallback(deviceCallback);
        //Disconnect the original connection
        BluetoothLe.getDefault().disconnect();
        BluetoothLe.getDefault().close();
        //Reconnect the device using Device connection
        mdevice.connect(this);
    }


    //Device connection listening
    private Device.DeviceStateCallback deviceCallback = new Device.DeviceStateCallback() {
        @Override
        public void onConnected(Device device) { }
        @Override
        public void onDisconnected(Device device) { }
        @Override
        public void onServicesDiscovered(Device device, final List<BluetoothGattService> services) {
            //Connect successfully, get the device uuid
            UUID serviceUUID = null;
            for (BluetoothGattService service : services) {
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    if (characteristic.getUuid().equals(Device.CHARACTERISTIC_UUID_WRITE)) {
                        serviceUUID = service.getUuid();
                        break;
                    }
                }
            }
            if (serviceUUID != null) {
                device.SERVICE_UUID = serviceUUID;
            }

//            String path = saveFileName + "deviceUpdateOta.zip";
            String path = "c60_dreamit_v1.15.zip";
            Log.d("DD33","path:" + path);
            //Convert empty file to byte array
            byte[] firmware = readFirmware(path);
            Log.d("DD33","firmware:" + ByteDataConvertUtil.bytesToHexString2(firmware));
            if (firmware == null) {
                showToast("firmware null");
                return;
            }


            //Send air-lift file like equipment
            mdevice.startOta(firmware);
        }

        @Override
        public void onOtaStateChanged(Device device, int state) {
            //Monitor upgrade progress
            switch (state) {
                case Device.STATE_PROGRESS:
                    //Upgrading
                    mInfoHandler.obtainMessage(MSG_PROGRESS, device.getOtaProgress()).sendToTarget();
                    break;
                case Device.STATE_SUCCESS:
                    //Upgrade succeeded
                    mInfoHandler.obtainMessage(MSG_INFO, "ota complete").sendToTarget();
                    break;
                case Device.STATE_FAILURE:
                    //Upgrade failed
                    mInfoHandler.obtainMessage(MSG_INFO, "ota failure").sendToTarget();
                    break;
            }
        }
    };
    private String copyAssetGetFilePath(String fileName) {
        try {
            File cacheDir = getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return null;
                }
            } else {
                if (outFile.length() > 10) {//Indicates that it has been written once
                    return outFile.getPath();
                }
            }
            InputStream is = getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[2048];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return outFile.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void requestPermissionsSuccess(int requestCode) {
        super.requestPermissionsSuccess(requestCode);
        updateDfu();
    }
    @Override
    public void requestPermissionsFail(int requestCode) {
        showToast(getResources().getString(R.string.privilege_grant_failed));
        FirmwareUpdateOtaActivity.this.finish();
    }
    private byte[] readFirmware(String fileName) {
        try {
//            InputStream stream = new FileInputStream(fileName);
            InputStream stream = getAssets().open(fileName);
            int length = stream.available();
            byte[] firmware = new byte[length];
            stream.read(firmware);
            stream.close();
            return firmware;
        } catch (IOException e) {
            Log.d("DD33","e:" + e);
            e.printStackTrace();
        }
        return null;
    }

    private final static int MSG_PROGRESS = 11;
    private final static int MSG_INFO = 12;
    @SuppressLint("HandlerLeak")
    private Handler mInfoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_PROGRESS) {
                mProgressBar.setProgress((Integer) msg.obj);
                progressRate.setText(msg.obj + "%");
                tvTips2.setText(getResources().getString(R.string.dfu_upgrading));
            } else if (msg.what == MSG_INFO) {
                tvTips2.setText(String.valueOf(msg.obj));
                if (msg.obj.equals("ota complete")) {
                    showToast(getResources().getString(R.string.upgrade_succeeded));
                    if (mdevice.isConnected()) {
                        mdevice.disconnect();
                    }
                    ThreadUtil.delayTask(() -> {
                        BluetoothLe.getDefault().reconnect(FirmwareUpdateOtaActivity.this);
                        FirmwareUpdateOtaActivity.this.finish();
                    }, 3000);
                } else if (msg.obj.equals("ota failure")) {
                    showToast(getResources().getString(R.string.upgrade_failed));
                    if (mdevice.isConnected()) {
                        mdevice.disconnect();
                    }
                    BluetoothLe.getDefault().reconnect(FirmwareUpdateOtaActivity.this);
                    FirmwareUpdateOtaActivity.this.finish();
                }
            }
        }
    };
}
