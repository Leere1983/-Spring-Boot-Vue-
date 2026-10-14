# 基于 Spring Boot + Vue 的在线拍卖平台

一个前后端分离的在线拍卖系统。后端提供 REST 接口，前端分为**用户前台**与**管理后台**两套界面，支持商品发布与审核、在线竞拍出价、订单流转、支付宝沙箱支付、实时私聊、AI 问答等完整业务闭环。

---

## 一、技术栈

### 后端

| 分类 | 技术 | 版本 |
| --- | --- | --- |
| 语言 / 构建 | Java、Maven | JDK 1.8、Maven 3.6+ |
| 核心框架 | Spring Boot | 2.3.12.RELEASE |
| 持久层 | MyBatis-Plus | 3.4.3.1 |
| 数据库 | MySQL | 8.0.x |
| 连接池 | HikariCP | 随 Spring Boot |
| 数据库工具 | db-query-spring-boot-starter（jntoo） | 1.2.18 |
| 身份认证 | JJWT（JWT） | 0.10.7 |
| 接口文档 | Springfox Swagger2 / Swagger UI | 2.9.2 |
| 实时通信 | Spring WebSocket | 随 Spring Boot |
| 验证码 | Kaptcha | 2.3.2 |
| 支付 | 支付宝沙箱 SDK | 4.38.157.ALL |
| 工具库 | Hutool、Fastjson、Guava、Commons-IO | 5.7.5 / 1.2.78 / 20.0 / 2.6 |

### 前端

| 分类 | 技术 | 版本 |
| --- | --- | --- |
| 框架 | Vue | 3.3.x |
| 构建工具 | Vite | 4.4.9 |
| UI 组件库 | Element Plus | 2.8.x |
| 状态管理 | Pinia | 2.1.x |
| 路由 | Vue Router | 4.2.x |
| HTTP 请求 | Axios | 1.4.x |
| 图表 | ECharts | 4.9.x |
| 富文本 | TinyMCE | 6.7.x |
| 其他 | Swiper、mitt、vuedraggable、vue-baidu-map-3x | — |

---

## 二、目录结构

```
bishe/
├── database/
│   └── bishe.sql                # 数据库脚本（建库建表 + 初始数据）
├── java/                        # 后端 Maven 工程
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/spboot/app/
│       │   ├── StartApplication.java    # 启动类
│       │   ├── config/                  # 拦截器、Swagger、支付宝、验证码、MyBatis-Plus 配置
│       │   ├── controller/              # 接口层（21 个 Controller）
│       │   ├── service/                 # 业务层（含定时任务）
│       │   ├── mapper/                  # MyBatis-Plus Mapper
│       │   ├── pojo/                    # 实体类
│       │   ├── encoder/                 # 密码编码器（MD5 / 明文）
│       │   ├── utils/                   # JWT、分页、统一响应、会话工具
│       │   └── websocket/               # WebSocket 聊天支持
│       └── resources/
│           └── application.properties   # 后端配置文件
└── vue/                         # 前端 Vite 工程
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── config.js            # 后端地址与接口路径配置
        ├── router/              # 路由（前台 web-routes / 后台 admin-routes）
        ├── module/              # 各业务模块的接口封装
        ├── views/               # 页面
        ├── components/          # 公共组件
        ├── stores/              # Pinia 状态
        └── utils/               # Ajax 封装、工具函数
```

---

## 三、环境准备

| 软件 | 要求 |
| --- | --- |
| JDK | 1.8（配置中 `java.version=1.8`） |
| Maven | 3.6 及以上 |
| Node.js | 14.0 及以上 |
| MySQL | 8.0.x |
| 开发工具 | IntelliJ IDEA（后端）、VS Code（前端） |

Maven 仓库已配置阿里云镜像（`https://maven.aliyun.com/repository/public`），无需额外设置即可加速依赖下载。

---

## 四、部署运行

### 1. 初始化数据库

创建名为 `bishe` 的数据库，并导入 `database/bishe.sql`：

