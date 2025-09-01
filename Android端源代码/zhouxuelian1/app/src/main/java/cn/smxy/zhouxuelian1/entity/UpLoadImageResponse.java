package cn.smxy.zhouxuelian1.entity;

public class UpLoadImageResponse {
    private int code;
    private String msg;
    private String dataobject;

    public UpLoadImageResponse() {
    }

    public UpLoadImageResponse(int code, String msg, String dataobject) {
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

    public String getDataobject() {
        return dataobject;
    }

    public void setDataobject(String dataobject) {
        this.dataobject = dataobject;
    }

    @Override
    public String toString() {
        return "UpLoadImageResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", dataobject='" + dataobject + '\'' +
                '}';
    }
}

