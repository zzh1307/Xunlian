package com.zhaozihao.xunlian.XunLian.UI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;

/**
 * 自定义dialog
 * @author Mr.Xu
 *
 */

public class MiBaoDialog extends Dialog {
    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    Tools tools;
    Boolean isFinish = false;
    String[] value;
    String[] key;
    String account;
    private OnCustomDialogListener customDialogListener;
    Button btn;
    int type;
    EditText wenti;
    EditText daan;
    String Strwenti;
    String Strdaan;
/*
type 1  就是设置密保问题的
type 2  就是验证密保问题的
 */
    public MiBaoDialog(int type, Context context,String[] key, String[] value, OnCustomDialogListener customDialogListener) {
            super(context);
        this.key =key;
        this.value = value;
        this.type = type;
        tools = new Tools(context);
        this.customDialogListener = customDialogListener;
    }

    public MiBaoDialog(int type, Context context,String account ,OnCustomDialogListener customDialogListener) {
        super(context);
        this.account =account;
        this.type = type;
        tools = new Tools(context);
        this.customDialogListener = customDialogListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mibao);
        //设置标题
       initView(type);
    }

    private void initView(int type) {
        if (type == 1){
            setTitle("设置密保问题");
        }else{
            setTitle("验证密保问题");
        }
        btn = (Button) findViewById(R.id.dialog_btn);
        wenti = (EditText) findViewById(R.id.mibao_edt_wenti);
        daan = (EditText) findViewById(R.id.mibao_edt_daan);
        btn.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Strwenti = wenti.getText().toString();
            Strdaan = daan.getText().toString();
            if(type==1){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                              Thread.sleep(5000);
                              if(!isFinish){
                                  customDialogListener.back("outTime");
                                  MiBaoDialog.this.dismiss();
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

                if(Strwenti.equals("")||Strdaan.equals("")){
                    customDialogListener.back("error");
                    isFinish = true;
                }else{
                    try {
                        value[3] = Strwenti;
                        value[4] = Strdaan;
                        String result = tools.sendString2ServersSocket(tools.Key2Json("2", key,value));
                        String[] strarry = tools.parseJSONMark12(result);
                        if(strarry[0].equals("failure")){
                            customDialogListener.back("failure");
                            MiBaoDialog.this.dismiss();
                            isFinish = true;
                        }else if(strarry[0].equals("success")){
                            customDialogListener.back("success");
                            MiBaoDialog.this.dismiss();
                            isFinish = true;
                        }
                    } catch ( Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

            }else{
                // TODO: 2016/1/19 0019 type 2 是验证密保问题及其密码的,这个方法里面要验证密保问题
                //将验证后的结果返回
                customDialogListener.back("");
                MiBaoDialog.this.dismiss();


            }

        }
    };

}