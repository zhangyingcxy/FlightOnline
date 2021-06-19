package com.example.flightonline;

import android.os.Bundle;
import android.view.View;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginClicked(View v){
        MainActivity.isLoggedIn=true;////////////////////////////////////////////////
        toastForShort("登录成功！");
        finish();
    }

}