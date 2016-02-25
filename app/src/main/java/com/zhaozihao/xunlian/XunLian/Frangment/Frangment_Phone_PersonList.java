package com.zhaozihao.xunlian.XunLian.Frangment;

import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.SORT_KEY_ALTERNATIVE);
        while(cursor.moveToNext()) {
            map = new HashMap<String, Object>();
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFieldColumnIndex);
            map.put("Name", contact);
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if(phone.moveToNext())
            {
                String Number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                map.put("Phone", Number);
                data.add(map);
            }
        }
        cursor.close();
                ma = new PhonePersonAdapter(getActivity(), data,1);
    	    	phone_person_list.setAdapter(ma);
			}


    }

    

 