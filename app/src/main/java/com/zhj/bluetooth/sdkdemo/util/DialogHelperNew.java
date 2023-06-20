package com.zhj.bluetooth.sdkdemo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;


import com.zhj.bluetooth.sdkdemo.R;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * Custom Dialog。
 */

public class DialogHelperNew {
    private static Dialog waitDialog;

    /**
     * Create Wait Dialog
     *
     * @param c
     *         Context
     * @param cancelable
     *         Can Dialog return to cancel
     *
     * @return Dialog
     */
    public static Dialog buildWaitDialog(Context c, boolean cancelable) {
        if(waitDialog == null){
            waitDialog = new Dialog(c, R.style.theme_dialog);
        }
        waitDialog.setContentView(LayoutInflater.from(c).inflate(R.layout.dialog_wait,null));
        waitDialog.setCancelable(cancelable);
        waitDialog.show();
        return waitDialog;
    }

    public static void dismissWait(){
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }


    public static Dialog showRemindDialog(Activity context, String title, String tips,
                                          String sureText,View.OnClickListener listener,
                                          View.OnClickListener canleListener){
        Dialog dialog = new Dialog(context, R.style.center_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_remind,null);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        dialogTitle.setText(title);
        TextView tvTips = view.findViewById(R.id.tvTips);
        tvTips.setText(tips);
        view.findViewById(R.id.tvCanle).setOnClickListener(v -> {
            dialog.dismiss();
            canleListener.onClick(view);
        });
        TextView tvSure = view.findViewById(R.id.tvSure);
        tvSure.setText(sureText);
        tvSure.setOnClickListener(v ->{
            dialog.dismiss();
            listener.onClick(view);
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // Get screen width and height
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

}
