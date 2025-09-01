package cn.smxy.zhouxuelian1.entity;

public class CommonResponse {
    private int code;
    private String msg;
    private Object dataobject;

    public CommonResponse() {
    }

    public CommonResponse(int code, String msg, Object dataobject) {
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

    public Object getDataobject() {
        return dataobject;
    }

    public void setDataobject(Object dataobject) {
        this.dataobject = dataobject;
    }
}