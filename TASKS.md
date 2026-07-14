# PlayMate 任务清单

> 状态标记：⬜ 待办 | 🔄 进行中 | ✅ 已完成 | ❌ 已取消

---

## 一、基础设施

| # | 任务 | 状态 | 备注 |
|---|------|------|------|
| 1.1 | 初始化 Git 仓库 | ✅ | 根目录 .gitignore 已配置 |
| 1.2 | 创建开发日志 DEVLOG.md | ✅ | |
| 1.3 | 创建任务清单 TASKS.md | ✅ | 本文件 |
| 1.4 | 后端 pom.xml 依赖更新 | ✅ | SQLite JDBC / Security / JWT / Redis / springdoc |
| 1.5 | application.yaml 配置 | ✅ | SQLite / Redis / JWT / 文件上传 |
| 1.6 | schema.sql 移入 resources | ✅ | 后端启动时自动建表 |
| 1.7 | 创建 RULES.md 项目规则 | ✅ | 技术栈约束 + 编码规范 + Skill 使用 |

## 二、后端开发

### 2.1 Entity 层
| # | 任务 | 状态 | 对应表 |
|---|------|------|--------|
| 2.1.1 ~ 2.1.10 | 全部 10 个 Entity | ✅ | User/Game/Player/PlayerGame/Order/OrderLog/WalletTransaction/WithdrawRequest/Review/ChatMessage |

### 2.2 Repository 层
| # | 任务 | 状态 |
|---|------|------|
| 2.2.1 | 全部 10 个 Repository 接口 | ✅ |

### 2.3 Service 层
| # | 任务 | 状态 | 说明 |
|---|------|------|------|
| 2.3.1 ~ 2.3.7 | 全部 7 个 Service | ✅ | UserService/PlayerService/OrderService/WalletService/ReviewService/GameService/ChatService |
| 2.3.8 | AdminService | ⬜ | 后台管理逻辑已内联在 AdminController 中 |

### 2.4 Controller 层
| # | 任务 | 状态 | 路径前缀 |
|---|------|------|----------|
| 2.4.1 ~ 2.4.8 | 全部 8 个 Controller | ✅ | User/Player/Order/Wallet/Review/Game/Chat/Admin |

### 2.5 安全认证
| # | 任务 | 状态 |
|---|------|------|
| 2.5.1 ~ 2.5.5 | Security + JWT 全套 | ✅ | SecurityConfig / JwtUtil / JwtFilter / BCrypt / 角色权限 |

### 2.6 通用组件
| # | 任务 | 状态 |
|---|------|------|
| 2.6.1 | 统一响应体 Result<T> | ✅ |
| 2.6.2 | 全局异常处理器 | ✅ | BusinessException + GlobalExceptionHandler |
| 2.6.3 | 文件上传配置 | ⬜ | 配置已有，Controller 待补充 |
| 2.6.4 | API 文档配置 | ✅ | 已用 springdoc-openapi 替代 Knife4j |

## 三、前端开发

| # | 页面 | 状态 | 路径 |
|---|------|------|------|
| 3.1 ~ 3.17 | 全部 17 个页面 | ✅ | 页面已有内容（非空壳），需联调验证 |

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
1. 🔴 **P0** — ✅ 后端基础搭建（已完成）
2. 🔴 **P0** — ✅ 用户模块（已完成）
3. 🔴 **P0** — 🔄 后端编译验证 + 前后端联调
4. 🟡 **P1** — 前端页面联调 + 细节完善
5. 🟢 **P2** — 后端单元测试 + 接口测试
6. 🔵 **P3** — Nginx 部署
