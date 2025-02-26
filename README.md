# 🛒 E-Commerce Platform (Пет-проект)

### 📌 Описание

Микросервисная платформа для интернет-магазина с корзиной, заказами и оплатой.

## ⚙️ Технологии:

- **Backend:** Java 21, Spring Boot 3.3.2
- **База данных:** PostgreSQL
- **Сообщения:** Kafka
- **Контейнеризация:** Docker

## 🛠️ Структура микросервисов:

| Микросервис         | Описание                          | Технологии                        |Разработчик                        |
|---------------------|-----------------------------------|-----------------------------------|-----------------------------------|
| **User Service**    | Регистрация, авторизация          | Spring Boot, JWT, PostgreSQL      | Георгий ([qwesha](https://github.com/qwesha))  |
| **Product Service** | Управление товарами               | Spring Boot, PostgreSQL           | Мария ([MariaDudinova](https://github.com/MariaDudinova))  |
| **Order Service**   | Корзина, заказы                   | Spring Boot, PostgreSQL           | Екатерина ([kateshap](https://github.com/kateshap))  |
| **Payment Service** | Оплата                            | Spring Boot, PostgreSQL, Kafka    | Дмитрий ([fuckstrout](https://github.com/fuckstrout))  |
| **Comment Service**  | Отзывы, комментарии               | Spring Boot, Kafka         | Игорь ([Cakypa217](https://github.com/Cakypa217))  |
---
