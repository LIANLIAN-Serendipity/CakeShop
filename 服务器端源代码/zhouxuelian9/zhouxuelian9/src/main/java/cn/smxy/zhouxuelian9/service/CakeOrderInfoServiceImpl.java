package cn.smxy.zhouxuelian9.service;

import cn.smxy.zhouxuelian9.entity.CakeOrderInfo;
import cn.smxy.zhouxuelian9.mapper.CakeOrderInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CakeOrderInfoServiceImpl implements CakeOrderInfoService {
    @Autowired
    private CakeOrderInfoMapper CakeOrderInfoMapper;
    @Override
    public List<CakeOrderInfo> findByUserId(String cakeuserId){
        return CakeOrderInfoMapper.selectByUserId(cakeuserId);
    }

    @Override
    public void addOrder(CakeOrderInfo cakeOrderInfo){
        CakeOrderInfoMapper.insert(cakeOrderInfo);
    }

    @Override
    public List<CakeOrderInfo> findAllOrders() {
        return CakeOrderInfoMapper.selectAllOrders();
    }

    @Override
    public void updateOrder(CakeOrderInfo cakeOrderInfo) {
        CakeOrderInfoMapper.updateOrder(cakeOrderInfo);
    }

    @Override
    public void deleteOrder(String orderId) {
        CakeOrderInfoMapper.deleteOrder(orderId);
    }

    @Override
    public CakeOrderInfo selectOrderById(String cakeorderId) {
        return CakeOrderInfoMapper.selectOrderById(cakeorderId);
    }

}
