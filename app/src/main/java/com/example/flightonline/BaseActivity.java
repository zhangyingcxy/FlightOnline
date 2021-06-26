package com.example.flightonline;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;

public class BaseActivity extends AppCompatActivity {
    //用于连接数据库的信息
    public String ip = "121.36.199.16";
    public int port = 3306;
    public String dbName = "flightonline";
    public String HOST = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
    public String USER = "app";
    public String PASSWORD = "123456";
    // Java数据库连接JDBC驱动
    public String DRIVER = "com.mysql.jdbc.Driver";
    public Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityController.addActivity(this);//将当前正在创建的活动添加到活动管理器中
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//启用扩展支持库窗口功能，参数：去掉标题栏
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);//将正在退出的活动从活动管理器中删除
    }

    public void toastForShort(final String str){//快捷使用toast
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    public void toastForLong(final String str){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
}
