package com.zhj.bluetooth.sdkdemo.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.bean.DatumLine;

import butterknife.BindView;

public class DatumLineActivity extends BaseActivity {

    @BindView(R.id.etVauleHeart)
    EditText etVauleHeart;
    @BindView(R.id.etVauleFz)
    EditText etVauleFz;
    @BindView(R.id.etVauleSs)
    EditText etVauleSs;
    @BindView(R.id.etVauleOxygen)
    EditText etVauleOxygen;

    @Override
    protected int getContentView() {
        return R.layout.activity_datumline;
    }


    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.heart_rate_reference_line));
        rightText.setText(getResources().getString(R.string.preservation));
        rightText.setOnClickListener(view -> {
            DatumLine datumLine = new DatumLine();
            if(!TextUtils.isEmpty(etVauleHeart.getText().toString())) {
                datumLine.setHeart(Integer.parseInt(etVauleHeart.getText().toString()));
            }
            if(!TextUtils.isEmpty(etVauleFz.getText().toString())) {
                datumLine.setFz(Integer.parseInt(etVauleFz.getText().toString()));
            }
            if(!TextUtils.isEmpty(etVauleSs.getText().toString())) {
                datumLine.setSs(Integer.parseInt(etVauleSs.getText().toString()));
            }
            if(!TextUtils.isEmpty(etVauleOxygen.getText().toString())) {
                datumLine.setOxygen(Integer.parseInt(etVauleOxygen.getText().toString()));
            }
        });
    }
}
