# 🌸 PetalArt – Backend de Ecommerce para Floristería

Backend REST completo desarrollado con **Spring Boot** como proyecto final del Grado Superior de Desarrollo de Aplicaciones Web (DAW).

---

## 🧩 Tecnologías

- Java 11+ / 17+  
- Spring Boot (MVC, Data JPA, Security)  
- MySQL (o H2 en desarrollo)  
- JWT para autenticación  
- Dockerfile listo para contenerizar  
- Gradle como herramienta de compilación  

---

## 🚀 Características principales

- Registro y login de usuarios con roles (`ROLE_USER`, `ROLE_ADMIN`) y tokens JWT.
- Endpoints para gestionar:
  - **Productos**: CRUD completo.
  - **Categorías**.
  - **Direcciones** de envío por usuario.
  - **Pedidos**: creación y consulta por usuario/admin.
- Control de acceso:
  - Usuarios normales: acceso a productos, crear pedidos, ver sus datos.
  - Administradores: gestión global de productos, categorías y pedidos.

---

## 🛠️ Instalación y uso

### Requisitos

- Java 11+ instalado  
- MySQL (localhost o contenedor)  
- Docker (opcional)  

---

## 📦 Estructura del proyecto

petalart-backend/
├── src/
│ ├── main/
│ │ ├── java/…/config # Seguridad, JWT
│ │ ├── controller/ # Endpoints REST
│ │ ├── model/ # Entidades JPA
│ │ ├── repository/ # Interfaces CRUD
│ │ └── service/ # Lógica de negocio
│ └── resources/
│ ├── application*.properties
│ └── data.sql # Datos iniciales
├── Dockerfile
├── build.gradle
└── gradlew*, settings.gradle


---

## 👩‍💻 Autor

**Roxana Manea**  
📧 manea.roxanaa@gmail.com  
🔗 [GitHub: roxanamanea29](https://github.com/roxanamanea29)  

Proyecto desarrollado como parte del Trabajo de Fin de Grado en DAW.

---

## 📜 Licencia

Este proyecto está licenciado bajo los términos de la **Licencia MIT**.  
Consulta el archivo [LICENSE](./LICENSE) para más detalles.
