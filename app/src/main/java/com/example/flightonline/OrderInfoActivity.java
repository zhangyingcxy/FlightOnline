package com.example.flightonline;

import android.os.Bundle;
import android.view.View;

public class OrderInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
    }
    public void onBackClicked(View v){
        finish();
    }
}