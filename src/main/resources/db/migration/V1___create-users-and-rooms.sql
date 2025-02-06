-- Criação da tabela users
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(70) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    phone VARCHAR(20) UNIQUE NOT NULL,
    avatar VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CHECK (CHAR_LENGTH(name) >= 3),
    CHECK (phone REGEXP '^[0-9]+$')
) ENGINE=InnoDB;

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    CONSTRAINT fk_user_roles FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE (user_id, role) -- Impede duplicação de roles para o mesmo usuário
) ENGINE=InnoDB;

-- Criação da tabela rooms
CREATE TABLE rooms (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) DEFAULT NULL,
    icon VARCHAR(255) DEFAULT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CHECK (CHAR_LENGTH(name) >= 3)
) ENGINE=InnoDB;

-- Criação da tabela users_rooms
CREATE TABLE users_rooms (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    role ENUM('ADMIN', 'MEMBER') DEFAULT 'MEMBER',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES rooms (id) 
        ON DELETE CASCADE,
    UNIQUE (user_id, room_id) 
) ENGINE=InnoDB;