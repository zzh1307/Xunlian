package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.Person;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.dao.PersonDao;

import org.json.JSONException;

import java.util.List;


public class Xunlian_PersonInfo extends Activity{
    Button add = null;
	TextView name = null;
    TextView peraccount = null;
    TextView phone1 = null;
    TextView phone2 = null;
    TextView phone3 = null;
    TextView email1 = null;
    TextView email2 = null;
    TextView email3 = null;
    TextView qq = null;
    String[] array = null,update = null;
    TextView weibo = null;
    ImageView head = null;
    String account = null;
    SharedPreferences sp = null;
    Tools tools =  new Tools(Xunlian_PersonInfo.this);
    String request = null;
    String friendaccount = null;
    ProgressDialog pd = null;
    AppManger appManger;
    String type;
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                // TODO Auto-generated method stub
                pd = tools.creatDialog(Xunlian_PersonInfo.this,"正在添加","我在努力的加载中....");
                pd.show();
                //transaction.replace(R.id.PersonList, system);
            }else if(msg.what==2){

                Toast.makeText(Xunlian_PersonInfo.this,msg.obj.toString(),1).show();
            }else if(msg.what==3){
                if(pd!=null){
                    pd.dismiss();
                    Toast.makeText(Xunlian_PersonInfo.this,msg.obj.toString(),1).show();
                }

            }


        }

    };
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        setContentView(R.layout.personinfo);
        init();
        appManger = AppManger.getAppManger();
        appManger.addActivity(Xunlian_PersonInfo.this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        String infostr = intent.getStringExtra("info");
        Log.e("infostr",infostr);
        String strupdate = infostr.substring(infostr.indexOf("&") + 1, infostr.indexOf("#"));
        Log.e("strupdate",strupdate);
        //在这里判断一下Intent的type,然后根据不同的type将传过来的string进行截取
        if(type.equals("update")){
            infostr = infostr.substring(infostr.indexOf("#")+1,infostr.length());
            Log.e("update"+"infostr",infostr);
        }else{
            infostr = infostr.substring(infostr.lastIndexOf("&") + 1, infostr.length());
            Log.e("infostr",infostr);
        }
        array = infostr.split("#");
        if(type.equals("look")){
            setPersonInfo(array);
            friendaccount = phone1.getText().toString();
            add.setVisibility(View.INVISIBLE);
        }else if(type.equals("update")){
            update = strupdate.split("@");
            Log.e("update,length", update.length+"");
            setPersonInfo(array);
            friendaccount = phone1.getText().toString();
            add.setVisibility(View.INVISIBLE);
        }else if(type.equals("add")){
            setPersonInfo(array);
            account = getAccount();
            friendaccount = intent.getStringExtra("result");
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    request = tools.Key2Json("15", "account", account, "friendaccount", friendaccount);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String string = tools.sendString2ServersSocket(request);
                                String[] strarry;
                                strarry = tools.parseJSONMark12(string);
                                if (strarry[0].equals("failure")) {
                                    Message msg = Message.obtain();
                                    msg.what = 2;
                                    msg.obj = strarry[1];
                                    handler.sendMessage(msg);
                                    appManger.finishActivity(Xunlian_PersonInfo.this);
                                } else if (strarry[0].equals("success")) {
                                    Message msg = Message.obtain();
                                    msg.what = 2;
                                    msg.obj = "添加成功";
                                    handler.sendMessage(msg);
                                    setFirendInfo(array);
                                    appManger.finishActivity(Xunlian_PersonInfo.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }
    }

    private void setPersonInfo(String[] info) {

        name.setText(info[0]);
        phone1.setText(info[1]);
        phone2.setText(info[2]);
        phone3.setText(info[3]);

        email1.setText(info[4]);
        email2.setText(info[5]);
        email3.setText(info[6]);

        qq.setText(info[7]);
        weibo.setText(info[8]);
        peraccount.setText(info[9]);
        if(type.equals("update")){
        for(String  x : update){
            switch (Integer.valueOf(x)){
                case 1:
                    phone1.setTextColor(Color.parseColor("#ff0000"));
                    break;
                case 2:
                    phone2.setTextColor(Color.parseColor("#ff0000"));
                    break;
                case 4:
                    phone3.setTextColor(Color.parseColor("#ff0000"));
                    break;
                case 8:
                    email1.setTextColor(Color.parseColor("#ff0000"));
                    break;
                case 16:
                    email2.setTextColor(Color.parseColor("#ff0000"));
                    break;
                case 32:
                    email3.setTextColor(Color.parseColor("#ff0000"));
                    break;
                case 64:
                    qq.setTextColor(Color.parseColor("#ff0000"));
                    break;
                case 128:
                    weibo.setTextColor(Color.parseColor("#ff0000"));
                    break;
            }

        }
        }
    }

    private void init() {
        account = getAccount();
        peraccount = (TextView) findViewById(R.id.account_number);

        name = (TextView) findViewById(R.id.newinfo_edt_name);
        phone1 = (TextView) findViewById(R.id.addresult_txt_StrPhone1);
        phone2 = (TextView) findViewById(R.id.addresult_txt_StrPhone2);
        phone3 = (TextView) findViewById(R.id.addresult_txt_StrPhone3);
        email1 = (TextView) findViewById(R.id.addresult_txt_StrEmail1);
        email2 = (TextView) findViewById(R.id.addresult_txt_StrEmail2);
        email3 = (TextView) findViewById(R.id.addresult_txt_StrEmail3);
        qq = (TextView) findViewById(R.id.addresult_txt_Strqq);
        weibo = (TextView) findViewById(R.id.addresult_txt_Strweibo1);
        head = (ImageView) findViewById(R.id.addresult_img_photo);
        add = (Button) findViewById(R.id.addresult_btn_Add);
    }

    public void setFirendInfo(String[] str){
        PersonDao dao = new PersonDao(getApplication(),account);
        dao.insert(new Person(str));


    }

    public String getAccount() {
        sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String str = sp.getString("account","");
        return  str;
    }
}  