# URL Shortener API

Este proyecto es una API RESTful desarrollada con **Spring Boot**, que permite acortar URLs largas, redirigir a las URLs originales y proporcionar estadísticas sobre cuántas veces se ha accedido a cada URL corta. También incluye integración con **Redis** para el manejo eficiente de conteo de accesos y **PostgreSQL** para el almacenamiento de datos.

## Características principales
- Crear una URL corta a partir de una URL larga.
- Redirigir a la URL original utilizando la URL corta.
- Obtener estadísticas sobre cuántas veces se ha accedido a la URL corta.
- Eliminar URLs cortas y sus contadores de accesos.

## Tecnologías utilizadas
- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA** (para la interacción con PostgreSQL)
- **PostgreSQL** (base de datos relacional)
- **Redis** (para cachear y gestionar el contador de accesos)
- **Lombok** (para reducir código repetitivo)
- **Maven** (para la gestión de dependencias)

## Requisitos previos

### Instalación de herramientas
- **Java 17** o superior.
- **Maven** (para la construcción y ejecución del proyecto).
- **PostgreSQL** (versión 13 o superior).
- **Redis** (en ejecución en el puerto 6379).

### Configuración de la base de datos (PostgreSQL)
1. Instala PostgreSQL y crea una base de datos para este proyecto:
   ```sql
   CREATE DATABASE url_shortener_db;
    ```
2. Asegúrate de configurar las credenciales de acceso en el archivo application.properties: