package com.example.flightonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.flightonline.adapter.FlightListAdapter;
import com.example.flightonline.adapter.FlightListItem;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class InquiryFlightActivity extends BaseActivity {
    private TextView left_city;
    private TextView right_city;
    private ImageView middle_fly;
    private TextView middle_date;
    private LinearLayout pre_day;
    private LinearLayout post_day;
    private ListView flightList;
    private FlightListAdapter flightListAdapter;

    private String start_city;
    private String end_city;
    private String date;

    private ArrayList<FlightListItem> flightListItems;//用于存放搜索到的航班信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_flight);
        Intent intent=getIntent();
        start_city=intent.getStringExtra("start_city");
        end_city=intent.getStringExtra("end_city");
        date=intent.getStringExtra("date");
        initUI();
        refreshUI();//读取数据刷新界面
        setUpListener();
    }

    //连接数据库，读取数据并刷新UI
    private void refreshUI(){
        //参考代码：https://blog.csdn.net/lkp1603645756/article/details/80259912
        new Thread() {
            @Override
            public void run() {
                for(int i=0;i<4;i++){//如果连接失败重复三次
                    try {
                        Class.forName(DRIVER).newInstance();
                        connection = DriverManager.getConnection(HOST, USER, PASSWORD);
                        System.out.println("Database connected successfully!");
                        if(connection!=null)break;//连接成功后跳出循环
                    } catch (ClassNotFoundException e) {
                        Log.e("refreshUI", "run: "+e);
                    } catch (SQLException e) {
                        Log.e("refreshUI", "run: "+e);
                    } catch (Exception e) {
                        Log.e("refreshUI", "run: "+e);
                    }
                    try {
                        Thread.sleep(200);  //隔0.2秒重复
                    } catch (InterruptedException e) {
                        Log.e("refreshUI", "run: "+e);
                    }
                }

                if(connection!=null){
                    //查询使用的SQL语句
                    String sql="select * from flighttable where D_PLACE=\""+start_city+"\" and A_PLACE=\""+end_city
                            +"\" and D_TIME like \""+middle_date.getText().toString()+"%\";";
                    Log.d("SQL", sql);
                    try {
                        Statement statement = connection.createStatement();
                        // 查询
                        ResultSet rSet = statement.executeQuery(sql);
                        flightListItems=new ArrayList<FlightListItem>();
                        while (rSet.next()) {
                            String _start=rSet.getString("D_TIME");
                            String _end=rSet.getString("A_TIME");
                            int plus=0;
                            if(!_start.substring(0,10).contentEquals(_end.substring(0,10))){//简单地认为如果出发和到达不在同一天就是隔了一天
                                plus=1;
                            }
                            flightListItems.add(new FlightListItem(rSet.getString("F_NUMBER"),_start.substring(11,16),
                                    _end.substring(11,16),rSet.getString("D_POINT"),rSet.getString("A_POINT"),
                                    rSet.getInt("PRICE"),plus));
                        }
                        if(flightListAdapter!=null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    flightListAdapter.dataChange(flightListItems);
                                }
                            });
                        }
                    } catch (SQLException e) {
                        Log.e("TAG", "createStatement error");
                    }

                    try {
                        connection.close();
                    } catch (SQLException e) {
                        Log.e("TAG", "关闭连接失败");
                    }
                }
            }
        }.start();
    }


    private void test(){//用于调试和测试
        flightListItems=new ArrayList<FlightListItem>();
        flightListItems.add(new FlightListItem("中联航KN5228",
                "22:25","09:25","双流T2","虹桥T2",700,1));
        flightListItems.add(new FlightListItem("川航3U8967",
                "12:20","16:50","天府T2","虹桥T2",949,0));
        flightListItems.add(new FlightListItem("中联航KN5228",
                "22:25","09:25","双流T2","虹桥T2",700,1));
        flightListItems.add(new FlightListItem("川航3U8967",
                "12:20","16:50","天府T2","虹桥T2",949,0));
    }

    //点击返回时返回时间，修改主页面日期
    @Override
    public void onBackPressed() {
        int year,month,day;
        String date_str;
        date_str=middle_date.getText().toString();
        year=Integer.parseInt(date_str.substring(0,4));
        month=Integer.parseInt(date_str.substring(5,7));
        day=Integer.parseInt(date_str.substring(8));
        Intent intent=new Intent();
        intent.putExtra("year",year);
        intent.putExtra("month",month);
        intent.putExtra("day",day);
        setResult(RESULT_OK,intent);

        super.onBackPressed();
    }

    //将“2021年06月17日”格式转换为“2021-06-17”
    private String changeDateFormat(String date){
        char[] ch=date.toCharArray();
        String ret="";
        int i=0;
        for(char s:ch){
            if(s>='0'&&s<='9') {
                ret=ret.concat(String.valueOf(s));
                i++;
            }else{
                if(i==4||i==7){
                    ret=ret.concat("-");
                    i++;
                }
            }
            if(i==10)
                break;
        }
        return ret;
    }

    //将年月日转换为2021-06-17格式
    public static String getDateString(int year,int month,int day){
        String str="";
        str+=year+"-";
        str+=month>=10?month+"-":"0"+month+"-";
        str+=day>=10?day:"0"+day;
        return str;
    }

    //将日期转为下一天
    private void change2DayAfter(){
        int year,month,day;
        String date_str;

        date_str=middle_date.getText().toString();
        year=Integer.parseInt(date_str.substring(0,4));
        month=Integer.parseInt(date_str.substring(5,7));
        day=Integer.parseInt(date_str.substring(8));

        if(month==12&&day==31){
            year++;
            month=1;
            day=1;
        }else{
            switch (month){
                case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                    month=month%12+day/31;
                    day=day%31+1;
                    break;
                case 4: case 6: case 9: case 11:
                    month+=day/30;
                    day=day%30+1;
                    break;
                case 2:
                    if((year%4==0&&year%100!=0)||year%400==0){//闰年闰月
                        month+=day/29;
                        day=day%29+1;
                    }else{
                        month+=day/28;
                        day=day%28+1;
                    }
            }
        }
        middle_date.setText(getDateString(year,month,day));
        refreshUI();
    }

    //将日期转为前一天
    private void change2DayBefore(){
        int year,month,day;
        String date_str;

        date_str=middle_date.getText().toString();
        year=Integer.parseInt(date_str.substring(0,4));
        month=Integer.parseInt(date_str.substring(5,7));
        day=Integer.parseInt(date_str.substring(8));

        if(month==1&&day==1){
            year--;
            month=12;
            day=31;
        }else if(day>1){
            day--;
        }else{
            month--;
            switch (month){
                case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                    day=31;
                    break;
                case 4: case 6: case 9: case 11:
                    day=30;
                    break;
                case 2:
                    if((year%4==0&&year%100!=0)||year%400==0){//闰年闰月
                        day=29;
                    }else{
                        day=28;
                    }
            }
        }
        middle_date.setText(getDateString(year,month,day));
        refreshUI();
    }

    private void initUI(){
        left_city=findViewById(R.id.left_city);
        right_city=findViewById(R.id.right_city);
        middle_fly=findViewById(R.id.middle_fly);
        middle_date=findViewById(R.id.middle_date);
        pre_day=findViewById(R.id.pre_day);
        post_day=findViewById(R.id.post_day);
        flightList=findViewById(R.id.flightList);

        left_city.setText(start_city);
        right_city.setText(end_city);
        middle_date.setText(changeDateFormat(date));

        flightListAdapter=new FlightListAdapter(InquiryFlightActivity.this,new ArrayList<FlightListItem>());
        flightList.setAdapter(flightListAdapter);
    }

    private void setUpListener(){
        middle_fly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence ch=left_city.getText();
                left_city.setText(right_city.getText());
                right_city.setText(ch);
                String temp=start_city;
                start_city=end_city;
                end_city=temp;
                refreshUI();
            }
        });
        post_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change2DayAfter();
            }
        });
        pre_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change2DayBefore();
            }
        });
        middle_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog= new AlertDialog.Builder(InquiryFlightActivity.this);
                View view=(LinearLayout) getLayoutInflater().inflate(R.layout.date_dialog,null);
                final DatePicker datePicker=(DatePicker)view.findViewById(R.id.date_picker);
                Calendar calendar=Calendar.getInstance();
                final int year=calendar.get(Calendar.YEAR);       //当前年
                final int month=calendar.get(Calendar.MONTH);     //当前月
                int end_day= calendar.get(Calendar.DAY_OF_MONTH);                            //设置可订票的最后天
                int end_month=(month+5)%12;                 //最后月,可提前5个月订票
                int end_year=year+(month+5)/12;             //最后年

                //处理超出月份最大天数情况
                if(end_month==1&&end_day>=29){//对应二月份
                    if((end_year%4==0&&end_year%100!=0)||end_year%400==0){//闰年
                        end_day=29;
                    }else end_day=28;
                }else if(end_day==31&&(end_month==3||end_month==5||end_month==8||end_month==10)){//对应3 6 9 11月份
                    end_day=30;
                }

                datePicker.setMinDate(System.currentTimeMillis());//设置当前日期为最小日期
                datePicker.setMaxDate(MainActivity.getDateLong(end_month,end_day,end_year));

                dialog.setView(view);//设置弹窗布局
                dialog.setTitle("选择日期");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year=datePicker.getYear();
                        int month=datePicker.getMonth()+1;
                        int day=datePicker.getDayOfMonth();
                        middle_date.setText(getDateString(year,month,day));
                        dialog.cancel();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.create().show();
            }
        });
        //点击一条航班，打开订票页面
        flightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!MainActivity.isLoggedIn){//如果未登录先登录
                    Intent intent=new Intent(InquiryFlightActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    FlightListItem item;
                    item=(FlightListItem)flightListAdapter.getItem(position);
                    Intent intent=new Intent(InquiryFlightActivity.this,BookFlightActivity.class);
                    intent.putExtra("startCity",left_city.getText().toString());
                    intent.putExtra("endCity",right_city.getText().toString());
                    intent.putExtra("date",middle_date.getText().toString());
                    intent.putExtra("flightID",item.getFlightID());
                    intent.putExtra("startTime",item.getStartTime());
                    intent.putExtra("endTime",item.getEndTime());
                    intent.putExtra("startAirport",item.getStartAirport());
                    intent.putExtra("endAirport",item.getEndAirport());
                    intent.putExtra("price",item.getPrice());
                    intent.putExtra("plus",item.getPlus());
                    startActivity(intent);
                }

            }
        });
    }
}
