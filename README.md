# Крокодил

## Описание игры
В игре "Крокодил" игроки делятся на два типа: рисующие и угадывающие. 
Рисующий игрок всегда один. В начале раунда ему предлагается выбрать из списка слово, которое он будет рисовать.
Угадывающие пишут в чат свои варианты того, что они предполагают, изображено на холсте. Ведущий может помечать ответы
положительно и отрицательно, сообщая таким образом игрокам верно их направление мысли или нет. 
В конце раунда, рисующим становится угадавший слово игрок.

![интерфейс рисующего слова](/doc/images/room_draw.png)

## Основные экраны:
- Регистрация
- Авторизация
- Выбор/Создание комнаты
- Комната
- Друзья

Описание логики экранов со скриншотами можно посмотреть здесь: [application_screens.md](doc/application_screens.md)

## Используемые технологии
- Docker compose
- PostgreSQL
- Бэкенд
  - Spring boot
  - Hibernate
  - Mapstruct
  - JWT
  - Webscoket
- Фронтенд
  - React
  - Effector
  - Ant design

# Развертывание
0. В случае, когда какие-то из портов заняты, их и другие параметры можно определить в файле `.\.env`
1. выполнить команду `.\mvnw install`
2. выполнить команду `docker compose up`

- React сервер будет доступен по адресу http://localhost:3000/
- Spring boot сервер будет доступен по адресу http://localhost:8080/
- БД Postgres будет доступен по адресу http://localhost:5433/

# TODO
- Комнаты: открытая, скрытая по ссылке, с паролем
- Таймер с обратным отсчетом