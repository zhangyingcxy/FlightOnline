package com.example.flightonline;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.flightonline.adapter.ExitListAdapter;
import com.example.flightonline.adapter.OrderAdapter;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocatedCity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {//继承自BaseActivity，方便进行管理
    static final int REQUEST_CODE_INQUIRY=0;

    private LinearLayout pageLayout;
    private ImageView page1;
    private ImageView page2;
    private TextView startCity;
    private TextView endCity;
    private TextView date;
    private ImageView changeDirection;
    private Button enquireButton;
    private View include1;
    private ImageView portrait0;
    private ImageView portrait1;
    private LinearLayout notLoggedIn;
    private LinearLayout loggedIn;
    private ListView exitList;//退出登录和退出程序
    private LinearLayout orderLayout;
    private ListView orderBar;//订单
    private ListView orderList;//详细订单列表
    private View include2;

    private ExitListAdapter exitListAdapter;
    private OrderAdapter orderAdapter;

    public static boolean isPageLeft=true;//用于标志当前显示的页面（主界面/用户界面）
    public static boolean isLoggedIn=false;//标志是否已登录

    List<HotCity>   hotCities;//检索城市时使用的热门城市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initHotCities();
        initUI();
        setUpListener();


    }

    @Override
    protected void onResume() {//重新回到该页面需要检查登录状态是否在其他地方被修改
        super.onResume();
        if(!isPageLeft){//在该条件下一定不是第一次创建
            if(isLoggedIn){
                loggedIn.setVisibility(View.VISIBLE);
                notLoggedIn.setVisibility(View.GONE);
                orderLayout.setVisibility(View.VISIBLE);
                exitListAdapter.setLength(2);
            }else{
                loggedIn.setVisibility(View.GONE);
                notLoggedIn.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
                orderLayout.setVisibility(View.GONE);
                exitListAdapter.setLength(1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_INQUIRY:
                if(resultCode==RESULT_OK){
                    int year,month,day;
                    year=data.getIntExtra("year",0);
                    month=data.getIntExtra("month",0);
                    day=data.getIntExtra("day",0);
                    if(year!=0&&day!=0)
                        date.setText(getDateChineseString(year,month+1,day));
                }
                break;
            default:
        }
    }

    //获取年月日的字符串形式：2021年06月17日
    private String getDateChineseString(int year,int month,int day){
        String str="";
        str+=year+"年";
        str+=month>=10?month+"月":"0"+month+"月";
        str+=day>=10?day+"日":"0"+day+"日";
        return str;
    }

    //将年月日转换为long类型的日期
    private long getDateLong(int month,int day,int year){
        String str="";
        str+=year;
        str+="-";
        str+=(month>=9)?(month+1):"0"+(month+1);
        str+="-";
        str+=(day>=10)?day:"0"+day;
        str+=" 12:00:00";

        //以下参考：https://blog.csdn.net/chentaishan/article/details/106389256
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date _date=null;
        try {
            _date = (Date)sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert _date != null;
        return _date.getTime();
    }

    private void initUI(){
        pageLayout=findViewById(R.id.pageLayout);
        page1=findViewById(R.id.page1);
        page2=findViewById(R.id.page2);

        include1=findViewById(R.id.include1);
        startCity=(TextView)include1.findViewById(R.id.startCity);
        endCity=(TextView)include1.findViewById(R.id.endCity);
        date=(TextView)include1.findViewById(R.id.date);
        changeDirection=(ImageView)include1.findViewById(R.id.changeDirection);
        enquireButton=(Button)include1.findViewById(R.id.enquireButton);

        include2=findViewById(R.id.include2);
        portrait0=(ImageView)include2.findViewById(R.id.portrait0);
        portrait1=(ImageView)include2.findViewById(R.id.portrait1);
        notLoggedIn=(LinearLayout)include2.findViewById(R.id.notLoggedIn);
        loggedIn=(LinearLayout)include2.findViewById(R.id.LoggedIn);
        exitList=(ListView) include2.findViewById(R.id.exitList);
        orderLayout=(LinearLayout) include2.findViewById(R.id.orderLayout);
        orderBar=(ListView) include2.findViewById(R.id.orderBar);
        orderList=(ListView) include2.findViewById(R.id.orderList);

        String[] exitOps={"退出程序","退出登录"};
        exitListAdapter=new ExitListAdapter(MainActivity.this,exitOps,1);
        exitList.setAdapter(exitListAdapter);
        String[] arr={"我的订单"};
        orderAdapter=new OrderAdapter(MainActivity.this,arr,1);
        orderBar.setAdapter(orderAdapter);


        //打开程序时显示主界面
        page1.setImageLevel(0);
        page2.setImageLevel(3);
        include1.setVisibility(View.VISIBLE);
        include2.setVisibility(View.GONE);

        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);       //当前年
        final int month=calendar.get(Calendar.MONTH);     //当前月
        final int day=calendar.get(Calendar.DAY_OF_MONTH);//当前日
        date.setText(getDateChineseString(year,month+1,day));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpListener(){
        onTabberTouchListener ontabberTouchListener=new onTabberTouchListener();
        page1.setOnTouchListener(ontabberTouchListener);
        page2.setOnTouchListener(ontabberTouchListener);

        onPosClickListener onposClickListener=new onPosClickListener();
        startCity.setOnClickListener(onposClickListener);
        endCity.setOnClickListener(onposClickListener);

        changeDirection.setOnClickListener(new View.OnClickListener(){//交换出发地和目的地
            @Override
            public void onClick(View v) {
                String tmp=startCity.getText().toString();
                startCity.setText(endCity.getText());
                endCity.setText(tmp);
            }
        });

        enquireButton.setOnClickListener(new View.OnClickListener() {//开启查询航班页面,传递要查询的航班信息
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,InquiryFlightActivity.class);
                intent.putExtra("start_city",startCity.getText().toString());
                intent.putExtra("end_city",endCity.getText().toString());
                intent.putExtra("date",date.getText().toString());
                startActivityForResult(intent,REQUEST_CODE_INQUIRY);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                View view=(LinearLayout) getLayoutInflater().inflate(R.layout.date_dialog,null);
                final DatePicker datePicker=(DatePicker)view.findViewById(R.id.date_picker);
                Calendar calendar=Calendar.getInstance();
                final int year=calendar.get(Calendar.YEAR);       //当前年
                final int month=calendar.get(Calendar.MONTH);     //当前月
                final int day=calendar.get(Calendar.DAY_OF_MONTH);//当前日
                int end_day=day;                            //设置可订票的最后天
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

                datePicker.setMinDate(System.currentTimeMillis());//设置最小日期为当前日期
                datePicker.setMaxDate(getDateLong(end_month,end_day,end_year));

                dialog.setView(view);//设置弹窗布局
                dialog.setTitle("选择日期");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year=datePicker.getYear();
                        int month=datePicker.getMonth()+1;
                        int day=datePicker.getDayOfMonth();
                        date.setText(getDateChineseString(year,month,day));
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

        portrait0.setOnClickListener(new View.OnClickListener() {//点击头像进入登陆界面
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        exitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//点击退出登录/程序的操作
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id==0){//退出程序
                    ActivityController.finishAll();
                    android.os.Process.killProcess(android.os.Process.myPid());//杀掉当前进程
                }else if(id==1){//退出登录
                    isLoggedIn=false;
                    loggedIn.setVisibility(View.GONE);
                    notLoggedIn.setVisibility(View.VISIBLE);
                    orderLayout.setVisibility(View.GONE);
                    orderList.setVisibility(View.GONE);
                    exitListAdapter.setLength(1);
                }
            }
        });
        orderBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {//点击我的订单操作
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id==0){
                    if(orderAdapter.getImageLevel()==1){
                        orderList.setVisibility(View.VISIBLE);
                        orderAdapter.setImageLevel(2);
                    }else if(orderAdapter.getImageLevel()==2){
                        orderList.setVisibility(View.GONE);
                        orderAdapter.setImageLevel(1);
                    }
                }
            }
        });
    }

    private void initHotCities(){
        hotCities=new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100")); //code为城市代码
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        hotCities.add(new HotCity("成都", "四川", "101270101"));
    }

    private void selectPos(final boolean isStartCity){
        //initHotCities();

        //以下部分引用代码：https://github.com/zaaach/CityPicker
        CityPicker.from(MainActivity.this) //activity或者fragment
                .enableAnimation(true)	//启用动画效果，默认无
                //.setAnimationStyle(anim)	//自定义动画
                .setLocatedCity(new LocatedCity("成都", "四川", "101270101")/*null*/)  //APP自身已定位的城市，传null会自动定位（默认）
                .setHotCities(hotCities)	//指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        //toastForShort(data.getName());
                        if(isStartCity)
                            startCity.setText(data.getName());
                        else
                            endCity.setText(data.getName());
                    }

                    @Override
                    public void onCancel(){
                        //toastForShort("取消选择");
                    }

                    @Override
                    public void onLocate() {
                        //定位接口，需要APP自身实现，这里模拟一下定位
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //定位完成之后更新数据
                                //CityPicker.getInstance()
                                //.locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                                toastForShort("xxxxxx");///////////////
                            }
                        }, 3000);
                    }
                })
                .show();
    }

    /*
     * 借鉴代码：https://github.com/LCiZY/ShareOnLan
     * 底部两个图标触摸的监听器
     * 实现功能：按下去图标缩小，松开后变大；图标颜色改变，左右页面切换
     */
    class onTabberTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(v.getId() == page1.getId()){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN://按下去
                        //按钮图标变换，小图标
                        changeHomeTabSize(-1);
                        break;
                    case MotionEvent.ACTION_UP://弹起来
                        //按钮图标变换,正常图标
                        page1.setImageLevel(0);
                        page2.setImageLevel(3);
                        //切换到左页面
                        include1.setVisibility(View.VISIBLE);
                        include2.setVisibility(View.GONE);
                        isPageLeft=true;
                        changeHomeTabSize(1);
                        break;
                    case MotionEvent.ACTION_CANCEL://保持按下操作时手指移动到其他控件上时
                        page1.setImageLevel(1);
                        changeHomeTabSize(1);
                        break;
                }
            }else if(v.getId() == page2.getId()){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //按钮图标变换，小图标
                        changeFileTabSize(-1);
                        break;
                    case MotionEvent.ACTION_UP:
                        //按钮图标变换,正常图标
                        //按钮图标变换
                        page1.setImageLevel(1);
                        page2.setImageLevel(2);
                        //切换到右页面
                        include1.setVisibility(View.GONE);
                        include2.setVisibility(View.VISIBLE);
                        isPageLeft=false;
                        //检查登录状态
                        if(isLoggedIn){
                            loggedIn.setVisibility(View.VISIBLE);
                            notLoggedIn.setVisibility(View.GONE);
                            orderLayout.setVisibility(View.VISIBLE);
                            exitListAdapter.setLength(2);
                        }else{
                            loggedIn.setVisibility(View.GONE);
                            notLoggedIn.setVisibility(View.VISIBLE);
                            orderList.setVisibility(View.GONE);
                            orderLayout.setVisibility(View.GONE);
                            exitListAdapter.setLength(1);
                        }
                        changeFileTabSize(1);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        page2.setImageLevel(3);
                        changeFileTabSize(1);
                        break;
                }
            }
            return true;
        }
        //flag为1表示增大图标尺寸，-1反之
        private void changeHomeTabSize(int flag){
            ViewGroup.LayoutParams sizeParamHome=page1.getLayoutParams();
            sizeParamHome.height = sizeParamHome.height+10*flag;
            sizeParamHome.width = sizeParamHome.width+10*flag;
            page1.setLayoutParams(sizeParamHome);
        }
        private void changeFileTabSize(int flag){
            ViewGroup.LayoutParams sizeParamFile=page2.getLayoutParams();
            sizeParamFile.height = sizeParamFile.height+10*flag;
            sizeParamFile.width = sizeParamFile.width+10*flag;
            page2.setLayoutParams(sizeParamFile);
        }
    }

    //出发城市和到达城市两个TextView的触摸监听器
    class onPosClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == startCity.getId()){
                selectPos(true);
            }else if(v.getId()==endCity.getId()){
                selectPos(false);
            }
        }
    }
}