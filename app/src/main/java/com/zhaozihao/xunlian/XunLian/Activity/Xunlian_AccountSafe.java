package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.UI.TianJiaDialog;

import static com.zhaozihao.xunlian.R.id.AccountSafe_mibao;
import static com.zhaozihao.xunlian.R.id.AccountSafe_password;

public class Xunlian_AccountSafe extends Activity implements OnClickListener {
    TextView mibao = null;
    TextView mima = null;
    TextView tianjia = null;
    AppManger appManger = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.xunlian__account_safe);
        initView();
    }

    private void initView() {
        appManger = AppManger.getAppManger();
        appManger.addActivity(Xunlian_AccountSafe.this);
        mima = (TextView) findViewById(AccountSafe_password);
        mibao = (TextView) findViewById(AccountSafe_mibao);
        tianjia = (TextView) findViewById(R.id.AccountSafe_addquestion);
        mima.setOnClickListener(this);
        mibao.setOnClickListener(this);
        tianjia.setOnClickListener(this);
    }
    public String getAccount() {
        SharedPreferences sp = null;
        sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String str = sp.getString("account", "");
        return  str;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AccountSafe_addquestion:
                new TianJiaDialog(1, Xunlian_AccountSafe.this, getAccount(), new TianJiaDialog.OnCustomDialogListener() {
                    @Override
                    public void back(String strinfo) {
                        if(strinfo.equals("success")){
                            MyToast.showToast(Xunlian_AccountSafe.this,"添加问题设置成功",0);
                        }else{
                            MyToast.showToast(Xunlian_AccountSafe.this,"设置失败,请重试",0);
                        }


                    }
                }).show();
                break;
            case R.id.AccountSafe_mibao:
                startActivity(new Intent(Xunlian_AccountSafe.this,Xunlian_ForgetMibao.class));

                break;
            case R.id.AccountSafe_password:
                startActivity(new Intent(Xunlian_AccountSafe.this, Xunlian_ForgetPassword.class));
                break;
        }

    }
}
