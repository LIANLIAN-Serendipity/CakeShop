package cn.smxy.zhouxuelian9.service;

import cn.smxy.zhouxuelian9.entity.CakeType;
import cn.smxy.zhouxuelian9.mapper.CakeTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CakeTypeServiceImpl implements CakeTypeService {
    @Autowired
    private CakeTypeMapper cakeTypeMapper;
    @Override
    public List<CakeType> findAll(){
        return cakeTypeMapper.selectAll();
    }
}
