package com.syd.elderguard.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class FaceBmob extends BmobUser {
    String name = "";
    int sex = 0;
    BmobFile Photo = null;
    Long Birthday = null;
    String Relati0nshipID = "";
    String remark = "";

    public FaceBmob() {
        name = "";
        sex = 0;
        Photo = null;
        Birthday = null;
        Relati0nshipID = "";
        remark = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public BmobFile getPhoto() {
        return Photo;
    }

    public void setPhoto(BmobFile photo) {
        Photo = photo;
    }

    public Long getBirthday() {
        return Birthday;
    }

    public void setBirthday(Long birthday) {
        Birthday = birthday;
    }

    public String getRelati0nshipID() {
        return Relati0nshipID;
    }

    public void setRelati0nshipID(String relati0nshipID) {
        Relati0nshipID = relati0nshipID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
