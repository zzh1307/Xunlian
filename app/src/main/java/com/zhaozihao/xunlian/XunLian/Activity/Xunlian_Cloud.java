package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Xunlian_Cloud extends Activity implements View.OnClickListener {
    TextView push = null;
    TextView pull = null;
    Boolean is = false;
    Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
    Uri dataUri = Uri.parse("content://com.android.contacts/data");
    HashMap<String, Object> map = null;
    HashMap<String, Object> map1 = null;
    List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    Cursor c =null;
    Tools tools = new Tools(this);
    ProgressDialog pd = null;
    MyToast myToast = null;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 0:
                    pd = tools.creatDialog(Xunlian_Cloud.this,"请稍等",msg.obj.toString()+"...");
                    pd.show();
                   break;
               case 1:
                   if(pd!=null){
                       pd.dismiss();
                       myToast.showToast(Xunlian_Cloud.this, msg.obj.toString(), 0);
                   }
                   break;
            }


        }

    };
    public void queryContacts() {
        Cursor cursor = getContentResolver().query(uri, new String[]{"_id"}, null,null, ContactsContract.Contacts.DISPLAY_NAME);
        if (cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String selection = "raw_contact_id = ?";
                String[] selectionArgs = {String.valueOf(id)};
                c = getContentResolver().query(dataUri,new String[]{"data1","mimetype"}, selection,selectionArgs,null);
                if (c!=null&&c.getCount()>0){
                    map = new HashMap<String, Object>();
                    while (c.moveToNext()){
                        String mimetype = c.getString(1);
                        String data1 = c.getString(0);
                        if("vnd.android.cursor.item/name".equals(mimetype)){
                            Log.e("data1", data1);
                            map.put("Name", data1);
                            map1 = map;
                            continue;
                        }else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                            if(!data.equals("")){
                                map.put("Phone", data1);
                                Log.e("data1--", data1);
                                data.add(map);
                            }else{
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cloud);
        myToast = new MyToast();
        initView();
    }

    private void initView() {
        push = (TextView) findViewById(R.id.cloud_push);
        push.setOnClickListener(this);
        pull = (TextView) findViewById(R.id.cloud_pull);
        pull.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cloud_pull:
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = "正在获取云端数据";
                handler.sendMessage(msg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                            if(!is){
                                Message msg = new Message();
                                msg.what = 1;
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
                        String result = tools.sendString2ServersSocket(tools.Key2Json("20", "account",getAccount()));
                        pd.dismiss();
                    }
                }).start();
                break;
            case R.id.cloud_push:
                Message msg4 = Message.obtain();
                msg4.what = 0;
                msg4.obj = "正在努力推送数据";
                handler.sendMessage(msg4);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(8000);
                            if(!is){
                                Message msg = new Message();
                                msg.what = 1;
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
                        queryContacts();
                            String result = tools.sendString2ServersSocket(tools.Key2Json19("19",getAccount(), data));
                        try {
                            String[] strarry = tools.parseJSONMark12(result);
                            if (strarry[0].equals("success")) {
                                Message msg1 = new Message();
                                is = true;
                                msg1.what = 1;
                                msg1.obj = strarry[1];
                                handler.sendMessage(msg1);
                            } else if (strarry[0].equals("failure")) {
                                Message msg2 = new Message();
                                msg2.what = 1;
                                msg2.obj = "备份失败";
                                is = true;
                                handler.sendMessage(msg2);
                            }
                            pd.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
        }

    }
    public String getAccount() {
        SharedPreferences sp = null;
        sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String str = sp.getString("account","");
        return  str;
    }
}
