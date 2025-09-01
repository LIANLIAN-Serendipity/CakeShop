package cn.smxy.zhouxuelian9.controller;

import cn.smxy.zhouxuelian9.entity.CakeType;
import cn.smxy.zhouxuelian9.service.CakeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
public class CakeTypeController {
    @Autowired
    private CakeTypeService cakeTypeService;

    @RequestMapping("/cakeType/findAll")
    public Map<String, Object>findAll(){
        Map<String,Object> map = new HashMap<>();
        List<CakeType> cakeTypeList = cakeTypeService.findAll();
        if(cakeTypeList != null && cakeTypeList.size() > 0){
            map.put("code",2000);
            map.put("msg","查询所有蛋糕类型列表成功");
            map.put("dataobject",cakeTypeList);
        }else{
            map.put("code",4040);
            map.put("msg","没有查询到蛋糕类型列表");
            map.put("dataobject",null);
        }
        return map;
    }
}
