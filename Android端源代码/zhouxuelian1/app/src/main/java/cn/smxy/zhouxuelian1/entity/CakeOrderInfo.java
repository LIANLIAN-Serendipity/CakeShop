package cn.smxy.zhouxuelian1.entity;

import java.io.Serializable;
import java.util.List;

public class CakeOrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cakeorderId;
    private int cakeuserId;
    private String cakeorderTime;
    private float caketotalPrice;
    private int cakeorderStatus;
    private String deliveryAddress;
    private String customerName;
    private String customerPhone;
    private List<CakeOrderDetail> orderDetails;

    // 构造方法
    public CakeOrderInfo() {}

    // Getter和Setter方法
    public int getCakeorderId() {
        return cakeorderId;
    }

    public void setCakeorderId(int cakeorderId) {
        this.cakeorderId = cakeorderId;
    }

    public int getCakeuserId() {
        return cakeuserId;
    }

    public void setCakeuserId(int cakeuserId) {
        this.cakeuserId = cakeuserId;
    }

    public String getCakeorderTime() {
        return cakeorderTime;
    }

    public void setCakeorderTime(String cakeorderTime) {
        this.cakeorderTime = cakeorderTime;
    }

    public float getCaketotalPrice() {
        return caketotalPrice;
    }

    public void setCaketotalPrice(float caketotalPrice) {
        this.caketotalPrice = caketotalPrice;
    }

    public int getCakeorderStatus() {
        return cakeorderStatus;
    }

    public void setCakeorderStatus(int cakeorderStatus) {
        this.cakeorderStatus = cakeorderStatus;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<CakeOrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<CakeOrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "CakeOrderInfo{" +
                "cakeorderId=" + cakeorderId +
                ", cakeuserId=" + cakeuserId +
                ", cakeorderTime='" + cakeorderTime + '\'' +
                ", caketotalPrice=" + caketotalPrice +
                ", cakeorderStatus=" + cakeorderStatus +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", orderDetails=" + orderDetails +
                '}';
    }
}