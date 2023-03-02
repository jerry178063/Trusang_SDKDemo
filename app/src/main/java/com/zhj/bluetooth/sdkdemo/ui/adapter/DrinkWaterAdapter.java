package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.zhjsdkcustomized.bean.DrinkWaterGoal;
import com.zhj.zhjsdkcustomized.util.StringUtils;

import java.util.List;

import butterknife.BindView;

public class DrinkWaterAdapter extends BaseAdapter<DrinkWaterGoal, DrinkWaterAdapter.ViewHolder> {
    private Context context;
    public DrinkWaterAdapter(Context mContext, List<DrinkWaterGoal> mList) {
        super(mContext, mList);
        this.context = mContext;
    }

    @Override
    protected void onNormalBindViewHolder(DrinkWaterAdapter.ViewHolder holder, DrinkWaterGoal itemBean, int position) {
        holder.tvName.setText(context.getResources().getString(R.string.section)+(position+1)+context.getResources().getString(R.string.cup_of_water));
        holder.tvTime.setText(StringUtils.format("%02d",itemBean.getHour())+":"+StringUtils.format("%02d",itemBean.getMinuter()));
    }

    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_drink_water_goal,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvTime)
        TextView tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
