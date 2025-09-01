package cn.smxy.zhouxuelian9.service;

import cn.smxy.zhouxuelian9.entity.CakeOrderInfo;

import java.util.List;

public interface CakeOrderInfoService {
    public List<CakeOrderInfo> findByUserId(String cakeuserId);
    public void addOrder(CakeOrderInfo cakeOrderInfo);
    public List<CakeOrderInfo> findAllOrders();
    public void updateOrder(CakeOrderInfo cakeOrderInfo);
    public void deleteOrder(String orderId);

    CakeOrderInfo selectOrderById(String cakeorderId);
}
