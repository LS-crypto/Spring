# PlayMate 开发日志

## 项目概述
PlayMate 陪玩点单平台 — 实习项目  
技术栈：Vue 3 + Element Plus | Spring Boot 4.1 + Java 21 | SQLite | Redis | Nginx

---

## 2026-07-14 — Qoderwork 阶段（前期搭建）

### 完成内容
1. **前端项目骨架搭建**
   - Vite + Vue 3 项目初始化
   - 安装依赖：Element Plus、Pinia、Vue Router、Axios
   - 6 个 API 模块定义（user/player/order/wallet/review/admin）
   - 2 套布局组件（DefaultLayout 前台 + AdminLayout 后台）
   - 路由配置（15 个页面路由 + 路由守卫）
   - Axios 拦截器封装（Token 自动携带、错误处理）
   - Pinia 用户状态管理
   - 全局样式（CSS 变量、卡片样式、状态标签）

2. **数据库设计**
   - SQLite 建表脚本 `schema.sql`
   - 10 张表：users, games, players, player_games, orders, order_logs, wallet_transactions, withdraw_requests, reviews, chat_messages
   - 索引 + 初始数据（管理员账号 + 10 款游戏分类）

3. **项目规划文档**
   - 功能模块梳理（三种角色：用户/陪玩师/管理员）
   - 40+ RESTful API 接口设计
   - 7 周开发计划

### 遗留问题
- 前端页面组件（views*）全部为空壳，未实现
- 后端 Java 代码为零
- 未初始化 Git 仓库

---

## 2026-07-14 — 本会话开始（全权接手开发）

### 工作记录
- [x] 全面盘点项目现状
- [x] 初始化 Git 仓库
- [x] 创建开发日志 + 任务清单
- [ ] 后端环境搭建（pom.xml 依赖、application.yaml 配置）
- [ ] 后端 Entity / Repository / Service / Controller 开发
- [ ] 前端页面组件开发
- [ ] 联调测试

### 技术栈增强决策
- 新增：Spring Security + JWT（认证授权）
- 新增：Redis（缓存、Token 管理）
- 新增：Knife4j（API 文档）
- 部署：Nginx（反向代理 + 静态资源）
