package com.zhaozihao.xunlian.XunLian.Tools;

/**
 * Created by zhaozihao on 2015/10/24.
 */
public class Person {
    private String name= "姓名";

    private String email1 = "邮箱1";
    private String email2= "邮箱2";
    private String email3= "邮箱3";

    private String phone1= "手机1";
    private String phone2= "手机2";
    private String phone3= "手机3";

    private String qq= "QQ";
    private String weibo= "微博";
    private String account;


    public Person(String[] info) {
        super();
        this.name     = info[0];

        this.phone1   = info[1];
        this.phone2   = info[2];
        this.phone3   = info[3];

        this.email1   = info[4];
        this.email2   = info[5];
        this.email3   = info[6];

        this.qq       = info[7];

        this.weibo    = info[8];
        this.account  =  info[9];

    }
    public Person() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getAccount() {
        return account;
    }
    public String getQq() {
        return qq;
    }

    @Override
    public String toString() {
        return this.name+"#"+this.phone1+"#"+this.phone2+"#"+this.phone3+"#"+this.email1+"#"+this.email2+"#"+this.email3+"#"+this.qq+"#"+this.weibo+"#"+this.account;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

}


