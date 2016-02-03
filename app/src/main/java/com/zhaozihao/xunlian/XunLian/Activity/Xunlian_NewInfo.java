package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Tools.AppManger;
import com.zhaozihao.xunlian.XunLian.Tools.MyToast;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.XunLian.UI.TianJiaDialog;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Xunlian_NewInfo extends Activity implements OnClickListener{
	TextView weiboT = null;
	TextView qqT = null;
	TextView phoneT = null;
	TextView emailT = null;
	Button push = null;
	EditText phone1 = null;
	TextView Title = null;
	TextView peraccount = null;
	EditText phone2 = null;
	EditText phone3 = null;
	EditText email1 = null;
	EditText email2 = null;
	EditText email3 = null;
	EditText per_name = null;
	EditText qq = null;
	String account;
	SharedPreferences sp = null;
	String[] newinfo;
	EditText weibo1 = null;
	String[] update;
	String[] oldinfo;
	String[] oldinfo1;
	Tools tools = new Tools(Xunlian_NewInfo.this);
	Bitmap headbit = null;
	private String[] items = new String[] { "打开图库", "使用相机" };
	private static int CAMERA_REQUEST_CODE = 1;
	private static int GALLERY_REQUEST_CODE = 2;
	private static int CROP_REQUEST_CODE = 3;
	ImageView photo = null;
	ImageView phoneP = null;
	ImageView emailP = null;
	ImageView QQP = null;
	ImageView weiboP = null;
	String requestResult = "";
	ProgressDialog pd = null;
	AppManger appManger;
	String data = "";
	MyToast myToast = null;
	String[] info = new String[]{"","","","","","","","","","","",""};
	private Handler mhandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
				case 0:
					myToast.showToast(Xunlian_NewInfo.this, msg.obj.toString(), 0);
			}
		};
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_info);
		init(getAccount());

		final Intent intent = getIntent();
		String type = intent.getStringExtra("type");
		if(type.equals("Edit")){
			setTextVisible();
			push.setText("更新");
			Title.setText("更新个人信息");
			String infostr = intent.getStringExtra("info");
			oldinfo = infostr.split("#");
			oldinfo1 = getSelfInfo();
			String selfInfoStr = "";
			for(int i = 0;i<oldinfo1.length;i++){
				selfInfoStr += oldinfo1[i]+"#";
			}
			Log.e("Xunlian_NewInfo", infostr);
			Log.e("Xunlian_NewInfo", selfInfoStr);
			if(selfInfoStr.equals("无#无#无#无#无#无#无#无#无#无#")){
				setPersonInfo(oldinfo);
			}else{
				setPersonInfo(oldinfo1);
				oldinfo = oldinfo1;
			}
			push.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					int type;
					getEditString();
					for (int i = 1; i < newinfo.length; i++) {
						if (!oldinfo[i].equals(newinfo[i])) {
							type = (int) Math.pow(2, i - 1);
							data = data + type + "#" + newinfo[i] + "#!";
						}
						Log.e("data",data);
					}

					if (!newinfo[0].equals(oldinfo[0])&&data.equals("")) {
						update = new String[]{"no"};
					}else{
						update = data.split("!");
					}

					if (!data.equals("")||update[0].equals("no")) {
						new Thread(new Runnable() {
							@Override
							public void run() {

								String strname;
								strname = tools.decryption(newinfo[0]);
								String result = tools.sendString2ServersSocket(tools.Key2Json("6", peraccount.getText().toString(), strname, "1", update));
								String[] strarry = new String[]{"false"};
								try {
									strarry = tools.parseJSONMark12(result);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								if (strarry[0].equals("success")) {
									Message msg = new Message();
									msg.what = 0;
									msg.obj = "更新成功";
									mhandler.sendMessage(msg);
									setselfInfo(peraccount.getText().toString(), newinfo);
									update = null;
									appManger.finishActivity(Xunlian_NewInfo.this);
								} else {
									Message msg1 = new Message();
									msg1.what = 0;
									msg1.obj = "更新失败";
									mhandler.sendMessage(msg1);
								}

							}
						}).start();
					} else {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = "您还没有更新什么内容哦,更新后再来点击我吧";
						mhandler.sendMessage(msg);
					}

				}
			});

		}else{
			push = (Button) findViewById(R.id.newinfo_but_push);
			push.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					new TianJiaDialog(1, Xunlian_NewInfo.this, getAccount(), new TianJiaDialog.OnCustomDialogListener() {
						@Override
						public void back(String strinfo) {
							// TODO: 2016/1/20 0020 将添加密码设置添加到完善信息里面
							if (strinfo.equals("success")) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										try {
											Thread.sleep(5000);
											if (pd != null) {
												pd.dismiss();
												Message msg = new Message();
												msg.what = 4;
												msg.obj = "网络超时,请检查您的网络连接";
												handler.sendMessage(msg);
											}
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}).start();
								new Thread(new Runnable() {
									String name[] = {"account", "mark", "name", "head", "personMailNumber", "homeMailNumber", "workMailNumber", "personPhoneNumber", "workPhoneNumber", "homePhoneNumber", "qqNumber", "weiboNumber"};
									String value[] = getEditString();

									@Override
									public void run() {
										// TODO Auto-generated method stub
										String forgetstr = tools.key2Json(name, value);
										Message message = Message.obtain();
										message.what = 1;
										handler.sendMessage(message);
										try {
											requestResult = tools.sendString2ServersSocket(forgetstr);
											String[] strarry = tools.parseJSONMark12(requestResult);
											if (strarry[0].equals("failure")) {
												Thread.sleep(1000);
												pd.dismiss();
												pd = null;
												Message msg1 = new Message();
												msg1.what = 2;
												msg1.obj = "注册失败";
												handler.sendMessage(msg1);
											} else if (strarry[0].equals("success")) {
												Thread.sleep(1000);
												pd.dismiss();
												pd = null;
												setselfInfo(info[0], newinfo);
												Intent intent = new Intent(Xunlian_NewInfo.this, Xunlian_MainActivity.class);
												startActivity(intent);
												appManger.finishActivity(Xunlian_NewInfo.this);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}).start();
							} else if (info.equals("outTime")) {
								Message msg1 = new Message();
								msg1.what = 2;
								msg1.obj = "网络连接超时,请重新设置";
								handler.sendMessage(msg1);
							} else {
								Message msg1 = new Message();
								msg1.what = 2;
								msg1.obj = "发生不可预知的错误";
								handler.sendMessage(msg1);
							}

						}
					}).show();


				}
			});
		}

        }
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	public String getAccount() {
		SharedPreferences sp = null;
		sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
		String str = sp.getString("account", "");
		return  str;
	}

	private void setPersonInfo(String[] info) {
		per_name.setText(tools.decryption(info[0]));
		phone1.setText(tools.decryption(info[1]));
		phone2.setText(tools.decryption(info[2]));
		phone3.setText(tools.decryption(info[3]));
		email1.setText(tools.decryption(info[4]));
		email2.setText(tools.decryption(info[5]));
		email3.setText(tools.decryption(info[6]));
		qq.setText(tools.decryption(info[7]));
		weibo1.setText(tools.decryption(info[8]));
		peraccount.setText(tools.decryption(info[9]));
	}
	public String[] getSelfInfo() {
		sp = getSharedPreferences( account + "info", Context.MODE_PRIVATE);
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
	private void init(String account_number) {
		appManger = AppManger.getAppManger();
		appManger.addActivity(Xunlian_NewInfo.this);
		myToast = new MyToast();
		account = account_number;
		Resources r = this.getResources();
        phoneT = (TextView) findViewById(R.id.newinfo_phoneT);
		phoneP = (ImageView) findViewById(R.id.newinfo_img_phoneP);
		emailT = (TextView) findViewById(R.id.newinfo_emailT);
		emailP = (ImageView) findViewById(R.id.newinfo_img_emailP);
		qqT = (TextView) findViewById(R.id.newinfo_qqT);
		QQP = (ImageView) findViewById(R.id.newinfo_img_QQP);
		weiboT = (TextView) findViewById(R.id.newinfo_weiboT);
		weiboP = (ImageView) findViewById(R.id.newinfo_img_weiboP);
		headbit = BitmapFactory.decodeResource(r, R.drawable.demo);
		phone1 = (EditText) findViewById(R.id.newinfo_edt_StrPhone1);
		Title = (TextView) findViewById(R.id._title);
		peraccount = (TextView) findViewById(R.id.account_number);
		peraccount.setText(account_number);
		phone2 = (EditText) findViewById(R.id.newinfo_edt_StrPhone2);
		phone3 = (EditText) findViewById(R.id.newinfo_edt_StrPhone3);
		email1 = (EditText) findViewById(R.id.newinfo_edt_StrEmail1);
		email2 = (EditText) findViewById(R.id.newinfo_edt_StrEmail2);
		email3 = (EditText) findViewById(R.id.newinfo_edt_StrEmail3);
		per_name = (EditText) findViewById(R.id.newinfo_edt_name);
		qq = (EditText) findViewById(R.id.newinfo_edt_Strqq);
		weibo1 = (EditText) findViewById(R.id.newinfo_edt_Strweibo1);
		photo = (ImageView) findViewById(R.id.newinfo_img_photo);
		photo.setOnClickListener(this);
		phoneP.setOnClickListener(this);
		weiboP.setOnClickListener(this);
		QQP.setOnClickListener(this);
		emailP.setOnClickListener(this);
		phoneT.setOnClickListener(this);
		weiboT.setOnClickListener(this);
		qqT.setOnClickListener(this);
		emailT.setOnClickListener(this);
		push = (Button) findViewById(R.id.newinfo_but_push);

	}


	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				// TODO Auto-generated method stub
				pd = tools.creatDialog(Xunlian_NewInfo.this,"正在提交","我在努力的加载中....");
				pd.show();
		    }else if(msg.what==2){
				Toast.makeText(Xunlian_NewInfo.this,msg.obj.toString(),0).show();
		    }else if(msg.what == 4){
				if(pd!=null){
					pd.dismiss();
					Toast.makeText(Xunlian_NewInfo.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				}
				}

		}

	};


	private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle("选择头像")
                .setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 0:
								Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
								intent.setType("image/*");
								startActivityForResult(intent, GALLERY_REQUEST_CODE);
								break;
							case 1:
								Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								startActivityForResult(intent1, CAMERA_REQUEST_CODE);
								break;
						}
					}
				})
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

    }


	public static Bitmap createImageThumbnail(String filePath){
		Bitmap bitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 400*400);
		opts.inJustDecodeBounds = false;

		try {
			bitmap = BitmapFactory.decodeFile(filePath, opts);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CAMERA_REQUEST_CODE)
		{
			if(data == null)
			{
				return;
			}
			else
			{
				Bundle extras = data.getExtras();
				if(extras != null)
				{
					Bitmap bm = extras.getParcelable("data");
					tools.saveBitmap(bm,"avater");
					headbit = createImageThumbnail(tools.createDirOnSDCard() + "/" + "avater.jpg");
					tools.saveBitmap(headbit,"avater");
					Uri uri = Uri.fromFile(new File(tools.createDirOnSDCard() + "/" + "avater.jpg"));
					//photo.setImageBitmap(headbit);
					startImageZoom(uri);
				}
			}
		}
		else if(requestCode == GALLERY_REQUEST_CODE)
		{
			if(data == null)
			{
				return;
			}
			Uri uri1 = data.getData();

			InputStream is = null;
			try {
				is = getContentResolver().openInputStream(uri1);
				Bitmap bm = BitmapFactory.decodeStream(is);
				is.close();
				tools.saveBitmap(bm, "avater");
				headbit = createImageThumbnail(tools.createDirOnSDCard() + "/" + "avater.jpg");
				tools.saveBitmap(headbit,"avater");
				Uri uri = Uri.fromFile(new File(tools.createDirOnSDCard() + "/" + "avater.jpg"));
				//photo.setImageBitmap(headbit);
				startImageZoom(uri);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(requestCode == CROP_REQUEST_CODE)
		{
			if(data == null)
			{
				return;
			}
			Bundle extras = data.getExtras();
			if(extras == null){
				return;
			}
			Bitmap bm = extras.getParcelable("data");
			photo.setImageBitmap(bm);
		}
	}


	/**
     *
     * @param uri
     */
	private void startImageZoom(Uri uri)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_REQUEST_CODE);
	}


