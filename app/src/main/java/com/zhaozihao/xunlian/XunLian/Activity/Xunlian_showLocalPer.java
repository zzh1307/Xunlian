package com.zhaozihao.xunlian.XunLian.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.zhaozihao.xunlian.R;
import com.zhaozihao.xunlian.XunLian.Frangment.Frangment_showPhone_PersonList;

import java.util.ArrayList;
import java.util.HashMap;

public class Xunlian_showLocalPer extends Activity {
    Frangment_showPhone_PersonList system = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_xunlian_show_local_per);
        ArrayList<HashMap<String, Object>> listObj =  (ArrayList<HashMap<String, Object>>)getIntent().getSerializableExtra("person");
        Log.e("fff", listObj.size() + "----");
        Bundle bundle = new Bundle();
        bundle.putSerializable("person", listObj);
        system = new Frangment_showPhone_PersonList();
        system.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction1 = fm.beginTransaction();
        transaction1.replace(R.id.showLocalPer_content, system);
        transaction1.commit();
    }

}
