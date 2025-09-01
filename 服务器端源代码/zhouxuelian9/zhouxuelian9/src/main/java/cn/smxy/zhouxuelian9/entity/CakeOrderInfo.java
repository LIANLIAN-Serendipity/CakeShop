package cn.smxy.zhouxuelian9.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CakeOrderInfo {
    private int cakeorderId;
    private int cakeuserId;
    private String cakeorderTime;
    private float caketotalPrice;
    private int cakeorderStatus;
    private  String deliveryAddress;
    private String customerName;
    private String customerPhone;

    private List<CakeOrderDetail> orderDetails;

}
