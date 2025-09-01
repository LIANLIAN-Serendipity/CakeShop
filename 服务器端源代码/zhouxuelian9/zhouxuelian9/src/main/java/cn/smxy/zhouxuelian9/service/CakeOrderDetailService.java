package cn.smxy.zhouxuelian9.service;

import cn.smxy.zhouxuelian9.entity.CakeOrderDetail;
import java.util.List;

public interface CakeOrderDetailService {
    void addOrderDetail(CakeOrderDetail cakeOrderDetail);
    List<CakeOrderDetail> findAllOrderDetails();
    List<CakeOrderDetail> findByOrderId(String cakeorderId);
    void updateOrderDetail(CakeOrderDetail cakeOrderDetail);
    void deleteOrderDetail(int detailId);
}