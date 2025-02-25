-- Создание таблицы Category
 CREATE TABLE categories (
 id SERIAL PRIMARY KEY,
 name VARCHAR(255) NOT NULL,
 deleted BOOLEAN NOT NULL DEFAULT FALSE
 );

-- Создание таблицы Product
CREATE TABLE products (
 id SERIAL PRIMARY KEY,
 name VARCHAR(50) NOT NULL,
 description VARCHAR(255),
 price NUMERIC(10, 2) NOT NULL,
 stock INT NOT NULL,
 category_id INT NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 deleted BOOLEAN NOT NULL DEFAULT FALSE,
 CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Вставка тестовых данных в таблицу Category
INSERT INTO categories (id, name, deleted) VALUES (1, 'Электроника', FALSE);
INSERT INTO categories (id, name, deleted) VALUES (2, 'Книги', FALSE);
INSERT INTO categories (id, name, deleted) VALUES (3, 'Одежда', FALSE);

-- Вставка тестовых данных в таблицу Product
INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at, deleted) VALUES
('Ноутбук', 'High-end laptop', 28999.9, 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
('iPhone 15', '256GB Белый', 76480, 15, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
('Мартин Иден', 'роман Джека Лондона', 19.99, 50, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
('Рубашка', 'Хлопок 100%', 3000, 100, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);
