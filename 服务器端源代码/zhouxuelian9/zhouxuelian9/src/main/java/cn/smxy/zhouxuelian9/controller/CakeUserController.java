package cn.smxy.zhouxuelian9.controller;

import cn.smxy.zhouxuelian9.entity.Cake;
import cn.smxy.zhouxuelian9.entity.CakeUserInfo;
import cn.smxy.zhouxuelian9.service.CakeUserService;
import cn.smxy.zhouxuelian9.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
public class CakeUserController {
    @Autowired
    private CakeUserService cakeUserService;

    @RequestMapping("/user/checkToken")
    public Map<String, Object> checkToken() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 2000);
        map.put("msg", "校验令牌成功");
        return map;
    }

    @PostMapping("/user/login")
    public Map<String, Object> login(CakeUserInfo user) {
        log.info("login方法执行了");
        log.info("login方法接收到的用户名是:{},密码是:{},Role:{}", user.getCakeusername(), user.getPassword(), user.getRole());


        String role = user.getRole();
        Map<String, Object> map = new HashMap<>();

        CakeUserInfo u = cakeUserService.login(user);
        if (u != null) {
            // 登录成功
            Map<String, String> payload = new HashMap<>();
            payload.put("cakeuserId", u.getCakeuserId().toString());
            payload.put("cakeusername", u.getCakeusername());
            String token = JWTUtil.createToken(payload);
            if ("2".equals(role)) {
                map.put("code", 2002);
                map.put("msg", "普通用户登录成功");
                map.put("token", token);
                map.put("cakeuserId", u.getCakeuserId());
                map.put("cakeusername", u.getCakeusername());
                map.put("cakeuserImage", u.getCakeuserImage());
            } else {
                map.put("code", 2001);
                map.put("msg", "管理员登录成功");
                map.put("token", token);
                map.put("cakeuserId", u.getCakeuserId());
                map.put("cakeusername", u.getCakeusername());
                map.put("cakeuserImage", u.getCakeuserImage());
            }
            System.out.println("token:" + token);
            return map;
        } else {
            // 用户名或者密码错误
            map.put("code", 4040);
            map.put("msg", "用户名或者密码错误");
            map.put("token", null);
            map.put("userId", null);
            map.put("username", null);
            return map;
        }
    }

    @GetMapping("/user/getUserById/{id}")
    public Map<String, Object> getUserById(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<>();
        CakeUserInfo user = cakeUserService.getUserById(id);
        if (user != null) {
            map.put("code", 2000);
            map.put("msg", "查询成功");
            map.put("user", user);
        } else {
            map.put("code", 4040);
            map.put("msg", "用户不存在");
        }
        return map;
    }

    @PostMapping("/user/finduser")
    public Map<String, Object> finduser(CakeUserInfo cakeuser) {
        Map<String, Object> map = new HashMap<>();
        CakeUserInfo u = cakeUserService.finduser(cakeuser);
        if (u != null) {
            map.put("code", 2000);
            map.put("msg", "查询成功");
            map.put("user", u);
        } else {
            map.put("code", 4040);
            map.put("msg", "用户名或者密码错误");
        }
        return map;
    }

//    @PostMapping("/user/register")
//    public Map<String, Object> add(CakeUserInfo cakeuserInfo) {
//        Map<String, Object> map = new HashMap<>();
//        cakeUserService.add(cakeuserInfo);
//        map.put("code", 2000);
//        map.put("msg", "添加用户成功");
//        map.put("dataobject", null);
//        return map;
//    }
@PostMapping("/user/register")
public Map<String, Object> add(CakeUserInfo cakeuserInfo) {
    Map<String, Object> map = new HashMap<>();
    // 强制设置主键为null，避免客户端传递错误值
    cakeuserInfo.setCakeuserId(null);
    cakeUserService.add(cakeuserInfo);
    map.put("code", 2000);
    map.put("msg", "添加用户成功");
    map.put("dataobject", null);
    return map;
}

    @PostMapping("/user/update")
    public Map<String, Object> updatepwd(CakeUserInfo cakeuserInfo) {
        Map<String, Object> map = new HashMap<>();
        cakeUserService.update(cakeuserInfo);
        map.put("code", 2000);
        map.put("msg", "重置密码密码成功，请返回登录");
        map.put("dataobject", null);
        return map;
    }

    @RequestMapping("/user/findAll")
    public Map<String, Object> FindAll(){
        Map<String, Object> map=new HashMap<>();
        List<CakeUserInfo> userList=cakeUserService.findAll();
        if(userList!=null && userList.size()>0){
            map.put("code",2000);
            map.put("msg","查询所有用户列表成功");
            map.put("dataobject",userList);
        }else{
            map.put("code",4000);
            map.put("msg","没有查询所有用户列表");
            map.put("dataobject",null);
        }
        return map;
    }

    @GetMapping("/user/delete/{cakeuserId}")
    public Map<String, Object> deleteUser(@PathVariable Integer cakeuserId) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (cakeuserId == null || cakeuserId <= 0) {
                map.put("code", 4000);
                map.put("msg", "无效的用户ID");
                return map;
            }

            cakeUserService.deleteUser(cakeuserId);
            map.put("code", 2000);
            map.put("msg", "删除用户成功");
        } catch (Exception e) {
            map.put("code", 5000);
            map.put("msg", "删除用户失败: " + e.getMessage());
            log.error("删除用户失败", e);
        }
        map.put("dataobject", null);
        return map;
    }

    @PostMapping("/admin/user/updateUser")
    public Map<String,Object> updateUser(CakeUserInfo cakeUserInfo){
        cakeUserService.updateUser(cakeUserInfo);
        Map<String,Object> map=new HashMap<>();
        map.put("code",2000);
        map.put("msg", "修改用户信息成功");
        map.put("dataobject", cakeUserInfo);
        return map;
    }
}