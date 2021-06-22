package com.example.flightonline;

import android.os.Bundle;
import android.view.View;

public class InquiryFlightActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_flight);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ////////////////////////////////////

        finish();
    }

    public void onBackClicked(View v){
        ////////////////////////////////////

        finish();
    }
}