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
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.util.Device;
import com.zhj.bluetooth.sdkdemo.util.TelinkLog;
import com.zhj.zhjsdkcustomized.bean.BLEDevice;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
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

public class FirmwareUpdateActivity extends BaseActivity {

    private  String saveFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SDKDemo/Device_update/";
    private String[] pers = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=200;
    private final String downUrl = "http://47.75.143.120/file_v2/images/1645414466101.bin";
    private BluetoothDevice mDfuDevice;
    @BindView(R.id.pb_update_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.progressRate)
    TextView progressRate;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTips2)
    TextView tvTips2;
    @Override
    protected int getContentView() {
        return R.layout.activity_firmware;
    }

    @Override
    protected void initView() {
        super.initView();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            saveFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/SDKDemo/Device_update/";
        }else {
            saveFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SDKDemo/Device_update/";
        }
        titleName.setText(getResources().getString(R.string.main_update));
        if (!checkSelfPermission(pers)){
            requestPermissions(WRITE_EXTERNAL_STORAGE_REQUEST_CODE,pers);
        } else{
            downLoadFile(downUrl);
//            updateDfu();
        }
        DfuServiceListenerHelper.registerProgressListener(this, dfuProgressListener);
        BluetoothLe.getDefault().setOnConnectListener(TAG, new OnLeConnectListener()  {
            @Override
            public void onDeviceConnecting() {
                LogUtil.d("Linking device");
            }

            @Override
            public void onDeviceConnected() {
                LogUtil.d("Linked device");
            }

            @Override
            public void onDeviceDisconnected() {
                LogUtil.d("break link");
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt) {
                LogUtil.d("Discover the service and start empty generation");
                BluetoothLe.getDefault().stopScan();
                final DfuServiceInitiator starter = new DfuServiceInitiator(mDfuDevice.getAddress())
                        .setDeviceName("DfuTarg").setKeepBond(true);
                // If you want to have experimental buttonless DFU feature supported call additionally:starter.setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
                starter.setZip(saveFileName+"deviceUpdate.zip");
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    starter.setForeground(false);
                    starter.setDisableNotification(true);
                }
                starter.start(FirmwareUpdateActivity.this, DfuService.class);
            }
            @Override
            public void onDeviceConnectFail(ConnBleException e) {
                LogUtil.d("Link failed:"+e.toString());
                ThreadUtil.delayTask(() -> {
                    BluetoothLe.getDefault().startConnect(mDfuDevice);
                },2000);
            }
        });
    }

    private byte[] readFirmware(String fileName) {
        try {
            InputStream stream = new FileInputStream(fileName);
            int length = stream.available();
            byte[] firmware = new byte[length];
            stream.read(firmware);
            stream.close();
            return firmware;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void requestPermissionsSuccess(int requestCode) {
        super.requestPermissionsSuccess(requestCode);
//        downLoadFile(downUrl);
        updateDfu();
    }
    @Override
    public void requestPermissionsFail(int requestCode) {
        showToast(getResources().getString(R.string.privilege_grant_failed));
        FirmwareUpdateActivity.this.finish();
    }
    private void downLoadFile(String filrUrl) {
        File file = new File(saveFileName);
        if (!file.exists()) file.mkdirs();
        final File apkFile = new File(saveFileName + "deviceUpdate.zip");
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
                    FirmwareUpdateActivity.this.finish();
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

    private void updateDfu() {
        mProgressBar.setProgress(0);
        progressRate.setText("0%");
        tvTips2.setText(getResources().getString(R.string.dfu_connecting));
        BleSdkWrapper.enterOta(new OnLeWriteCharacteristicListener() {

            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                BluetoothLe.getDefault().enableNotification(false, BluetoothUUID.SERVICE_PAIR,
                        new UUID[]{BluetoothUUID.READ});
                BluetoothLe.getDefault().close();
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
    private void startDfu() {
        BluetoothLe.getDefault().setScanPeriod(10000)
//                .setScanWithDeviceName("DfuTarg")
                .startScan(FirmwareUpdateActivity.this,new OnLeScanListener() {
                    @Override
                    public void onScanResult(BluetoothDevice bluetoothDevice, int i, ScanRecord scanRecord) {
                        if(bluetoothDevice != null && bluetoothDevice.getName() != null){
                            if(bluetoothDevice.getName().contains("DfuTarg")){
                                mDfuDevice = bluetoothDevice;
                                LogUtil.d("Search for device");
                                BluetoothLe.getDefault().stopScan();
                                BluetoothLe.getDefault().startConnect(mDfuDevice.getAddress());
                            }
                        }
                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> list) {

                    }

                    @Override
                    public void onScanCompleted() {

                    }

                    @Override
                    public void onScanFailed(ScanBleException e) {

                    }
                });
    }

    private final DfuProgressListener dfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(@NonNull final String deviceAddress) { tvTips2.setText(getResources().getString(R.string.dfu_connected)); }
        @Override
        public void onDfuProcessStarting(@NonNull final String deviceAddress) { tvTips2.setText(getResources().getString(R.string.dfu_upgrading)); }
        @Override
        public void onEnablingDfuMode(@NonNull final String deviceAddress) { }
        @Override public void onFirmwareValidating(@NonNull final String deviceAddress) { }
        @Override
        public void onDeviceDisconnecting(@NonNull final String deviceAddress) { tvTips2.setText(getResources().getString(R.string.device_disconnected)); }
        @Override
        public void onDfuCompleted(@NonNull final String deviceAddress) {
            new Handler().postDelayed(() -> {
                // if this activity is still open and upload process was completed, cancel the notification
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(DfuService.NOTIFICATION_ID);
                tvTips2.setText(getResources().getString(R.string.upgrade_completed));
            }, 200);
        }
        @Override
        public void onDfuAborted(@NonNull final String deviceAddress) {
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler().postDelayed(() -> {
                // if this activity is still open and upload process was completed, cancel the notification
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(DfuService.NOTIFICATION_ID);
            }, 200);
        }

        @Override
        public void onProgressChanged(@NonNull final String deviceAddress, final int percent,
                                      final float speed, final float avgSpeed,
                                      final int currentPart, final int partsTotal) {
            mProgressBar.setProgress(percent);
            progressRate.setText(percent+"%");
        }

        @Override
        public void onError(@NonNull final String deviceAddress, final int error, final int errorType, final String message) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothLe.getDefault().destroy(TAG);
        DfuServiceListenerHelper.unregisterProgressListener(this, dfuProgressListener);
    }
}
