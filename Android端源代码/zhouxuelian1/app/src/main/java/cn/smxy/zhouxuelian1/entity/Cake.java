package cn.smxy.zhouxuelian1.entity;

import java.io.Serializable;

public class Cake implements Serializable {
    private int cakeId;
    private String cakeName;
    private String introduce;
    private float price;
    private String cakePicture;
    private int caketypeId;
    private int num;

    public Cake() {
    }

    // 完整构造方法
    public Cake(int cakeId, String cakeName, String introduce, float price, String cakePicture, int caketypeId, int num) {
        this.cakeId = cakeId;
        this.cakeName = cakeName;
        this.introduce = introduce;
        this.price = price;
        this.cakePicture = cakePicture;
        this.caketypeId = caketypeId;
        this.num = num;
    }

    public int getCakeId() {
        return cakeId;
    }

    public void setCakeId(int cakeId) {
        this.cakeId = cakeId;
    }

    public String getCakeName() {
        return cakeName;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCakePicture() {
        return cakePicture;
    }

    public void setCakePicture(String cakePicture) {
        this.cakePicture = cakePicture;
    }

    public int getCaketypeId() {
        return caketypeId;
    }

    public void setCaketypeId(int caketypeId) {
        this.caketypeId = caketypeId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Cake{" +
                "cakeId=" + cakeId +
                ", cakeName='" + cakeName + '\'' +
                ", introduce='" + introduce + '\'' +
                ", price=" + price +
                ", cakePicture='" + cakePicture + '\'' +
                ", caketypeId=" + caketypeId +
                ", num=" + num +
                '}';
    }
}