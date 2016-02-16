package com.zhaozihao.xunlian.XunLian.Frangment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;
import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_ForgetPassword;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_MainActivity;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;

import static com.zhaozihao.xunlian.R.id.Login_BTN_Forget;


public class Frangment_Login extends Fragment implements View.OnClickListener{
    Button Login = null;
    Button Forget = null;
    MyToast myToast = null;
    AutoCompleteTextView Account = null;
    EditText Password = null;
    ImageView Head = null;
    Bitmap HeadBTP = null;
    String account = null;
    TextView show = null;
    AppManger appManger;
    String password = null,mima = null;
    CheckBox savePasswordCB = null;
    CheckBox autoLoginCB = null;
    SharedPreferences sp = null;
    String result = null;
    boolean isMD5 = false;
    Tools tools = new Tools(getActivity());
    ProgressDialog pd = null;
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                // TODO Auto-generated method stub
                pd = tools.creatDialog(getActivity(),msg.obj.toString(),"我在努力的加载中....");
                pd.show();
            }else if(msg.what==2){

                myToast.showToast(getActivity(), msg.obj.toString(), 0);
            }else if(msg.what==3){
                if(pd!=null){
                    pd.dismiss();
                    myToast.showToast(getActivity(), msg.obj.toString(), 0);
                }

            }


        }

    };
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
    	View view = inflater.inflate(R.layout.login, container, false);
        init(view);
        appManger = AppManger.getAppManger();
        UmengUpdateAgent.update(getActivity());
        PushAgent mPushAgent = PushAgent.getInstance(getActivity());
        mPushAgent.enable();
        String device_token = UmengRegistrar.getRegistrationId(getActivity());
        Log.e("device_token", device_token);
        return   view;
    }
    public String getAccount() {
        SharedPreferences sp = null;
        sp = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        String str = sp.getString("account", "");
        return  str;
    }
    private void init(View view) {
        myToast = new MyToast();
        Login = (Button) view.findViewById(R.id.Login_BTN_Login);
        show = (TextView) view.findViewById(R.id.show);
        Forget = (Button) view.findViewById(Login_BTN_Forget);
        Login.setOnClickListener(this);
        Forget.setOnClickListener(this);
        Password = (EditText) view.findViewById(R.id.Login_EDT_Password);
        Head = (ImageView) view.findViewById(R.id.Login_IMG_Head);
        savePasswordCB = (CheckBox) view.findViewById(R.id.Login_CHB_Rember);
        autoLoginCB = (CheckBox) view.findViewById(R.id.Login_CHB_AutoLogin);
        savePasswordCB.setChecked(true);
        sp = getActivity().getSharedPreferences("passwordFile", Context.MODE_PRIVATE);
        Account = (AutoCompleteTextView) view.findViewById(R.id.Login_EDT_Account);
        Account.setThreshold(1);
        if(!getAccount().equals("")||getAccount() != null){
            Account.setText(getAccount());
            Password.setText(sp.getString(Account.getText()
                    .toString(), ""));// 自动输入密码
            isMD5 = true;
        }

        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                String pass = Password.getText().toString().replaceAll(" ", "");
                if (pass.equals(sp.getString(Account.getText()
                        .toString(), ""))) {
                    isMD5 = true;
                } else {
                    isMD5 = false;
                }
            }
        });
        Account.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
                allUserName = sp.getAll().keySet().toArray(new String[0]);
                // sp.getAll()返回一张hash map
                // keySet()得到的是a set of the keys.
                // hash map是由key-value组成的

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        allUserName);

                Account.setAdapter(adapter);// 设置数据适配器

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Password.setText(sp.getString(Account.getText()
                        .toString(), ""));// 自动输入密码

            }
        });

    }

    public void setAccount(String account){
        sp = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account",account);
        editor.commit();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.Login_BTN_Login:
                account = Account.getText().toString().replaceAll(" ", "");
                password = Password.getText().toString().replaceAll(" ", "");
                if(account.equals("")||password.equals("")){
                    myToast.showToast(getActivity(), "账号和密码不能为空啊,亲...", 0);
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                if(pd!=null){
                                    Message msg = Message.obtain();
                                    msg.what = 3;
                                    msg.obj = "登录失败";
                                    handler.sendMessage(msg);
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
                                Message msg = Message.obtain();
                                msg.what = 1;
                                msg.obj = "正在登录";
                                handler.sendMessage(msg);
                                Log.e("isMD5", isMD5 + "");
                                Log.e("原密码",password);
                                if(!isMD5){
                                    mima = tools.MD5(password);
                                }else{
                                    mima = Password.getText().toString().replaceAll(" ", "");
                                }
                                String[] K = new String[]{"account","secret"};
                                String[] V = new String[]{account,mima};
                                result = tools.sendString2ServersSocket(tools.Key2Json("12", K,V));
                                String[] strarry = tools.parseJSONMark12(result);
                                if(strarry[0].equals("failure")){
                                    pd.dismiss();
                                    pd = null;
                                    Message msg1 = new Message();
                                    msg1.what = 2;
                                    msg1.obj = strarry[1];
                                    handler.sendMessage(msg1);
                                }else if(strarry[0].equals("success")){
                                    if (savePasswordCB.isChecked()) {
                                        // 登陆成功才保存密码
                                        sp.edit().putString(account, mima).commit();
                                    }
                                    if (autoLoginCB.isChecked()){
                                        SharedPreferences sp;
                                        sp = getActivity().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putBoolean("isAutoLogin",true);
                                        editor.commit();
                                    }
                                    pd.dismiss();
                                    pd = null;
                                    Message msg1 = new Message();
                                    msg1.what = 2;
                                    msg1.obj = strarry[1];
                                    handler.sendMessage(msg1);
                                    setAccount(account);
                                    Intent intent = new Intent(getActivity(), Xunlian_MainActivity.class);
                                    startActivity(intent);
                                    appManger.finishActivity(getActivity());
                                }
                            } catch ( Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }).start();
                }
                break;
            case Login_BTN_Forget:
               startActivity(new Intent(getActivity(), Xunlian_ForgetPassword.class));
            break;
        }
    }
}  
 