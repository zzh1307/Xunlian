package com.zhaozihao.xunlian.XunLian.Tools;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by 赵孜豪 on 2016/1/11 0011.
 */
public class MyToast {
    private static Toast mToast;
    private static Handler mhandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        };
    };

    public static void showToast(Context context, String text, int duration) {
        mhandler.removeCallbacks(r);
        if (null != mToast) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        mhandler.postDelayed(r, 5000);
        mToast.show();
    }

    public static void showToast(Context context, int strId, int duration) {
        showToast(context, context.getString(strId), duration);
    }
}
