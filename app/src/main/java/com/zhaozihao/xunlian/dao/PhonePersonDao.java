package com.zhaozihao.xunlian.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhaozihao.xunlian.XunLian.Tools.PhonePerson;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.db.PhonePersonSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhaozihao on 2015/11/21 0021.
 */
public class PhonePersonDao {
    public Tools tools= null;


    private PhonePersonSQLiteOpenHelper mOpenHelper;	// 数据库的帮助类对象

    public PhonePersonDao(Context context) {
        mOpenHelper = new PhonePersonSQLiteOpenHelper(context);
        tools = new Tools(context);
    }

    /**
     * 添加到person表一条数据
     * @param
     */
    public void insert(PhonePerson phonePersonn) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if(db.isOpen()) {	// 如果数据库打开, 执行添加的操作

            // 执行添加到数据库的操作
            db.execSQL("insert into MyPersonList (name, phone1) values(?,?);", new Object[]{phonePersonn.getName(), phonePersonn.getPhone()});

            db.close();	// 数据库关闭
        }
    }

    /**
     * 根据phone删除所有
     * @param
     */
    public void deleteAll() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();	// 获得可写的数据库对象
        if(db.isOpen()) {	// 如果数据库打开, 执行添加的操作

            db.execSQL("delete from MyPersonList where 1 = 1;");

            db.close();	// 数据库关闭
        }
    }


    public List<PhonePerson> queryAll() {
        List<PhonePerson> PhonepersonList = new ArrayList<PhonePerson>();
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();	// 获得一个只读的数据库对象
        if(db.isOpen()) {

            Cursor cursor = db.rawQuery("select  * from MyPersonList order by name;", null);

            if(cursor != null && cursor.getCount() > 0) {
                PhonePerson phonePerson;
                while(cursor.moveToNext()) {

                    phonePerson = new PhonePerson(cursor.getString(1),cursor.getString(2));
                    PhonepersonList.add(phonePerson);
                }
                db.close();
                return PhonepersonList;
            }

            db.close();
            return PhonepersonList;
        }
        return PhonepersonList;
    }
}

