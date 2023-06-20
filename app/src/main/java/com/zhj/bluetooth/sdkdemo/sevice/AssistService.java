package com.zhj.bluetooth.sdkdemo.sevice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.content.ContextCompat;


import com.zhj.bluetooth.sdkdemo.MyAppcation;
import com.zhj.bluetooth.sdkdemo.util.PhoneUtil;
import com.zhj.zhjsdkcustomized.ble.DeviceCallbackWrapper;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;
import com.zhj.zhjsdkcustomized.util.LogUtil;


/**
 * Auxiliary service, listening for incoming calls
 *
 * @author Administrator
 */
public class AssistService extends Service {

    private static final String ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";
    private Vibrator mVib;
    private boolean hasFirstReigsterPhone;
    private boolean isCommingPhone = false;
    private boolean isRemind = false;
    private String TAG = "AssistService";
    /**
     * All SMS messages
     */
    private Uri SMS_URI = Uri.parse("content://sms/");

    /**
     * Inbox SMS
     */
    private Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    private boolean isRingOrVibrate = true;    //Whether the bell rings or vibrates
    private TelephonyManager tpm;
    private PhoneStateListener phoneStateListener;
    private Handler handler = new Handler();

    /**
     * Delayed when calling
     */
    private Handler callHandler = new Handler();
    private long exitTime = 0;
    private MediaPlayer mMediaPlayer;
    Runnable vibrateAndMediaRunnable = new Runnable() {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            if (isRingOrVibrate) {
                if (System.currentTimeMillis() - exitTime >= (10 * 1000)) {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                    if (mVib != null) {
                        mVib.cancel();
                        mVib = null;
                    }
                }
                handler.postDelayed(this, 1000);
            } else {
                handler.removeCallbacks(this);
            }
        }
    };
    // Stop looking for the ringing tone and vibration of the phone through the volume key
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_VOLUME_CHANGED)) {
                //Stop looking for the ringing tone and vibration of the phone through the volume key
                isRingOrVibrate = false;
                try {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                    if (mVib != null && mVib.hasVibrator()) {
                        mVib.cancel();
                        mVib = null;
                    }
                } catch (Exception e) {

                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("onCreate ");
        Log.d(TAG,"assitservice_oncreate");
        BluetoothLe.getDefault().addDeviceCallback(new DeviceCallbackWrapper() {
            @Override
            public void findPhone() {
                super.findPhone();

                LogUtil.d("Receive the command of bracelet -- start to search for mobile phone settings successfully");
//            boolean findPhoneOnOff =SPHelper.getDeviceConfig().isFindPhone;
//            if (findPhoneOnOff) {
                isRingOrVibrate = true;
                playRingtone(true);
//            }
            }

            @Override
            public void sos() {
                super.sos();
                boolean isPermissions = ContextCompat.checkSelfPermission(MyAppcation.getInstance(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
                if (isPermissions) {
                }
            }

            @Override
            public void answerRingingCall() {
                super.answerRingingCall();
//                DeviceConfig onOff = SPHelper.getDeviceConfig();
//                if (onOff == null) {
//                    return;
//                }
//                if (!onOff.isDisturbMode) {
//                    return;
//                }
                PhoneUtil.answerRingingCall(getApplicationContext());
            }

            @Override
            public void endRingingCall() {
                super.endRingingCall();
//                DeviceConfig onOff = SPHelper.getDeviceConfig();
//                if (onOff == null) {
//                    return;
//                }
//                if (!onOff.isDisturbMode){
//                    return;
//                }
                PhoneUtil.endCall(getApplicationContext());
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_VOLUME_CHANGED);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(Intent.ACTION_MEDIA_BUTTON);
        registerReceiver(receiver, filter);

    }

    private Notification notification;
    public static final int NOTICE_ID = 1001;
    private String CHANNEL_ONE_ID = "com.hearth.notification.assist";

    private boolean phonePermissions = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        phonePermissions = ContextCompat.checkSelfPermission(AssistService.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED;
        boolean smsPermissions = ContextCompat.checkSelfPermission(AssistService.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ;
        boolean smsPermissions2 = ContextCompat.checkSelfPermission(AssistService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ;
        LogUtil.d("phonePermissions:" + phonePermissions);
        LogUtil.d("smsPermissions:" + smsPermissions);
        if (!phonePermissions && !smsPermissions2) {
            tpm = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            registerPhoneListener();
        }
        if (!smsPermissions) {
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @SuppressLint("MissingPermission")
    private void playRingtone(boolean isVibrate) {
        handler.removeCallbacks(vibrateAndMediaRunnable);
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        if (mVib == null && isVibrate) {
            mVib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        }

        boolean isPlaying = false;
        try {
            isPlaying = mMediaPlayer.isPlaying();
        } catch (Exception e) {
            mMediaPlayer = null;
            mMediaPlayer = new MediaPlayer();
        }
        if (isPlaying) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mMediaPlayer = new MediaPlayer();
        }
        try {
            Uri uri = getSystemDefultRingtoneUri();// mAppSharedPreferences.getRingtoneUrl();
            // mMediaPlayer.reset();
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            //Start timing
            exitTime = System.currentTimeMillis();
            mMediaPlayer.start();

            handler.postDelayed(vibrateAndMediaRunnable, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isVibrate) {
            mVib.vibrate(new long[]{500, 2000}, 0);
        }
    }

    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }

    // TODO Listen for phone status change events
    private void registerPhoneListener() {
        // Create a listening object to listen for phone status change events
        tpm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void unregisterPhoneListener() {
        if (tpm != null && phoneStateListener != null) {
            tpm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("onDestroy......");
        unregisterReceiver(receiver);
        if (!phonePermissions) {
            unregisterPhoneListener();
        }
    }
}