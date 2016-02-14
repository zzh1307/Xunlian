package com.zhaozihao.xunlian.XunLian.Frangment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Adapter.AppPersonListAdapter;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;



public class Frangment_PersonList extends Fragment implements OnClickListener{
	Frangment_Xunlian_PersonList local = null;
	Frangment_Phone_PersonList system = null;
	Button System_Btn = null;
	Button Local_Btn = null;
	View viewall = null;
	Tools tools = new Tools(getActivity());
	 FragmentTransaction transaction;
	ProgressDialog pd = null;
	String account = "";
	AppPersonListAdapter.CheckListener checkListener;

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0){
				pd = tools.creatDialog(getActivity(),"正在加载本地联系人","我在拼命的加载中....");
				pd.show();
			}else if(msg.what==1){
				FragmentManager fm = getFragmentManager();
				FragmentTransaction transaction1 = fm.beginTransaction();
				transaction1.replace(R.id.PersonList, system);
				transaction1.commit();
			}


		}

	};
	
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    
    {  
    	viewall = inflater.inflate(R.layout.personlist, container, false);
    	setDefaultFragment();
		checkListener = (AppPersonListAdapter.CheckListener) getActivity();
		System_Btn = (Button) viewall.findViewById(R.id.system_btn);
    	Local_Btn = (Button) viewall.findViewById(R.id.local_btn);
    	System_Btn.setOnClickListener(this);
    	Local_Btn.setOnClickListener(this);
        return   viewall;
    }

   
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		FragmentManager fm = getFragmentManager();
        transaction = fm.beginTransaction();
		switch (arg0.getId()) {
		case R.id.local_btn:
			checkListener.check(true);
			Message msg = Message.obtain();
			msg.what = 0;
			handler.sendMessage(msg);
			new Thread(new Runnable() {
				@Override
				public void run() {
						system = new Frangment_Phone_PersonList();
				}
			}).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						Message msg = Message.obtain();
						msg.what = 1;
						handler.sendMessage(msg);
						Thread.sleep(100);
						pd.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			Local_Btn.setTextColor(Color.parseColor("#0000ff"));
			System_Btn.setTextColor(Color.parseColor("#000000"));
			break;
		case R.id.system_btn:
			local = new Frangment_Xunlian_PersonList();
			Local_Btn.setTextColor(Color.parseColor("#000000"));
			System_Btn.setTextColor(Color.parseColor("#0000ff"));
			transaction.replace(R.id.PersonList, local);
			break;
		default:
			break;
		}
		 transaction.commit(); 
	}
   
	 private void setDefaultFragment()  
	    {  
	        FragmentManager fm = getFragmentManager();  
	        FragmentTransaction transaction1 = fm.beginTransaction();
	        local = new Frangment_Xunlian_PersonList();
	        transaction1.replace(R.id.PersonList, local);
	        transaction1.commit();
	    }  
}
