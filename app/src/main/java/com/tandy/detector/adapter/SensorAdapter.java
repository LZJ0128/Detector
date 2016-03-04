package com.tandy.detector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tandy.detector.R;
import com.tandy.detector.entity.BaseEntity;

import java.util.List;

/**
 * Created by Administrator on 3/3 0003.
 */
public class SensorAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<BaseEntity> mEntityList;

    public SensorAdapter(Context context, List<BaseEntity> list){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mEntityList = list;
    }

    public int getCount(){
        return mEntityList.size();
    }

    public BaseEntity getItem(int position){
        return mEntityList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View concertView, ViewGroup parent){
        SensorHolder holder = null;
        if (concertView == null){
            holder = new SensorHolder();
            concertView = mInflater.inflate(R.layout.item_sensor, null);
            holder.textView = (TextView)concertView.findViewById(R.id.txv_sensor);
            concertView.setTag(holder);
        }else {
            holder = (SensorHolder)concertView.getTag();
        }
        BaseEntity entity = getItem(position);
        holder.textView.setText(entity.getContent());

        return concertView;
    }

    static class SensorHolder{
        private TextView textView;
    }
}
