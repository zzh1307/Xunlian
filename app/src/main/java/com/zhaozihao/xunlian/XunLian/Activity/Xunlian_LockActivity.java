package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zhaozihao.xunlian.PatternUnlock.PatternUnlock;
import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;

import java.util.List;


public class Xunlian_LockActivity extends Activity {
    AlertDialog.Builder ad;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    SharedPreferences sp1;
    SharedPreferences.Editor editor1;
    int num = 3;
    TextView Lock_Title = null;
    AppManger appManger;
    Boolean isReset = false;
    Tools tools = new Tools(Xunlian_LockActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock);
        PatternUnlock patterbUnlock = (PatternUnlock) findViewById(R.id.LockView);
        Lock_Title = (TextView) findViewById(R.id.Lock_Title);
        sp = Xunlian_LockActivity.this.getSharedPreferences("PatternUnlockpassword", Xunlian_LockActivity.this.MODE_PRIVATE);
        editor = sp.edit();
        Intent intent = getIntent();
        isReset = intent.getBooleanExtra("isReset",false);
        if(isReset){
            Lock_Title.setText("确认所设图案");
        }
        appManger = AppManger.getAppManger();
        appManger.addActivity(Xunlian_LockActivity.this);
        patterbUnlock.setOnDrawFinishedListener(new PatternUnlock.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                String strPass = sp.getString("password", "");
                StringBuilder sb = new StringBuilder();
                for (Integer i : passList) {
                    sb.append(i);
                }

                if(num>0){
                    if (strPass.equals(tools.MD5(sb.toString()))) {
                        num = 3;
                        sp1 = getSharedPreferences("autoLogin", Xunlian_LockActivity.this.MODE_PRIVATE);
                        editor1 = sp1.edit();
                         if(sp1.getBoolean("isAutoLogin",false)){
                             if(isReset){
                                 startActivity(new Intent(Xunlian_LockActivity.this, Xunlian_PatternUnlockSetting.class));
                             }else{
                                 startActivity(new Intent(Xunlian_LockActivity.this, Xunlian_MainActivity.class));
                             }
                        }  else{
                            startActivity(new Intent(Xunlian_LockActivity.this, Xunlian_Account.class));
                        }
                        finish();
                        return true;
                    }
                    MyToast.showToast(Xunlian_LockActivity.this, "密码错误,您还有" + num + "次机会", Toast.LENGTH_SHORT);
                    num--;
                    return false;
                }else{
                    creat();
                    num = 3;
                }
                return false;
            }
        });
    }
    public void creat(){
        ad = new AlertDialog.Builder(Xunlian_LockActivity.this)
                .setTitle("输入错误")
                .setMessage("您已经输入错误3次,点击确定将重新登陆您的帐号")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.clear();
                        editor.commit();
                        sp = Xunlian_LockActivity.this.getSharedPreferences("passwordFile", MODE_PRIVATE);
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(Xunlian_LockActivity.this, Xunlian_Account.class);
                        startActivity(intent);
                        appManger.finishActivity(Xunlian_LockActivity.this);
                    }
                });
        ad.setCancelable(false);
        ad.show();

    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
