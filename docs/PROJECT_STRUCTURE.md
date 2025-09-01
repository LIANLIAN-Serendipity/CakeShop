# 📁 项目结构详细说明

## 整体架构

本项目采用前后端分离的架构设计：
- **Android客户端**: 负责用户界面和交互
- **Spring Boot后端**: 提供RESTful API服务
- **MySQL数据库**: 存储业务数据

## Android客户端结构

### 核心包结构
```
cn.smxy.zhouxuelian1/
├── activity/           # 活动页面
│   ├── MainActivity.java              # 主界面
│   ├── LoginActivity.java             # 登录页面
│   ├── RegisterActivity.java          # 注册页面
│   ├── CakeListActivity.java          # 蛋糕列表
│   ├── CakeDetailActivity.java        # 蛋糕详情
│   ├── AddCakeActivity.java           # 添加蛋糕(管理)
│   ├── ManagementActivity.java        # 管理主界面
│   ├── ManageUserActivity.java        # 用户管理
│   ├── ManagOrderActivity.java        # 订单管理
│   └── OrderDetailActivity.java       # 订单详情
├── fragment/           # 碎片页面
│   ├── CakeHomeFragment.java          # 首页碎片
│   ├── CakeListByTypeFragment.java    # 分类蛋糕列表
│   ├── CartFragment.java              # 购物车
│   ├── MultipleFragment.java          # 多功能页面
│   ├── PersonCenterFragment.java      # 个人中心
│   └── CakeUserOrderFragment.java     # 用户订单
├── adapter/            # 列表适配器
│   ├── CakeListAdapter.java           # 蛋糕列表适配器
│   ├── CakeListManagerAdapter.java    # 管理端蛋糕列表适配器
│   ├── CartListAdapter.java           # 购物车适配器
│   └── UserOrderAdapter.java          # 用户订单适配器
├── entity/             # 数据实体类
│   ├── Cake.java                      # 蛋糕实体
│   ├── CakeUser.java                  # 用户实体
│   ├── CakeOrder.java                 # 订单实体
│   └── ServerResponse.java            # 服务器响应实体
├── dao/                # 数据访问对象
│   ├── CakeCartDao.java               # 购物车数据访问
│   └── CakeCartDao1.java             # 购物车数据访问(备用)
├── service/            # 后台服务
│   └── MusicService.java              # 音乐播放服务
├── utils/              # 工具类
│   ├── MyDBHelper.java                # 数据库帮助类
│   ├── MyUtil.java                    # 通用工具类
│   ├── SPUtil.java                    # SharedPreferences工具类
│   └── UrlConstants.java              # URL常量
└── dialog/             # 对话框
    └── DeliveryInfoDialog.java        # 配送信息对话框
```

### 主要功能模块

#### 1. 用户认证模块
- 用户注册、登录、密码重置
- JWT Token验证
- 自动登录检查

#### 2. 商品展示模块
- 蛋糕分类浏览
- 商品搜索和筛选
- 商品详情展示

#### 3. 购物车模块
- 添加/删除商品
- 数量修改
- 价格计算

#### 4. 订单管理模块
- 订单创建
- 订单状态跟踪
- 订单历史查看

#### 5. 管理后台模块
- 商品管理(CRUD)
- 用户管理
- 订单管理

## Spring Boot后端结构

### 核心包结构
```
cn.smxy.zhouxuelian9/
├── config/             # 配置类
│   └── WebConfig.java                # Web配置
├── controller/         # 控制器层
│   ├── CakeController.java           # 蛋糕相关API
│   ├── CakeUserController.java       # 用户相关API
│   ├── CakeOrderInfoController.java  # 订单相关API
│   ├── CakeTypeController.java       # 蛋糕类型API
│   └── FileController.java           # 文件上传API
├── service/            # 业务逻辑层
│   ├── CakeService.java              # 蛋糕业务逻辑
│   ├── CakeUserService.java          # 用户业务逻辑
│   ├── CakeOrderInfoService.java     # 订单业务逻辑
│   └── CakeTypeService.java          # 蛋糕类型业务逻辑
├── mapper/             # 数据访问层
│   ├── CakeMapper.java               # 蛋糕数据访问
│   ├── CakeUserMapper.java           # 用户数据访问
│   ├── CakeOrderInfoMapper.java      # 订单数据访问
│   └── CakeTypeMapper.java           # 蛋糕类型数据访问
├── entity/             # 实体类
│   ├── Cake.java                     # 蛋糕实体
│   ├── CakeUserInfo.java             # 用户信息实体
│   ├── CakeOrderInfo.java            # 订单信息实体
│   └── CakeType.java                 # 蛋糕类型实体
├── interceptor/        # 拦截器
│   └── LoginInterceptor.java         # 登录拦截器
├── util/               # 工具类
│   └── JWTUtil.java                  # JWT工具类
└── Zhouxuelian9Application.java      # 主启动类
```

### 主要功能模块

#### 1. 用户管理模块
- 用户注册、登录
- JWT Token生成和验证
- 用户信息管理

#### 2. 商品管理模块
- 蛋糕信息的增删改查
- 蛋糕分类管理
- 图片上传处理

#### 3. 订单管理模块
- 订单创建和查询
- 订单状态管理
- 订单详情处理

#### 4. 文件管理模块
- 图片上传
- 文件存储

## 数据库设计

### 核心数据表

#### 1. cake (蛋糕表)
- `cakeId`: 主键ID
- `cakeName`: 蛋糕名称
- `introduce`: 蛋糕介绍
- `price`: 价格
- `cakePicture`: 图片URL
- `caketypeId`: 类型ID

#### 2. cakeuser (用户表)
- `cakeuserId`: 用户ID
- `username`: 用户名
- `password`: 密码
- `phone`: 手机号
- `address`: 地址

#### 3. cakeorderinfo (订单信息表)
- `cakeorderId`: 订单ID
- `cakeuserId`: 用户ID
- `cakeorderTime`: 下单时间
- `caketotalPrice`: 总价格
- `status`: 订单状态

#### 4. cakeorderdetail (订单详情表)
- `cakedetailId`: 详情ID
- `cakeorderId`: 订单ID
- `cakeId`: 蛋糕ID
- `num`: 数量
- `subtotal`: 小计
- `remark`: 备注

#### 5. caketype (蛋糕类型表)
- `caketypeId`: 类型ID
- `caketypeName`: 类型名称

## 技术特点

### Android端
- 采用Fragment + ViewPager架构
- 使用BottomNavigationView实现底部导航
- 网络请求使用OkHttp + Gson
- 图片加载使用Glide
- 本地存储使用SharedPreferences

### 后端
- RESTful API设计
- JWT Token认证
- MyBatis ORM框架
- Druid连接池
- 分页查询支持

## 部署说明

### 开发环境
- Android Studio Arctic Fox+
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 生产环境
- 建议使用Docker容器化部署
- 数据库建议使用云数据库服务
- 图片存储建议使用对象存储服务 