package cn.smxy.zhouxuelian9.service;

import cn.smxy.zhouxuelian9.entity.Cake;
import cn.smxy.zhouxuelian9.entity.CakeUserInfo;

import java.util.List;

public interface CakeUserService {
    public List<CakeUserInfo> findAll();
    CakeUserInfo login(CakeUserInfo cakeuser);
    CakeUserInfo getUserById(Integer id);
    CakeUserInfo finduser(CakeUserInfo cakeuser);
    void add(CakeUserInfo cakeuserInfo);
    void update(CakeUserInfo cakeuserInfo);
    public void deleteUser(Integer cakeuserId);
    public void updateUser(CakeUserInfo cakeUserInfo);
}