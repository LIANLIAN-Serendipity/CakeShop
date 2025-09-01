package cn.smxy.zhouxuelian1.entity;

public class CakeUserInfo {
    private int cakeuserId;
    private String cakeusername;
    private String password;
    private String sex;
    private String address;
    private String phone;
    private String cakeuserImage;
    private String role;

    public CakeUserInfo() {
    }

    public CakeUserInfo(int cakeuserId, String cakeusername, String password,  String sex, String address, String phone, String cakeuserImage, String role) {
        this.cakeuserId = cakeuserId;
        this.cakeusername = cakeusername;
        this.password = password;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.cakeuserImage = cakeuserImage;
        this.role = role;
    }

    public int getCakeuserId() {
        return cakeuserId;
    }

    public void setCakeuserId(int cakeuserId) {
        this.cakeuserId = cakeuserId;
    }

    public String getCakeusername() {
        return cakeusername;
    }

    public void setCakeusername(String cakeusername) {
        this.cakeusername = cakeusername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCakeuserImage() {
        return cakeuserImage;
    }

    public void setCakeuserImage(String cakeuserImage) {
        this.cakeuserImage = cakeuserImage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // 获取角色文字
    public String getRoleText() {
        if ("1".equals(role)) {
            return "管理员";
        } else if ("2".equals(role)) {
            return "普通用户";
        } else {
            return "未知角色";
        }
    }

    @Override
    public String toString() {
        return "CakeUserInfo{" +
                "cakeuserId=" + cakeuserId +
                ", cakeusername='" + cakeusername + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", cakeuserImage='" + cakeuserImage + '\'' +
                ", role='" + role + "(" + getRoleText() + ")" + '\'' +
                '}';
    }
}