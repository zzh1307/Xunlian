package com.zhaozihao.xunlian.XunLian.Frangment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_Account;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_AccountSafe;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_LockActivity;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_NewInfo;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_PatternUnlockSetting;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.XunLian.UI.QRCodeDialog;
import com.zhaozihao.xunlian.zxing.encoding.EncodingHandler;


public class Frangment_Setting extends Fragment implements OnClickListener{
	TextView Setting_Clean = null;
	TextView Setting_SafeItem = null;
	TextView Item_Name = null;
	TextView Item_Account = null;
	TextView Item_Phone = null;
	TextView Setting_DropOut = null;
	ImageView Setting_HeadPic = null;
	ImageView QR_share = null;
	String account = null;
	Intent intent1;
	String[] info;
	Tools tools;
	SharedPreferences sp;
	SharedPreferences.Editor editor;
	View view;
	Boolean isFinish = false;
	MyToast myToast = null;
	ProgressDialog pd = null;
	public android.os.Handler handler = new android.os.Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				pd = tools.creatDialog(getActivity(),"正在加载","我在努力的加载中....");
				pd.show();
			}else if(msg.what==2){
				if(pd!=null){
					pd.dismiss();
					pd=null;
					myToast.showToast(getActivity(), msg.obj.toString(), 0);
				}
			}else if(msg.what==3){
				set();
				if(pd!=null){
					pd.dismiss();
					pd=null;
				}
			}else if(msg.what==4){
				myToast.showToast(getActivity(),msg.obj.toString(),0);
			}
			}

	};
	

    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
		view = new View(getActivity());
    	view = inflater.inflate(R.layout.setting, container, false);
		init(view);
		tools = new Tools(getActivity());
        return view;
          
    }
	void getselfinfo(){
		info = getSelfInfo();
		intent1 = new Intent();
		String infostr = "";
		for(int i = 0;i<info.length;i++) {
			infostr += info[i]+"#";
		}
		if(infostr.equals("无#无#无#无#无#无#无#无#无#无#")){
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
			account = getAccount();
			getAccountinfo(account);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						if(isFinish){
							intent1 = new Intent();
							String infostr = "";
							for(int i = 0;i<info.length;i++) {
								infostr += info[i]+"#";
							}
							Message msg1 = new Message();
							msg1.what = 3;
							handler.sendMessage(msg1);
							setselfInfo(account,info);
							intent1.putExtra("type", "Edit");
							intent1.putExtra("info", infostr);
						}else{
							Message msg1 = new Message();
							msg1.what = 2;
							msg1.obj = "网络连接超时,无法获取个人信息";
							handler.sendMessage(msg1);
							Item_Name.setOnClickListener(null);
							Item_Phone.setOnClickListener(null);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();

		}else{
			Message msg1 = new Message();
			msg1.what = 3;
			handler.sendMessage(msg1);
			intent1.putExtra("type", "Edit");
			intent1.putExtra("info", infostr);
		}
	}

	public void setselfInfo(String account,String[] info){
		sp = getActivity().getSharedPreferences(account+"info", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("name",  info[0]);
		editor.putString("phone1",info[1]);
		editor.putString("phone2",info[2]);
		editor.putString("phone3", info[3]);
		editor.putString("email1",info[4]);
		editor.putString("email2", info[5]);
		editor.putString("email3", info[6]);
		editor.putString("qq", info[7]);
		editor.putString("weibo", info[8]);
		editor.putString("account", info[9]);
		editor.commit();
	}

	private void init(View view) {
		account = getAccount();
		Item_Account = (TextView) view.findViewById(R.id.Setting_AccountItem);
		QR_share = (ImageView) view.findViewById(R.id.QR_share);
		Setting_Clean = (TextView) view.findViewById(R.id.Setting_Clean);
		Setting_SafeItem = (TextView) view.findViewById(R.id.Setting_SafeItem);
		Setting_DropOut = (TextView) view.findViewById(R.id.Setting_DropOut);
		Setting_HeadPic = (ImageView) view.findViewById(R.id.Setting_HeadIMG);
		Item_Name = (TextView) view.findViewById(R.id.ItemName);
		Item_Phone = (TextView) view.findViewById(R.id.ItemPhone);
		QR_share.setOnClickListener(this);
		Setting_Clean.setOnClickListener(this);
		Setting_SafeItem.setOnClickListener(this);
		Setting_DropOut.setOnClickListener(this);
		Setting_HeadPic.setOnClickListener(this);
		Item_Account.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		getselfinfo();
		super.onStart();
	}

	private void set() {
		Item_Name.setText(tools.decryption(info[0]));
		Item_Phone.setText("讯连账号:"+tools.decryption(info[9]));
		Item_Name.setOnClickListener(this);
		Item_Phone.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.Setting_HeadIMG:
				break;
			case R.id.QR_share:
				try {
				Bitmap bm = EncodingHandler.createQRCode(getAccount(), 400);
				QRCodeDialog dg = new QRCodeDialog(getActivity(),bm,account,new QRCodeDialog.OnCustomDialogListener() {

					@Override
					public void back(String name) {
						Toast.makeText(getActivity(),name,0).show();
					}
				});
				dg.show();
				}catch (Exception e){
				}
				break;
			case R.id.Setting_Clean:
				myToast.showToast(getActivity(),"正在备份联系人...",0);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(4000);
							Message mag = new Message();
							mag.what = 4;
							mag.obj = "备份 成功";
							handler.sendMessage(mag);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();

				break;
			case R.id.Setting_SafeItem:
				Intent intent = new Intent();
				sp = getActivity().getSharedPreferences("PatternUnlockpassword", getActivity().MODE_PRIVATE);
				editor = sp.edit();
				String strPass = sp.getString("password", "");
				if(strPass.equals("")){
					intent.setClass(getActivity(), Xunlian_PatternUnlockSetting.class);
				}else{
					intent.setClass(getActivity(), Xunlian_LockActivity.class);
				}
				intent.putExtra("isReset",true);
				startActivity(intent);
				break;
			case R.id.Setting_DropOut:
				// TODO: 2015/11/23 0023 退出账户,并且将图案所解锁
				AlertDialog ad = null;
				ad = new AlertDialog.Builder(getActivity())
						.setTitle("退出账号")
						.setMessage("确认对出账号?退出账号后,将会删除本地的缓存数据.")
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								sp = getActivity().getSharedPreferences("PatternUnlockpassword", getActivity().MODE_PRIVATE);
								editor = sp.edit();
								editor.clear();
								editor.commit();
								sp = getActivity().getSharedPreferences("autoLogin", getActivity().MODE_PRIVATE);
								editor = sp.edit();
								editor.clear();
								editor.commit();
								Message msg = new Message();
								msg.what = 2;
								msg.obj = "退出成功";
								handler.sendMessage(msg);
								sp = getActivity().getSharedPreferences(account + "info", getActivity().MODE_PRIVATE);
								editor = sp.edit();
								editor.clear();
								editor.commit();
								sp = getActivity().getSharedPreferences("Account", getActivity().MODE_PRIVATE);
								editor = sp.edit();
								editor.clear();
								editor.commit();

								sp = getActivity().getSharedPreferences("passwordFile", getActivity().MODE_PRIVATE);
								editor = sp.edit();
								editor.putString(account,"");
								editor.commit();
								startActivity(new Intent(getActivity(), Xunlian_Account.class));
								getActivity().finish();
							}
						})
						.show();


				break;
			case R.id.Setting_AccountItem:
				startActivity(new Intent(getActivity(), Xunlian_AccountSafe.class ));
				break;
			default:
				intent1.setClass(getActivity(), Xunlian_NewInfo.class);
				startActivity(intent1);
				break;
		}
	}
	public String getAccount() {

		SharedPreferences sp = null;
		sp = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
		String str = sp.getString("account","");
		return  str;
	}
	public void getAccountinfo(final String account) {
		 new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String result = tools.sendString2ServersSocket(tools.Key2Json("18", "account",account));
					info = tools.parseJSONMark9(result);
					Thread.sleep(1000);
					for (int i = 0;i<info.length;i++){
						info[i] = tools.encryption(info[i]);
					}
					isFinish = true;
				}
				catch ( Exception e) {
					e.printStackTrace();
				}
			}

		 }).start();
}
	public String[] getSelfInfo() {
		sp = getActivity().getSharedPreferences(account + "info", Context.MODE_PRIVATE);
		String[] selfinfo = new String[10];
		selfinfo[0] = sp.getString("name","无");
		selfinfo[1] = sp.getString("phone1","无");
		selfinfo[2] = sp.getString("phone2","无");
		selfinfo[3] = sp.getString("phone3","无");
		selfinfo[4] = sp.getString("email1","无");
		selfinfo[5] = sp.getString("email2","无");
		selfinfo[6] = sp.getString("email3","无");
		selfinfo[7] = sp.getString("qq","无");
		selfinfo[8] = sp.getString("weibo","无");
		selfinfo[9] = sp.getString("account","无");
		return selfinfo;
	}
}
