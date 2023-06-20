package com.zhj.bluetooth.sdkdemo.util;

import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.zhj.bluetooth.sdkdemo.MyAppcation;

import java.lang.reflect.Method;

/**
 */
public class PhoneUtil {

    public static String TAG = PhoneUtil.class.getSimpleName();

//    public static ITelephony getITelephony(TelephonyManager telephony) throws Exception {
//        @SuppressLint("SoonBlockedPrivateApi")
//        Method getITelephonyMethod = telephony.getClass().getDeclaredMethod("getITelephony");
//        getITelephonyMethod.setAccessible(true);//私有化函数也能使用
//        return (ITelephony)getITelephonyMethod.invoke(telephony);
//    }
    /**
     * Hang up
     *
     * @param context
     */
    public static void endCall(Context context) {
        TelecomManager tm = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        }
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                tm.endCall();
            }else{
                //Judge whether permission has been granted

            }
        }else{

        }
    }

    private static Object getTelephonyObject(Context context) {
        Object telephonyObject = null;
        try {
            // Initialize iTelephony
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            // Will be used to invoke hidden methods with reflection
            // Get the current object implementing ITelephony interface
            Class telManager = telephonyManager.getClass();
            @SuppressLint("SoonBlockedPrivateApi") Method getITelephony = telManager.getDeclaredMethod("getITelephony");
            getITelephony.setAccessible(true);
            telephonyObject = getITelephony.invoke(telephonyManager);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return telephonyObject;
    }


    /**
     * Answer the call by reflecting the call method. This method is only valid on systems before Android 2.3.
     * Neither user 11164 nor current process has android.permission.MODIFY_PHONE_STATE.
     * @param context
     */
    public static void answerRingingCallWithReflect(Context context) {
        try {
            Object telephonyObject = getTelephonyObject(context);
            if (null != telephonyObject) {
                Class telephonyClass = telephonyObject.getClass();
                Method endCallMethod = telephonyClass.getMethod("answerRingingCall");
                endCallMethod.setAccessible(true);
                endCallMethod.invoke(telephonyObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Forge a broadcast with a wired headset inserted and press the answer button to let the system begin to answer the call.
     * Send Intent.ACTION_ HEADSET_ PLUG broadcast crash
     * java.lang.SecurityException: Permission Denial: not allowed to send broadcast android.intent.action.HEADSET_PLUG from pid=8281, uid=11164
     * @param context
     */
    public static void answerRingingCallWithBroadcast(Context context) {
        AudioManager localAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //Determine whether the headset is plugged in
        boolean isWiredHeadsetOn = localAudioManager.isWiredHeadsetOn();
        if (!isWiredHeadsetOn) {
//            Intent headsetPluggedIntent = new Intent(Intent.ACTION_HEADSET_PLUG);
//            headsetPluggedIntent.putExtra("state", 1);
//            headsetPluggedIntent.putExtra("microphone", 0);
//            headsetPluggedIntent.putExtra("name", "");
//            context.sendBroadcast(headsetPluggedIntent);
//
//            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
//            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
//            context.sendOrderedBroadcast(meidaButtonIntent, null);
//
//            Intent headsetUnpluggedIntent = new Intent(Intent.ACTION_HEADSET_PLUG);
//            headsetUnpluggedIntent.putExtra("state", 0);
//            headsetUnpluggedIntent.putExtra("microphone", 0);
//            headsetUnpluggedIntent.putExtra("name", "");
//            context.sendBroadcast(headsetUnpluggedIntent);

            // 2.3 Above, execute the following code to realize automatic answering
            Intent mintent = new Intent(Intent.ACTION_MEDIA_BUTTON);

            //Press volume
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
            mintent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
            // Call permission allows the program to make calls and replace the dialer interface of the system
            MyAppcation.getInstance().sendOrderedBroadcast(mintent,"android.permission.CALL_PRIVILEGED");

            mintent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            keyEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
            mintent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
            MyAppcation.getInstance().sendOrderedBroadcast(mintent,"android.permission.CALL_PRIVILEGED");
        } else {
//            DebugLog.d("Forge a wired earphone insertion ------ 2=" + isWiredHeadsetOn);
            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
            context.sendOrderedBroadcast(meidaButtonIntent, null);
        }
    }
    public static void answerRingingCallWithBroadcast(Context context, TelephonyManager telmanager){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //Determine whether the headset is plugged in
        if (! audioManager.isWiredHeadsetOn()) {
            //4.1 The above systems limit some permissions, and use Samsung version 4.1 test prompt warning:Permission Denial: not allowed to send broadcast android.intent.action.HEADSET_PLUG from pid=1324, uid=10017
//It should be noted here that if the permission "android.permission.CALL_PRIVLEGED" is added when sending a broadcast, the permission should also be added when accepting the broadcast. However, it seems that this permission can only be obtained by system applications in versions above 4.1. During the test, the customized receiver could not accept this broadcast. Later, the permission was removed and set to NULL to listen.
            Log.d(TAG,"No headset");
            if(Build.VERSION.SDK_INT >=15 ){
                Log.d(TAG,">=15");
                Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
                context.sendOrderedBroadcast(meidaButtonIntent, "android.permission.CALL_PRIVILEGED");
            }else{
// The following applies to Android 2.3 and above, but the test found that 4.1 does not work.
                Log.d(TAG,"<15");
                Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent1.putExtra("state", 1);
                localIntent1.putExtra("microphone", 1);
                localIntent1.putExtra("name", "Headset");
                context.sendOrderedBroadcast(localIntent1,  "android.permission.CALL_PRIVILEGED");

                Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,   KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent2.putExtra(Intent.EXTRA_KEY_EVENT,   localKeyEvent1);
                context.sendOrderedBroadcast(localIntent2,  "android.permission.CALL_PRIVILEGED");

                Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,  KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent3.putExtra(Intent.EXTRA_KEY_EVENT,  localKeyEvent2);
                context.sendOrderedBroadcast(localIntent3,   "android.permission.CALL_PRIVILEGED");

                Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent4.putExtra("state", 0);
                localIntent4.putExtra("microphone", 1);
                localIntent4.putExtra("name", "Headset");
                context.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
            }

        } else {
            Log.d(TAG,"Plug in the headset");
            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
            context.sendOrderedBroadcast(meidaButtonIntent, null);
        }
    }

    /**
     * Answer the phone
     *
     * @param context
     */
    public static void answerRingingCall(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {  //2.3 or above systems
            answerRingingCallWithBroadcast(context,telephonyManager);
//            answerRingingCallWithBroadcast(context);
            Log.d(TAG,"Answering calls>2.3");
        } else {
            Log.d(TAG,"Answer phone<2.3");
            answerRingingCallWithReflect(context);
        }
    }


    /**
     * Dial
     *
     * @param context
     * @param phoneNumber
     */
    public static void dialPhone(Context context, String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                context.startActivity(callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}