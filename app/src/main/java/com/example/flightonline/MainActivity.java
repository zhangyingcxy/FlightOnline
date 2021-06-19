package com.example.flightonline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity {//继承自BaseActivity，方便进行管理
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
    private ListView userList;
    private View include2;

    public static boolean isPageLeft=true;//用于标志当前显示的页面（主界面/用户界面）
    public static boolean isLoggedIn=false;//标志是否已登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            }else{
                loggedIn.setVisibility(View.GONE);
                notLoggedIn.setVisibility(View.VISIBLE);
            }
        }
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
        userList=(ListView) include2.findViewById(R.id.userList);

        //打开程序时显示主界面
        page1.setImageLevel(0);
        page2.setImageLevel(3);
        include1.setVisibility(View.VISIBLE);
        include2.setVisibility(View.GONE);

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
                startActivity(intent);
            }
        });

        portrait0.setOnClickListener(new View.OnClickListener() {//点击头像进入登陆界面
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
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
                        }else{
                            loggedIn.setVisibility(View.GONE);
                            notLoggedIn.setVisibility(View.VISIBLE);
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
                Intent intent=new Intent(MainActivity.this,SelectPosActivity.class);
                //SelectPosActivity销毁时会返回数据给当前活动，在onActivityResult中处理
                startActivityForResult(intent,1);
            }else if(v.getId()==endCity.getId()){
                Intent intent=new Intent(MainActivity.this,SelectPosActivity.class);
                startActivityForResult(intent,2);
            }
        }
    }

    //根据SelectPosActivity活动的返回值设置相应的城市名
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK && data!=null && !data.getStringExtra("city_name").contentEquals("")){
                    startCity.setText(data.getStringExtra("city_name"));
                    //startCity.setText(data.getStringExtra(SelectPosActivity.KEY_PICKED_CITY));
                }
            case 2:
                if (resultCode==RESULT_OK && data!=null && !data.getStringExtra("city_name").contentEquals("")){
                    endCity.setText(data.getStringExtra("city_name"));
                    //endCity.setText(data.getStringExtra(SelectPosActivity.KEY_PICKED_CITY));
                }
        }
    }
}