package com.example.flightonline;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BookFlightActivity extends BaseActivity {
    private TextView start_city_2;
    private TextView end_city_2;
    private TextView date_2;
    private TextView flight_id_2;
    private TextView start_time_2;
    private TextView end_time_2;
    private TextView start_airport_2;
    private TextView end_airport_2;
    private TextView price_2;
    private TextView interval;

    private String startCity;
    private String endCity;
    private String date;
    private String flightID;
    private String startTime;
    private String endTime;
    private String startAirport;
    private String endAirport;
    private int price;
    private int plus;
    private String p_name;
    private String tele_number;
    private String id_number;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_flight);
        Intent intent=getIntent();

        start_city_2=findViewById(R.id.start_city_2);
        end_city_2=findViewById(R.id.end_city_2);
        date_2=findViewById(R.id.date_2);
        flight_id_2=findViewById(R.id.flight_id_2);
        start_time_2=findViewById(R.id.start_time_2);
        end_time_2=findViewById(R.id.end_time_2);
        start_airport_2=findViewById(R.id.start_airport_2);
        end_airport_2=findViewById(R.id.end_airport_2);
        price_2=findViewById(R.id.price_2);
        interval=findViewById(R.id.interval);

        startCity=intent.getStringExtra("startCity");
        endCity=intent.getStringExtra("endCity");
        date=intent.getStringExtra("date");
        flightID=intent.getStringExtra("flightID");
        startTime=intent.getStringExtra("startTime");
        endTime=intent.getStringExtra("endTime");
        startAirport=intent.getStringExtra("startAirport");
        endAirport=intent.getStringExtra("endAirport");
        price=intent.getIntExtra("price",-1);
        plus=intent.getIntExtra("plus",0);
        p_name="张颖";
        tele_number="12345678910";
        id_number="2314562*********25";

        start_city_2.setText(startCity);
        end_city_2.setText(endCity);
        date_2.setText(date);
        flight_id_2.setText(flightID);
        start_time_2.setText(startTime);
        end_time_2.setText(endTime);
        start_airport_2.setText(startAirport);
        end_airport_2.setText(endAirport);
        price_2.setText("￥"+price);
        interval.setText(getIntervalString(startTime,endTime));
    }

    //根据开始时间和结束时间求时间间隔，均为String类型
    //如12:20到16:50，间隔为4时30分
    public static String getIntervalString(String start,String end){
        if(start==null||end==null){
            return "-";
        }
        int hour1=Integer.parseInt(start.substring(0,2));
        int hour2=Integer.parseInt(end.substring(0,2));
        int min1=Integer.parseInt(start.substring(3,5));
        int min2=Integer.parseInt(end.substring(3,5));
        int delta_min,delta_hour;

        if(min1<=min2){
            delta_min=min2-min1;
            delta_hour=(hour1<hour2)?hour2-hour1:hour2+24-hour1;
        }else{
            delta_min=min2+60-min1;
            delta_hour=(hour1<hour2-1)?hour2-1-hour1:hour2+23-hour1;
        }
        return delta_hour+"时"+delta_min+"分";
    }

    public void onBackClicked(View v){
        finish();
    }

    public void onBtnClicked(View v){
        AlertDialog.Builder dialog1=new AlertDialog.Builder(BookFlightActivity.this);
        dialog1.setTitle("确认订购此机票？");
        dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread() {//订票操作：修改远程数据库
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
                            String sql_query="select * from recordtable where D_DATE=\""+ date+"\" and D_TIME=\""+startTime+"\" and F_NUMBER=\""+flightID
                                    +"\" and NAME= \""+p_name+"\" and ID_NUMBER=\""+id_number+"\";";
                            Log.d("SQL", sql_query);
                            try {
                                Statement statement = connection.createStatement();
                                //先查找是否有该记录
                                ResultSet rs=statement.executeQuery(sql_query);
                                if(rs.next()){
                                    toastForLong("已订过该票，请勿重复购票");
                                }
                                else{
                                    String sql_insert="insert into recordtable(F_NUMBER,D_TIME,A_TIME,D_PLACE,A_PLACE,D_POINT,A_POINT,PRICE," +
                                            "PLUS,NAME,TELE_NUMBER,ID_NUMBER,D_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                    PreparedStatement ps = null;
                                    ps=connection.prepareStatement(sql_insert);
                                    ps.setString(1,flightID);
                                    ps.setString(2,startTime);
                                    ps.setString(3,endTime);
                                    ps.setString(4,startCity);
                                    ps.setString(5,endCity);
                                    ps.setString(6,startAirport);
                                    ps.setString(7,endAirport);
                                    ps.setInt(8,price);
                                    ps.setInt(9,plus);
                                    ps.setString(10,p_name);
                                    ps.setString(11,tele_number);
                                    ps.setString(12,id_number);
                                    ps.setString(13,date);

                                    if(ps.executeUpdate()==0){
                                        toastForLong("购票失败！");
                                    }else{
                                        toastForLong("购票成功！\n请返回用户界面查看订单");
                                    }
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
        });
        dialog1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消操作
            }
        });
        dialog1.create().show();
    }
}