package com.example.flightonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flightonline.R;

public class OrderAdapter extends BaseAdapter {
    private String[] item;
    private LayoutInflater layoutInflater;
    private int ImageLevel;

    public OrderAdapter(Context context, String[] items,int imageLevel){
        this.item=items;
        this.layoutInflater= LayoutInflater.from(context);
        this.ImageLevel=imageLevel;
    }

    public int getImageLevel() {
        return ImageLevel;
    }

    public void setImageLevel(int imageLevel) {//设置图像层数,可选数值：1 2
        ImageLevel = imageLevel;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return item.length;
    }

    @Override
    public Object getItem(int position) {
        return item[position];
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

        if(item.length>0&&item.length>position){
            viewHolder.text.setText(item[position]);
            viewHolder.image.setImageLevel(ImageLevel);
        }
        return convertView;
    }
}
