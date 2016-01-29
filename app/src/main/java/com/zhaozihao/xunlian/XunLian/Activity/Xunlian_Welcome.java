package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;


public class Xunlian_Welcome extends Activity implements Runnable {

    static SharedPreferences sp;
    SharedPreferences.Editor editor;
    AppManger appManger;
	
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
        setContentView(R.layout.welcome);
        appManger = AppManger.getAppManger();
        appManger.addActivity(Xunlian_Welcome.this);
        new Thread(this).start();
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
  
    public void run() {  
        try {
            Thread.sleep(2000);

            sp = getSharedPreferences("isFirstUse", Context.MODE_PRIVATE);
            if(sp.getBoolean("isFirstUse",true)){
            }else{
            	//startActivity(new Intent(Welcome.this, GuideActivity.class));
                appManger.finishActivity(Xunlian_Welcome.this);

            }
            sp = getSharedPreferences("PatternUnlockpassword", Xunlian_Welcome.this.MODE_PRIVATE);
            editor = sp.edit();
            if(sp.getBoolean("isStrat",false)){

                startActivity(new Intent(Xunlian_Welcome.this, Xunlian_LockActivity.class));
                appManger.finishActivity(Xunlian_Welcome.this);
            }else{

                sp = getSharedPreferences("autoLogin", Xunlian_Welcome.this.MODE_PRIVATE);
                editor = sp.edit();

                if(sp.getBoolean("isAutoLogin",false)){

                startActivity(new Intent(Xunlian_Welcome.this, Xunlian_MainActivity.class));
                    appManger.finishActivity(Xunlian_Welcome.this);
            }  else{
                startActivity(new Intent(Xunlian_Welcome.this, Xunlian_Account.class));
                    appManger.finishActivity(Xunlian_Welcome.this);
            }
            }
            finish();
       
        } catch (Exception e) {
  
        }  
    }  
}

//
//