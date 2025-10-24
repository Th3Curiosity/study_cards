# StudyCards — backend для создания колод карточек

[![Springdoc OpenAPI](https://img.shields.io/badge/Springdoc-OpenAPI%203.0-blue)](http://localhost:8080/swagger-ui.html) ![Java](https://img.shields.io/badge/Java-21-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)

**Краткое описание**

CardDecks — это Java/Spring Boot backend-приложение для создания и управления колодами карточек для обучения. Проект включает в себя Spring Web, Spring Data JPA, Spring Security и OpenAPI/Swagger. Данные хранятся в PostgreSQL. Для удобного развёртывания в репозитории есть `docker-compose.yml`.

**Проект ещё в процессе разработки!**

Быстро запустить приложение можно выполнив эту команду:

`git clone https://github.com/Th3Curiosity/study_cards`

а затем эту команду в корневой папке проекта:

`docker compose up --build`

В DOCKERFILE реализована multi-stage сборка

## Особенности

* REST API с OpenAPI/Swagger документацией
* Авторизация и аутентификация через Spring Security
* Работает с PostgreSQL, готово к запуску через Docker Compose
* Создание, обновление и удаление колод (Decks)
* Создание, обновление и удаление карточек (Cards) в пределах колоды
* У каждой карточки — `front` и `back` (возможность хранить вопрос и ответ)

P.S Некоторые фичи, касающиеся колод и карточек - в процессе разоработки. Идентификация реализована пока условно по логину, в будущем возможно добавлю почту.

## Технологический стек

* Java 21
* Spring Boot

  * Spring Web
  * Spring Data JPA
  * Spring Security
  * Springdoc OpenAPI / Swagger
* PostgreSQL
* Docker & Docker Compose
* (в будущем) Redis


## Структура API

OpenAPI JSON: [`http://localhost:8080/v3/api-docs`](http://localhost:8080/v3/api-docs)

Swagger UI: [`http://localhost:8080/swagger-ui/index.html`](`http://localhost:8080/swagger-ui/index.html`)

## Конфигурация (переменные окружения)

---

### Важно!
Необходимо поменять и вынести `jwt.secret` из `application.properties` например в переменные окружения

---

Переменные окружения, которые использует `docker-compose` для контейнеров java приложения Postgres храняться в docker-compose.yml

## Локальная разработка

Если вы хотите запустить приложение локально без Docker:

1. Настройте PostgreSQL (локально или удалённо)
2. Укажите в переменных окружения, где работает приложение `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.

---

## Spring Security

В проекте реализована JWT аутентификация. Доступ без неё разрешён только к эндпоинтам `/api/auth/**`
