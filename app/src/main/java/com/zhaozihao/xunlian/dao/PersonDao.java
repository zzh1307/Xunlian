package com.zhaozihao.xunlian.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zhaozihao.xunlian.XunLian.Tools.Person;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.db.PersonSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhaozihao on 2015/11/21 0021.
 */
public class PersonDao {
    public Tools tools= null;

    private PersonSQLiteOpenHelper mOpenHelper;	// 数据库的帮助类对象

    public PersonDao(Context context, String name) {
        mOpenHelper = new PersonSQLiteOpenHelper(context,name);
        tools = new Tools(context);
    }

    /**
     * 添加到person表一条数据
     * @param person
     */
    public void insert(Person person) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if(db.isOpen()) {	// 如果数据库打开, 执行添加的操作

            // 执行添加到数据库的操作
            Log.e("eeeeee",person.toString());
            db.execSQL("insert into friendInfo (name, phone1,phone2,phone3,email1,email2,email3,qq,weibo,account) values(?,?,?,?,?,?,?,?,?,?);", new Object[]{person.getName(), tools.encryption(person.getPhone1()),tools.encryption(person.getPhone2()),tools.encryption(person.getPhone3()),tools.encryption(person.getEmail1()),tools.encryption(person.getEmail2()),tools.encryption(person.getEmail3()), tools.encryption(person.getQq()),tools.encryption(person.getWeibo()),tools.encryption(person.getAccount())});

            db.close();	// 数据库关闭
        }
    }

    /**
     * 根据phone删除记录
     * @param
     */
    public void delete(String account) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();	// 获得可写的数据库对象
        if(db.isOpen()) {	// 如果数据库打开, 执行添加的操作

            db.execSQL("delete from friendInfo where account = ?;", new String[]{tools.encryption(account)});
            Log.e("delete", account);
            db.close();	// 数据库关闭
        }
    }
    /**
     * 根据phone删除所有
     * @param
     */
    public void deleteAll(String phone) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();	// 获得可写的数据库对象
        if(db.isOpen()) {	// 如果数据库打开, 执行添加的操作

            db.execSQL("delete from friendInfo where 1 = 1;");

            db.close();	// 数据库关闭
        }
    }


    /**
     * 根据id找到记录, 并且修改姓名
     * @param
     * @param
     */
    public void update(String key, String value,String phone) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if(db.isOpen()) {	// 如果数据库打开, 执行添加的操作

            db.execSQL("update friendInfo set "+key+" = ? where phone1 = ?;", new Object[]{value,phone});

            db.close();	// 数据库关闭
        }
    }

    public List<Person> queryAll() {
        List<Person> personList = new ArrayList<Person>();
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();	// 获得一个只读的数据库对象
        if(db.isOpen()) {

            Cursor cursor = db.rawQuery("select  * from friendInfo order by name;", null);

            if(cursor != null && cursor.getCount() > 0) {

                String[] info = new  String[10];
                Person person;

                while(cursor.moveToNext()) {
                    info[0] = cursor.getString(1);	// 取姓名
                    info[1] = tools.decryption(cursor.getString(2));	// 取年龄
                    info[2] = tools.decryption(cursor.getString(3));
                    info[3] = tools.decryption(cursor.getString(4));
                    info[4] = tools.decryption(cursor.getString(5));
                    info[5] = tools.decryption(cursor.getString(6));
                    info[6] = tools.decryption(cursor.getString(7));
                    info[7] = tools.decryption(cursor.getString(8));
                    info[8] = tools.decryption(cursor.getString(9));
                    info[9] = tools.decryption(cursor.getString(10));
                    person = new Person(info);
                    personList.add(person);
                    Log.e("PersonDao",person.toString());
                }
                db.close();
                Log.e("PersonDao", "personList.isEmpty() = "+personList.isEmpty()+"\npersonList.size() = "+personList.size());
                return personList;
            }

            db.close();
            return personList;
        }
        return personList;
    }

    /**
     * 根据id查询人
     * @param phone
     * @return
     */
    public Person queryItem(String phone) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();	// 获得一个只读的数据库对象
        if(db.isOpen()) {
            Cursor cursor = db.rawQuery("select _id, name, phone1,phone2,phone3,email1,email2,email3,qq,weibo from friendInfo where phone1 = ?;", new String[]{phone});
            String[] info = new  String[10];
            if(cursor != null && cursor.moveToFirst()) {
                info[0] = cursor.getString(1);	// 取姓名
                info[1] = cursor.getString(2);	// 取年龄
                info[2] = cursor.getString(3);
                info[3] = cursor.getString(4);

                info[4] = cursor.getString(5);
                info[5] = cursor.getString(6);
                info[6] = cursor.getString(7);

                info[7] = cursor.getString(8);

                info[8] = cursor.getString(9);
                info[9] = tools.decryption(cursor.getString(10));

                db.close();
                return new Person(info);
            }
            db.close();
        }
        return null;
    }
}

