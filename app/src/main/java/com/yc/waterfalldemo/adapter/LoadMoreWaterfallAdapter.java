package com.yc.waterfalldemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sunzn.utils.library.ScreenUtils;
import com.sunzn.utils.library.SizeUtils;
import com.yc.waterfalldemo.R;
import com.yc.waterfalldemo.bean.WaterFallBean;

import java.util.ArrayList;

/**
 * @author by CNKIFU on 2019-04-23.
 */
public class LoadMoreWaterfallAdapter extends RecyclerView.Adapter<LoadMoreWaterfallAdapter.WaterfallViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<WaterFallBean> mDatas;
    private final RequestOptions mRequestOptions;

    public LoadMoreWaterfallAdapter(Context context) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mRequestOptions = new RequestOptions().placeholder(R.color.colorPrimary);
    }

    public void setData(ArrayList<WaterFallBean> mDatas) {
        this.mDatas = mDatas;
    }

    public ArrayList<WaterFallBean> getData() {
        return mDatas;
    }

    @NonNull
    @Override
    public WaterfallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_water_fall, parent, false);
        return new WaterfallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterfallViewHolder viewHolder, int position) {
        WaterFallBean waterFallBean = mDatas.get(position);
        ViewGroup.LayoutParams layoutParams = viewHolder.imageView.getLayoutParams();
        int width = ScreenUtils.getScreenWidth(mContext) / 2 - SizeUtils.dp2px(mContext, 8);
        layoutParams.height = (int) (width * (Float.parseFloat(waterFallBean.getHeight()) / Float.parseFloat(waterFallBean.getWidth())));

        Glide.with(mContext)
                .load(waterFallBean.getImg())
                .apply(mRequestOptions).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class WaterfallViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        WaterfallViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.water_fall_img);
        }
    }

}