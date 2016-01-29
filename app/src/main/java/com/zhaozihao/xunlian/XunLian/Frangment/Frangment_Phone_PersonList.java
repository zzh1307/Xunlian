package com.zhaozihao.xunlian.XunLian.Frangment;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Adapter.PhonePersonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Frangment_Phone_PersonList extends Fragment {
	private static final String TAG ="MainActivity";
    Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
    Uri dataUri = Uri.parse("content://com.android.contacts/data");
    ListView phone_person_list = null;
	PhonePersonAdapter ma = null;
    HashMap<String, Object> map = null;
    HashMap<String, Object> map1 = null;
	List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(); 
    Cursor c =null;
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
    	View view = inflater.inflate(R.layout.system_person_list, container, false);
    	phone_person_list = (ListView) view.findViewById(R.id.Phone_PersonList);
    	queryContacts();
        return   view;
    }  
    public void queryContacts() {
        Cursor cursor = getActivity().getContentResolver().query(uri, new String[]{"_id"}, null,null, ContactsContract.Contacts.DISPLAY_NAME);
        if (cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String selection = "raw_contact_id = ?";
                String[] selectionArgs = {String.valueOf(id)};
                c = getActivity().getContentResolver().query(dataUri,new String[]{"data1","mimetype"}, selection,selectionArgs,null);
                if (c!=null&&c.getCount()>0){
                    map = new HashMap<String, Object>();
                    while (c.moveToNext()){
                            String mimetype = c.getString(1);
                            String data1 = c.getString(0);
                        if("vnd.android.cursor.item/name".equals(mimetype)){
                            Log.e("data1", data1);
                            map.put("Name", data1);
                            map1 = map;
                               	continue;
                               }else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                            	   if(!data.equals("")){
                                       map.put("Phone", data1);
                                       Log.e("data1--", data1);
                            		   data.add(map);
                            	   }else{
                                       continue;
                                   }
                               }
                    }
                }
                ma = new PhonePersonAdapter(getActivity(), data,1);
    	    	phone_person_list.setAdapter(ma);
			}
            }
        }
    }

    

 