package org.ncsist.mdm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//import junit.framework.Test;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<Record> records;

    public MyAdapter(Context context, List<Record> record){
        myInflater = LayoutInflater.from(context);
        this.records = record;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return records.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView==null){
            convertView = myInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.event),
                    (TextView) convertView.findViewById(R.id.recordtime),
                    (TextView) convertView.findViewById(R.id.uploadtime)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Record record = (Record)getItem(position);

        //標題的顏色
        int color_event[] = {Color.BLACK,Color.WHITE,Color.YELLOW};
        //時間的顏色
        int color_time[] = {Color.GRAY,Color.WHITE,Color.YELLOW};
        //背景的顏色
        int color_back[] = {Color.WHITE,Color.BLUE, Color.BLACK};
        //時間是否顯示
        int time_vis[] = {View.VISIBLE,View.GONE,View.VISIBLE};

        int type_num = record.getType();
        holder.textEvent.setText(record.getEvent());
        holder.textEvent.setTextColor(color_event[type_num]);
        // holder.textEvent.setBackgroundColor(color_back[type_num]);
        holder.textEvent.setTextSize(24);
        holder.textEvent.setVisibility(time_vis[type_num]);
        holder.textRecordTime.setText(record.getRecordtime());
        holder.textRecordTime.setTextColor(color_time[type_num]);
        holder.textRecordTime.setVisibility(time_vis[type_num]);
        holder.textUploadTime.setText(record.getUploadtime());
        holder.textUploadTime.setTextColor(color_time[type_num]);
        holder.textUploadTime.setVisibility(time_vis[type_num]);

        return convertView;
    }

    private class ViewHolder {
        TextView textEvent;
        TextView textRecordTime;
        TextView textUploadTime;

        public ViewHolder(TextView textEvent, TextView textRecordTime, TextView textUploadTime){
            this.textEvent = textEvent;
            this.textRecordTime = textRecordTime;
            this.textUploadTime = textUploadTime;
        }
    }
}
