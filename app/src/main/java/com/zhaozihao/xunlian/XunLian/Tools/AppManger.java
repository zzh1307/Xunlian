package com.zhaozihao.xunlian.XunLian.Tools;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaozihao on 2015/12/3 0003.
 */
public class AppManger {

    private List<Activity> mActivityList = new LinkedList<Activity>();
    private static AppManger instance;

    private AppManger(){}
    /**
     * 单一实例
     */
    public static AppManger getAppManger(){
        if(instance==null){
            instance=new AppManger();
        }
        return instance;
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity){
        mActivityList.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if(activity!=null){
            mActivityList.remove(activity);
            activity.finish();
            activity=null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        while(mActivityList.size() > 0) {
            Activity activity = mActivityList.get(mActivityList.size() - 1);
            mActivityList.remove(mActivityList.size() - 1);
            activity.finish();
        }
    }
    /**
     * 退出应用程序
     */
    public void AppExit() {
       // Countly.sharedInstance().onStop();
        try {
            finishAllActivity();
        } catch (Exception e) { }
    }
}
