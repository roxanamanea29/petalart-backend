# 🌸 PetalArt – Aplicación Web para Floristería Online (Frontend + Backend)

**PetalArt** es una aplicación web de comercio electrónico desarrollada como Trabajo de Fin de Grado en Desarrollo de Aplicaciones Web (DAW). Integra un frontend moderno con React + Vite y un backend robusto con Spring Boot, ofreciendo una experiencia completa para clientes y administradores.

---

## 🧰 Tecnologías utilizadas

### 🔙 Backend – Spring Boot

* Java 17
* Spring Boot (MVC, Security, Data JPA)
* JWT para autenticación segura
* MySQL como base de datos
* Gradle como gestor de dependencias
* Dockerfile para despliegue opcional

### 🔜 Frontend – React + Vite

* React 18
* Vite como bundler ultrarrápido
* React Router DOM para navegación SPA
* Fetch API para llamadas HTTP
* Tailwind CSS + CSS personalizado
* Context API para gestión de estado

---

## 🚀 Funcionalidades

* Catálogo de productos por categorías
* Registro / login de usuarios con JWT
* Carrito persistente con y sin login
* Checkout con direcciones y confirmación de pedido
* Panel de administración para gestión de productos, categorías y pedidos
* Rutas protegidas por roles (USER / ADMIN)
* Gestión de direcciones por usuario

---

## 📂 Estructura del repositorio

```
petalart/
├── backend/            # Spring Boot API REST
│   ├── src/main/java/com/example/login_api/
│   │   ├── controller/      # Controladores REST
│   │   ├── service/         # Lógica de negocio
│   │   ├── entity/          # Entidades JPA
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── repository/      # Repositorios
│   │   ├── security/        # Seguridad y filtros JWT
│   │   ├── config/          # Configuración general
│   │   └── mapper/          # ModelMapper config
│   ├── src/main/resources/  # application.properties, data.sql
│   └── build.gradle
├── frontend/           # Interfaz React
│   ├── src/
│   │   ├── components/      # Componentes reutilizables
│   │   ├── components_admin/ # Panel admin
│   │   ├── pages/user/      # Páginas para usuarios
│   │   ├── pages/admin/     # Páginas para admin
│   │   ├── hooks/           # Custom hooks
│   │   ├── Configuration/   # API config
│   │   ├── routes/          # Definición de rutas
│   │   └── assets/, css/    # Estilos e imágenes
│   └── vite.config.js
```

---

## 🛠️ Instalación y ejecución

### Clonar el proyecto

```bash
git clone https://github.com/roxanamanea29/petalart.git
cd petalart
```

### 🔙 Backend (Spring Boot)

1. Configura `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/petalart
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
jwt.secret=tu_secreto_jwt
```

2. Ejecuta:

```bash
cd backend
./gradlew bootRun
```

API disponible en `http://localhost:8080`

### 🔜 Frontend (React)

```bash
cd frontend
npm install
npm run dev
```

App disponible en `http://localhost:5173`

---

## 📰 Endpoints destacados del backend

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
