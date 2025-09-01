package cn.smxy.zhouxuelian1.utils;

public class UrlConstants {
    public static final String BASE_URL = "http://192.168.131.210:8089/zhouxuelian9";

    //    蛋糕部分
    public static final String CAKE_LIST_URL = BASE_URL + "/cake/findAll";
    public static final String CAKE_TYPE_LIST_URL = BASE_URL + "/cakeType/findAll";
    public static final String CAKE_LIST_BY_TYPE_URL = BASE_URL + "/cake/findByType";
    public static final String ADD_CAKE_URL = BASE_URL + "/cake/addCake";
    public static final String DELETE_CAKE_URL = BASE_URL + "/admin/cake/delById/";
    public static final String CAKE_UPDATE_URL = BASE_URL + "/admin/cake/updateCake";
    public static final String CAKE_FINDONE_URL = BASE_URL + "/admin/cake/findCakeById/";

    //    登录用户部分
    public static final String LOGIN_URL = BASE_URL + "/user/login";
    public static final String CHECK_TOKEN_URL = BASE_URL + "/user/checkToken";
    public static final String FINDUSER_URL = BASE_URL + "/user/finduser";
    public static final String REPASSWARD_URL = BASE_URL + "/user/update";
    public static final String REGISTER_URL = BASE_URL + "/user/register";


    // 管理用户 - 修正后的删除URL
    public static final String USER_LIST_URL = BASE_URL + "/user/findAll";
    public static final String DELETE_USER_URL = BASE_URL + "/user/delete/"; // 移除占位符
    public static final String USER_FIND_BY_ID_URL = BASE_URL + "/user/getUserById/";
    public static final String USER_UPDATE_URL = BASE_URL + "/admin/user/updateUser";


    //    订单部分
    public static final String USER_ORDER_LIST_URL = BASE_URL + "/user/order/findById";
    public static final String ORDER_DETAIL_URL = BASE_URL + "/orderDetail/findByOrderId";
    public static final String ADD_ORDER_DETAIL_URL = BASE_URL + "/orderDetail/add";
    public static final String ADD_ORDER_URL = BASE_URL + "/user/order/addOrder";
    public static final String UPDATE_ORDER_URL = BASE_URL + "/order/update";
    public static final String FIND_ORDER_URL = BASE_URL + "/order/all";
    public static final String DELETE_ORDER_URL = BASE_URL + "/order/delete/{orderId}";


    // 图片上传URL
    public static final String UPLOAD_IMAGE_URL = "http://192.168.131.210:8089/zhouxuelian9/uploadFile";
}