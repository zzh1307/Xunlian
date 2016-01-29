package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Frangment.Frangment_Login;
import com.zhaozihao.xunlian.XunLian.Frangment.Frangment_Registered;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;


/**
 * Created by 赵孜豪 on 2016/1/7 0007.
 */
public class Xunlian_Account extends Activity implements View.OnClickListener{
    private LinearLayout Account_Login;
    private LinearLayout Account_Regs;
    private Frangment_Login Login;
    private Frangment_Registered Registered;
    TextView Account_Title;
    TextView Account_Regs_Text;
    TextView Account_Login_Text;
    Button Account_Regs_But;
    Button Account_Login_But;
    AppManger appManger;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account);
       init();
    }

    private void init() {
        appManger = AppManger.getAppManger();
        appManger.addActivity(Xunlian_Account.this);
        Account_Title = (TextView) findViewById(R.id.Account_Title);
        Account_Regs_Text = (TextView) findViewById(R.id.Account_Regs_Text);
        Account_Login_Text = (TextView) findViewById(R.id.Account_Login_Text);
        Account_Regs_But = (Button) findViewById(R.id.Account_Regs_But);
        Account_Login_But = (Button) findViewById(R.id.Account_Login_But);
        setDefaultFragment();
        Account_Login = (LinearLayout) findViewById(R.id.Account_Login);
        Account_Regs = (LinearLayout) findViewById(R.id.Account_Regs);
        Account_Login.setOnClickListener(this);
        Account_Regs.setOnClickListener(this);
        Account_Login_But.setOnClickListener(this);
        Account_Regs_But.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Login = new Frangment_Login();
        transaction.replace(R.id.Account_content, Login);
        transaction.commit();
        TextViewChange(2);
    }

    private void TextViewChange(int mode) {
        if(mode == 1){
            Account_Title.setText("讯连注册");
            Account_Regs_Text.setBackgroundColor(Color.parseColor("#6495ED"));
            Account_Login_Text.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            Account_Title.setText("讯连登录");
            Account_Regs_Text.setBackgroundColor(Color.parseColor("#ffffff"));
            Account_Login_Text.setBackgroundColor(Color.parseColor("#6495ED"));

        }
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        switch (view.getId()){
             case R.id.Account_Login_But:
                if (Login == null)
                {
                    Login = new Frangment_Login();
                }
                TextViewChange(2);
                transaction.replace(R.id.Account_content, Login);
                break;
            case R.id.Account_Regs_But:
                if (Registered == null)
                {
                    Registered = new Frangment_Registered();
                }
                TextViewChange(1);
                transaction.replace(R.id.Account_content, Registered);
                break;
            case R.id.Account_Login:
                if (Login == null)
                {
                    Login = new Frangment_Login();
                }
                TextViewChange(2);
                transaction.replace(R.id.Account_content, Login);
                break;
            case R.id.Account_Regs:
                if (Registered == null)
                {
                    Registered = new Frangment_Registered();
                }
                TextViewChange(1);
                transaction.replace(R.id.Account_content, Registered);
                break;


        }
        transaction.commit();
    }
}
