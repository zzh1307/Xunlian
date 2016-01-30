package com.zhaozihao.xunlian.XunLian.UI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;

import org.json.JSONException;

/**
 * 自定义dialog
 * @author Mr.Xu
 *
 */

public class TianJiaDialog extends Dialog {
    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    Tools tools;
    Boolean isFinish = false;
    String account;
    private OnCustomDialogListener customDialogListener;
    Button btn;
    int type;
    EditText daan;
    EditText wenti;
    TextView tianjiainfo = null;
    String Strdaan;
    Context context;
    String Strwenti;
    String question;
    String friendaccount;

/*
type 1  就是设置添加密码
type 2  就是验证添加密码
 */
    public TianJiaDialog(int type, Context context, String account, OnCustomDialogListener customDialogListener) {
        super(context);
        this.context = context;
        this.account =account;
        this.type = type;
        tools = new Tools(context);
        this.customDialogListener = customDialogListener;
    }
    public TianJiaDialog(int type, Context context, String account,String question,String friendaccount, OnCustomDialogListener customDialogListener) {
        super(context);
        this.account =account;
        this.type = type;
        this.context = context;
        this.question = question;
        tools = new Tools(context);
        this.friendaccount = friendaccount;
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tianjia);
        //设置标题

       initView(type);
    }

    private void initView(int type) {
        tianjiainfo  = (TextView) findViewById(R.id.tianjia_info);
        btn = (Button) findViewById(R.id.tianjia_btn);
        daan = (EditText) findViewById(R.id.tianjia_edt_daan);
        wenti = (EditText) findViewById(R.id.tianjia_edt_wenti);
        btn.setOnClickListener(clickListener);
        if (type == 1){
            setTitle("设置添加问题和密码");
        }else{
            tianjiainfo.setText("您可以向您的好友索取添加密码");
            setTitle("验证添加密码");
            wenti.setEnabled(false);
            wenti.setText(question);
        }

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Strdaan = daan.getText().toString();
            Strwenti = wenti.getText().toString();
            if(type==1){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                              Thread.sleep(5000);
                              if(!isFinish){
                                  customDialogListener.back("outTime");
                                  TianJiaDialog.this.dismiss();
                              }
                                isFinish = false;
                             } catch (InterruptedException e) {
                                 e.printStackTrace();
                             }
                    }
                }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                if(Strdaan.equals("")||Strwenti.equals("")){
                    customDialogListener.back("error");
                    isFinish = true;
                }else{
                    try {
                        String[] key = new String[]{"account","addquestion","addanswer"};
                        String[] value = new String[]{account,Strwenti,Strdaan};
                        String result = tools.sendString2ServersSocket(tools.Key2Json("16",key,value));
                        String[] strarry = tools.parseJSONMark12(result);
                        if(strarry[0].equals("failure")){
                            customDialogListener.back("failure");
                            TianJiaDialog.this.dismiss();
                            isFinish = true;
                        }else if(strarry[0].equals("success")){
                            customDialogListener.back("success");
                            TianJiaDialog.this.dismiss();
                            isFinish = true;
                        }
                    } catch ( Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String[] key = new String[]{"account","friendaccount","answer"};
                            String[] value = new String[]{account,friendaccount,Strdaan};
                            String result = tools.sendString2ServersSocket(tools.Key2Json("17",key,value));
                            String[] strarry = tools.parseJSONMark12(result);
                            if(strarry[1].equals("no")){
                                customDialogListener.back("failure");
                                TianJiaDialog.this.dismiss();
                                isFinish = true;
                            }else if(strarry[1].equals("yes")){
                                customDialogListener.back("success");
                                TianJiaDialog.this.dismiss();
                                isFinish = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        }
    };

}