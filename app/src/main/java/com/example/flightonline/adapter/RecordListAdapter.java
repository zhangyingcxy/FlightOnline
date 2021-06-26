package com.example.flightonline.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.flightonline.OrderInfoActivity;
import com.example.flightonline.R;

import java.util.ArrayList;

public class RecordListAdapter extends BaseAdapter {
    private ArrayList<RecordListItem> items;
    private LayoutInflater layoutInflater;

    public RecordListAdapter(Context context, ArrayList<RecordListItem> items){
        this.items=items;
        this.layoutInflater=LayoutInflater.from(context);
    }

    public void dataChange(ArrayList<RecordListItem> items){
        this.items=items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public final TextView start_time;
        public final TextView end_time;
        public final TextView left_airport;
        public final TextView right_airport;
        public final TextView flightID;
        public final TextView plus1;
        public final TextView right_price;
        public final TextView passenger_name;
        public final TextView left_date;
        public final TextView right_date;
        public final View root;

        public ViewHolder(View root) {
            start_time=(TextView)root.findViewById(R.id.start_time);
            end_time=(TextView)root.findViewById(R.id.end_time);
            left_airport=(TextView)root.findViewById(R.id.left_airport);
            right_airport=(TextView)root.findViewById(R.id.right_airport);
            flightID=(TextView)root.findViewById(R.id.flightID);
            plus1=(TextView)root.findViewById(R.id.plus1);
            right_price=(TextView)root.findViewById(R.id.right_price);
            passenger_name=(TextView)root.findViewById(R.id.passenger_name);
            left_date=(TextView)root.findViewById(R.id.left_date);
            right_date=(TextView)root.findViewById(R.id.right_date);
            this.root = root;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.record_item,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }

        if(items.size()>0&&items.size()>position){
            viewHolder.start_time.setText(items.get(position).getStartTime());
            viewHolder.end_time.setText(items.get(position).getEndTime());
            viewHolder.left_airport.setText(items.get(position).getStartAirport());
            viewHolder.right_airport.setText(items.get(position).getEndAirport());
            viewHolder.flightID.setText(items.get(position).getFlightID());
            viewHolder.right_price.setText("￥"+items.get(position).getPrice());
            viewHolder.passenger_name.setText("出行人："+items.get(position).getName());
            viewHolder.left_date.setText(items.get(position).getDate());
            viewHolder.right_date.setText(OrderInfoActivity.getEndDate(items.get(position).getDate(),items.get(position).getPlus()));
            int plus=items.get(position).getPlus();
            if(plus==0){
                viewHolder.plus1.setText("");
            }else if(plus>0&&plus<=9){
                viewHolder.plus1.setText("+"+plus);
            }
        }

        return convertView;
    }
}
