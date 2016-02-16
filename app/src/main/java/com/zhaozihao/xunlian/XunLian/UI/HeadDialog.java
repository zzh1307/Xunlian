package com.zhaozihao.xunlian.XunLian.UI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhaozihao.xunlian.R;

/**
 * 自定义dialog
 * @author Mr.Xu
 *
 */

public class HeadDialog extends Dialog {
    RadioGroup rg;
    RadioButton btn1;
    RadioButton btn2;
    RadioButton btn3;
    RadioButton btn4;

    private OnSingleDialogListener singleDialogListener;

    public HeadDialog(Context context,OnSingleDialogListener singleDialogListener) {
            super(context);
        this.singleDialogListener = singleDialogListener;
    }
    //定义回调事件，用于dialog的点击事件
    public interface OnSingleDialogListener{
        public void choose(int what);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_head);
        setTitle("选择头像");
        initView();
    }

    private void initView() {
        rg = (RadioGroup) findViewById(R.id.head_rg);
        btn1=(RadioButton)findViewById(R.id.head_but1);
        btn2=(RadioButton)findViewById(R.id.head_but2);
        btn3=(RadioButton)findViewById(R.id.head_but3);
        btn4=(RadioButton)findViewById(R.id.head_but4);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.head_but1:
                        singleDialogListener.choose(1);
                        break;
                    case R.id.head_but2:
                        singleDialogListener.choose(2);
                        break;
                    case R.id.head_but3:
                        singleDialogListener.choose(3);
                        break;
                    case R.id.head_but4:
                        singleDialogListener.choose(4);
                        break;
                }
            }

        });
    }

}