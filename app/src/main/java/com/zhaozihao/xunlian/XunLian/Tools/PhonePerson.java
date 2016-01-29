package com.zhaozihao.xunlian.XunLian.Tools;

/**
 * Created by 赵孜豪 on 2015/12/9.
 */
public class PhonePerson {
    private String name;

    private String phone;

    public PhonePerson(){
        super();
    }
    public PhonePerson(String name,String phone){
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
