# PlayMate 任务清单

> 状态标记：⬜ 待办 | 🔄 进行中 | ✅ 已完成 | ❌ 已取消

---

## 一、基础设施

| # | 任务 | 状态 | 备注 |
|---|------|------|------|
| 1.1 | 初始化 Git 仓库 | ✅ | 根目录 .gitignore 已配置 |
| 1.2 | 创建开发日志 DEVLOG.md | ✅ | |
| 1.3 | 创建任务清单 TASKS.md | ✅ | 本文件 |
| 1.4 | 后端 pom.xml 依赖更新 | ⬜ | SQLite JDBC / Security / JWT / Redis / Knife4j |
| 1.5 | application.yaml 配置 | ⬜ | SQLite / Redis / JWT 密钥 |
| 1.6 | schema.sql 移入 resources | ⬜ | 后端启动时自动建表 |

## 二、后端开发

### 2.1 Entity 层
| # | 任务 | 状态 | 对应表 |
|---|------|------|--------|
| 2.1.1 | User.java | ⬜ | users |
| 2.1.2 | Game.java | ⬜ | games |
| 2.1.3 | Player.java | ⬜ | players |
| 2.1.4 | PlayerGame.java | ⬜ | player_games |
| 2.1.5 | Order.java | ⬜ | orders |
| 2.1.6 | OrderLog.java | ⬜ | order_logs |
| 2.1.7 | WalletTransaction.java | ⬜ | wallet_transactions |
| 2.1.8 | WithdrawRequest.java | ⬜ | withdraw_requests |
| 2.1.9 | Review.java | ⬜ | reviews |
| 2.1.10 | ChatMessage.java | ⬜ | chat_messages |

### 2.2 Repository 层
| # | 任务 | 状态 |
|---|------|------|
| 2.2.1 | 全部 Repository 接口 | ⬜ |

### 2.3 Service 层
| # | 任务 | 状态 | 说明 |
|---|------|------|------|
| 2.3.1 | UserService | ⬜ | 注册/登录/资料/头像 |
| 2.3.2 | PlayerService | ⬜ | 列表/详情/申请/定价 |
| 2.3.3 | OrderService | ⬜ | 下单/接单/完成/取消/退款 |
| 2.3.4 | WalletService | ⬜ | 余额/充值/提现/流水 |
| 2.3.5 | ReviewService | ⬜ | 评价创建/查询 |
| 2.3.6 | GameService | ⬜ | 游戏分类 CRUD |
| 2.3.7 | ChatService | ⬜ | 消息发送/历史记录 |
| 2.3.8 | AdminService | ⬜ | 后台管理聚合 |

### 2.4 Controller 层
| # | 任务 | 状态 | 路径前缀 |
|---|------|------|----------|
| 2.4.1 | UserController | ⬜ | /api/user/** |
| 2.4.2 | PlayerController | ⬜ | /api/player/** |
| 2.4.3 | OrderController | ⬜ | /api/order/** |
| 2.4.4 | WalletController | ⬜ | /api/wallet/** |
| 2.4.5 | ReviewController | ⬜ | /api/review/** |
| 2.4.6 | GameController | ⬜ | /api/game/** |
| 2.4.7 | ChatController | ⬜ | /api/chat/** |
| 2.4.8 | AdminController | ⬜ | /api/admin/** |

### 2.5 安全认证
| # | 任务 | 状态 |
|---|------|------|
| 2.5.1 | Spring Security 配置类 | ⬜ |
| 2.5.2 | JWT 工具类（生成/验证） | ⬜ |
| 2.5.3 | JWT 过滤器 | ⬜ |
| 2.5.4 | 密码加密（BCrypt） | ⬜ |
| 2.5.5 | 角色权限控制（user/player/admin） | ⬜ |

### 2.6 通用组件
| # | 任务 | 状态 |
|---|------|------|
| 2.6.1 | 统一响应体 Result<T> | ⬜ |
| 2.6.2 | 全局异常处理器 | ⬜ |
| 2.6.3 | 文件上传配置 | ⬜ |
| 2.6.4 | Knife4j 接口文档配置 | ⬜ |

## 三、前端开发

| # | 页面 | 状态 | 路径 |
|---|------|------|------|
| 3.1 | 首页 HomePage | ⬜ | / |
| 3.2 | 陪玩师列表 PlayerList | ⬜ | /players |
| 3.3 | 陪玩师详情 PlayerDetail | ⬜ | /players/:id |
| 3.4 | 登录页 LoginPage | ⬜ | /login |
| 3.5 | 注册页 RegisterPage | ⬜ | /register |
| 3.6 | 我的订单 MyOrders | ⬜ | /orders |
| 3.7 | 订单详情 OrderDetail | ⬜ | /orders/:id |
| 3.8 | 钱包 WalletPage | ⬜ | /wallet |
| 3.9 | 个人中心 ProfilePage | ⬜ | /profile |
| 3.10 | 申请陪玩师 ApplyPlayer | ⬜ | /apply-player |
| 3.11 | 聊天 ChatPage | ⬜ | /chat |
| 3.12 | 管理-数据概览 Dashboard | ⬜ | /admin |
| 3.13 | 管理-用户管理 UserManage | ⬜ | /admin/users |
| 3.14 | 管理-陪玩师审核 PlayerManage | ⬜ | /admin/players |
| 3.15 | 管理-订单管理 OrderManage | ⬜ | /admin/orders |
| 3.16 | 管理-游戏分类 GameManage | ⬜ | /admin/games |
| 3.17 | 管理-提现审核 WithdrawManage | ⬜ | /admin/withdraw |

## 四、测试

| # | 任务 | 状态 | 目录 |
|---|------|------|------|
| 4.1 | 后端单元测试 | ⬜ | playmate-backend/src/test/ |
| 4.2 | 接口测试 | ⬜ | playmate-backend/src/test/ |

## 五、部署

| # | 任务 | 状态 |
|---|------|------|
| 5.1 | Nginx 配置文件 | ⬜ |
| 5.2 | 前端打包部署到 Nginx | ⬜ |
| 5.3 | 后端打包运行 | ⬜ |

---

## 开发优先级
1. 🔴 **P0** — 后端基础搭建（依赖/配置/安全/通用组件）
2. 🔴 **P0** — 用户模块（注册/登录/认证）
3. 🟡 **P1** — 陪玩师模块 + 订单模块
4. 🟡 **P1** — 前端核心页面（首页/登录/陪玩师列表/订单）
5. 🟢 **P2** — 钱包/评价/聊天模块
6. 🟢 **P2** — 管理后台页面
7. 🔵 **P3** — 测试 + 部署
