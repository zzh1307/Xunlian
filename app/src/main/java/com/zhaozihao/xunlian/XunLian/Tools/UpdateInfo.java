package com.zhaozihao.xunlian.XunLian.Tools;

/**
 * Created by 赵孜豪1 on 2016/2/13 0013.
 */
public class UpdateInfo {
    String account = null;
    String value = null;
    int key  = 0;
    String name = null;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UpdateInfo(String account,int key,String value){
        this.account = account;
        this.value = value;
        this.key = key;

    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "account='" + account + '\'' +
                ", value='" + value + '\'' +
                ", key=" + key +
                '}';
    }
}
