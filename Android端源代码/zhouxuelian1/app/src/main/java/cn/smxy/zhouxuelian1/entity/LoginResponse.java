package cn.smxy.zhouxuelian1.entity;

//loginresponse是一个Java实体类，用于表示登录接口的响应数据结构，他的作用是将从服务器端返回的jason数据映射为一个Java对象
public class LoginResponse {
    private Integer code; //响应状态码
    private String msg; //响应描述信息
    private String token; //认证令牌
    private Integer cakeuserId;
    private String cakeusername;
    private String cakeuserImage;

    public LoginResponse() {
    }

    public LoginResponse(Integer code, String msg, String token, Integer cakeuserId, String cakeusername, String cakeuserImage) {
        this.code = code;
        this.msg = msg;
        this.token = token;
        this.cakeuserId = cakeuserId;
        this.cakeusername = cakeusername;
        this.cakeuserImage = cakeuserImage;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCakeuserId() {
        return cakeuserId;
    }

    public void setCakeuserId(Integer cakeuserId) {
        this.cakeuserId = cakeuserId;
    }

    public String getCakeusername() {
        return cakeusername;
    }

    public void setCakeusername(String cakeusername) {
        this.cakeusername = cakeusername;
    }

    public String getCakeuserImage() {
        return cakeuserImage;
    }

    public void setCakeuserImage(String cakeuserImage) {
        this.cakeuserImage = cakeuserImage;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", token='" + token + '\'' +
                ", cakeuserId=" + cakeuserId +
                ", cakeusername='" + cakeusername + '\'' +
                ", cakeuserImage='" + cakeuserImage + '\'' +
                '}';
    }
}