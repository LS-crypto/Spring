-- ============================================
-- PlayMate 陪玩点单平台 - SQLite 建表脚本
-- ============================================

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nickname VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    avatar VARCHAR(500) DEFAULT '',
    gender VARCHAR(10) DEFAULT 'secret',
    bio TEXT DEFAULT '',
    role VARCHAR(20) DEFAULT 'user',       -- user / player / admin
    balance DECIMAL(10,2) DEFAULT 0.00,
    banned BOOLEAN DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 游戏分类表
CREATE TABLE IF NOT EXISTS games (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    icon VARCHAR(10) DEFAULT '',
    sort INTEGER DEFAULT 0,
    status BOOLEAN DEFAULT 1
);

-- 陪玩师表
CREATE TABLE IF NOT EXISTS players (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL UNIQUE REFERENCES users(id),
    real_name VARCHAR(50) NOT NULL,
    description TEXT DEFAULT '',
    base_price DECIMAL(10,2) DEFAULT 0.00,
    rating DECIMAL(2,1) DEFAULT 5.0,
    order_count INTEGER DEFAULT 0,
    like_count INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'pending',  -- pending / approved / rejected
    reject_reason VARCHAR(500) DEFAULT '',
    online BOOLEAN DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 陪玩师-游戏关联表
CREATE TABLE IF NOT EXISTS player_games (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_id INTEGER NOT NULL REFERENCES players(id),
    game_id INTEGER NOT NULL REFERENCES games(id),
    rank_level VARCHAR(50) DEFAULT '',
    price DECIMAL(10,2) DEFAULT 0.00,
    UNIQUE(player_id, game_id)
);

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE,
    user_id INTEGER NOT NULL REFERENCES users(id),
    player_id INTEGER NOT NULL REFERENCES players(id),
    game_id INTEGER NOT NULL REFERENCES games(id),
    quantity INTEGER DEFAULT 1,
    unit_price DECIMAL(10,2) DEFAULT 0.00,
    total_price DECIMAL(10,2) DEFAULT 0.00,
    remark TEXT DEFAULT '',
    status VARCHAR(20) DEFAULT 'pending',  -- pending / active / completing / completed / cancelled / refunded
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    accepted_at DATETIME,
    completed_at DATETIME,
    cancelled_at DATETIME
);

-- 订单日志表
CREATE TABLE IF NOT EXISTS order_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL REFERENCES orders(id),
    action VARCHAR(30) NOT NULL,           -- created / accepted / rejected / completed / cancelled / refunded
    content TEXT DEFAULT '',
    operator_id INTEGER,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 钱包流水表
CREATE TABLE IF NOT EXISTS wallet_transactions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL REFERENCES users(id),
    type VARCHAR(20) NOT NULL,             -- recharge / withdraw / order_pay / order_income / refund
    amount DECIMAL(10,2) NOT NULL,
    balance_after DECIMAL(10,2) DEFAULT 0.00,
    related_order_id INTEGER,
    remark VARCHAR(200) DEFAULT '',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 提现记录表
CREATE TABLE IF NOT EXISTS withdraw_requests (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL REFERENCES users(id),
    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(20) NOT NULL,           -- alipay / wechat / bank
    account VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',  -- pending / approved / rejected
    reject_reason VARCHAR(500) DEFAULT '',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    processed_at DATETIME
);

-- 评价表
CREATE TABLE IF NOT EXISTS reviews (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL UNIQUE REFERENCES orders(id),
    user_id INTEGER NOT NULL REFERENCES users(id),
    player_id INTEGER NOT NULL REFERENCES players(id),
    rating INTEGER NOT NULL CHECK(rating >= 1 AND rating <= 5),
    content TEXT DEFAULT '',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 聊天消息表
CREATE TABLE IF NOT EXISTS chat_messages (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    conversation_id VARCHAR(64) NOT NULL,
    sender_id INTEGER NOT NULL REFERENCES users(id),
    receiver_id INTEGER NOT NULL REFERENCES users(id),
    content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 索引
-- ============================================
CREATE INDEX idx_players_status ON players(status);
CREATE INDEX idx_players_user_id ON players(user_id);
CREATE INDEX idx_player_games_player ON player_games(player_id);
CREATE INDEX idx_player_games_game ON player_games(game_id);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_player ON orders(player_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_logs_order ON order_logs(order_id);
CREATE INDEX idx_wallet_tx_user ON wallet_transactions(user_id);
CREATE INDEX idx_withdraw_user ON withdraw_requests(user_id);
CREATE INDEX idx_withdraw_status ON withdraw_requests(status);
CREATE INDEX idx_reviews_player ON reviews(player_id);
CREATE INDEX idx_chat_conv ON chat_messages(conversation_id);
CREATE INDEX idx_chat_created ON chat_messages(created_at);

-- ============================================
-- 初始数据
-- ============================================

-- 默认管理员（密码: admin123，实际需用 BCrypt 加密）
INSERT INTO users (nickname, phone, password, role, balance)
VALUES ('管理员', '13800000000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Zx', 'admin', 0);

-- 默认游戏分类
INSERT INTO games (name, icon, sort) VALUES
('英雄联盟', '⚔️', 1),
('王者荣耀', '👑', 2),
('和平精英', '🔫', 3),
('永劫无间', '🗡️', 4),
('CS2', '💣', 5),
('DOTA2', '🛡️', 6),
('原神', '✨', 7),
('第五人格', '🎭', 8),
('金铲铲之战', '♟️', 9),
('崩坏：星穹铁道', '🚂', 10);
