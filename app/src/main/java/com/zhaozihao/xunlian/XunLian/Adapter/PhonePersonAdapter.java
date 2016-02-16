package com.zhaozihao.xunlian.XunLian.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhaozihao.xunlian.R;

import java.util.HashMap;
import java.util.List;

public class PhonePersonAdapter extends BaseAdapter {
	LayoutInflater mInflater;  
	List<HashMap<String, Object>> list;  
    Context context;
    int colcr;
    String colors[] = {"#FFEBCD","#FF7256","#BCEE68","#87CEEB","#4EEE94","#8A8A8A"};

    public PhonePersonAdapter(Context context, List<HashMap<String, Object>> list, int colors) {
        super();  
        this.mInflater = LayoutInflater.from(context);  
        this.list = list; 
        this.colcr = colors;
        this.context = context;
  
    }  
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
		
	}

	@Override
	public HashMap<String, Object> getItem(int position) {  
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

                    R.layout.phone_person_list_item, null);

            holder.Itemtext = (TextView) convertView.findViewById(R.id.ItemText);

            holder.phone = (TextView) convertView.findViewById(R.id.ItemPhone);  

            holder.name = (TextView) convertView.findViewById(R.id.ItemName);  

            holder.tel = (ImageButton) convertView.findViewById(R.id.ItemTel);

            holder.mes = (ImageButton) convertView.findViewById(R.id.ItemMes);

            convertView.setTag(holder);  

        } else {  

            holder = (ViewHolder) convertView.getTag();  

        }  


        final int  p = position;
        holder.Itemtext.setText((String) getItem(position).get("Name").toString().trim().substring(0,1).toUpperCase());
        holder.Itemtext.setBackgroundColor(Color.parseColor(colors[p % 6]));
        holder.phone.setText((String) getItem(position).get("Phone"));

        holder.name.setText((String) getItem(position).get("Name"));

        holder.tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:"+getItem(p).get("Phone"));
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        holder.mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + getItem(p).get("Phone"));
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                sendIntent.putExtra("sms_body", "");
                context.startActivity(sendIntent);
            }
        });


        return convertView;  

    }  

  



    final class ViewHolder {

        TextView Itemtext;

        TextView phone;  

        TextView name;

        ImageButton tel;

        ImageButton mes;

    }  

}