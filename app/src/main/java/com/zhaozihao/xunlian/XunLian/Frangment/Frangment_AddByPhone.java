package com.zhaozihao.xunlian.XunLian.Frangment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_PersonInfo;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.XunLian.UI.TianJiaDialog;

import org.json.JSONObject;

public class Frangment_AddByPhone extends Fragment {
	Button check = null;
	EditText getPhone = null;
	String account = null;
	String friendaccount  = "";
	Tools tools = new Tools(getActivity());
	String[] info ;
	MyToast myToast = null;
	ProgressDialog pd = null;
	String question;
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0){
				pd = tools.creatDialog(getActivity(),"等等就好",msg.obj.toString());
				pd.show();
			}else if(msg.what==3){
				if(pd!=null){
					pd.dismiss();
					myToast.showToast(getActivity(), msg.obj.toString(), 0);
				}
			}else if(msg.what==4){
				myToast.showToast(getActivity(), msg.obj.toString(), 0);
			}else if(msg.what == 5){
				tianjia();
			}
		}

	};
	void tianjia(){
		new TianJiaDialog(2, getActivity(), account,question,friendaccount, new TianJiaDialog.OnCustomDialogListener() {
			@Override
			public void back(String info) {
				if (info.equals("success")) {
					getInfo();
				} else {
					Message msg1 = new Message();
					msg1.what = 4;
					msg1.obj = "添加问题错误";
					handler.sendMessage(msg1);
				}
			}
		}).show();
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
    	View view = inflater.inflate(R.layout.add_phone, container, false);
		myToast = new MyToast();
    	check = (Button) view.findViewById(R.id.check);
    	getPhone = (EditText) view.findViewById(R.id.getPhone);
    	check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				friendaccount = getPhone.getText().toString();
				account = getAccount();
				if (friendaccount.equals("")) {
					myToast.showToast(getActivity(), "输入的手机号不可以为空哦...", Toast.LENGTH_SHORT);
				} else if(friendaccount.equals(account)){
					myToast.showToast(getActivity(), "不可以添加自己哦", Toast.LENGTH_SHORT);
				}else{
					/*
					 *这里面添加请求朋友所有信息的
					 */
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								String result = tools.sendString2ServersSocket(tools.Key2Json("9", new String[]{"account", "friendaccount"}, new String[]{account, friendaccount}));
								JSONObject jsonObj = new JSONObject(result);
								int error = jsonObj.getInt("error");
								if(error == 3){
									getInfo();
								}else if(error == 4){
									JSONObject jresult = jsonObj.getJSONObject("result");
									question = jresult.getString("ResultINFO");
									Message msg = Message.obtain();
									msg.what = 5;
									handler.sendMessage(msg);
								}


							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();

				}
			}
		});
       return  view;
    }
	void getInfo(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					if(pd!=null){
						Message msg = Message.obtain();
						msg.what = 3;
						msg.obj = "网络连接超时,查询失败";
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
				try {
					Message msg = Message.obtain();
					msg.obj = "正在查找信息";
					handler.sendMessage(msg);
					String result = tools.sendString2ServersSocket(tools.Key2Json("18", "account",friendaccount));
					JSONObject jsonObj = new JSONObject(result);
					int error = jsonObj.getInt("error");
					if(error==0){
						info = tools.parseJSONMark9(result);
						String infostr = "";
						for(int i = 0;i<info.length;i++) {
							infostr += info[i]+"#";
						}
						Intent intent = new Intent();
						intent.putExtra("info",infostr);
						intent.putExtra("result", friendaccount);
						intent.putExtra("type", "add");
						intent.setClass(getActivity(), Xunlian_PersonInfo.class);
						startActivity(intent);
						pd.dismiss();
						pd = null;
					}else{
						info = tools.parseJSONMark12(result);
						Message msg1 = new Message();
						msg1.what = 4;
						msg1.obj = info[1];
						handler.sendMessage(msg1);
						pd.dismiss();
						pd = null;
					}
				}
				catch ( Exception e) {
					e.printStackTrace();
				}
			}



		}).start();
	}
	public String getAccount() {
		SharedPreferences sp = null;
		sp = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
		String str = sp.getString("account","");
		return  str;
	}
}