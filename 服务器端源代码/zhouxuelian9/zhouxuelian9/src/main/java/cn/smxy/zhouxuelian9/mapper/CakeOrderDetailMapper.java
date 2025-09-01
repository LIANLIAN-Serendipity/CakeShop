package cn.smxy.zhouxuelian9.mapper;

import cn.smxy.zhouxuelian9.entity.CakeOrderDetail;

import java.util.List;

public interface CakeOrderDetailMapper {
    void insert(CakeOrderDetail cakeOrderDetail);
    List<CakeOrderDetail> selectAllOrderDetails();
    List<CakeOrderDetail> selectByOrderId(String cakeorderId);
    void updateOrderDetail(CakeOrderDetail cakeOrderDetail);
    void deleteOrderDetail(int detailId);
}