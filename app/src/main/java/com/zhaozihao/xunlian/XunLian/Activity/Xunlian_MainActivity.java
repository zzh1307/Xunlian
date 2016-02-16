package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Adapter.AppPersonListAdapter;
import com.zhaozihao.xunlian.XunLian.Frangment.Frangment_Add;
import com.zhaozihao.xunlian.XunLian.Frangment.Frangment_PersonList;
import com.zhaozihao.xunlian.XunLian.Frangment.Frangment_Setting;
import com.zhaozihao.xunlian.XunLian.Frangment.Frangment_Title;
import com.zhaozihao.xunlian.XunLian.Frangment.Frangment_Xunlian_PersonList;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.Person;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.XunLian.Tools.UpdateInfo;
import com.zhaozihao.xunlian.dao.PersonDao;

import java.util.List;

public class Xunlian_MainActivity extends  Activity implements View.OnClickListener,AppPersonListAdapter.CheckListener,Frangment_Xunlian_PersonList.UpdateListener {
    private Button but0;
    private Button but1;
    SharedPreferences sp = null;
    private Button but2;
    AlertDialog.Builder builder;
    private Button but3;  
    private long mExitTime;
    String account = null;
    List<List<UpdateInfo>> perlist1;
    android.support.v4.app.Fragment talk;
    Tools tools = new Tools(Xunlian_MainActivity.this);
    View view;
    private Frangment_Title titlefrag;
    private Frangment_PersonList frag1;
    private Frangment_Add frag2;
    private Frangment_Setting frag3;
    AppManger appManger;
    Boolean isPressed = true,isShow = false;
    View title;
    FragmentTransaction transaction;
    ProgressDialog pd = null;
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                // TODO Auto-generated method stub
                pd = tools.creatDialog(Xunlian_MainActivity.this,"正在加载","我在拼命的加载中....");
                pd.show();
            }else if(msg.what==1){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction1 = fm.beginTransaction();
                transaction.replace(R.id.id_content, frag3);
                transaction1.commit();
            }else if(msg.what == 2){
                showListDialog((List<List<UpdateInfo>>) msg.obj);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp.edit().putBoolean("isFirstGetUser", false).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("isFirstGetUser", Context.MODE_PRIVATE);
        sp.edit().putBoolean("isFirstGetUser", true).commit();
        String device_token = UmengRegistrar.getRegistrationId(this);
        Log.e("device_token", device_token);
        PushAgent mPushAgent = PushAgent.getInstance(Xunlian_MainActivity.this);
        mPushAgent.enable();
        title = findViewById(R.id.id_title);
       //mIMKit = YWAPI.getIMKitInstance();
        appManger = AppManger.getAppManger();
        appManger.addActivity(Xunlian_MainActivity.this);
        but0 = (Button) findViewById(R.id.but0);
        but1 = (Button) findViewById(R.id.but1);
      but2 = (Button) findViewById(R.id.but2);
      but3 = (Button) findViewById(R.id.but3);
        but0.setOnClickListener(this);
      but1.setOnClickListener(this);
      but2.setOnClickListener(this);
      but3.setOnClickListener(this);

      account = getAccount();
      FragmentManager fm = getFragmentManager();
      FragmentTransaction transaction = fm.beginTransaction();
      titlefrag = new Frangment_Title();
        //todo
      transaction.replace(R.id.id_title, titlefrag);
      transaction.commit();
      setDefaultFragment();
    }
    public String getAccount() {
        SharedPreferences sp = null;
        sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String str = sp.getString("account","");
        return  str;
    }
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        frag1 = new Frangment_PersonList();
        transaction.replace(R.id.id_content, frag1);
        transaction.commit();
    }

    @Override
    protected void onPostResume() {
        if(isPressed&&isShow){
            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = perlist1;
            handler.sendMessage(msg);
        }
        super.onPostResume();
    }

    @Override
    public void onClick(View v)  
    {
        FragmentManager fm = getFragmentManager();

        transaction = fm.beginTransaction();

        switch (v.getId())  
        {
//            case R.id.but0:
//                but1.setTextColor(-65536);
//                but2.setTextColor(-16777216);
//                but3.setTextColor(-16777216);
//                if (talk == null)
//                {
//                    talk = mIMKit.getConversationFragment();
//                }
//                title.setVisibility(View.VISIBLE);
//                transaction.replace(R.id.id_content, talk);
//                break;
        case R.id.but1:
            isPressed = true;
        	but1.setTextColor(-65536);
        	but2.setTextColor(-16777216);
            but3.setTextColor(-16777216);
            frag1 = new Frangment_PersonList();
            title.setVisibility(View.VISIBLE);
            transaction.replace(R.id.id_content, frag1);
            break;
        case R.id.but2:
            isPressed = false;
            if (frag2 == null)
               {
                 frag2 = new Frangment_Add();
               }
            title.setVisibility(View.VISIBLE);
            transaction.replace(R.id.id_content, frag2);
        	but1.setTextColor(-16777216);
        	but2.setTextColor(-65536);
        	but3.setTextColor(-16777216);
			break;
        case R.id.but3:
            isPressed = false;
            title.setVisibility(View.GONE);
        	but1.setTextColor(-16777216);
        	but2.setTextColor(-16777216);
        	but3.setTextColor(-65536);
            if (frag3 == null)
            {
            	frag3 = new Frangment_Setting();
            }

            transaction.replace(R.id.id_content, frag3);
            break;

        }
        transaction.commit();
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2500) {

                Toast.makeText(this,"双击返回键可以退出",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                appManger.finishAllActivity();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {

            super.openOptionsMenu();
        }
        return true;
    }
    public void showListDialog(final List<List<UpdateInfo>> perlist){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("点击查看更新信息");
        String[] persons = new String[perlist.size()];
        for(int i = 0;i<perlist.size();i++){
            persons[i] = perlist.get(i).get(i).getName();
        }
        builder.setItems(persons, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PersonDao dao;
                dao = new PersonDao(Xunlian_MainActivity.this, account);
                Person aa = dao.queryItem(tools.encryption(perlist.get(which).get(0).getAccount()));
                Intent Intent = new Intent();
                String mark = "@";
                for (int i = 0; i < perlist.get(which).size(); i++) {
                    mark += perlist.get(which).get(i).getKey() + "@";
                }
                Intent.setClass(Xunlian_MainActivity.this, Xunlian_PersonInfo.class);
                Intent.putExtra("type", "update");
                Intent.putExtra("info", mark + "#" + aa.toString());
                startActivity(Intent);
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("朕知道了,退下吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShow = false;
                dialog.dismiss();
            }
        });
        builder.show();
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.e("Xunlian_MainActivity", "xiaoshi ");
                isShow = false;
                dialog.dismiss();
            }
        });
    }

    @Override
    public void send(List<List<UpdateInfo>> perlist) {
        perlist1 = perlist;
        isShow = true;
        Message msg = Message.obtain();
        msg.what = 2;
        msg.obj = perlist;
        handler.sendMessage(msg);

    }

    @Override
    public void check(Boolean ischeck) {
        if(ischeck){
            isShow = false;
        }
    }
}
