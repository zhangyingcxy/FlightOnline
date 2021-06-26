package com.example.flightonline;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends BaseActivity {
    private EditText account;
    private EditText password;
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account=findViewById(R.id.account);
        password=findViewById(R.id.password);
    }

    public void onLoginClicked(View v){
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
                    String sql="select PASSWORD from accounttable where TELE_NUMBER=\""+account.getText().toString()+"\";";
                    Log.d("SQL", sql);
                    try {
                        Statement statement = connection.createStatement();
                        // 查询
                        ResultSet rs = statement.executeQuery(sql);
                        if (rs.next()) {
                            if(rs.getString("PASSWORD").contentEquals(password.getText().toString())){
                                MainActivity.isLoggedIn=true;
                                toastForShort("登录成功！");
                                flag=true;
                            }else{
                                toastForLong("密码错误！");
                            }
                        }else
                            toastForLong("账号不存在！");
                    } catch (SQLException e) {
                        Log.e("TAG", "createStatement error");
                    }

                    try {
                        connection.close();
                    } catch (SQLException e) {
                        Log.e("TAG", "关闭连接失败");
                    }
                }
                if(flag)finish();
            }
        }.start();
    }

}