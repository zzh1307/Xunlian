package com.zhaozihao.xunlian.XunLian.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhaozihao.xunlian.BuildConfig;
import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Activity.Xunlian_PersonInfo;
import com.zhaozihao.xunlian.XunLian.Tools.Person;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.dao.PersonDao;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

public class AppPersonListAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	List<Person> list;
    Context context;
    int singleSelectedId;
    int po;
    private OnDeleteResult onDeleteResult;
    Tools tools;
    String account;
    String[] strarry = new String[]{"failure"};
    private ExplosionField mExplosionField;
    public Handler handler ;
    View Parentview;
    PopupWindow popwindow;



    public AppPersonListAdapter(final Context context, List<Person> list, String account, OnDeleteResult onDeleteResult,View view) {
        super();  
        this.mInflater = LayoutInflater.from(context);  
        this.list = list; 
        tools = new Tools(context);
        this.context = context;
        this.account = account;
        this.Parentview = view;
        handler =  new Handler(context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    View view1 = (View)msg.obj;
                    view1.findViewById(R.id.Item_Set).setVisibility(View.GONE);
                    addListener(view1.findViewById(R.id.item_bg));
                }else{

                }


            }

        };
        this.onDeleteResult = onDeleteResult;
  
    }
    private void addListener( View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                addListener(parent.getChildAt(i));
            }
        } else {
            mExplosionField.explode(root);
        }
    }
    public interface OnDeleteResult{
        public void back(String name);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
		
	}

	@Override
	public Person getItem(int position) {

        return list.get(position);
    } 

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {  

        ViewHolder holder = null;  

        if (convertView == null) {  

            holder = new ViewHolder();  

            convertView = LayoutInflater.from(context).inflate(  

                    R.layout.app_person_list_item, null);


            holder.image = (ImageView) convertView.findViewById(R.id.ItemImage);  

            holder.account = (TextView) convertView.findViewById(R.id.ItemPhone);

            holder.name = (TextView) convertView.findViewById(R.id.ItemName);

            holder.more = (ImageView) convertView.findViewById(R.id.ItemMore);

            holder.ll = (LinearLayout) convertView.findViewById(R.id.Item_Set);

            holder.tel = (ImageButton) convertView.findViewById(R.id.ItemTel);

            holder.mes = (ImageButton) convertView.findViewById(R.id.ItemMes);

            holder.delete = (ImageButton) convertView.findViewById(R.id.ItemDelete);

            convertView.setTag(holder);

        } else {  
            holder = (ViewHolder) convertView.getTag();
        }


        final ViewHolder holder1 = holder;

        final int  p = position;
        po = p;
        mExplosionField = new ExplosionField(context);

        final View view1 = convertView;

        holder1.account.setText(getItem(position).getAccount());

        holder1.name.setText(getItem(position).getName());

        holder1.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("po", p + "");
                Person person = getItem(p);
                String info = person.toString();
                Log.e("info", info);
                Intent Intent = new Intent();
                Intent.setClass(context, Xunlian_PersonInfo.class);
                Intent.putExtra("type", "look");
                Intent.putExtra("info", info);
                context.startActivity(Intent);
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("po",p+"");
                Person person = getItem(p);
                String info = person.toString();
                Log.e("info", info);
                Intent Intent = new Intent();
                Intent.setClass(context, Xunlian_PersonInfo.class);
                Intent.putExtra("type", "look");
                Intent.putExtra("info", info);
                context.startActivity(Intent);
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuildConfig.DEBUG) Log.d("AppPersonListAdapter", "po:" + p);
                if (BuildConfig.DEBUG)
                    Log.d("AppPersonListAdapter", "po:" + getItem(p).getPhone1());

                showPopwindow(getItem(p).getAccount(), view1, p);
            }

        });

        return convertView;
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow(final String postaccount, final View delview, final int position) {
        // 利用layoutInflater获得View
        View view =  LayoutInflater.from(context).inflate(R.layout.popwindow, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        final PopupWindow finalWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        finalWindow.setFocusable(true);


        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xbffffff);
        finalWindow.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
       // window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        finalWindow.showAtLocation(Parentview.findViewById(R.id.APP_list),
                Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        Person person = getItem(position);
        List<String> strlist = new ArrayList<String>();
        String[] phone1= new String[]{person.getPhone1(),person.getPhone2(),person.getPhone3()};
        for (int i = 0;i<3;i++){

            if(!phone1[i].equals("")){
                strlist.add(phone1[i]);
            }

        }
        final String[] phone = new String[strlist.size()];
        for (int i = 0;i<strlist.size();i++){
            phone[i] = strlist.get(i);
        }

        Button mes = (Button) view.findViewById(R.id.mes);

        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalWindow.dismiss();
                if(phone.length==0){
                    noDialog();
                }else{
                    singleDialog(phone,1);
                }

            }
        });
        Button chat = (Button) view.findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2016/1/29 0029 将聊天加进去 
            }
        });
        
        Button tel = (Button) view.findViewById(R.id.tel);
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalWindow.dismiss();

                if(phone.length==0){
                    noDialog();
                }else{
                    singleDialog(phone,2);
                }

            }
        });
        TextView dismiss = (TextView) view.findViewById(R.id.id_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalWindow.dismiss();
            }
        });

        Button del = (Button) view.findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalWindow.dismiss();
                new AlertDialog.Builder(context)
                        .setTitle("删除讯连好友")
                        .setMessage(Html.fromHtml("  <br>确定要删除   " + "<big><font color=#000000> &quot;" + getItem(position).getName() + "&quot; </font></big>" + " 吗?"))
                        .setPositiveButton(Html.fromHtml("<big><font color=#FF4040>" + "狠心删除" + "</font></big>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String result = tools.Key2Json("13", "account", account, "friendaccount", postaccount);
                                        String string = tools.sendString2ServersSocket(result);
                                        try {
                                            strarry = tools.parseJSONMark12(string);

                                            if (strarry[0].equals("failure")) {
                                                onDeleteResult.back("删除失败");
                                            } else if (strarry[0].equals("success")) {
                                                PersonDao dao = new PersonDao(context, account);
                                                dao.delete(postaccount);
                                                list.remove(position);
                                                Message msg = Message.obtain();
                                                msg.what = 1;
                                                msg.obj = delview;
                                                handler.sendMessage(msg);
                                                onDeleteResult.back("删除成功");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            onDeleteResult.back("删除失败");
                                        }
                                    }
                                }).start();
                            }
                        })
                        .setNegativeButton("我点错了", null)
                        .show();

            }
        });

        //popWindow消失监听方法
        finalWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });

    }

    private void noDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("这个家伙没有手机信息");
        builder.setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void singleDialog(final String[] phone, final int type){
        singleSelectedId = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选择号码");
        builder.setSingleChoiceItems(phone, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                singleSelectedId = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                    if (type == 1) {
                        Uri uri = Uri.parse("smsto:" + phone[singleSelectedId]);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                        sendIntent.putExtra("sms_body", "");
                        context.startActivity(sendIntent);
                    } else {
                        Uri uri = Uri.parse("tel:" + phone[singleSelectedId]);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        builder.create().show();
    }

    final class ViewHolder {

        ImageView image;

        ImageView more;

        TextView account;

        TextView name;

        LinearLayout ll;

        ImageButton tel;

        ImageButton delete;

        ImageButton mes;

    }  

}