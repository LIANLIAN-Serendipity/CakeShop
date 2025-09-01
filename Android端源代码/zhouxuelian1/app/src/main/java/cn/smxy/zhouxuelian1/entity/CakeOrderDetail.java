package cn.smxy.zhouxuelian1.entity;

import java.io.Serializable;

public class CakeOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cakedetailId;       // 订单详情ID（数据库自增主键）
    private int cakeorderId;        // 订单ID
    private int cakeId;             // 蛋糕ID
    private int num;                // 数量
    private float subtotal;         // 小计
    private String remark;          // 备注
    private Cake cake;              // 蛋糕信息

    // 构造方法
    public CakeOrderDetail() {}

    // 只提供getter方法，不提供setter，防止手动设置主键
    public int getCakedetailId() {
        return cakedetailId;
    }

    // 其他字段的getter和setter
    public int getCakeorderId() {
        return cakeorderId;
    }

    public void setCakeorderId(int cakeorderId) {
        this.cakeorderId = cakeorderId;
    }

    public int getCakeId() {
        return cakeId;
    }

    public void setCakeId(int cakeId) {
        this.cakeId = cakeId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Cake getCake() {
        return cake;
    }

    public void setCake(Cake cake) {
        this.cake = cake;
    }

    @Override
    public String toString() {
        return "CakeOrderDetail{" +
                "cakedetailId=" + cakedetailId +
                ", cakeorderId=" + cakeorderId +
                ", cakeId=" + cakeId +
                ", num=" + num +
                ", subtotal=" + subtotal +
                ", remark='" + remark + '\'' +
                ", cake=" + cake +
                '}';
    }
}