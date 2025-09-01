# 🎂 CakeShop - 蛋糕店电商应用

一个完整的蛋糕店电商应用，包含Android客户端和Spring Boot后端服务。

## ✨ 功能特性

### 🍰 用户端功能
- 用户注册/登录/密码重置
- 蛋糕浏览和搜索
- 购物车管理
- 订单下单和支付
- 个人中心管理
- 订单历史查看

### 🛠️ 管理端功能
- 蛋糕商品管理（增删改查）
- 用户管理
- 订单管理
- 数据统计

## 🛠️ 技术栈

### Android客户端
- **开发语言**: Java
- **最低SDK**: API 24 (Android 7.0)
- **目标SDK**: API 34 (Android 14)
- **主要依赖**:
  - OkHttp + Retrofit (网络请求)
  - Glide (图片加载)
  - Gson (JSON解析)
  - Material Design组件

### 后端服务
- **框架**: Spring Boot 2.7.15
- **数据库**: MySQL 8.0
- **ORM**: MyBatis
- **连接池**: Druid
- **认证**: JWT Token
- **分页**: PageHelper

## 📱 应用截图

> 请在此处添加应用的主要界面截图

## 🚀 快速开始

### 环境要求
- Android Studio Arctic Fox或更高版本
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 安装步骤

#### 1. 克隆项目
```bash
git clone https://github.com/your-username/CakeShop-Android.git
cd CakeShop-Android
```

#### 2. 数据库配置
1. 创建MySQL数据库
2. 导入`database/cakeshop.sql`脚本
3. 修改`springboot-server/src/main/resources/application.properties`中的数据库连接信息

#### 3. 启动后端服务
```bash
cd springboot-server
mvn spring-boot:run
```

#### 4. 运行Android应用
1. 用Android Studio打开`android-client`目录
2. 修改网络请求的服务器地址
3. 构建并运行应用

## 📁 项目结构

```
android-client/
├── app/src/main/java/cn/smxy/zhouxuelian1/
│   ├── activity/          # 活动页面
│   ├── adapter/           # 列表适配器
│   ├── entity/            # 数据实体类
│   ├── fragment/          # 碎片页面
│   ├── service/           # 后台服务
│   └── utils/             # 工具类

springboot-server/
├── src/main/java/cn/smxy/zhouxuelian9/
│   ├── controller/        # 控制器层
│   ├── service/           # 业务逻辑层
│   ├── mapper/            # 数据访问层
│   ├── entity/            # 实体类
│   └── config/            # 配置类
```

## 🔧 配置说明

### 数据库配置
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cakeshop?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 服务器地址配置
在Android项目中修改`UrlConstants.java`文件中的服务器地址。

## 📊 数据库设计

主要数据表：
- `cake`: 蛋糕商品信息
- `cakeuser`: 用户信息
- `cakeorderinfo`: 订单信息
- `cakeorderdetail`: 订单详情
- `caketype`: 蛋糕分类

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 📄 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

## 👨‍💻 作者

**周雪莲** - 项目开发者

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户！

---

⭐ 如果这个项目对您有帮助，请给我们一个Star！ 