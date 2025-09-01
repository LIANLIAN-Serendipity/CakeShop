package cn.smxy.zhouxuelian1.entity;

import java.util.List;

public class CakeTypeListResponse {
    private int code;
    private String msg;
    private List<CakeType> dataobject;

    public CakeTypeListResponse(int code, String msg, List<CakeType> dataobject) {
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

    public List<CakeType> getDataobject() {
        return dataobject;
    }

    public void setDataobject(List<CakeType> dataobject) {
        this.dataobject = dataobject;
    }

    @Override
    public String toString() {
        return "CakeTypeListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", dataobject=" + dataobject +
                '}';
    }
}
