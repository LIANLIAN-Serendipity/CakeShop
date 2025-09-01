package cn.smxy.zhouxuelian9.controller;

import cn.smxy.zhouxuelian9.entity.Cake;
import cn.smxy.zhouxuelian9.entity.CakeOrderDetail;
import cn.smxy.zhouxuelian9.service.CakeOrderDetailService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class CakeOrderDetailController {
    @Autowired
    private CakeOrderDetailService cakeOrderDetailService;

    @PostMapping("/orderDetail/add")
    public Map<String, Object> add(String cakeList, int cakeorderId) { // 将 cakeorderId 的类型改为 int
        Map<String, Object> map = new HashMap<>();
        Cake[] cakes = new Gson().fromJson(cakeList, Cake[].class);
        List<Cake> cakeListData = Arrays.asList(cakes);

        for (Cake cake : cakeListData) {
            CakeOrderDetail cakeOrderDetail = new CakeOrderDetail();
            cakeOrderDetail.setCakeorderId(cakeorderId); // 传递 int 类型的 cakeorderId
            cakeOrderDetail.setCakeId(cake.getCakeId());
            cakeOrderDetail.setNum(cake.getNum());
            cakeOrderDetailService.addOrderDetail(cakeOrderDetail);
        }

        map.put("code", 2000);
        map.put("msg", "添加订单详情成功");
        map.put("dataobject", null);

        return map;
    }

    @RequestMapping("/orderDetail/findAll")
    public Map<String, Object> findAllOrderDetails() {
        Map<String, Object> map = new HashMap<>();
        List<CakeOrderDetail> cakeOrderDetailList = cakeOrderDetailService.findAllOrderDetails();

        if (cakeOrderDetailList != null && !cakeOrderDetailList.isEmpty()) {
            map.put("code", 2000);
            map.put("msg", "查询所有订单详情成功");
            map.put("dataobject", cakeOrderDetailList);
        } else {
            map.put("code", 4040);
            map.put("msg", "没有查询到订单详情");
            map.put("dataobject", null);
        }
        return map;
    }

    @RequestMapping("/orderDetail/findByOrderId")
    public Map<String, Object> findByOrderId(@RequestParam int cakeorderId) { // 将 cakeorderId 的类型改为 int
        Map<String, Object> map = new HashMap<>();
        List<CakeOrderDetail> cakeOrderDetailList = cakeOrderDetailService.findByOrderId(String.valueOf(cakeorderId)); // 如果服务层需要 String 类型，进行转换

        if (cakeOrderDetailList != null && !cakeOrderDetailList.isEmpty()) {
            map.put("code", 2000);
            map.put("msg", "根据订单ID查询订单详情成功");
            map.put("dataobject", cakeOrderDetailList);
        } else {
            map.put("code", 4040);
            map.put("msg", "没有查询到该订单的详情");
            map.put("dataobject", null);
        }
        return map;
    }

    @PostMapping("/orderDetail/update")
    public Map<String, Object> updateOrderDetail(CakeOrderDetail cakeOrderDetail) {
        Map<String, Object> map = new HashMap<>();
        cakeOrderDetailService.updateOrderDetail(cakeOrderDetail);
        map.put("code", 2000);
        map.put("msg", "更新订单详情成功");
        map.put("dataobject", null);
        return map;
    }

    @GetMapping("/orderDetail/delete/{detailId}")
    public Map<String, Object> deleteOrderDetail(@PathVariable int detailId) {
        Map<String, Object> map = new HashMap<>();
        cakeOrderDetailService.deleteOrderDetail(detailId);
        map.put("code", 2000);
        map.put("msg", "删除订单详情成功");
        map.put("dataobject", null);
        return map;
    }
}