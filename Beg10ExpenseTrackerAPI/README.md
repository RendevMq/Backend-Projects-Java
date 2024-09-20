# Sistema de Gestión de Gastos

Este es un sistema de gestión de gastos que permite a los usuarios registrar, organizar y visualizar sus gastos. La aplicación incluye roles diferenciados para **usuarios** y **administradores**, y utiliza **JWT** para autenticar a los usuarios y autorizar sus acciones.

<p align="center">
  <img src="images/reto.png" alt="reto" width="800"/>
</p>

## Reto
Para más información sobre el reto, visita el siguiente enlace: [Expense Tracker API Challenge](https://roadmap.sh/projects/expense-tracker-api)

## Tecnologías Utilizadas

- **Java 11+**: Lenguaje de programación principal para la lógica de la aplicación.
- **Spring Boot**: Framework para crear la aplicación de backend de manera rápida y eficiente.
- **Spring Security**: Para la gestión de la autenticación y autorización de usuarios.
- **JWT (JSON Web Tokens)**: Mecanismo de autenticación basado en tokens para sesiones sin estado (stateless).
- **JPA/Hibernate**: ORM utilizado para la interacción con la base de datos.
- **PostgreSQL**: Base de datos relacional utilizada para almacenar usuarios, categorías y gastos.
- **Maven**: Herramienta para la gestión de dependencias y la construcción del proyecto.
- **Postman**: Cliente API sugerido para probar los endpoints de la aplicación.

## Características Principales

### 1. Autenticación y Autorización

- **Autenticación mediante JWT**: Los usuarios se registran e inician sesión para obtener un token JWT, que se utiliza para autenticar todas las solicitudes posteriores.
- **Roles**:
    - `ADMIN`: Tiene permisos para gestionar categorías globales, ver todos los gastos y eliminar usuarios.
    - `USER`: Solo puede gestionar sus propias categorías personalizadas y gastos.

### 2. Gestión de Categorías

- **Usuarios (USER)**:
    - Pueden crear, actualizar y eliminar **categorías personalizadas**.
    - Solo pueden eliminar categorías si no tienen gastos asociados.
- **Administradores (ADMIN)**:
    - Pueden crear, actualizar y eliminar **categorías globales** que son visibles para todos los usuarios.
    - No pueden eliminar una categoría si hay gastos asociados a ella.

### 3. Gestión de Gastos

- **Usuarios (USER)** pueden:
    - Crear, actualizar y eliminar **sus propios gastos**.
    - Asociar gastos a categorías globales o personalizadas.
    - Filtrar gastos por categoría o por fechas:
        - **Filtros disponibles**:
            - `last_week`: Gastos de la última semana.
            - `last_month`: Gastos del último mes.
            - `last_3_months`: Gastos de los últimos tres meses.
            - `startDate` y `endDate`: Filtrar por un rango de fechas personalizado.
    - No pueden ver ni gestionar los gastos de otros usuarios.

- **Administradores (ADMIN)** pueden:
    - Ver y filtrar **todos los gastos** de todos los usuarios, aplicando los mismos filtros por fechas:
        - `last_week`, `last_month`, `last_3_months`, o por rango personalizado (`startDate`, `endDate`).

### 4. Gestión de Usuarios (Solo para Administradores)

- **Administradores (ADMIN)** pueden:
    - Eliminar usuarios del sistema.
    - Al eliminar un usuario, se eliminan todas sus categorías personalizadas y gastos.

---
## Flujos de Trabajo

### Autenticación

1. **Registro de usuarios**: Los usuarios pueden registrarse en `/auth/signup`.
2. **Inicio de sesión**: Los usuarios obtienen un **JWT** en `/auth/login`.
3. **Acceso protegido**: Todas las peticiones posteriores deben incluir el token JWT en el encabezado de la solicitud: `Authorization: Bearer <token>`.

### Gestión de Categorías

- **Usuarios** pueden:
    - Crear, actualizar y eliminar **categorías personalizadas**. La lógica interna determina si es personalizada o global basándose en el rol del usuario:
        - `POST /api/categories`: Crear una categoría personalizada o global (la lógica determina el tipo de categoría).
        - `PUT /api/categories/{id}`: Actualizar una categoría (Solo si es de su propiedad o si es un administrador para categorías globales).
        - `DELETE /api/categories/{id}`: Eliminar una categoría (Solo si no hay gastos asociados a la categoría).

- **Administradores** pueden:
    - Crear, actualizar y eliminar **categorías globales**. La lógica interna se encarga de determinar el tipo de categoría basado en el rol del usuario:
        - `POST /api/categories`: Crear una categoría global (ADMIN).
        - `PUT /api/categories/{id}`: Actualizar una categoría global (ADMIN).
        - `DELETE /api/categories/{id}`: Eliminar una categoría global (ADMIN, solo si no hay gastos asociados).

### Gestión de Gastos

- **Usuarios** pueden:
    - Crear, actualizar y eliminar **sus propios gastos** usando los siguientes endpoints:
        - `POST /api/expenses`: Crear un nuevo gasto.
        - `PUT /api/expenses/{id}`: Actualizar un gasto existente (Solo si es de su propiedad).
        - `DELETE /api/expenses/{id}`: Eliminar un gasto (Solo si es de su propiedad).
    - Asociar gastos a **categorías globales** o **categorías personalizadas** creadas por el usuario.
    - **Filtrar sus propios gastos** por rango de fechas:
        - Filtros disponibles:
            - `last_week`: Gastos de la última semana.
            - `last_month`: Gastos del último mes.
            - `last_3_months`: Gastos de los últimos 3 meses.
            - `startDate` y `endDate`: Filtrar por un rango de fechas personalizado.
        - Endpoint de filtrado:
            - `GET /api/expenses?filter=last_week`: Filtrar por la última semana.
            - `GET /api/expenses?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`: Filtrar por un rango de fechas personalizado.

- **Administradores** pueden:
    - Ver **todos los gastos** de todos los usuarios usando el mismo endpoint:
        - `GET /api/expenses`: Ver y filtrar todos los gastos del sistema.
        - Aplicar los mismos filtros de fecha que los usuarios:
            - `last_week`, `last_month`, `last_3_months`, o filtrar por rango personalizado (`startDate` y `endDate`).

### Gestión de Usuarios (Administradores)

- **Administradores** pueden:
    - Eliminar usuarios del sistema usando el siguiente endpoint:
        - `DELETE /api/users/{id}`: Eliminar un usuario y todos sus datos asociados, incluyendo categorías personalizadas y gastos.



---
## Endpoints

### Autenticación

- `POST /auth/signup`: Registrar un nuevo usuario.
- `POST /auth/login`: Iniciar sesión y obtener un **JWT**.

### Categorías

- `POST /api/categories`: Crear una categoría. La lógica interna decidirá si es global (ADMIN) o personalizada (USER).
- `PUT /api/categories/{id}`: Actualizar una categoría (ADMIN/USER). Solo los usuarios pueden actualizar sus categorías personalizadas y los administradores las categorías globales.
- `DELETE /api/categories/{id}`: Eliminar una categoría (ADMIN/USER). Solo se puede eliminar si no hay gastos asociados a la categoría.

### Gastos

- `POST /api/expenses`: Crear un nuevo gasto (USER).
- `PUT /api/expenses/{id}`: Actualizar un gasto (USER). Solo el propietario del gasto puede actualizarlo.
- `DELETE /api/expenses/{id}`: Eliminar un gasto (USER). Solo el propietario del gasto puede eliminarlo.
- `GET /api/expenses`: Listar y filtrar gastos:
    - **Usuarios**: Solo pueden ver y filtrar sus propios gastos.
    - **Administradores**: Pueden ver y filtrar los gastos de todos los usuarios.
    - Filtros disponibles:
        - `filter=last_week`: Gastos de la última semana.
        - `filter=last_month`: Gastos del último mes.
        - `filter=last_3_months`: Gastos de los últimos tres meses.
        - `startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`: Filtrar por un rango de fechas personalizado.

### Usuarios

- `DELETE /api/users/{id}`: Eliminar un usuario (ADMIN). Esto eliminará todas las categorías personalizadas y gastos asociados al usuario.

---

## Seguridad y Roles

- **ADMIN**:
    - Crear, actualizar y eliminar **categorías globales**.
    - Eliminar usuarios.
    - Administrar recursos globales y de todos los usuarios.

- **USER**:
    - Crear, actualizar y eliminar **categorías personalizadas**.
    - Crear, actualizar y eliminar **sus propios gastos**.
    - No pueden ver o modificar los recursos de otros usuarios.

---

## Instalación y Ejecución

### Requisitos Previos

- **Java 11+**
- **Maven 3.6+**
- **MySQL** o cualquier base de datos compatible con JPA.
- **Postman** o cualquier cliente de API para probar los endpoints.

### Pasos de Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-repo.git
    ```
2. Configura la base de datos en el archivo application.properties:
   ```bash
    #hibernate
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.hibernate.ddl-auto=update
    
    spring.config.import=optional:file:.env[.properties]
    
    #DATABASE CONFIGURATION
    spring.datasource.url=${SPRING_DATASOURCE_URL}
    spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
    spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.show-sql= true
    
    #SECURITY CONFIG
    security.jwt.key.private = ${SPRING_JWT_KEY_PRIVATE}
    security.jwt.user.generator=${SPRING_JWT_USER_GENERATOR}
    ```

### Pruebas
Usa Postman o cualquier cliente HTTP para probar los endpoints de la API.