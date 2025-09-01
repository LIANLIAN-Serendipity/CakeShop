package cn.smxy.zhouxuelian9.controller;

import cn.smxy.zhouxuelian9.entity.CakeOrderInfo;
import cn.smxy.zhouxuelian9.service.CakeOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class CakeOrderInfoController {
    @Autowired
    private CakeOrderInfoService cakeOrderInfoService;

    @RequestMapping("/user/order/findById")
    public Map<String, Object> findbyUserId(@RequestParam String cakeuserId) {
        Map<String, Object> map = new HashMap<>();
        List<CakeOrderInfo> cakeOrderInfoList = cakeOrderInfoService.findByUserId(cakeuserId);

        if(cakeOrderInfoList != null && !cakeOrderInfoList.isEmpty()) {
            map.put("code", 2000);
            map.put("msg", "根据用户id查询所有订单列表成功");
            map.put("dataobject", cakeOrderInfoList);
        } else {
            map.put("code", 4040);
            map.put("msg", "根据用户id没有查询到订单列表");
            map.put("dataobject", null);
        }
        return map;
    }

    @PostMapping("/user/order/addOrder")
    public Map<String,Object> addOrder(CakeOrderInfo cakeOrderInfo){
        Map<String,Object> map = new HashMap<>();

        cakeOrderInfoService.addOrder(cakeOrderInfo);

        map.put("code",2000);
        map.put("msg","添加订单成功");
        map.put("dataobject",null);

        return map;
    }

    @RequestMapping("/order/all")
    public Map<String, Object> findAllOrders() {
        Map<String, Object> map = new HashMap<>();
        List<CakeOrderInfo> cakeOrderInfoList = cakeOrderInfoService.findAllOrders();

        if(cakeOrderInfoList != null && !cakeOrderInfoList.isEmpty()) {
            map.put("code", 2000);
            map.put("msg", "查询所有订单列表成功");
            map.put("dataobject", cakeOrderInfoList);
        } else {
            map.put("code", 4040);
            map.put("msg", "没有查询到订单列表");
            map.put("dataobject", null);
        }
        return map;
    }

    @PostMapping("/order/update")
    public Map<String,Object> updateOrder(CakeOrderInfo cakeOrderInfo){
        Map<String,Object> map = new HashMap<>();

        cakeOrderInfoService.updateOrder(cakeOrderInfo);

        map.put("code",2000);
        map.put("msg","更新订单成功");
        map.put("dataobject",null);

        return map;
    }

    @GetMapping("/order/delete/{orderId}")
    public Map<String, Object> deleteOrder(@PathVariable String orderId) {
        Map<String, Object> map = new HashMap<>();
        cakeOrderInfoService.deleteOrder(orderId);
        map.put("code", 2000);
        map.put("msg", "删除订单成功");
        map.put("dataobject", null);
        return map;
    }

    // 三级级联查询
    @RequestMapping("/user/order/retrieveOrder")
    public Map<String, Object> retrieveOrder(@RequestParam String cakeorderId) {
        Map<String, Object> map = new HashMap<>();
        CakeOrderInfo cakeOrderInfo = cakeOrderInfoService.selectOrderById(cakeorderId);

        if (cakeOrderInfo != null) {
            map.put("code", 2000);
            map.put("msg", "查询订单及详情成功");
            map.put("dataobject", cakeOrderInfo);
        } else {
            map.put("code", 4040);
            map.put("msg", "没有查询到该订单");
            map.put("dataobject", null);
        }
        return map;
    }
}