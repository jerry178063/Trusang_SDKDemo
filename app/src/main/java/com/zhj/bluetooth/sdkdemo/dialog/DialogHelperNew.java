package com.zhj.bluetooth.sdkdemo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;

public class DialogHelperNew {

    public static Dialog showRemindDialog(Activity context, String title, String tips,
                                          String sureText, View.OnClickListener listener,
                                          View.OnClickListener canleListener){
        Dialog dialog = new Dialog(context, R.style.center_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_remind,null);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        dialogTitle.setText(title);
        if(TextUtils.isEmpty(title)){
            dialogTitle.setVisibility(View.GONE);
        }else{
            dialogTitle.setVisibility(View.VISIBLE);
        }
        TextView tvTips = view.findViewById(R.id.tvTips);
        tvTips.setText(tips);
        if(TextUtils.isEmpty(tips)){
            tvTips.setVisibility(View.GONE);
        }else{
            tvTips.setVisibility(View.VISIBLE);
        }
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
