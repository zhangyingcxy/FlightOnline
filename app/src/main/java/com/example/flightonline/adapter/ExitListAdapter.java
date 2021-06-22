package com.example.flightonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flightonline.R;

public class ExitListAdapter extends BaseAdapter {
    private String [] items;
    private LayoutInflater layoutInflater;
    private int length;//记录有几个item，未登录下是1个，已登录下是2个

    public ExitListAdapter(Context context, String[] items,int length){
        this.items=items;
        this.layoutInflater=LayoutInflater.from(context);
        this.length=length;
    }

    public void setLength(int length){
        this.length=length;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        public TextView text;
        public ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.user_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.text=convertView.findViewById(R.id.itemTextLeft);
            viewHolder.image=convertView.findViewById(R.id.itemImage);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }

        if(length>0&&length>position){
            viewHolder.text.setText(items[position]);
            viewHolder.image.setImageLevel(0);
        }
        return convertView;
    }
}
