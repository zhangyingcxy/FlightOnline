package com.example.flightonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class OrderInfoActivity extends BaseActivity {
    private TextView state;
    private TextView flight_id;
    private TextView start_airport;
    private TextView end_airport;
    private TextView start_time;
    private TextView end_time;
    private TextView start_date;
    private TextView end_date;
    private TextView price;
    private TextView passenger;
    private TextView id_number;
    private TextView tele_number;
    private TextView buttonText;

    private String _flightID;
    private String _startAirport;
    private String _endAirport;
    private String _startTime;
    private String _endTime;
    private String _startDate;
    private String _endDate;
    private int _price;
    private int _plus;
    private String _passenger;
    private String _idNumber;
    private String _teleNumber;
    private String currentDate;//今天日期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        Intent intent=getIntent();

        state=findViewById(R.id.state);
        flight_id=findViewById(R.id.flight_id);
        start_airport=findViewById(R.id.start_airport);
        end_airport=findViewById(R.id.end_airport);
        start_time=findViewById(R.id.start_time);
        end_time=findViewById(R.id.end_time);
        start_date=findViewById(R.id.start_date);
        end_date=findViewById(R.id.end_date);
        price=findViewById(R.id.price);
        passenger=findViewById(R.id.passenger);
        id_number=findViewById(R.id.id_number);
        tele_number=findViewById(R.id.tele_number);
        buttonText=findViewById(R.id.buttonText);

        _flightID=intent.getStringExtra("flightID");
        _startAirport=intent.getStringExtra("startAirport");
        _endAirport=intent.getStringExtra("endAirport");
        _startTime=intent.getStringExtra("startTime");
        _endTime=intent.getStringExtra("endTime");
        _startDate=intent.getStringExtra("date");
        _plus=intent.getIntExtra("plus",0);
        _endDate=getEndDate(_startDate,_plus);
        _price=intent.getIntExtra("price",-1);
        _passenger=intent.getStringExtra("name");
        _idNumber=intent.getStringExtra("idNumber");
        _teleNumber=intent.getStringExtra("teleNumber");


        flight_id.setText(_flightID);
        start_airport.setText(_startAirport);
        end_airport.setText(_endAirport);
        start_time.setText(_startTime);
        end_time.setText(_endTime);
        start_date.setText(_startDate);
        end_date.setText(_endDate);
        price.setText(""+_price);
        passenger.setText(_passenger);
        id_number.setText(_idNumber);
        tele_number.setText(_teleNumber);

        Calendar calendar=Calendar.getInstance();
        currentDate=InquiryFlightActivity.getDateString(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH));
        if(currentDate.compareTo(_startDate)<=0){//还未到出发日期
            buttonText.setText("退票");
            state.setText("未出行");
        }else {
            buttonText.setText("删除");
            state.setText("已出行");
        }
    }

    //获取到达日期
    //比如根据2021-07-17号和plus=1得到到达日期为2021-07-18
    public static String getEndDate(String str,int p){
        if (p==1){
            int year=Integer.parseInt(str.substring(0,4));
            int month=Integer.parseInt(str.substring(5,7));
            int day=Integer.parseInt(str.substring(8));

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
            return InquiryFlightActivity.getDateString(year,month,day);
        }else return str;
    }

    public void onBackClicked(View v){
        finish();
    }

    public void onBtn1Clicked(View v){//退票或删除订单
        AlertDialog.Builder dialog=new AlertDialog.Builder(OrderInfoActivity.this);
        if(currentDate.compareTo(_startDate)<=0) {//还未到出发日期
            dialog.setTitle("确认退票？");
        }else dialog.setTitle("确认删除该订单？");
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                            String sql="delete from recordtable where D_DATE=\""+_startDate+"\" and D_TIME=\""+_startTime+"\" and F_NUMBER=\""+_flightID
                                    +"\" and NAME= \""+_passenger+"\" and ID_NUMBER=\""+_idNumber+"\";";
                            Log.d("SQL", sql);
                            try {
                                Statement statement = connection.createStatement();
                                PreparedStatement ps = null;
                                ps=connection.prepareStatement(sql);
                                if(ps.executeUpdate()>0){
                                    toastForLong("操作成功！");
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
                finish();
            }
        });
        dialog.create().show();
    }
}