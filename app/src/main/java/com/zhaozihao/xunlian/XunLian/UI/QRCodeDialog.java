package com.zhaozihao.xunlian.XunLian.UI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.R;


/**
 * 自定义dialog
 * @author Mr.Xu
 *
 */

public class QRCodeDialog extends Dialog {
    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    Tools tools;
    private Bitmap bm;
    String account;
    private OnCustomDialogListener customDialogListener;
    ImageView img;
    TextView title;

    public QRCodeDialog(Context context, Bitmap bm, String account, OnCustomDialogListener customDialogListener) {
        super(context);
        this.bm = bm;
        this.account =account;
        tools = new Tools(context);
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qrcode);
        //设置标题
        setTitle("扫描二维码加我好友");
        title = (TextView) findViewById(R.id.qr_title);
        title.setText("讯连账号:"+account);
        img = (ImageView) findViewById(R.id.dialog_img);
        img.setImageBitmap(bm);
        img.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            customDialogListener.back(tools.saveBitmap(bm,account));

            QRCodeDialog.this.dismiss();



        }
    };

}