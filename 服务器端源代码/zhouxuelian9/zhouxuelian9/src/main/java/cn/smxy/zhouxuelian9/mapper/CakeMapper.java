package cn.smxy.zhouxuelian9.mapper;

import cn.smxy.zhouxuelian9.entity.Cake;

import java.util.List;

public interface CakeMapper {
    public List<Cake> selectAll();
    public List<Cake>selectCakeListByTypeId(Integer caketypeId);
    public void insert(Cake cake);
    public void delById(Integer id);
    public List<Cake> findByType(int caketypeId);
    public void update(Cake cake);
}
