package com.zhaozihao.xunlian.XunLian.Frangment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Adapter.AppPersonListAdapter;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Person;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.XunLian.Tools.UpdateInfo;
import com.zhaozihao.xunlian.dao.PersonDao;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class Frangment_Xunlian_PersonList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

	ListView lv = null;
	AppPersonListAdapter ma = null;
	String account = "";
	SharedPreferences sp = null;
	AlertDialog.Builder ad;
	List<UpdateInfo> list = new ArrayList<UpdateInfo>();;
	List<List<UpdateInfo>> perlist ;
	Boolean isSuccess = false;
	List<Person> personList = new ArrayList<Person>();
	private SwipeRefreshLayout mSwipeLayout;
	PersonDao dao;
	Boolean is1 = false;
	View view;
	MyToast myToast = null;
	Tools tools = new Tools(getActivity());
	ProgressDialog pd = null;
	public UpdateListener updateListener;
	private Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					if (!isSuccess) {
						mSwipeLayout.setRefreshing(false);
						myToast.showToast(getActivity(), "网络连接超时,刷新失败", 0);
					} else {
						myToast.showToast(getActivity(), msg.obj.toString(), 0);
						ma.notifyDataSetChanged();
						lv.setAdapter(ma);
						mSwipeLayout.setRefreshing(false);
					}
					break;
				case 1:
					mSwipeLayout.setRefreshing(false);
					myToast.showToast(getActivity(), msg.obj.toString(), 0);
					break;
				case 2:
					if (!isSuccess) {
						myToast.showToast(getActivity(), "网络连接超时,刷新失败", 0);
					} else {
						ma.notifyDataSetChanged();
						lv.setAdapter(ma);
						mSwipeLayout.setRefreshing(false);
					}
					break;
				case 3:
					if(pd!=null){
						pd.dismiss();
					}
					ad.show();
					mSwipeLayout.setRefreshing(false);
					break;
				case 4:
					if(pd!=null){
						pd.dismiss();
					}
					ma.notifyDataSetChanged();
					lv.setAdapter(ma);
					break;
				case 5:
					ma.notifyDataSetChanged();
					lv.setAdapter(ma);
					myToast.showToast(getActivity(), msg.obj.toString(), 0);
					break;
				case 6:
					if (!isSuccess) {
						mSwipeLayout.setRefreshing(false);
						myToast.showToast(getActivity(), "网络连接超时,刷新失败", 0);
					} else {
						myToast.showToast(getActivity(), msg.obj.toString(), 0);
						ma.notifyDataSetChanged();
						lv.setAdapter(ma);
						mSwipeLayout.setRefreshing(false);

					}
					break;
				case 7:
					pd = tools.creatDialog(getActivity(),msg.obj.toString(),"我在努力的加载中....");
					pd.show();
					break;
			}
		}
	};
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		System.out.println("LeftFragment--->onAttach");
		updateListener = (UpdateListener) activity;
	}
	public interface UpdateListener
	{
		public void send(List<List<UpdateInfo>> perlist );
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.local_person_list, container, false);
		init(view);

		return view;

	}

	private void init(View view) {

		myToast = new MyToast();
		mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);
		account = getAccount();
		ad = new AlertDialog.Builder(getActivity());
		ad.setTitle("友情提示");
		ad.setMessage("\n您的讯连好友列表还没有添加\n点击添加按钮,添加小伙伴们吧....\n");
		sp = getActivity().getSharedPreferences(account, Context.MODE_PRIVATE);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_purple, android.R.color.holo_blue_light);
		lv = (ListView) view.findViewById(R.id.Local_PersonList);
		dao = new PersonDao(getActivity(), account);
		personList = dao.queryAll();
		if (is1||personList.equals(null) || personList.size() == 0 || personList.isEmpty()) {
			Refresh(1);
		} else {
			isSuccess = true;
			ma = new AppPersonListAdapter(getActivity(), personList, account, new AppPersonListAdapter.OnDeleteResult() {
				@Override
				public void back(String name) {
					Message msg = Message.obtain();
					msg.what = 5;
					msg.obj = name;
					mhandler.sendMessage(msg);
				}
			},view);
			Message msg = Message.obtain();
			msg.what = 4;
			mhandler.sendMessage(msg);

		}


	}

	public String getAccount() {
		SharedPreferences sp = null;
		sp = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
		String str = sp.getString("account", "");
		return str;
	}


	@Override
	public void onRefresh() {
		Refresh(2);
	}

	private void Refresh(final int a) {
		dao = new PersonDao(getActivity(), account);
		isSuccess = false;
		if (a == 1) {
			is1 = true;
//			Message msg = Message.obtain();
//			msg.what = 7;
//			msg.obj = "我正在帮您查找数据...";
//			mhandler.sendMessage(msg);
			new Thread(new Runnable() {
				@Override
				public void run() {

					String string = tools.sendString2ServersSocket(tools.Key2Json("7", "account", account));
					try {
						personList = tools.parseJSONMark7(string);
						dao.deleteAll("");
						for (int i = 0; i < personList.size(); i++) {
							dao.insert(personList.get(i));
						}
						isSuccess = true;
						if (personList.size() == 0) {
							Message msg = Message.obtain();
							msg.what = 3;
							mhandler.sendMessage(msg);
							return;
						}
						personList = dao.queryAll();
						ma = new AppPersonListAdapter(getActivity(), personList, account, new AppPersonListAdapter.OnDeleteResult() {
							@Override
							public void back(String result) {
								Message msg = Message.obtain();
								msg.what = 5;
								msg.obj = result;
								mhandler.sendMessage(msg);
							}
						}, view);
						Message msg = Message.obtain();
						msg.what = 4;
						mhandler.sendMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else {

			new Thread(new Runnable() {
				@Override
				public void run() {
					String string = tools.sendString2ServersSocket(tools.Key2Json("8", "account", account));
					try {
						perlist = tools.parseJSONMark8(string);
						if (perlist.size() < 1) {
							Message msg = Message.obtain();
							msg.what = 1;
							isSuccess = true;
							msg.obj = "列表信息已经是最新的";
							mhandler.sendMessage(msg);
							return;
						} else {

							for(int i = 0;i<perlist.size();i++){
								List<UpdateInfo> uplist = perlist.get(i);
								for(int j = 0;j<uplist.size();j++){
									UpdateInfo info = uplist.get(j);
									list.add(info);
								}
							}
							if(dao.update(list,list.get(0).getName())){
								isSuccess = true;
								personList = dao.queryAll();
								if(personList.equals(null)){
									Message msg = Message.obtain();
									msg.what = 1;
									msg.obj = "更新失败";
									mhandler.sendMessage(msg);
								}else {
									String string1 = tools.sendString2ServersSocket(tools.Key2Json("22", "account", account));
									String[] strarry = tools.parseJSONMark12(string1);
									if(strarry[0].equals("failure")){
										Message msg = Message.obtain();
										msg.what = 0;
										msg.obj =strarry[1];
										mhandler.sendMessage(msg);
									}else if(strarry[0].equals("success")){
										ma = new AppPersonListAdapter(getActivity(), personList, account, new AppPersonListAdapter.OnDeleteResult() {
											@Override
											public void back(String result) {
												Message msg = Message.obtain();
												msg.what = 5;
												msg.obj = result;
												mhandler.sendMessage(msg);
											}
										}, view);
										updateListener.send(perlist);
										Message msg = Message.obtain();
										msg.what = 6;
										msg.obj =strarry[1];
										mhandler.sendMessage(msg);
									}

								}


							}else{
								Message msg = Message.obtain();
								msg.what = 1;
								msg.obj = "更新失败";
								mhandler.sendMessage(msg);
							}

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				}).start();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					if (!isSuccess) {
						Message msg = Message.obtain();
						msg.what = 1;
						msg.obj = "网络连接超时,刷新失败";
						mhandler.sendMessage(msg);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}

