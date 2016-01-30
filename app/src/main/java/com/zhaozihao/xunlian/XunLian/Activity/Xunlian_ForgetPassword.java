package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;


public class Xunlian_ForgetPassword extends Activity implements View.OnClickListener{
    Button next = null;
    Button getaccount = null;
    Button setPassword = null;
    EditText daan = null;
    EditText Password1 = null;
    EditText Password2 = null;
    EditText account = null;
    TextView wenti = null;
    MyToast myToast = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    LinearLayout LLmibao = null;
    Tools tools = new Tools(Xunlian_ForgetPassword.this);
    ProgressDialog pd = null;
    AppManger appManger = null;
    String strAccount = null;
    String strWenti = null;
    String strPassword1 = null;
    String strdaan = null;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    pd = tools.creatDialog(Xunlian_ForgetPassword.this,msg.obj.toString(),"我在努力的加载中....");
                    pd.show();
                    break;
                case 1:

                        myToast.showToast(Xunlian_ForgetPassword.this, msg.obj.toString(), 0);
                        getaccount.setEnabled(false);
                        getaccount.setBackgroundResource(R.drawable.buttonpressed);
                        LLmibao.setVisibility(View.VISIBLE);
                        wenti.setText(strWenti + "?");

                    break;
                case 2:
                    myToast.showToast(Xunlian_ForgetPassword.this, msg.obj.toString(), 0);
                    break;
                case 3:
                    if(pd!=null) {
                        pd.dismiss();
                        myToast.showToast(Xunlian_ForgetPassword.this, msg.obj.toString(), 0);

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        appManger = AppManger.getAppManger();
        appManger.addActivity(Xunlian_ForgetPassword.this);
        getaccount = (Button) findViewById(R.id.forget_btn_getaccount);
        account = (EditText) findViewById(R.id.forget_edt_account);
        LLmibao = (LinearLayout) findViewById(R.id.forget_ll_mibao);
        setPassword = (Button) findViewById(R.id.forget_btn_setPassword);
        daan = (EditText) findViewById(R.id.forget_edt_daan);
        wenti = (TextView) findViewById(R.id.forget_txt_wenti);
        Password1 = (EditText) findViewById(R.id.forget_edt__Password1);
        Password2 = (EditText) findViewById(R.id.forget_edt_Password2);
        setPassword.setOnClickListener(this);
        getaccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forget_btn_setPassword:
                strPassword1 = Password1.getText().toString();
                if (!Password1.getText().toString().equals(Password2.getText().toString())||strPassword1.equals("") || strPassword1.length() < 8 || strPassword1.length() > 16) {
                    myToast.showToast(Xunlian_ForgetPassword.this, "密码输入不符合规范,长度8-16", 0);
                } else {

                    if(daan.getText().toString().equals("")){
                        myToast.showToast(Xunlian_ForgetPassword.this, "密保答案不可以为空", 0);
                    }else{
                        strPassword1 = tools.MD5(strPassword1);
                    }
                Message msg1 = new Message();
                msg1.what = 0;
                msg1.obj = "正在加载";
                mHandler.sendMessage(msg1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                            if(pd!=null){
                                Message msg = Message.obtain();
                                msg.what = 3;
                                msg.obj = "验证失败,请重试";
                                mHandler.sendMessage(msg);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                new Thread(new Runnable() {


                    String result = null;
                    @Override
                    public void run() {

                            try {
                                strdaan = daan.getText().toString();
                                String[] K = new String[]{"account", "secret", "flag", "question", "answer"};
                                String[] V = new String[]{strAccount, strPassword1, "3", strWenti, strdaan};
                                result = tools.sendString2ServersSocket(tools.Key2Json("2", K, V));
                                String[] strarry = tools.parseJSONMark12(result);
                                if (strarry[0].equals("failure")) {
                                    pd.dismiss();
                                    pd = null;
                                    Message msg1 = new Message();
                                    msg1.what = 1;
                                    msg1.obj = strarry[1];
                                    mHandler.sendMessage(msg1);
                                } else if (strarry[0].equals("success")) {
                                    pd.dismiss();
                                    pd = null;
                                    Message msg1 = new Message();
                                    msg1.what = 1;
                                    msg1.obj = strarry[1];
                                    mHandler.sendMessage(msg1);
                                    sp = getSharedPreferences("passwordFile", MODE_PRIVATE);
                                    editor = sp.edit();
                                    editor.putString(strAccount, strPassword1);
                                    editor.commit();
                                    Message msg2 = Message.obtain();
                                    msg2.what = 2;
                                    msg2.obj = "密码修改成功,您现在可以直接登录了";
                                    mHandler.sendMessage(msg2);
                                    appManger.finishActivity(Xunlian_ForgetPassword.this);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                    }
                }).start();}
                ;break;
            case R.id.forget_btn_getaccount:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                            if(pd!=null){
                                Message msg = Message.obtain();
                                msg.what = 3;
                                msg.obj = "获取密保问题失败,请检查后重试";
                                mHandler.sendMessage(msg);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                strAccount = account.getText().toString();
                Log.e("account", strAccount);
                if(strAccount.equals("")||strAccount.length()<8||strAccount.length()>16){
                    myToast.showToast(Xunlian_ForgetPassword.this, "请输入正确的账号", 0);

                }else{
                    Message msg1 = new Message();
                    msg1.what = 0;
                    msg1.obj = "正在加载";
                    mHandler.sendMessage(msg1);
                new Thread(new Runnable() {
                    String result = null;
                    @Override
                    public void run() {
                        try {

                            result = tools.sendString2ServersSocket(tools.Key2Json("3", "account",strAccount));
                            String[] strarry = tools.parseJSONMark12(result);
                            if(strarry[0].equals("success")){
                                pd.dismiss();
                                pd = null;
                                strWenti = strarry[1];
                                Message msg1 = Message.obtain();
                                msg1.what = 1;
                                msg1.obj = "请验证密保问题";
                                mHandler.sendMessage(msg1);
                            }else  if(strarry[0].equals("failure")){
                                pd.dismiss();
                                pd = null;
                                Message msg2 = Message.obtain();
                                msg2.what = 2;
                                msg2.obj = "请输入正确的账号后重试";
                                mHandler.sendMessage(msg2);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();}
                ;break;
        }

    }
}
