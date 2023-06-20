package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.zhjsdkcustomized.bean.Alarm;


import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2019/7/11.
 */

public class AlarmListAdapter extends BaseAdapter<Alarm,AlarmListAdapter.ViewHolder> {

    public AlarmListAdapter(Context mContext, List<Alarm> mList) {
        super(mContext, mList);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onNormalBindViewHolder(AlarmListAdapter.ViewHolder holder, Alarm itemBean, int position) {
        holder.tvOpen.setText(mContext.getResources().getString(R.string.alarm_item_swith)+itemBean.getOn_off());
        //The alarm clock cycle starts on Monday. Whether each Boolean value corresponds to the current day is selected
        StringBuilder sb = new StringBuilder();
        for (boolean b : itemBean.getWeekRepeat()){
            sb.append(b);
            sb.append(",");
        }
        holder.tvCycle.setText(mContext.getResources().getString(R.string.alarm_item_cycle)+sb.toString());

        holder.tvTime.setText(mContext.getResources().getString(R.string.alarm_item_time)+itemBean.getAlarmHour()+":"+ itemBean.getAlarmMinute());
        //0x00: Other 0x01: Drink water 0x02: Take medicine 0x03: Eat 0x04: Exercise 0x05: Sleep 0x06: Get up 0x07: Appointment 0x08: Party 0x09: Conference
        holder.tvType.setText(mContext.getResources().getString(R.string.alarm_item_type)+itemBean.getAlarmType());
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_alarm_list,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvOpen)
        TextView tvOpen;
        @BindView(R.id.tvCycle)
        TextView tvCycle;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvType)
        TextView tvType;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