public String[] getEditString(){

	info[0] = peraccount.getText().toString();
	info[1] = "14";

	info[2] = per_name.getText().toString();

//	ByteArrayOutputStream stream = new ByteArrayOutputStream();
//	headbit.compress(Bitmap.CompressFormat.JPEG, 60, stream);
//	byte[] bytes = stream.toByteArray();
//	String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
//	Log.e("photo",bytes.length/1024+"");
//// TODO: 2015/11/19 0019
	info[3] = "1";

	info[4] = email1.getText().toString();
	info[5] = email2.getText().toString();
	info[6] = email3.getText().toString();

	info[7] = phone1.getText().toString();
	info[8] = phone2.getText().toString();
	info[9] = phone3.getText().toString();

	info[10] = qq.getText().toString();
	info[11] = weibo1.getText().toString();
	newinfo = new String[]{tools.encryption(info[2]),tools.encryption(info[7]),tools.encryption(info[8]),tools.encryption(info[9]),tools.encryption(info[4]),tools.encryption(info[5]),tools.encryption(info[6]),tools.encryption(info[10]),tools.encryption(info[11]),tools.encryption(info[0])};

	return info;
}
	void setTextVisible(){

		phone1.setVisibility(View.VISIBLE);
		phone2.setVisibility(View.VISIBLE);
		phone3.setVisibility(View.VISIBLE);
		email1.setVisibility(View.VISIBLE);
		email2.setVisibility(View.VISIBLE);
		email3.setVisibility(View.VISIBLE);
		qq.setVisibility(View.VISIBLE);
		weibo1.setVisibility(View.VISIBLE);

		phoneT.setText("请更新手机号");
		emailT.setText("请更新邮箱号");
		qqT.setText("请更新 Q Q 号");
		weiboT.setText("请更新微博号");
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.newinfo_img_photo:
			showDialog();break;
		case R.id.newinfo_img_phoneP:
			if(phone1.getVisibility() == View.VISIBLE){
				phone1.setVisibility(View.GONE);
				phone2.setVisibility(View.GONE);
				phone3.setVisibility(View.GONE);
			}else{
				phone1.setVisibility(View.VISIBLE);
				phone2.setVisibility(View.VISIBLE);
				phone3.setVisibility(View.VISIBLE);
			}
			break;
			case R.id.newinfo_phoneT:
				if(phone1.getVisibility() == View.VISIBLE){
					phone1.setVisibility(View.GONE);
					phone2.setVisibility(View.GONE);
					phone3.setVisibility(View.GONE);
				}else{
					phone1.setVisibility(View.VISIBLE);
					phone2.setVisibility(View.VISIBLE);
					phone3.setVisibility(View.VISIBLE);
				}


				break;
			case R.id.newinfo_img_emailP:
				if(email1.getVisibility() == View.VISIBLE){
					email1.setVisibility(View.GONE);
					email2.setVisibility(View.GONE);
					email3.setVisibility(View.GONE);
				}else{
					email1.setVisibility(View.VISIBLE);
					email2.setVisibility(View.VISIBLE);
					email3.setVisibility(View.VISIBLE);
				}

				break;
			case R.id.newinfo_emailT:
				if(email1.getVisibility() == View.VISIBLE){
					email1.setVisibility(View.GONE);
					email2.setVisibility(View.GONE);
					email3.setVisibility(View.GONE);
				}else{
					email1.setVisibility(View.VISIBLE);
					email2.setVisibility(View.VISIBLE);
					email3.setVisibility(View.VISIBLE);
				}

				break;
			case R.id.newinfo_img_QQP:
				if(qq.getVisibility() == View.VISIBLE){
					qq.setVisibility(View.GONE);
				}else{
					qq.setVisibility(View.VISIBLE);
				}

				break;
			case R.id.newinfo_qqT:
				if(qq.getVisibility() == View.VISIBLE){
					qq.setVisibility(View.GONE);
				}else{
					qq.setVisibility(View.VISIBLE);
				}

				break;
			case R.id.newinfo_img_weiboP:
				if(weibo1.getVisibility() == View.VISIBLE){
					weibo1.setVisibility(View.GONE);
				}else{
					weibo1.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.newinfo_weiboT:
				if(weibo1.getVisibility() == View.VISIBLE){
					weibo1.setVisibility(View.GONE);
				}else{
					weibo1.setVisibility(View.VISIBLE);
				}
				break;
		default:
			break;
		}
		
	}
	public void setselfInfo(String account,String[] info){
		sp = getSharedPreferences(account+"info", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("name",  info[0]);
		editor.putString("phone1",info[1]);
		editor.putString("phone2",info[2]);
		editor.putString("phone3",info[3]);
		editor.putString("email1",info[4]);
		editor.putString("email2",info[5]);
		editor.putString("email3",info[6]);
		editor.putString("qq",    info[7]);
		editor.putString("weibo", info[8]);
		editor.putString("account", info[9]);
		editor.commit();
	}


}
