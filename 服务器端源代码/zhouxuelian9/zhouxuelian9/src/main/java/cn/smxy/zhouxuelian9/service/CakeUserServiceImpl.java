package cn.smxy.zhouxuelian9.service;

import cn.smxy.zhouxuelian9.entity.Cake;
import cn.smxy.zhouxuelian9.entity.CakeUserInfo;
import cn.smxy.zhouxuelian9.mapper.CakeUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CakeUserServiceImpl implements CakeUserService {
    @Autowired
    private CakeUserMapper cakeUserMapper;

    @Override
    public List<CakeUserInfo> findAll(){
        return cakeUserMapper.selectAll();
    }

    @Override
    public CakeUserInfo login(CakeUserInfo cakeuser) {
        return cakeUserMapper.selectOne(cakeuser);
    }

    @Override
    public CakeUserInfo getUserById(Integer id) {
        return cakeUserMapper.selectById(id);
    }

    @Override
    public CakeUserInfo finduser(CakeUserInfo cakeuser) {
        return cakeUserMapper.finduser(cakeuser);
    }

    @Override
    public void add(CakeUserInfo cakeuserInfo) {
        cakeUserMapper.add(cakeuserInfo);
    }

    @Override
    public void update(CakeUserInfo cakeuserInfo) {
        cakeUserMapper.update(cakeuserInfo);
    }

    @Override
    public void deleteUser(Integer cakeuserId) {
        cakeUserMapper.deleteUser(cakeuserId);
    }

    @Override
    public void updateUser(CakeUserInfo cakeUserInfo) {
        cakeUserMapper.updateUser(cakeUserInfo);
    }

}