package com.zhaozihao.xunlian.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zhaozihao.xunlian.XunLian.Tools.Person;
import com.zhaozihao.xunlian.XunLian.Tools.Tools;
import com.zhaozihao.xunlian.XunLian.Tools.UpdateInfo;
import com.zhaozihao.xunlian.db.PersonSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhaozihao on 2015/11/21 0021.
 */
public class PersonDao {
    public Tools tools= null;
    String[] where = {"phone1","phone2","phone3","email1","email2","email3","qq","weibo"};

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
            Log.e("eeeeee", person.toString());
            db.execSQL("insert into friendInfo (name, phone1,phone2,phone3,email1,email2,email3,qq,weibo,account) values(?,?,?,?,?,?,?,?,?,?);", new Object[]{person.getName(), tools.encryption(person.getPhone1()), tools.encryption(person.getPhone2()), tools.encryption(person.getPhone3()), tools.encryption(person.getEmail1()), tools.encryption(person.getEmail2()), tools.encryption(person.getEmail3()), tools.encryption(person.getQq()), tools.encryption(person.getWeibo()), tools.encryption(person.getAccount())});

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
     * 根据id找到记录, 并且修改
     * @param
     * @param
     */
    public boolean update(List<UpdateInfo> list,String name) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if(db.isOpen()) {
            db.execSQL("update friendInfo set " + "name" + " = ? where account = ?;", new Object[]{name, tools.encryption(list.get(0).getAccount())});
        for (int i = 0;i<list.size();i++){
            UpdateInfo info = list.get(i);
            switch (info.getKey()){
                case 1:
                    db.execSQL("update friendInfo set " + where[0] + " = ? where account = ?;", new Object[]{tools.encryption(info.getValue()), tools.encryption(info.getAccount())});
                    Log.e("sql",info.getValue()+"-----"+info.getAccount());
                    break;
                case 2:
                    db.execSQL("update friendInfo set "+where[1]+" = ? where account = ?;", new Object[]{tools.encryption(info.getValue()), tools.encryption(info.getAccount())});
                    Log.e("sql", info.getValue() + "-----" + info.getAccount());
                    break;
                case 4:
                    db.execSQL("update friendInfo set "+where[2]+" = ? where account = ?;", new Object[]{tools.encryption(info.getValue()), tools.encryption(info.getAccount())});
                    Log.e("sql", info.getValue() + "-----" + info.getAccount());
                    break;
                case 8:
                    db.execSQL("update friendInfo set "+where[3]+" = ? where account = ?;", new Object[]{tools.encryption(info.getValue()), tools.encryption(info.getAccount())});
                    Log.e("sql", info.getValue() + "-----" + info.getAccount());
                    break;
                case 16:
                    db.execSQL("update friendInfo set "+where[4]+" = ? where account = ?;", new Object[]{tools.encryption(info.getValue()), tools.encryption(info.getAccount())});
                    Log.e("sql", info.getValue() + "-----" + info.getAccount());
                    break;
                case 32:
                    db.execSQL("update friendInfo set "+where[5]+" = ? where account = ?;", new Object[]{tools.encryption(info.getValue()), tools.encryption(info.getAccount())});
                    Log.e("sql", info.getValue() + "-----" + info.getAccount());
                    break;
                case 64:
                    db.execSQL("update friendInfo set "+where[6]+" = ? where account = ?;", new Object[]{tools.encryption(info.getValue()), tools.encryption(info.getAccount())});
                    Log.e("sql", info.getValue() + "-----" + info.getAccount());
                    break;
                case 128:
                    db.execSQL("update friendInfo set "+where[7]+" = ? where account = ?;", new Object[]{tools.encryption(info.getValue()), tools.encryption(info.getAccount())});
                    Log.e("sql", info.getValue() + "-----" + info.getAccount());
                    break;
            }
        }

            db.close();	// 数据库关闭
            Log.e("sql", "close");
            return true;
        }
        return  false;
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
                    for (int i = 0;i<info.length;i++){
                        if(info[i].equals("")){
                            switch (i){
                                case 0:

                                    break;
                                case 1:
                                    info[i] = "手机1";

                                    break;
                                case 2:
                                    info[i] = "手机2";

                                    break;
                                case 3:
                                    info[i] = "手机3";

                                    break;
                                case 4:
                                    info[i] = "邮箱1";

                                    break;
                                case 5:
                                    info[i] = "邮箱2";
                                    break;
                                case 6:
                                    info[i] = "邮箱3";

                                    break;
                                case 7:
                                    info[i] = "QQ";
                                    break;
                                case 8:
                                    info[i] = "微博";
                                    break;
                                case 9:
                                    break;
                                case 10:
                                    break;
                            }
                        }
                    }
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
        }else{
            return null;
        }
    }

    /**
     * 根据account查询人
     * @param account
     * @return
     */
    public Person queryItem(String account) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();	// 获得一个只读的数据库对象
        if(db.isOpen()) {
            Cursor cursor = db.rawQuery("select _id, name, phone1,phone2,phone3,email1,email2,email3,qq,weibo,account from friendInfo where account = ?;", new String[]{account});
            String[] info = new  String[10];
            if(cursor != null && cursor.moveToFirst()) {
                info[0] = cursor.getString(1);    // 取姓名
                info[1] = tools.decryption(cursor.getString(2));    // 取年龄
                info[2] = tools.decryption(cursor.getString(3));
                info[3] = tools.decryption(cursor.getString(4));
                info[4] = tools.decryption(cursor.getString(5));
                info[5] = tools.decryption(cursor.getString(6));
                info[6] = tools.decryption(cursor.getString(7));
                info[7] = tools.decryption(cursor.getString(8));
                info[8] = tools.decryption(cursor.getString(9));
                info[9] = tools.decryption(cursor.getString(10));
                for (int i = 0;i<info.length;i++){
                    if(info[i].equals("")){
                        switch (i){
                            case 0:

                                break;
                            case 1:
                                info[i] = "手机1";

                                break;
                            case 2:
                                info[i] = "手机2";

                                break;
                            case 3:
                                info[i] = "手机3";

                                break;
                            case 4:
                                info[i] = "邮箱1";

                                break;
                            case 5:
                                info[i] = "邮箱2";
                                break;
                            case 6:
                                info[i] = "邮箱3";

                                break;
                            case 7:
                                info[i] = "QQ";
                                break;
                            case 8:
                                info[i] = "微博";
                                break;
                            case 9:
                                break;
                            case 10:
                                break;
                        }
                    }
                }
                db.close();
                return new Person(info);
            }
            db.close();
        }
        return null;
    }
}

