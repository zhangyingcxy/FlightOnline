package com.example.flightonline;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityController {
    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity){//向List中添加活动
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){//向List中添加活动
        activities.remove(activity);
    }
    public static void finishAll(){ //在任何地方只要调用ActivityController.finishAll()即可快捷退出程序
        for (Activity activity:activities){
            if(!activity.isFinishing())
                activity.finish();
        }
    }

}