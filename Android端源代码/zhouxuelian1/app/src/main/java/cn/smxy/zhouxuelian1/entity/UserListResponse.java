package cn.smxy.zhouxuelian1.entity;

import java.io.Serializable;
import java.util.List;


public class UserListResponse implements Serializable {

    private  int code;
    private String msg;
    private List<CakeUserInfo> dataobject;
    public UserListResponse() {
    }

    public UserListResponse(int code, String msg, List<CakeUserInfo> dataobject) {
        this.code = code;
        this.msg = msg;
        this.dataobject = dataobject;
    }

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

    public List<CakeUserInfo> getDataobject() {
        return dataobject;
    }

    public void setDataobject(List<CakeUserInfo> dataobject) {
        this.dataobject = dataobject;
    }

    @Override
    public String toString() {
        return "UserListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", dataobject=" + dataobject +
                '}';
    }
}