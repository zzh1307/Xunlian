package com.zhaozihao.xunlian.XunLian.Frangment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_NewInfo;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.XunLian.UI.MiBaoDialog;

import org.json.JSONException;


public class Frangment_Registered extends Fragment implements View.OnClickListener{

    Button Registered = null;
    Button Check = null;
    EditText Account = null;
    EditText Password1 = null;
    String[] value = new String[5];
    String[] key = new String[]{"account","secret","flag","question","answer"};
    EditText Password2 = null;
    String Phonenum = null;
    String requestResult = "";
    SharedPreferences sp = null;
    String Password = null;
    Tools tools = new Tools(getActivity());
    ProgressDialog pd = null;
    LinearLayout ll = null;
    Boolean is = false;
    Boolean is1 = false;
    MyToast myToast = null;
    AppManger appManger;

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                // TODO Auto-generated method stub
                pd = tools.creatDialog(getActivity(),"正在加载","我在努力的加载中....");
                pd.show();
            }else if(msg.what==2){
                myToast.showToast(getActivity(),msg.obj.toString(), 0);
            }else if(msg.what == 3){
                Check.setEnabled(false);
                Account.setEnabled(false);
                Check.setBackgroundResource(R.drawable.buttonpressed);
                ll.setVisibility(View.VISIBLE);
            }else if(msg.what == 4){
                if(pd!=null){
                    pd.dismiss();
                    myToast.showToast(getActivity(), msg.obj.toString(), 0);
                }else{
                    myToast.showToast(getActivity(), msg.obj.toString(), 0);
                }
            }else if(msg.what==5){
                Password1.setEnabled(false);
                Password2.setEnabled(false);
                myToast.showToast(getActivity(),msg.obj.toString(), 0);
                Registered.setVisibility(View.GONE);
            }
        }

    };
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
    	View view = inflater.inflate(R.layout.registered, container, false);
        hint(view);
        appManger = AppManger.getAppManger();
        appManger.addActivity(getActivity());
        return   view;
    }

    @Override
    public void onStart() {
        if(!Account.getText().toString().isEmpty())
        {
            Account.setText("");
        }
        super.onStart();
    }

    void key(){
        new MiBaoDialog(1, getActivity(), key,value, new MiBaoDialog.OnCustomDialogListener() {
            @Override
            public void back(String info) {
                 if(info.equals("outTime")){
                    Message msg1 = new Message();
                    msg1.what = 2;
                    msg1.obj = "网络连接超时,请重新设置";
                    handler.sendMessage(msg1);
                }else if(info.equals("success")){
                    Message msg1 = new Message();
                    msg1.what = 2;
                    msg1.obj ="密保问题设置成功";
                    handler.sendMessage(msg1);
                    Intent intent = new Intent(getActivity(), Xunlian_NewInfo.class);
                    intent.putExtra("type", "new");
                    startActivity(intent);
                    appManger.finishActivity(getActivity());
                    setAccount(Phonenum);
                }else{
                    Message msg1 = new Message();
                    msg1.what = 2;
                    msg1.obj ="发生不可预知的错误";
                    handler.sendMessage(msg1);
                }
            }
        }).show();
    }

    private void hint(View view ) {
        // TODO Auto-generated method stub
        myToast = new MyToast();
        Registered = (Button) view.findViewById(R.id.Reg_BTN_Next);
        Registered.setOnClickListener(this);
        Check = (Button) view.findViewById(R.id.Reg_BTN_Check);
        Check.setOnClickListener(this);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        Account = (EditText) view.findViewById(R.id.Reg_EDT_Account);
        Password1 = (EditText) view.findViewById(R.id.Reg_EDT_Password1);
        Password2 = (EditText) view.findViewById(R.id.Reg_EDT_Password2);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

        switch (arg0.getId()) {
            case R.id.Reg_BTN_Next:
                if(!Password1.getText().toString().equals(Password2.getText().toString())){
                    myToast.showToast(getActivity(), "好像两次不一样啊???", 0);
                }else{
                    Password = Password1.getText().toString();
                    Log.e("-----", "" + Password.length());
                    if(Password.equals("") || Password.length()<8 || Password.length()>16){
                        myToast.showToast(getActivity(),"密码长度为8-16位", 0);
                    }else {
                        value[0] = Phonenum;
                        value[1] = tools.MD5(Password);
                        value[2] = "1";
                        key();
                    }
                }
                break;
            case R.id.Reg_BTN_Check:
                Phonenum = Account.getText().toString();
                if(Phonenum.equals("")||Phonenum.length()<8){
                    myToast.showToast(getActivity(),"亲,账号格式不对哦...", 0);
                }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                            if(!is){
                                Message msg = new Message();
                                msg.what = 4;
                                msg.obj = "网络超时,请检查您的网络连接";
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
                        String result = tools.sendString2ServersSocket(tools.Key2Json("1", "account", Phonenum));
                        try {
                            String[] strarry = tools.parseJSONMark12(result);
                            if(strarry[0].equals("success")){
                                Message msg1 = new Message();
                                is = true;
                                msg1.what = 2;
                                msg1.obj = strarry[1];
                                handler.sendMessage(msg1);
                            }else if(strarry[0].equals("failure")){
                                Message msg2 = new Message();
                                msg2.what = 3;
                                is = true;
                                handler.sendMessage(msg2);
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = "请填写密码";
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                }
                break;
            default:
                break;
        }
    }
    public void setAccount(String account){
        sp = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account",account);
        editor.commit();
    }
}  
 