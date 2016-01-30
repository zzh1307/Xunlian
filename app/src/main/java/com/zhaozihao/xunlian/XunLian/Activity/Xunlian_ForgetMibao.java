package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;

public class Xunlian_ForgetMibao extends Activity implements View.OnClickListener{
    Button next = null;
    Button setMibao = null;
    EditText Password = null;
    EditText account = null;
    EditText wenti = null;
    EditText daan = null;
    MyToast myToast = null;
    LinearLayout LLmibao = null;
    String straccount = null;
    String strmima = null;
    AppManger appManger;
    String strwenti = null;
    String strdaan = null;
    Tools tools = new Tools(Xunlian_ForgetMibao.this);
    ProgressDialog pd = null;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 0:
                    pd = tools.creatDialog(Xunlian_ForgetMibao.this,msg.obj.toString(),"我在努力的加载中....");
                    pd.show();
                    break;
                case 1:

                    break;
                case 2:
                    myToast.showToast(Xunlian_ForgetMibao.this, msg.obj.toString(), 0);
                    account.setEnabled(false);
                    Password.setEnabled(false);
                    next.setEnabled(false);
                    next.setBackgroundResource(R.drawable.buttonpressed);
                    LLmibao.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    if(pd!=null){
                        pd.dismiss();
                        myToast.showToast(Xunlian_ForgetMibao.this, msg.obj.toString(), 0);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xunlian__forget_mibao);
        initView();
    }

    private void initView() {
        appManger = AppManger.getAppManger();
        appManger.addActivity(Xunlian_ForgetMibao.this);
        next = (Button) findViewById(R.id.mibao_btn_next);
        setMibao = (Button) findViewById(R.id.mibao_btn_setmibao);
        Password = (EditText) findViewById(R.id.mibao_edt_password);
        daan = (EditText) findViewById(R.id.mibao_daan);
        wenti = (EditText) findViewById(R.id.mibao_wenti);
        account = (EditText) findViewById(R.id.mibao_edt_account);
        LLmibao = (LinearLayout) findViewById(R.id.mibao_ll_mibao);
        next.setOnClickListener(this);
        setMibao.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.mibao_btn_next:
                straccount = account.getText().toString();
                strmima = Password.getText().toString();
                if(straccount.equals("")||strmima.equals("")){
                    myToast.showToast(Xunlian_ForgetMibao.this, "账号和密码不能为空啊,亲...", 0);
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(8000);
                                if(pd!=null){
                                    Message msg = Message.obtain();
                                    msg.what = 3;
                                    msg.obj = "验证失败";
                                    mHandler.sendMessage(msg);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                String result = null;
                                Message msg = Message.obtain();
                                msg.what = 0;
                                msg.obj = "正在验证";
                                mHandler.sendMessage(msg);
                                strmima = tools.MD5(Password.getText().toString().replaceAll(" ", ""));
                                String[] K = new String[]{"account","secret"};
                                String[] V = new String[]{straccount,strmima};
                                result = tools.sendString2ServersSocket(tools.Key2Json("12", K,V));
                                String[] strarry = tools.parseJSONMark12(result);
                                if(strarry[0].equals("failure")){
                                    pd.dismiss();
                                    pd = null;
                                    Message msg1 = new Message();
                                    msg1.what = 2;
                                    msg1.obj = strarry[1];
                                    mHandler.sendMessage(msg1);
                                }else if(strarry[0].equals("success")){
                                    pd.dismiss();
                                    pd = null;
                                    Message msg1 = new Message();
                                    msg1.what = 2;
                                    msg1.obj = "验证成功,请输入的新的密保问题及其密码";
                                    mHandler.sendMessage(msg1);
                                    //appManger.finishActivity(Xunlian_ForgetMibao.this);
                                }
                            } catch ( Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }).start();
                }
                break;
            case R.id.mibao_btn_setmibao:
                strdaan = daan.getText().toString();
                strwenti = wenti.getText().toString();
                if(strdaan.equals("")||strwenti.equals("")){
                    myToast.showToast(Xunlian_ForgetMibao.this, "密保问题及其密码不可以为空", 0);
                }else{
//            {
//                mark:2,
//                account:xxxx
//                secret:xxxxxx
//                flag:1帐号注册，2密码改密宝，3.密宝改密码
//                question:xxxxx
//                answer:xxxxx
//            }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                            if(pd!=null){
                                Message msg = Message.obtain();
                                msg.what = 3;
                                msg.obj = "验证失败";
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
                            String[] K = new String[]{"account","secret","flag","question","answer"};
                            String[] V = new String[]{straccount,strmima,"2",strwenti,strdaan};
                            result = tools.sendString2ServersSocket(tools.Key2Json("2", K,V));
                            String[] strarry = tools.parseJSONMark12(result);
                            if(strarry[0].equals("failure")){
                                Message msg1 = new Message();
                                msg1.what = 2;
                                msg1.obj = strarry[1];
                                mHandler.sendMessage(msg1);
                            }else if(strarry[0].equals("success")){
                                Message msg1 = new Message();
                                msg1.what = 2;
                                msg1.obj = strarry[1];
                                mHandler.sendMessage(msg1);
                                appManger.finishActivity(Xunlian_ForgetMibao.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                }
                break;
        }

    }
}
