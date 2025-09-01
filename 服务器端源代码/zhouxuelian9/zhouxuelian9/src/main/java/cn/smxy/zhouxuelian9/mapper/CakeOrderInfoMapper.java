package cn.smxy.zhouxuelian9.mapper;

import cn.smxy.zhouxuelian9.entity.CakeOrderInfo;

import java.util.List;

public interface CakeOrderInfoMapper {
    public List<CakeOrderInfo>selectByUserId(String cakeuserId);
    public void insert(CakeOrderInfo cakeOrderInfo);
    public List<CakeOrderInfo> selectAllOrders();
    public void updateOrder(CakeOrderInfo cakeOrderInfo);
    public void deleteOrder(String orderId);

    CakeOrderInfo selectOrderById(String cakeorderId);
}
