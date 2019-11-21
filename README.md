## 架构设计
```lua
flash-sale
│  ├─flash-sale-business -- 业务模块
│  │  ├─flash-sale-user -- 用户模块[7001]
│  │  ├─flash-sale-product -- 商品模块[7005]
│  │  ├─flash-sale-order -- 订单模块[7010]
│  ├─flash-sale-gateway -- 网关[8080]
│  ├─flash-sale-eureka -- 注册中心
```

SkyWalker[8006]

## 相关细节

### 服务
user    [7001 - 7003]
product [7011 - 7013]
order   [7021 - 7023]
gateway [8001 - 8003]
eureka  [8011 - 8013]

### 中间件
mysql   [9001 - 9003]
redis   [9011 - 9013][9014 - 9016]