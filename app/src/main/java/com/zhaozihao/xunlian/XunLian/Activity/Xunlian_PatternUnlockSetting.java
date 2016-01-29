package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.PatternUnlock.PatternUnlock;
import com.zhaozihao.xunlian.R;

import java.util.List;


public class Xunlian_PatternUnlockSetting extends Activity {
    List<Integer> passList;
    Tools tools = new Tools(Xunlian_PatternUnlockSetting.this);
    Boolean istwo = false;
    AppManger appManger;
    MyToast myToast = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pattern_unlock_setting);
        appManger = AppManger.getAppManger();
        appManger.addActivity(Xunlian_PatternUnlockSetting.this);
        final PatternUnlock lock = (PatternUnlock) findViewById(R.id.LockView);
        lock.setOnDrawFinishedListener(new PatternUnlock.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {

                if (passList.size() < 4) {
                    myToast.showToast(Xunlian_PatternUnlockSetting.this, "密码不能少于4个点", Toast.LENGTH_SHORT);
                    return false;
                } else {
                    SharedPreferences sp = Xunlian_PatternUnlockSetting.this.getSharedPreferences("PatternUnlockpassword", Xunlian_PatternUnlockSetting.this.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    StringBuilder sb = new StringBuilder();
                    for (Integer i : passList) {
                        sb.append(i);
                    }

                    if (istwo) {
                        String password2 = tools.MD5(sb.toString());
                        editor.putString("password2", password2);
                        editor.commit();
                        final String strpassword1 = sp.getString("password1", "");
                        final String strpassword2 = sp.getString("password2", "");
                        if (strpassword1.equals(strpassword2)) {
                            Xunlian_PatternUnlockSetting.this.passList = passList;
                            String password3 = tools.MD5(sb.toString());
                            editor.putString("password", password3);
                            editor.putString("password1", "");
                            editor.putString("password2", "");
                            editor.putBoolean("isStrat", true);
                            editor.commit();
                            myToast.showToast(Xunlian_PatternUnlockSetting.this, "保存完成", Toast.LENGTH_SHORT);
                            lock.resetPoints();
                            SharedPreferences sp1;
                            sp1 = getSharedPreferences("autoLogin", MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sp1.edit();
                            editor1.putBoolean("isAutoLogin", true);
                            editor1.commit();
                            istwo = false;
                            appManger.finishActivity(Xunlian_PatternUnlockSetting.this);
                            return true;
                        } else {
                            istwo = false;
                            myToast.showToast(Xunlian_PatternUnlockSetting.this, "两次输入不一致", Toast.LENGTH_SHORT);
                            lock.resetPoints();
                        }

                    } else {
                        String password1 = tools.MD5(sb.toString());
                        editor.putString("password1", password1);
                        editor.commit();
                        istwo = true;
                        lock.resetPoints();
                        myToast.showToast(Xunlian_PatternUnlockSetting.this, "重复刚才的图案", Toast.LENGTH_SHORT);
                    }
                }
                return false;
            }
        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    public String getAccount() {
        SharedPreferences sp = null;
        sp = Xunlian_PatternUnlockSetting.this.getSharedPreferences("Account", Context.MODE_PRIVATE);
        String str = sp.getString("account","");
        return  str;
    }
}
