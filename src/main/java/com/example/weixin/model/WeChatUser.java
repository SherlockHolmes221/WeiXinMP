package com.example.weixin.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WeChatUser {

    @Id
    @GeneratedValue
    private Integer id;


    private String  openid;

    public WeChatUser() {
    }


    public WeChatUser(String openId) {
        this.openid = openId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openid;
    }

    public void setOpenId(String openId) {
        this.openid = openId;
    }

    @Override
    public String toString() {
        return "WeChatUser{" +
                "id=" + id +
                ", openId='" + openid + '\'' +
                '}';
    }
}
