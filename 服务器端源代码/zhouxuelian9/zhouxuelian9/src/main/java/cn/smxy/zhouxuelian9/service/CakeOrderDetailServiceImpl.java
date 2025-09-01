package cn.smxy.zhouxuelian9.service;

import cn.smxy.zhouxuelian9.entity.CakeOrderDetail;
import cn.smxy.zhouxuelian9.mapper.CakeOrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CakeOrderDetailServiceImpl implements CakeOrderDetailService {
    @Autowired
    private CakeOrderDetailMapper cakeOrderDetailMapper;

    @Override
    public void addOrderDetail(CakeOrderDetail cakeOrderDetail) {
        cakeOrderDetailMapper.insert(cakeOrderDetail);
    }

    @Override
    public List<CakeOrderDetail> findAllOrderDetails() {
        return cakeOrderDetailMapper.selectAllOrderDetails();
    }

    @Override
    public List<CakeOrderDetail> findByOrderId(String cakeorderId) {
        return cakeOrderDetailMapper.selectByOrderId(cakeorderId);
    }

    @Override
    public void updateOrderDetail(CakeOrderDetail cakeOrderDetail) {
        cakeOrderDetailMapper.updateOrderDetail(cakeOrderDetail);
    }

    @Override
    public void deleteOrderDetail(int detailId) {
        cakeOrderDetailMapper.deleteOrderDetail(detailId);
    }
}