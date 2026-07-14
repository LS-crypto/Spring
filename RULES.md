# PlayMate 项目开发规则

> 本文件是所有 AI 助手（Qoder、Qoderwork 等）协作开发时必须遵守的规则。
> 每次新对话开始时，请 AI 先阅读本文件。

---

## 1. 项目概况

**PlayMate** — 陪玩点单平台（实习项目）

- 工作区：`d:\system\SPRING`
- 后端：`playmate-backend/`（Spring Boot 4.1.0 + Java 21）
- 前端：`playmate-frontend/`（Vue 3 + Vite 8 + Element Plus）
- 数据库：SQLite（`playmate-backend/src/main/resources/schema.sql`）
- 缓存：Redis（localhost:6379）
- 认证：Spring Security + JWT（jjwt 0.12.6）
- API 文档：springdoc-openapi（/swagger-ui.html）

---

## 2. 技术栈约束

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 4.1.0 | 基础框架 |
| Java | 21 | 语言版本 |
| Spring Data JPA | - | ORM 层 |
| SQLite JDBC | 3.47.1.0 | 数据库驱动 |
| hibernate-community-dialects | - | SQLite 方言 |
| Spring Security | - | 安全框架 |
| jjwt | 0.2.6 | JWT Token |
| Spring Data Redis | - | 缓存 |
| springdoc-openapi | 2.8.4 | API 文档 |
| Lombok | - | 简化代码 |

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3 | 框架 |
| Vite | 8 | 构建工具 |
| Element Plus | - | UI 组件库 |
| Pinia | - | 状态管理 |
| Vue Router | - | 路由 |
| Axios | - | HTTP 请求 |

### 禁止引入
- ❌ GraphQL（已放弃）
- ❌ Spring Modulith（已移除）
- ❌ MySQL / PostgreSQL（数据库锁定 SQLite）
- ❌ MyBatis（已选 JPA）

---

## 3. 架构规范

### 后端包结构
```
com.playmate
├── common/        # 通用类（Result, BusinessException, GlobalExceptionHandler）
├── controller/    # REST Controller
├── dto/           # 请求/响应 DTO
├── entity/        # JPA Entity
├── repository/    # Spring Data JPA Repository
├── security/      # JWT + Security 配置
└── service/       # 业务逻辑
```

### 后端编码规范
- 统一响应格式：`Result<T> { code, message, data }`
- 错误处理：抛 `BusinessException`，由 `GlobalExceptionHandler` 统一捕获
- JWT Token 放在 `Authorization: Bearer <token>` 头中
- 密码加密：BCrypt
- 三种角色：`user`（普通用户）/ `player`（陪玩师）/ `admin`（管理员）

### 前端目录结构
```
src/
├── api/           # API 接口定义（6 个模块）
├── assets/        # 静态资源 + 全局样式
├── components/    # 通用组件
├── layouts/       # 布局组件（DefaultLayout / AdminLayout）
├── router/        # 路由配置
├── stores/        # Pinia 状态管理
├── utils/         # 工具函数（request.js / auth.js）
└── views*/        # 页面组件（按模块分目录）
```

### 前端编码规范
- 使用 `<script setup>` 语法（Composition API）
- API 请求通过 `utils/request.js` 的 Axios 实例（自动携带 Token）
- 前端代理：`/api` → `http://localhost:8080`
- 开发端口：3000

---

## 4. 数据库规范

- 表名：复数形式，下划线分隔（如 `player_games`）
- 字段名：下划线分隔（如 `created_at`）
- 所有表都有 `id`（INTEGER PRIMARY KEY AUTOINCREMENT）
- 时间字段统一用 `created_at` / `updated_at`（DATETIME DEFAULT CURRENT_TIMESTAMP）
- 数据库结构变更必须同步更新 `schema.sql`
- **不要修改已有表结构，只能新增或追加**

---

## 5. Git 规范

- 远程仓库：`git@github.com:LS-crypto/Spring.git`（SSH 协议）
- 分支：`main`
- 提交信息格式：`<type>: <描述>`
  - `feat:` 新功能
  - `fix:` 修复
  - `refactor:` 重构
  - `docs:` 文档
  - `chore:` 杂项
- 每完成一个独立功能就提交，不要堆积大量改动
- 提交前确保代码能编译通过

---

## 6. Skill 使用要求

开发过程中**必须使用**以下两个 Skill 集合：

### superpowers（开发工作流）
路径：`C:\Users\LISS\.qoder-cn\skills\superpowers`

| Skill | 何时使用 |
|-------|----------|
| `writing-plans` | 多步骤任务开始前，先写实施计划 |
| `executing-plans` | 按计划执行时，设置检查点 |
| `brainstorming` | 任何创造性工作之前，探索需求和设计 |
| `test-driven-development` | 实现功能前先写测试 |
| `systematic-debugging` | 遇到任何 Bug，先找根因再修复 |
| `verification-before-completion` | 声称完成之前，必须运行验证命令 |
| `requesting-code-review` | 完成重要功能后，请求代码审查 |
| `receiving-code-review` | 收到审查反馈后，严谨处理 |
| `dispatching-parallel-agents` | 2+ 个独立任务时并行执行 |
| `finishing-a-development-branch` | 实现完成准备集成时 |

### taste-skill（前端设计）
路径：`C:\Users\LISS\.qoder-cn\skills\taste-skill`

| Skill | 何时使用 |
|-------|----------|
| `design-taste-frontend` | 创建/ redesign 前端页面时 |
| `high-end-visual-design` | 需要高端视觉质感时 |
| `minimalist-ui` | 简洁风格 UI 设计 |
| `redesign-existing-projects` | 升级现有页面设计质量 |
| `full-output-enforcement` | 需要完整代码输出，禁止省略 |

---

## 7. 开发工作流

### 每个功能的开发顺序
1. **brainstorming** — 明确需求和设计
2. **writing-plans** — 写实施计划
3. **test-driven-development** — 先写测试（如适用）
4. **实现代码** — 小步迭代，每步验证
5. **systematic-debugging** — 遇 Bug 先找根因
6. **verification-before-completion** — 验证通过再声称完成
7. **Git 提交** — 有意义的提交信息

### 多对话协作
- 每个独立功能/模块开新对话
- 对话结束前更新 `DEVLOG.md` 和 `TASKS.md`
- 新对话开始时先读 `RULES.md`（本文件）+ `DEVLOG.md` + `TASKS.md`
- 前端和后端可以分别开对话开发

### 文件整洁
- 测试文件放 `src/test/` 对应包下
- 不要在工作区根目录放临时文件
- 非代码文件（HTML 文档等）不进入 Git

---

## 8. API 接口规范

### 公开接口（无需登录）
- `POST /api/user/login` — 登录
- `POST /api/user/register` — 注册
- `GET /api/game/**` — 游戏列表
- `GET /api/player/list` — 陪玩师列表
- `GET /api/player/{id}` — 陪玩师详情

### 需认证接口
- `/api/user/**` — 用户相关
- `/api/order/**` — 订单相关
- `/api/wallet/**` — 钱包相关
- `/api/review/**` — 评价相关
- `/api/chat/**` — 聊天相关

### 管理员接口
- `/api/admin/**` — 需要 ROLE_ADMIN

---

## 9. 注意事项

- SQLite 不支持并发写入，避免多线程同时写操作
- Redis 如果未启动，应用应能降级运行（后续实现）
- JWT Secret 仅用于开发，生产环境需更换
- 文件上传路径：`uploads/`，URL 前缀：`/uploads/`
- 前端 `HelloWorld.vue` 是脚手架残留，可删除
