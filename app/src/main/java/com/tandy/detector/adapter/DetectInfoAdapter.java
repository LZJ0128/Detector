package com.tandy.detector.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tandy.detector.entity.DetectInfoEntity;
import com.tandy.detector.R;

import java.util.List;

/**
 * Created by Administrator on 2/29 0029.
 */
public class DetectInfoAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<DetectInfoEntity> mEntityList;

    public DetectInfoAdapter(Context context, List<DetectInfoEntity> list){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mEntityList = list;
    }

    public int getCount(){
        return mEntityList.size();
    }

    public DetectInfoEntity getItem(int position){
        return mEntityList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View concertView, ViewGroup parent){
        DetectInfoHolder holder = null;
        if (concertView == null){
            holder = new DetectInfoHolder();
            concertView = mInflater.inflate(R.layout.item_detect_info, null);
            holder.mTxvName = (TextView)concertView.findViewById(R.id.txv_name);
            holder.mTxvValue = (TextView)concertView.findViewById(R.id.txv_value);
            holder.mTxvEnabled = (TextView)concertView.findViewById(R.id.txv_enabled);
            concertView.setTag(holder);
        }else {
            holder = (DetectInfoHolder)concertView.getTag();
        }

        DetectInfoEntity entity = getItem(position);
        holder.mTxvName.setText(entity.getName());

        SharedPreferences sp = mContext.getSharedPreferences("flag", 1);
        boolean flag = sp.getBoolean("flag", false);
        if (flag){
            holder.mTxvName.setTextColor(Color.BLACK);
            holder.mTxvValue.setText(entity.getValue());
            holder.mTxvEnabled.setText(entity.getEnabled());
        }
        return concertView;
    }

    static class DetectInfoHolder{
        TextView mTxvName, mTxvValue, mTxvEnabled;
    }
}
