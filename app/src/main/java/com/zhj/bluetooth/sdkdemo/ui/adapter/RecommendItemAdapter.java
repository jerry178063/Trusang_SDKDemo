package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.base.BaseViewHolder;
import com.zhj.bluetooth.sdkdemo.util.ScreenUtil;
import com.zhj.zhjsdkcustomized.bean.DialCenterDetail;

import java.util.List;

import butterknife.BindView;


public class RecommendItemAdapter extends BaseAdapter<DialCenterDetail,RecommendItemAdapter.ViewHolder> {
    private int imgWidth;
    private int imgHeight;
    public RecommendItemAdapter(Context mContext, List<DialCenterDetail> mList, int width, int height) {
        super(mContext, mList);
        imgWidth = ScreenUtil.dp2px(width,mContext);
        imgHeight = ScreenUtil.dp2px(height,mContext);
        int maxWidth = (ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dp2px(10*6,mContext))/3;
        if(imgWidth > maxWidth){
            imgWidth = maxWidth;
            imgHeight = (int) (imgWidth * (height*1.0 / width));
        }
    }

    @Override
    protected void onNormalBindViewHolder(RecommendItemAdapter.ViewHolder holder, DialCenterDetail itemBean, int position) {
        LinearLayout.LayoutParams paramsBg = (LinearLayout.LayoutParams)holder.center_bg.getLayoutParams();
        paramsBg.width = imgWidth;
        paramsBg.height = imgHeight;
        holder.center_bg.setLayoutParams(paramsBg);
        load(mContext,holder.center_bg,itemBean.getPicUrl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });
    }

    private void load(Context context, ImageView imageView, String url) {
        if (null == context) return;
        RequestOptions options = new RequestOptions()
                //Specify the zoom type of the image as centerCrop (scale the image proportionally until the height of the image is greater than or equal to the width of the ImageView, and then intercept the middle display.)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   ;     //Skip disk cache
//                .skipMemoryCache(false)
//                .dontAnimate();
        Glide.with(context).load(url).apply(options).into(imageView);
    }


    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view;
        view = inflater.inflate(R.layout.recommend_item,parent,false);
        return new ViewHolder(view);
    }
    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.center_bg)
        ImageView center_bg;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
