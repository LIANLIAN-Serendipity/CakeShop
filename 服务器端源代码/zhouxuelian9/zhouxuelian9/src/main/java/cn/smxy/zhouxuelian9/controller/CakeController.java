package cn.smxy.zhouxuelian9.controller;

import cn.smxy.zhouxuelian9.entity.Cake;
import cn.smxy.zhouxuelian9.entity.CakeType;
import cn.smxy.zhouxuelian9.service.CakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class CakeController {
    @Autowired
    private CakeService cakeService;

    @RequestMapping("/cake/findAll")
    public Map<String, Object> FindAll(){
        Map<String, Object> map=new HashMap<>();
        List<Cake> productList=cakeService.findAll();
        if(productList!=null && productList.size()>0){
            map.put("code",2000);
            map.put("msg","查询所有产品列表成功");
            map.put("dataobject",productList);
        }else{
            map.put("code",4000);
            map.put("msg","没有查询所有产品列表");
            map.put("dataobject",null);
        }
        return map;
    }
    // 按类型ID查询蛋糕列表
    @RequestMapping("/cake/findByType")
    public Map<String, Object> findByType(@RequestParam("caketypeId") int caketypeId){
        Map<String, Object> map = new HashMap<>();
        List<Cake> cakeList = cakeService.findByType(caketypeId);
        if(cakeList != null && cakeList.size() > 0){
            map.put("code", 2000);
            map.put("msg", "按类型查询蛋糕列表成功");
            map.put("dataobject", cakeList);
        } else {
            map.put("code", 4040);
            map.put("msg", "按类型查询蛋糕列表失败");
            map.put("dataobject", null);
        }
        return map;
    }

    @PostMapping("/cake/addCake")
    public Map<String,Object> addCake(Cake cake){
        Map<String,Object> map = new HashMap<>();
        cakeService.addCake(cake);
        map.put("code",2000);
        map.put("msg","添加产品成功");
        map.put("dataobject",null);
        return map;
    }
    @GetMapping("/admin/cake/delById/{id}")
    public Map<String, Object> delById(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<>();
        cakeService.delById(id);
        map.put("code", 2000);
        map.put("msg", "删除用户成功");
        map.put("dataobject", null);
        return map;
    }
    @GetMapping("/admin/cake/findCakeById/{id}")
    public Map<String,Object> findProductById(@PathVariable Integer id){
        List<Cake> cakeList =cakeService.findByType(id);
        Map<String,Object> map=new HashMap<>();
        map.put("code",2000);
        map.put("msg", "根据id查询信息成功");
        map.put("dataobject", cakeList);
        return map;
    }
    @PostMapping("/admin/cake/updateCake")
    public Map<String,Object> updateCake(Cake cake){
        cakeService.update(cake);
        Map<String,Object> map=new HashMap<>();
        map.put("code",2000);
        map.put("msg", "修改蛋糕信息成功");
        map.put("dataobject", cake);
        return map;
    }
}
