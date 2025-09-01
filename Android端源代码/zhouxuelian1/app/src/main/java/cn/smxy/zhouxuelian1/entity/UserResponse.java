package cn.smxy.zhouxuelian1.entity;

public class UserResponse {
    private int code;
    private String msg;
    private CakeUserInfo user;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CakeUserInfo getUser() {
        return user;
    }

    public void setUser(CakeUserInfo user) {
        this.user = user;
    }
}