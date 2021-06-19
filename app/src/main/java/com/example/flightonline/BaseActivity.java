package com.example.flightonline;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
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
