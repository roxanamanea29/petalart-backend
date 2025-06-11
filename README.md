# 🌸 PetalArt – Aplicación Web para Floristería Online

**PetalArt** es una aplicación web de comercio electrónico desarrollada como Trabajo de Fin de Grado en Desarrollo de Aplicaciones Web (DAW). Este repositorio corresponde al **backend** del sistema, implementado con Spring Boot.

---

## 🧰 Tecnologías utilizadas

* Java 17
* Spring Boot (MVC, Security, Data JPA)
* JWT para autenticación segura
* MySQL como base de datos
* Gradle como gestor de dependencias
* Dockerfile para despliegue opcional

---

## 🚀 Funcionalidades

* Registro y login de usuarios con roles (USER / ADMIN)
* Gestión de productos, categorías, direcciones y pedidos
* Seguridad basada en JWT
* API RESTful conectada a frontend externo

---

## 📂 Estructura del proyecto

```
petalart-backend/
├── src/main/java/com/example/login_api/
│   ├── controller/      # Controladores REST
│   ├── service/         # Lógica de negocio
│   ├── entity/          # Entidades JPA
│   ├── dto/             # Data Transfer Objects
│   ├── repository/      # Repositorios
│   ├── security/        # Seguridad y filtros JWT
│   ├── config/          # Configuración general
│   └── mapper/          # ModelMapper config
├── src/main/resources/  # application.properties, data.sql
├── build.gradle
└── Dockerfile
```

---

## 🛠️ Instalación y ejecución

### Clonar el repositorio

```bash
git clone https://github.com/roxanamanea29/petalart-backend.git
cd petalart-backend
```

### Configuración

Edita el archivo `src/main/resources/application.properties` con tus credenciales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/petalart
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
jwt.secret=tu_secreto_jwt
```

### Ejecutar localmente

```bash
./gradlew bootRun
```

API disponible en: `http://localhost:8080`

---

## 📡 Endpoints destacados

| Método | URL               | Descripción                   | Acceso  |
| ------ | ----------------- | ----------------------------- | ------- |
| POST   | `/auth/register`  | Registro de usuario           | Público |
| POST   | `/auth/login`     | Autenticación y JWT           | Público |
| GET    | `/products`       | Listar productos              | Todos   |
| POST   | `/admin/products` | Crear producto                | ADMIN   |
| GET    | `/orders/user`    | Ver pedidos del usuario       | USER    |
| POST   | `/orders`         | Crear pedido                  | USER    |
| ...    |                   | *(ver código para más rutas)* |         |

---

## 👥 Autora

**Roxana Manea**
📧 [manea.roxanaa@gmail.com](mailto:manea.roxanaa@gmail.com)
👤 [GitHub: roxanamanea29](https://github.com/roxanamanea29)

Proyecto desarrollado como Trabajo de Fin de Grado en DAW.

---

## 📄 Licencia

Este proyecto está licenciado bajo los términos de la **Licencia MIT**.
Consulta el archivo [LICENSE](./LICENSE) para más detalles.
