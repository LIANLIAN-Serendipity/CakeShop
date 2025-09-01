package cn.smxy.zhouxuelian9.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CakeOrderDetail {
    private int cakedetailId;
    private int cakeorderId;
    private int cakeId;
    private int num;
    private float subtotal;
    private String remark;

    private Cake cake; // 蛋糕信息

}
