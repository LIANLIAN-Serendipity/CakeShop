package cn.smxy.zhouxuelian1.entity;

import java.util.List;

public class CakeListResponse {
    private  int code;
    private String msg;
    private List<Cake> dataobject;

    public CakeListResponse() {
    }

    public CakeListResponse(int code, String msg, List<Cake> dataobject) {
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

    public List<Cake> getDataobject() {
        return dataobject;
    }

    public void setDataobject(List<Cake> dataobject) {
        this.dataobject = dataobject;
    }

    @Override
    public String toString() {
        return "CakeListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", dataobject=" + dataobject +
                '}';
    }
}