```sql
CREATE DATABASE bishe DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

```bash
mysql -u root -p bishe < database/bishe.sql
```

脚本会自动建表并写入初始数据（含示例商品、商家、用户）。

### 2. 修改后端配置

编辑 `java/src/main/resources/application.properties`，按本机环境调整**数据库连接**：

```properties
spring.datasource.url=jdbc:mysql://localhost:3309/bishe?useUnicode=true&characterEncoding=UTF-8&useSSL=FALSE&serverTimezone=UTC&useOldAliasMetadataBehavior=true
spring.datasource.username=root
spring.datasource.password=root
```

> ⚠️ **注意**：默认端口写的是 **3309**，不是常见的 3306。若本机 MySQL 跑在 3306，请改成 3306 或对应端口。

同一文件中还需要核对以下配置：

- **支付宝沙箱**：`alipay.app-id`、`alipay.merchant-private-key`、`alipay.alipay-public-key`、`alipay.gateway-url`、`alipay.notify-url`、`alipay.return-url`
- **AI 问答**：`ai.enabled`、`ai.api-base`、`ai.api-key`、`ai.model`（默认 `qwen-turbo`，走阿里云百炼兼容模式）

> ⚠️ **安全提醒**：配置文件中的支付宝**商户私钥**和 AI **API Key** 是真实凭据，请勿公开提交到代码仓库。答辩演示或代码分发前建议替换为自己的密钥，或改用环境变量注入。

### 3. 启动后端

用 IDEA 以 Maven 项目打开 `java` 目录，等待依赖下载完成后，运行主启动类：

```
com.spboot.app.StartApplication
```

- 服务端口：**8080**（`server.port=8080`）
- 上下文路径：`/`
- Swagger 文档：`http://localhost:8080/swagger-ui.html`
- 文件上传大小已设为不限制（`spring.servlet.multipart.maxFileSize=-1`）

### 4. 启动前端

```bash
cd vue
npm install
npm run dev
```

前端开发服务器端口为 **5174**（见 `vite.config.js`），启动后访问终端提示的地址即可。

前端请求的后端地址在 `vue/src/config.js` 中配置：

```js
let service_url = "http://localhost:8080";
```

若后端部署在其他主机或端口，修改此处即可。

### 5. 生产环境打包

```bash
# 后端
cd java
mvn clean package
# 生成 target/spboot-1.0-SNAPSHOT.jar，可通过 java -jar 运行

# 前端
cd vue
npm run build
# 生成 dist 目录，部署到 Nginx 等 Web 服务器
```

---

## 五、系统功能

### 用户前台

首页、商品列表与详情、商品分类浏览、搜索、**在线竞拍出价**、竞拍记录查看、收藏商品、留言反馈、公告浏览、个人中心、余额充值、提现申请、订单查看、确认收货、订单评价、与商家私聊、AI 智能问答。

### 管理后台

管理员登录、工作台首页、修改密码，以及以下业务模块的**增删改查与审核**：

商品信息、商品分类、竞拍记录、竞拍订单、订单发货、确认收货、订单评价、商家管理、用户管理、收藏管理、私聊管理、消息管理、公告管理、轮播图管理、留言反馈管理。

### 账号角色

系统有三类身份：**管理员**（`admins`）、**用户/竞拍人**（`yonghu`）、**商家/拍卖行**（`shangjia`）。

