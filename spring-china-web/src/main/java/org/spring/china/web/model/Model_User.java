package org.spring.china.web.model;

import java.io.Serializable;

/**
 * Created by jzlover on 2017/6/30.
 */
@SuppressWarnings("serial")
public class Model_User implements Serializable{
    private String userName;
    private String password;
    private String nickName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
