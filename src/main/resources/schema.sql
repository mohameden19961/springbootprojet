-- Création de la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- 1. Tables de référence
CREATE TABLE IF NOT EXISTS language (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS nationality (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- 2. Tables Principales
CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS publisher (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS author (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    nationality_code VARCHAR(10),
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (nationality_code) REFERENCES nationality(code)
);

CREATE TABLE IF NOT EXISTS book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    language_code VARCHAR(10),
    category_id BIGINT,
    publisher_id BIGINT,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (language_code) REFERENCES language(code),
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (publisher_id) REFERENCES publisher(id)
);

CREATE TABLE IF NOT EXISTS book_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    barcode VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE,
    book_id BIGINT,
    FOREIGN KEY (book_id) REFERENCES book(id)
);

CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    member_type VARCHAR(20) NOT NULL,
    max_borrows INT NOT NULL DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- 3. Table de jointure (N-N)
CREATE TABLE IF NOT EXISTS book_author (
    book_id BIGINT,
    author_id BIGINT,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES book(id),
    FOREIGN KEY (author_id) REFERENCES author(id)
);

-- 4. Tables Transactionnelles
CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    book_id BIGINT,
    queue_position INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);

CREATE TABLE IF NOT EXISTS borrow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    book_item_id BIGINT,
    renewal_count INT DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (book_item_id) REFERENCES book_item(id)
);
