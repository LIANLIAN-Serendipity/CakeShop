package cn.smxy.zhouxuelian9.mapper;

import cn.smxy.zhouxuelian9.entity.Cake;
import cn.smxy.zhouxuelian9.entity.CakeUserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CakeUserMapper {
    public List<CakeUserInfo> selectAll();
    CakeUserInfo selectOne(CakeUserInfo cakeuserInfo);
    CakeUserInfo selectById(Integer id);
    CakeUserInfo finduser(CakeUserInfo cakeuser);
    void add(CakeUserInfo cakeuserInfo);
    void update(CakeUserInfo cakeuserInfo);
    public void deleteUser(Integer cakeuserId);
    public void updateUser(CakeUserInfo cakeUserInfo);
}