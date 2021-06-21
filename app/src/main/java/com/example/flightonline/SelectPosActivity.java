package com.example.flightonline;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;

import java.util.ArrayList;
import java.util.List;

public class SelectPosActivity extends BaseActivity {
    private String cityName="";
    List<HotCity>   hotCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pos);

        initHotCities();

        //引用代码：https://github.com/zaaach/CityPicker
        CityPicker.from(SelectPosActivity.this) //activity或者fragment
                .enableAnimation(true)	//启用动画效果，默认无
                //.setAnimationStyle(anim)	//自定义动画
                .setLocatedCity(null/*new LocatedCity("杭州", "浙江", "101210101")*/)  //APP自身已定位的城市，传null会自动定位（默认）
                .setHotCities(hotCities)	//指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        Toast.makeText(getApplicationContext(), data.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(){
                        Toast.makeText(getApplicationContext(), "取消选择", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLocate() {
                        //定位接口，需要APP自身实现，这里模拟一下定位
                        new Handler().postDelayed(new Runnable() {
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

    private void initHotCities(){
        hotCities=new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100")); //code为城市代码
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        hotCities.add(new HotCity("成都", "四川", "101270101"));
    }

//    //退出活动时返回城市名
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent=new Intent();
//        intent.putExtra("city_name",cityName);
//        setResult(RESULT_OK,intent);
//        finish();
//    }
}