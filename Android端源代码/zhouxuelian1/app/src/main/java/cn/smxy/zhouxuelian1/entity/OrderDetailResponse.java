package cn.smxy.zhouxuelian1.entity;

import java.util.List;

public class OrderDetailResponse {
    private int code;
    private String msg;
    private List<CakeOrderInfo> dataobject; // 改为List类型

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

    public List<CakeOrderInfo> getDataobject() {
        return dataobject;
    }

    public void setDataobject(List<CakeOrderInfo> dataobject) {
        this.dataobject = dataobject;
    }
}