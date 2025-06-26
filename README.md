### Hexlet tests and linter status:
[![Actions Status](https://github.com/sergeycherkasovv/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/sergeycherkasovv/java-project-99/actions)

### My tests and linter status:
[![my-actions](https://github.com/sergeycherkasovv/java-project-99/actions/workflows/build.yml/badge.svg)](https://github.com/sergeycherkasovv/java-project-99/actions/workflows/build.yml)

### SonarQube:
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sergeycherkasovv_java-project-99&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sergeycherkasovv_java-project-99)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=sergeycherkasovv_java-project-99&metric=bugs)](https://sonarcloud.io/summary/new_code?id=sergeycherkasovv_java-project-99)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=sergeycherkasovv_java-project-99&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=sergeycherkasovv_java-project-99)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sergeycherkasovv_java-project-99&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sergeycherkasovv_java-project-99)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=sergeycherkasovv_java-project-99&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=sergeycherkasovv_java-project-99)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=sergeycherkasovv_java-project-99&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=sergeycherkasovv_java-project-99)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sergeycherkasovv_java-project-99&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sergeycherkasovv_java-project-99)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=sergeycherkasovv_java-project-99&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=sergeycherkasovv_java-project-99)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=sergeycherkasovv_java-project-99&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=sergeycherkasovv_java-project-99)

**[Task Manager](https://taskmanager-wc0g.onrender.com)** — это полнофункциональный REST API для системы управления задачами, разработанный на Spring Boot.

## Проект включает в себя следующие основные сущности:
- **User** — пользователь системы
- **Task** — задача с возможностью назначения исполнителя и статуса
- **TaskStatus** — статус задачи
- **Label** — метки для категории задач

## Возможности:
- CRUD операции над сущностями
- Привязка исполнителей, статусов и меток к задачам
- Фильтрация задач по названию, исполнителю, статусу и меткам
- Аутентификация пользователей и защита маршрутов

## Технологии
- Java 21
- [Spring Boot](https://spring.io/projects/spring-boot) — (Web, Data JPA, Validation, Security)
- [MapStruct](https://mapstruct.org/) - для маппинга данных из одного объекта в другой
- [Datafake](https://www.datafaker.net/) - библиотека для наполнения БД (продакшен и тестирование)
- [H2 Database](https://www.h2database.com) — Локальная БД
- [PostgreSQL](https://www.postgresql.org/) — Серверная БД
- [JUnit5](https://junit.org/), [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver),
  [Instansio](https://www.instancio.org/articles/using-instancio-with-junit-jupiter/),
  [JsonUnit](https://github.com/lukas-krecan/JsonUnit)— тестирование
- [Gradle](https://gradle.org/), [GitHub Actions](https://github.com/features/actions),
  [SonarQube](https://www.sonarsource.com/) — CI/CD и покрытие
- [Sentry](https://sentry.io/welcome/?utm_source=google&utm_medium=cpc&utm_id=%7B20407805488%7D&utm_campaign=Google_Search_Brand_SentryKW_NORM_Alpha&utm_content=g&utm_term=sentry&gad_source=1&gad_campaignid=20407805488&gbraid=0AAAAADua1WLRT4nVMj6tr8ZfkW6oKh8uJ&gclid=CjwKCAjwvO7CBhAqEiwA9q2YJUx-AwPaaXQajEqNtuZwduX6WBI2vbn-NMg7yp3jh0UCF68PFKoZURoCihcQAvD_BwE) — отслеживание ошибок

## Установка и запуск локально
1. Клонировать репозиторий:
```bash
git git@github.com:sergeycherkasovv/java-project-99.git
cd java-project-99
````
2. Запустить локально:
```bash
make start-prod
```
3. Открыть в браузере:
```bash
http://lovalhost:8080
```
4. OpenAPI(Swagger) документация:
```bash
http://localhost:8080/swagger-ui.html
```
5. Посмотреть доступные команды:
```bash
make help
```

## 🗂 Структура проекта
    java-project-99
    ├── .github/                          # CI (GitHub Actions)
    ├── src/
    │   ├── main/
    │   │   ├── java/hexlet/code/         
    │   │   │   ├── component/            # Наполнение БД и ключи
    │   │   │   ├── config/               # Конфигурации Spring Security
    │   │   │   ├── controller/           # REST контроллеры
    │   │   │   ├── exception/            # Кастомные исключения
    │   │   │   ├── handler/              # Глобальный обработчик исключений
    │   │   │   ├── dto/                  # Классы DTO для передачи данных
    │   │   │   ├── mapper/               # Маппинг между DTO и сущностями
    │   │   │   ├── model/                # Модели: Task, Label, TaskStatus, User, AuthRequest
    │   │   │   ├── repository/           # Репозитории Spring Data
    │   │   │   ├── service/              # Сервисы (бизнес-логика)
    │   │   │   ├── specification/        # Фильтрация задач
    │   │   │   ├── util/                 # Security 
    │   │   │   └── AppApplication.java   # Точка входа Spring Boot
    │   │   └── resources/
    │   │       └── application.yml       # Конфиги Spring
    │   └── test/
    │       └── java/hexlet/code/         
    │           ├── controller/           # Интеграционные тесты
    │           └── util/                 # Наполнение БД для тестов
    ├── build.gradle.kts                  # Gradle сборка (Kotlin DSL)
    ├── Makefile                          # Команды для сборки, запуска и тестов
    └── README.md  

## 📮 Автор
Разработано в рамках обучения на Hexlet.
Автор: [sergeycherkasovv](https://github.com/sergeycherkasovv)

Почта: iamcherkasov.job@gmail.com