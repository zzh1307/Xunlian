package com.zhaozihao.xunlian.XunLian.Frangment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_PersonInfo;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.XunLian.UI.TianJiaDialog;
import com.zhaozihao.xunlian.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;



public class Frangment_Add extends Fragment implements View.OnClickListener{
	Button add_photo = null;
	Button add_phone = null;
	Frangment_AddByPhone addphone = null;
	String[] info;
	String account = null;
	String friendaccount;
	String question;
	MyToast myToast = null;
	String infostr = "";
	Boolean isFinish = false;
	Tools tools = new Tools(getActivity());
	ProgressDialog pd = null;
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0){
				pd = tools.creatDialog(getActivity(),"正在查找",msg.obj.toString());
				pd.show();
			}else if(msg.what==3){
				if(pd!=null){
					pd.dismiss();
					myToast.showToast(getActivity(), msg.obj.toString(), 0);
				}
			}else if (msg.what == 2) {
				if(pd!=null){
					pd.dismiss();
				}
			} else if (msg.what == 4) {
				myToast.showToast(getActivity(), msg.obj.toString(), 0);
			}else if(msg.what == 5){
				tianjia();
			}


		}

	};

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
    	View view = inflater.inflate(R.layout.addperson, container, false);
		init(view);
    	return view;
    }

	private void init(View view) {
		setDefaultFragment();
		add_phone = (Button) view.findViewById(R.id.addPreson_phonebtn);
		add_photo = (Button) view.findViewById(R.id.addPreson_photobtn);
		add_photo.setOnClickListener(this);
		add_phone.setOnClickListener(this);
		account = getAccount();
		myToast = new MyToast();
	}

	public String getAccount() {
		SharedPreferences sp = null;
		sp = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
		String str = sp.getString("account","");
		return  str;
	}
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode != -1){
    		return;
    	}else if(requestCode == 0||requestCode==300){
			friendaccount = data.getStringExtra("result");
			new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						String result = tools.sendString2ServersSocket(tools.Key2Json("9", new String[]{"account", "friendaccount"}, new String[]{account, friendaccount}));
						JSONObject jsonObj = null;
						jsonObj = new JSONObject(result);
						JSONObject jresult = jsonObj.getJSONObject("result");
						question = jresult.getString("ResultINFO");
						Message msg = new Message();
						msg.what = 5;
						handler.sendMessage(msg);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}).start();

			}
    	}

	private void tianjia() {
		new TianJiaDialog(2,getActivity(), account,question,friendaccount, new TianJiaDialog.OnCustomDialogListener() {
			@Override
			public void back(String key) {
				getPersonInfo(friendaccount);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2500);
							if(isFinish) {
								Message msg = Message.obtain();
								msg.what = 2;
								msg.obj = info[1];
								handler.sendMessage(msg);
								Intent intent = new Intent();
								intent.putExtra("info", infostr);
								intent.putExtra("result",friendaccount);
								intent.putExtra("type", "add");
								intent.setClass(getActivity(), Xunlian_PersonInfo.class);
								startActivity(intent);
							}else{
								Message msg = Message.obtain();
								msg.what = 3;
								msg.obj = info[1];
								handler.sendMessage(msg);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}).show();
	}

	public String[] getPersonInfo(final String friendaccount){

	Message msg = Message.obtain();
	msg.what = 0;
	msg.obj = "正在查找信息";
	handler.sendMessage(msg);
	new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				String result = tools.sendString2ServersSocket(tools.Key2Json("18", "account",friendaccount));
				JSONObject jsonObj = new JSONObject(result);
				int error = jsonObj.getInt("error");
				if(error==0){
					info = tools.parseJSONMark9(result);
					for(int i = 0;i<info.length;i++){
						infostr += info[i]+"#";
					}
					isFinish = true;
				}else{
					info = tools.parseJSONMark12(result);
					isFinish = false;
				}
			}
			catch ( Exception e) {
				e.printStackTrace();
			}
		}
	}).start();
	return info;
}
    private void setDefaultFragment()
    {  
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
		addphone = new Frangment_AddByPhone();
		transaction.replace(R.id.addPreson_content, addphone);
        transaction.commit();
    }  
	@Override
	public void onClick(View arg0) {
		FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();  
        switch (arg0.getId()) {
		case R.id.addPreson_phonebtn:
			 if (addphone == null)  
	            {  
				 addphone = new Frangment_AddByPhone();
	            }  
		        transaction.replace(R.id.addPreson_content, addphone);
			break;
			
		case R.id.addPreson_photobtn:
				Intent startScan = new Intent(getActivity(),CaptureActivity.class);
				startActivityForResult(startScan, 0);
			break;
		default:
			break;
		}
        transaction.commit();
	}
}