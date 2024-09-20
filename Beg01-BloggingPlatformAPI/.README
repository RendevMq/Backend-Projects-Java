# Blogging Platform API

Este proyecto es una API RESTful que permite gestionar publicaciones de blog personales mediante operaciones CRUD (Crear, Leer, Actualizar, Eliminar). La plataforma permite crear publicaciones, actualizarlas, eliminarlas, obtener publicaciones individuales o todas las publicaciones, y realizar búsquedas mediante un término específico.

## Reto
Para más información sobre el reto, visita el siguiente enlace: [Blogging Platform API Challenge](https://roadmap.sh/projects/blogging-platform-api)

## Objetivos

El propósito de este proyecto es:

- Entender qué son las APIs RESTful y conocer sus mejores prácticas y convenciones.
- Aprender a crear una API RESTful.
- Familiarizarse con métodos HTTP comunes como GET, POST, PUT, PATCH y DELETE.
- Manejar códigos de estado y control de errores en APIs.
- Realizar operaciones CRUD utilizando una API.
- Aprender a trabajar con bases de datos.

## Requerimientos

La API permite a los usuarios realizar las siguientes operaciones:

- Crear una nueva publicación de blog.
- Actualizar una publicación existente.
- Eliminar una publicación existente.
- Obtener una publicación individual.
- Obtener todas las publicaciones.
- Filtrar publicaciones por un término de búsqueda.

## Tecnologías Utilizadas

- **Java 11+**
- **Spring Boot 2.6+**
- **Spring Security con JWT (JSON Web Tokens)**
- **Spring Data JPA**
- **Hibernate**
- **MySQL**
- **Maven**

## Gestión de Usuarios y Roles con JWT

Este proyecto utiliza **Spring Security con JWT (JSON Web Tokens)** para la autenticación y autorización de usuarios. Existen tres roles predefinidos con diferentes niveles de permisos:

### Roles y Permisos

| Rol      | Permisos                                                              |
| -------- | --------------------------------------------------------------------- |
| **ADMIN** | - Crear publicaciones<br>- Leer todas las publicaciones<br>- Actualizar cualquier publicación<br>- Eliminar cualquier publicación |
| **USER**  | - Crear publicaciones<br>- Leer todas las publicaciones<br>- Actualizar solo sus propias publicaciones<br>- Eliminar solo sus propias publicaciones |
| **INVITED** | - Leer todas las publicaciones (sin permisos para crear, actualizar o eliminar) |

### Usuarios Predefinidos

- **José (ADMIN)**
  - **Username**: `jose`
  - **Password**: `123456`
  - **Rol**: `ADMIN`

- **Daniel (USER)**
  - **Username**: `daniel`
  - **Password**: `123456`
  - **Rol**: `USER`

- **Andrea (INVITED)**
  - **Username**: `andrea`
  - **Password**: `123456`
  - **Rol**: `INVITED`

## Endpoints de la API

### 1. Crear una nueva publicación de blog

- **URL**: `/api/posts`
- **Método**: `POST`
- **Descripción**: Crea una nueva publicación de blog.
- **Cuerpo de la solicitud (Request Body)**:
  
  ```json
  {
    "title": "Mi Primera Publicación",
    "content": "Este es el contenido de mi primera publicación.",
    "category": "Tecnología",
    "tags": ["Tech", "Programación"]
  }
  ```
- **Respuesta Exitosa:**

  ```json
  {
    "id": 1,
    "title": "Mi Primera Publicación",
    "content": "Este es el contenido de mi primera publicación.",
    "category": "Tecnología",
    "tags": ["Tech", "Programación"],
    "createdAt": "2023-09-19T12:00:00Z",
    "updatedAt": "2023-09-19T12:00:00Z"
  }
  ```

- **Códigos de estado posibles:**

  - **201 Created:** Publicación creada exitosamente.
  - **400 Bad Request:** Error en los datos enviados.

### 2. Obtener todas las publicaciones de blog
- **URL:** /api/posts

- **Método:** GET

- **Descripción:** Devuelve todas las publicaciones de blog. Se puede buscar por título, contenido, categoría o tags usando un término de búsqueda.

- **Parámetros de consulta (Query Params):**

  - **term (opcional):** Término para buscar publicaciones.
Respuesta Exitosa:

```json
[
  {
    "id": 1,
    "title": "Mi Primera Publicación",
    "content": "Este es el contenido de mi primera publicación.",
    "category": "Tecnología",
    "tags": ["Tech", "Programación"],
    "createdAt": "2023-09-19T12:00:00Z",
    "updatedAt": "2023-09-19T12:00:00Z"
  },
  {
    "id": 2,
    "title": "Mi Segunda Publicación",
    "content": "Este es el contenido de mi segunda publicación.",
    "category": "Ciencia",
    "tags": ["Ciencia", "Investigación"],
    "createdAt": "2023-09-19T13:00:00Z",
    "updatedAt": "2023-09-19T13:00:00Z"
  }
]
```
- **Códigos de estado posibles:**

  - **200 OK:** Publicaciones obtenidas exitosamente.

### 3. Obtener una publicación de blog por ID
- **URL:** /api/posts/{id}

- **Método:** GET

- **Descripción:** Devuelve una única publicación de blog por su ID.

- **Parámetros de ruta:**

  - **id (Long):** ID de la publicación.

- **Respuesta Exitosa:**

```json
{
  "id": 1,
  "title": "Mi Primera Publicación",
  "content": "Este es el contenido de mi primera publicación.",
  "category": "Tecnología",
  "tags": ["Tech", "Programación"],
  "createdAt": "2023-09-19T12:00:00Z",
  "updatedAt": "2023-09-19T12:00:00Z"
}
```
- **Códigos de estado posibles:**

  - **200 OK:** Publicación obtenida exitosamente.
  - **404 Not Found:** Publicación no encontrada.
### 4. Actualizar una publicación de blog
- **URL:** /api/posts/{id}

- **Método:** PUT

- **Descripción:** Actualiza una publicación existente por su ID.

- **Parámetros de ruta:**

  - **id (Long):** ID de la publicación a actualizar.

- **Cuerpo de la solicitud (Request Body):**

```json
{
  "title": "Mi Publicación Actualizada",
  "content": "Este es el contenido actualizado de mi primera publicación.",
  "category": "Tecnología",
  "tags": ["Tech", "Programación"]
}
```
- **Respuesta Exitosa:**

```json
{
  "id": 1,
  "title": "Mi Publicación Actualizada",
  "content": "Este es el contenido actualizado de mi primera publicación.",
  "category": "Tecnología",
  "tags": ["Tech", "Programación"],
  "createdAt": "2023-09-19T12:00:00Z",
  "updatedAt": "2023-09-19T12:30:00Z"
}
```
- **Códigos de estado posibles:**

- **200 OK:** Publicación actualizada exitosamente.
- **403 Forbidden:** No autorizado para realizar la operación.
- **404 Not Found:** Publicación no encontrada.

### 5. Eliminar una publicación de blog
- **URL:** /api/posts/{id}

- **Método:** DELETE

- **Descripción:** Elimina una publicación existente por su ID.

- **Parámetros de ruta:**

  - **id (Long):** ID de la publicación a eliminar.
- **Códigos de estado posibles:**

  - **204 No Content:** Publicación eliminada exitosamente.
  - **403 Forbidden:** No autorizado para realizar la operación.
  - **404 Not Found:** Publicación no encontrada.

