package com.zhaozihao.xunlian.XunLian.Frangment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Adapter.PhonePersonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Frangment_showPhone_PersonList extends Fragment {

    ListView phone_person_list = null;
	PhonePersonAdapter ma = null;
	List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
    	View view = inflater.inflate(R.layout.system_person_list, container, false);
    	phone_person_list = (ListView) view.findViewById(R.id.Phone_PersonList);
        Bundle bun = getArguments();
        data = (List<HashMap<String, Object>>) bun.getSerializable("person");
        ma = new PhonePersonAdapter(getActivity(), data,1);
        phone_person_list.setAdapter(ma);
        return   view;
    }
}

    

 