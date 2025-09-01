package cn.smxy.zhouxuelian9.service;

import cn.smxy.zhouxuelian9.entity.Cake;

import java.util.List;

public interface CakeService {
    public List<Cake> findAll();
    public List<Cake> findCakeListByTypeId(Integer caketypeId);
    public void addCake(Cake cake);
    public void delById(Integer id);
    public List<Cake> findByType(int caketypeId);
    public void update(Cake cake);
}
