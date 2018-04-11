package com.rabbit.rabbit.po;

import java.io.Serializable;

/***
 *  Created with IntelliJ IDEA.
 *  Author:  wenlin
 *  Date:  2018/4/10 15:02
 *  Description:
 **/
public class User implements Serializable{

    private String userName;
    private String userId;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