初始数据中的默认账号（密码与账号相同）：

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin` |
| 用户 | `001` | `001` |
| 用户 | `963` | `963` |
| 商家 | `100` | `100` |
| 商家 | `tianming200` | `123456` |

> 后台入口为 `/admin`，系统登录页为 `/login`。

---

## 六、数据库设计

共 19 张表：

| 表名 | 说明 |
| --- | --- |
| `admins` | 管理员 |
| `yonghu` | 用户（竞拍人/买家） |
| `shangjia` | 商家（拍卖行/卖家） |
| `shangpinfenlei` | 商品分类 |
| `shangpinxinxi` | **商品信息（核心表）** |
| `jingpaijilu` | 竞拍记录（出价明细） |
| `jingpaidingdan` | 竞拍订单 |
| `dingdanfahuo` | 订单发货 |
| `querenshouhuo` | 确认收货 |
| `dingdanpingjia` | 订单评价 |
| `shoucang` | 收藏 |
| `siliao` | 私聊会话 |
| `xiaoxi` | 聊天消息 |
| `gonggao` | 公告 |
| `lunbotu` | 首页轮播图 |
| `liuyanban` | 留言反馈 |
| `zaixianchongzhi` | 在线充值记录 |
| `yonghutixian` | 用户提现申请 |
| `shangjiatixian` | 商家提现申请 |

### 核心表 `shangpinxinxi` 关键字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `shangpinbianhao` | varchar(50) | 商品编号 |
| `fenlei` | int | 分类 ID |
| `shangpinmingcheng` | varchar(255) | 商品名称 |
| `shangpintupian` | text | 商品图片 |
| `qipaijia` | decimal(10,2) | 起拍价 |
| `jiajiafudu` | decimal(10,2) | 加价幅度 |
| `dangqianjingpaijia` | decimal(10,2) | 当前竞拍价 |
| `qipaishijian` | datetime | 起拍时间 |
| `jieshushijian` | datetime | 结束时间 |
| `jingpairenshu` | int | 竞拍人数 |
| `shangpinzhuangtai` | varchar(50) | 商品状态 |
| `jingpairen` / `faburen` | varchar(50) | 竞拍人 / 发布人 |
| `issh` | varchar(10) | 是否审核（是/否） |
| `is_deleted` | tinyint(1) | 逻辑删除标记 |

已对 `fenlei`、`qipaishijian`、`jieshushijian`、`shangpinzhuangtai`、`issh` 建立索引。

---

## 七、竞拍核心机制

### 商品状态流转

商品状态包含：`未开始` → `竞拍中` → `已成交` / `已流拍`。

状态由定时任务 `ShangpinStatusScheduler` 自动维护，**每 10 秒**执行一次（`@Scheduled(fixedDelay = 10000)`）：

1. 已审核（`issh=是`）且状态为空的商品 → 置为 `未开始`；
2. 起拍时间 ≤ 当前时间 ≤ 结束时间、且状态为 `未开始` → 置为 `竞拍中`；
3. 结束时间 < 当前时间且状态未终结的商品：
   - 竞拍人数为 0 → `已流拍`
   - 竞拍人数大于 0 → `已成交`

### 出价规则

竞拍出价以起拍价 `qipaijia` 为底价，每次加价不得低于加价幅度 `jiajiafudu`，出价写入 `jingpaijilu`，同时更新商品表的 `dangqianjingpaijia` 与 `jingpairenshu`。

---

## 八、接口约定

### 统一 CRUD 模式

除业务接口外，每个模块的 `Controller` 都提供一套标准接口，以 `/api/{模块名}` 为前缀：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET/POST | `/selectAll` | 查询全部 |
| GET/POST | `/selectPages` | 分页查询 |
| GET/POST | `/findById` | 按 ID 查询单条 |
| POST | `/insert` | 新增 |
| POST | `/update` | 修改 |
| POST | `/delete` | 删除 |

部分模块有额外的数据隔离接口，用于区分当前登录身份能看到的数据，例如：

- `/selectFaburen` —— 查询当前用户发布的数据
- `/selectJingpairen` —— 查询当前用户参与竞拍的数据
- `/selectUsername`、`/selectFankuiyonghu`、`/selectFaxinren` —— 按用户名/反馈人/发信人过滤

### 主要模块前缀

`/api/admins`、`/api/yonghu`、`/api/shangjia`、`/api/shangpinfenlei`、`/api/shangpinxinxi`、`/api/jingpaijilu`、`/api/jingpaidingdan`、`/api/dingdanfahuo`、`/api/querenshouhuo`、`/api/dingdanpingjia`、`/api/shoucang`、`/api/siliao`、`/api/xiaoxi`、`/api/gonggao`、`/api/lunbotu`、`/api/liuyanban`

### 功能接口

| 路径 | 说明 |
| --- | --- |
| `/api/user/login` | 登录 |
| `/api/user/tokenLogin` | 通过 token 获取用户信息 |
| `/api/user/logout` | 退出登录 |
| `/api/user/query`、`/api/user/select` | 通用查询 |
| `/api/user/editPassword` | 修改密码 |
| `/api/user/captch` | 图形验证码 |
| `/api/upload/save` | 文件上传 |
| `/api/payment` | 支付入口 |
| `/api/alipay/*` | 支付宝沙箱支付（含 notify / return 回调） |
| `/api/ai/*` | AI 问答 |
| `/api/chat/check`、`/checkmine`、`/updateRead`、`/chatMessage` | 聊天相关 |

### 认证方式

- 登录成功后下发 **JWT**，前端后续请求通过请求头 `token` 携带；
- 后端由 `TokenInterceptor` 统一拦截，解析 token 并写入当前会话上下文；
- 拦截器同时处理 **CORS 跨域**（允许 `token`、`Authorization` 等头，支持携带 Cookie，预检请求直接返回）；
- 密码支持 MD5 与明文两种编码方式（`encoder` 包下 `Md5PasswordEncoder` / `ClearPasswordEncoder`）。

---

## 九、常见问题

**1. 数据库连接失败**
检查 `application.properties` 中的端口（默认 **3309**）、库名 `bishe`、用户名密码，以及是否已导入 `bishe.sql`。

**2. 前端页面数据加载不出来**
确认后端已在 8080 端口启动，并检查 `vue/src/config.js` 中的 `service_url` 是否指向正确地址。

**3. 图片无法显示**
上传文件保存在后端本地 `upload` 目录，数据库中以 `/upload/xxx.png` 形式存储。请确认上传目录存在且后端静态资源映射正常。

**4. 支付宝支付报错**
本项目使用**支付宝沙箱环境**，需确保 `alipay.gateway-url` 为沙箱网关、密钥与沙箱应用匹配，且下单账号使用的是沙箱买家账号。回调地址 `notify-url` / `return-url` 指向 `localhost:8080`，如需外网回调请改用内网穿透地址。

**5. 竞拍状态没有自动变化**
状态由定时任务驱动，间隔 10 秒。请确认启动类上的 `@EnableScheduling` 生效，并检查商品的 `issh` 是否为 `是`（未审核的商品不参与状态流转）。

---

## 十、说明

本文档依据项目源码、`pom.xml`、`application.properties`、数据库脚本及前端配置整理而成，用于辅助部署与二次开发。

**部署要点:**

 一、软件和版本要求
需要安装的软件有：JDK 11 及以上版本、Maven 3.6 及以上、Node.js 14.0 及以上、MySQL 8.0.x。开发工具推荐使用 IntelliJ IDEA 或 VS Code。

二、后端部署步骤
首先，创建名为bishe的数据库，导入database文件夹中的bishe.sql脚本文件。然后，修改后端配置文件application.properties中的数据库用户名、密码和连接地址。接着，使用IDEA打开java目录作为 Maven 项目，等待依赖下载完成后，运行StartApplication主启动类即可启动后端，后端默认运行在8080端口。

三、前端部署步骤
首先，在 vue 目录下打开终端，执行 npm install 安装前端依赖。然后，检查前端 API 配置，确保后端接口地址指向 8080 端口。接着，执行 npm run dev 启动前端开发服务器，访问显示的地址即可打开系统。如果需要部署到生产环境，执行 npm run build 打包，将 dist 目录内容部署到 Nginx 等 Web 服务器即可。

<img width="2148" height="1242" alt="ScreenShot_2026-10-14_155907_547" src="https://github.com/user-attachments/assets/814888a6-6e69-4a3f-8580-44c8f789db94" />
