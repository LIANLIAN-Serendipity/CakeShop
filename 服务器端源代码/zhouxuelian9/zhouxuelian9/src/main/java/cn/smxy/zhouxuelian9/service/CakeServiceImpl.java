package cn.smxy.zhouxuelian9.service;

import cn.smxy.zhouxuelian9.entity.Cake;
import cn.smxy.zhouxuelian9.mapper.CakeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CakeServiceImpl implements CakeService{
    @Autowired
    private CakeMapper cakeMapper;
    @Override
    public List<Cake> findAll(){
        return cakeMapper.selectAll();
    }

    @Override
    public List<Cake> findCakeListByTypeId(Integer caketypeId){
        return cakeMapper.selectCakeListByTypeId(caketypeId);
    }

    @Override
    public void addCake(Cake cake){
        cakeMapper.insert(cake);
    }
    @Override
    public void delById(Integer id) {
        cakeMapper.delById(id);
    }
    @Override
    public List<Cake> findByType(int caketypeId) {
        return cakeMapper.findByType(caketypeId);
    }
    @Override
    public void update(Cake userInfo) {
        cakeMapper.update(userInfo);
    }
}
