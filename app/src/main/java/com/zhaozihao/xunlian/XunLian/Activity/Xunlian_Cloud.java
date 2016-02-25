package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;

import org.json.JSONException;

import java.io.Serializable;
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
    AlertDialog.Builder ad;
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
               case 2:
                       myToast.showToast(Xunlian_Cloud.this, msg.obj.toString(), 0);
                   break;
               case 3:
                   ad.show();

                   break;



           }


        }

    };
    public void queryContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.SORT_KEY_ALTERNATIVE);
        while(cursor.moveToNext()) {
            map = new HashMap<String, Object>();
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFieldColumnIndex);
            map.put("Name", contact);
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if(phone.moveToNext())
            {
                String Number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                map.put("Phone", Number);
                data.add(map);
            }
        }
        cursor.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cloud);
        myToast = new MyToast();
        ad = new AlertDialog.Builder(Xunlian_Cloud.this);
        initView();
    }

    private void initView() {
        push = (TextView) findViewById(R.id.cloud_push);
        push.setOnClickListener(this);
        pull = (TextView) findViewById(R.id.cloud_pull);
        pull.setOnClickListener(this);
        ad.setTitle("备份本地联系人");
        ad.setMessage("\n本操作会将您的本地联系人数据备份到讯连服务器,我们不会将您的数据用来从事任何与讯连APP不相关的操作,请放心使用本功能");
        ad.setPositiveButton("替我备份", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                is = false;
                dialog.dismiss();
                Message msg4 = Message.obtain();
                msg4.what = 0;
                msg4.obj = "正在努力推送数据";
                handler.sendMessage(msg4);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(8000);
                            if (!is) {
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
                        String result = tools.sendString2ServersSocket(tools.Key2Json19("19", getAccount(), data));
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


            }
        });
        ad.setNegativeButton("下次再说吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cloud_pull:
                is = false;
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
                        List<HashMap<String, Object>> data1 = tools.parseJSONMark20(result);
                        if(data1.size()>0){
                            is = true;
                            Intent intent = new Intent();
                            intent.setClass(Xunlian_Cloud.this, Xunlian_showLocalPer.class);
                            intent.putExtra("person", (Serializable) data1);
                            startActivity(intent);
                        }else{
                            is = true;
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = "您还没有备份过数据";
                            handler.sendMessage(msg);
                        }
                        is = true;
                        pd.dismiss();

                    }
                }).start();
                break;
            case R.id.cloud_push:
                Message msg4 = Message.obtain();
                msg4.what = 3;
                handler.sendMessage(msg4);
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
