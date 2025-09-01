package cn.smxy.zhouxuelian1.entity;

import java.util.List;

public class CakeOrderListResponse {
    private int code;
    private String msg;
    private List<CakeOrderInfo> dataobject;

    public CakeOrderListResponse() {
    }

    public CakeOrderListResponse(int code, String msg, List<CakeOrderInfo> dataobject) {
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

    public List<CakeOrderInfo> getDataobject() {
        return dataobject;
    }

    public void setDataobject(List<CakeOrderInfo> dataobject) {
        this.dataobject = dataobject;
    }

    @Override
    public String toString() {
        return "CakeOrderListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", dataobject=" + dataobject +
                '}';
    }
}