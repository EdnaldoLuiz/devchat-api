CREATE DATABASE IF NOT EXISTS websocket CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE websocket;

CREATE USER IF NOT EXISTS 'websocket_user'@'%' IDENTIFIED BY '06022003';
GRANT ALL PRIVILEGES ON websocket.* TO 'websocket_user'@'%';
FLUSH PRIVILEGES;
