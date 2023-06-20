package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.zhjsdkcustomized.bean.HealthSportItem;


import java.util.List;

import butterknife.BindView;

public class StepsHistoryAdapter extends BaseAdapter<HealthSportItem,StepsHistoryAdapter.ViewHolder> {
    public StepsHistoryAdapter(Context mContext, List<HealthSportItem> mList) {
        super(mContext, mList);
    }

    @Override
    protected void onNormalBindViewHolder(StepsHistoryAdapter.ViewHolder holder, HealthSportItem itemBean, int position) {
        holder.tvDate.setText(mContext.getResources().getString(R.string.step_history_time)+"\n"+itemBean.getYear()+"-"+itemBean.getMonth()+"-"+itemBean.getDay() + "  "+itemBean.getHour()+":"+itemBean.getMinuter());
        holder.tvSteps.setText(mContext.getResources().getString(R.string.step_history_steps)+"\n"+itemBean.getStepCount());
        //Calories (cal): step length (cm) * weight (kg) * steps * 0.78/100000
        //Distance (m): step length (cm) * steps/100
        //Default step length 75cm, weight 60kg
        holder.tvCal.setText(mContext.getResources().getString(R.string.step_history_cal)+"\n"+itemBean.getCalory() );
        holder.tvDistance.setText(mContext.getResources().getString(R.string.step_history_distance)+"\n"+itemBean.getDistance() );
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_steps, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvSteps)
        TextView tvSteps;
        @BindView(R.id.tvCal)
        TextView tvCal;
        @BindView(R.id.tvDistance)
        TextView tvDistance;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
