package com.zhaozihao.xunlian.XunLian.Frangment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;


public class Frangment_Title extends Fragment implements View.OnClickListener{
    AlertDialog ad = null;
    TextView about = null;
    TextView name = null;
    int versionCode;
    String versionName;
    MyToast myToast = null;
    String packageName;
    Signature[] signatures;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
    	View view = inflater.inflate(R.layout.title, container, false);
        about = (TextView) view.findViewById(R.id.about);
        myToast = new MyToast();
        name = (TextView) view.findViewById(R.id.name);
        about.setOnClickListener(this);
        return   view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about:
                PackageManager manager;
                PackageInfo info = null;
                manager = getActivity().getPackageManager();
                try {
                    info = manager.getPackageInfo(getActivity().getPackageName(), 0);
                    versionCode = info.versionCode;
                    versionName = info.versionName;
                    packageName = info.packageName;
                    signatures = info.signatures;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ad = new AlertDialog.Builder(getActivity())
                        .setTitle("关于讯连")
                        .setMessage("\t\t\n\t\t版本号:\t"+versionName+"\n")//\t\t开发团队:小萝卜")
                        .setPositiveButton("检查更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.e("adfadf", "dsfasfd");
                                UmengUpdateAgent.setUpdateAutoPopup(false);
                                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                                    @Override
                                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                                        switch (updateStatus) {
                                            case UpdateStatus.Yes: // has update
                                                UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
                                                break;
                                            case UpdateStatus.No: // has no update
                                                myToast.showToast(getActivity(), "亲,已经是最新版本了..", Toast.LENGTH_SHORT);
                                                break;
                                            case UpdateStatus.NoneWifi: // none wifi
                                                myToast.showToast(getActivity(), "当前网络下无法更新,请连接WIFI后重试", Toast.LENGTH_SHORT);
                                                break;
                                        }
                                    }
                                });
                                UmengUpdateAgent.update(getActivity());
                            }
                        })
                        .show();
        }
    }
}
 