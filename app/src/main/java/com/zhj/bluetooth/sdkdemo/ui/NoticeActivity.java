package com.zhj.bluetooth.sdkdemo.ui;



import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_STATE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_NOTICE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SEND_MESSAGE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SEND_MESSAGE_P;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_NOTICE;

import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.MainActivity;
import com.zhj.bluetooth.sdkdemo.MyAppcation;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.dialog.DialogHelperNew;
import com.zhj.bluetooth.sdkdemo.views.ItemToggleLayout;
import com.zhj.zhjsdkcustomized.bean.AppNotice;
import com.zhj.zhjsdkcustomized.bean.DeviceState;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.CmdHelper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.SharePreferenceUtils;

import butterknife.BindView;

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.itMessage)
    ItemToggleLayout itMessage;
    @BindView(R.id.itCall)
    ItemToggleLayout itCall;
    @BindView(R.id.itQQ)
    ItemToggleLayout itQQ;
    @BindView(R.id.itFacebook)
    ItemToggleLayout itFacebook;
    @BindView(R.id.itWeChat)
    ItemToggleLayout itWeChat;
    @BindView(R.id.itLinked)
    ItemToggleLayout itLinked;
    @BindView(R.id.itSkype)
    ItemToggleLayout itSkype;
    @BindView(R.id.itInstagram)
    ItemToggleLayout itInstagram;
    @BindView(R.id.itTwitter)
    ItemToggleLayout itTwitter;
    @BindView(R.id.itLine)
    ItemToggleLayout itLine;
    @BindView(R.id.itWhatsApp)
    ItemToggleLayout itWhatsApp;
    @BindView(R.id.itVK)
    ItemToggleLayout itVK;
    @BindView(R.id.itMessenger)
    ItemToggleLayout itMessenger;
    @BindView(R.id.tv_send_msg)
    TextView tv_send_msg;
    private Dialog mDialog;
    private static final int REQUEST_NOTICE_PERMISSION_CODE = 0x12;

    @Override
    protected int getContentView() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.message_notification_switch));
//        mDialog = DialogHelperNew.showRemindDialog(NoticeActivity.this, getResources().getString(R.string.permisson_notication_title),
//                getResources().getString(R.string.permisson_notication_tips_keep), getResources().getString(R.string.permisson_location_open), view2 -> {
//                    startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), REQUEST_NOTICE_PERMISSION_CODE);
//                    mDialog.dismiss();
//                }, view2 -> {
//                    mDialog.dismiss();
//                    NoticeActivity.this.finish();
//                });
//        BluetoothLe.getDefault().writeDataToCharacteristic(CmdHelper.CMD_GET_SLEEP);
        BleSdkWrapper.getDeviceState(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_DEVICE_STATE) {
                    Log.d("gf43r4","CODE_GET_DEVICE_STATE");
                }
            }


            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
        BleSdkWrapper.getNotice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_NOTICE) {
                    Log.d("ggfd33","notice:" + handlerBleDataResult.data);
                    AppNotice appNotice = (AppNotice) handlerBleDataResult.data;
                    setNoticeState(appNotice);
                }
            }


            @Override
            public void onFailed(WriteBleException e) {
            }
        } );


        tv_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAppcation.getInstance().isConnected()) {
                        for (int i = 0; i < 2; i++) {
                            startSendMess("title555555555555555555", "message66666666666666666666");
                        }
                } else {
                    showToast(getResources().getString(R.string.main_device_unconnected));
                }

            }
        });
    }
    private void startSendMess(String title ,String message) {
        BleSdkWrapper.sendMessage(title, message, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                                BluetoothLe.getDefault().cancelTag("SETMESSAGE");
                if (handlerBleDataResult.bluetooth_code == CODE_SEND_MESSAGE_P) {
                    Log.d("ggfd33", "sendMessage: "  + handlerBleDataResult.bluetooth_code + "  tagId:" +handlerBleDataResult.tagId);
                }
            }
            @Override
            public void onFailed(WriteBleException e) {
//                                BluetoothLe.getDefault().cancelTag("SETMESSAGE");
            }
        });

    }

    private void setNoticeState(AppNotice appNotice) {
        if(appNotice == null){
            return;
        }
        itMessage.setOpen(appNotice.sms);
        itQQ.setOpen(appNotice.qq);
        itFacebook.setOpen(appNotice.facebook);
        itInstagram.setOpen(appNotice.instagram);
        itLine.setOpen(appNotice.line);
        itLinked.setOpen(appNotice.linked);
        itSkype.setOpen(appNotice.skype);
        itTwitter.setOpen(appNotice.twitter);
        itVK.setOpen(appNotice.vk);
        itWeChat.setOpen(appNotice.wechat);
        itWhatsApp.setOpen(appNotice.whatsApp);
        itMessenger.setOpen(appNotice.messager);
        itMessage.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.messager = isSwitchOn;
            Log.d(TAG,"appNotice:" + appNotice + " appNotice.messager:" + appNotice.messager + "  isSwitchOn:" + isSwitchOn);
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itCall.setOpen(appNotice.incoming);
        itCall.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.incoming = isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itWhatsApp.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.whatsApp = isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itInstagram.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.instagram=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itQQ.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.qq=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itFacebook.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.facebook=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itLine.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.line=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itLinked.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.linked=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itSkype.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.skype=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itTwitter.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.twitter=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itVK.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.vk=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itWeChat.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.wechat=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itMessenger.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.messager=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });


    }
}
