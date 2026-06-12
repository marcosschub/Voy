# 🎉 Voy API

API REST para la gestión de eventos, tickets, recibos, usuarios y etiquetas de la plataforma **Voy**.

---

## 📋 Tabla de contenidos

- [Tecnologías](#tecnologías)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Documentación interactiva](#documentación-interactiva)
- [Autenticación](#autenticación)
- [Endpoints](#endpoints)
    - [Auth](#auth---apiauth)
    - [Users](#users---users)
    - [Parties](#parties---apiparties)
    - [Tickets](#tickets---apitickets)
    - [Receipts](#receipts---apireceipts)
    - [Tags](#tags---tags)
- [DTOs principales](#dtos-principales)
- [Validaciones](#validaciones)
- [Integrantes](#integrantes)

---

## Tecnologías

- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Security** + **JWT** (jjwt 0.13.0)
- **Spring Data JPA**
- **Spring Validation**
- **MySQL** (mysql-connector-j)
- **MapStruct 1.6.3**
- **SpringDoc OpenAPI 3.0.2** (Swagger UI)
- **Lombok**

---

## Instalación y ejecución

```bash
# Clonar el repositorio
git clone https://github.com/grupo12/voy.git
cd voy

# Configurar las siguientes variables de entorno:
# DATABASE_NAME  → nombre de la base de datos MySQL
# USERNAME       → usuario de la base de datos
# PASSWORD       → contraseña de la base de datos
# JWT.SECRET     → clave secreta para firmar los tokens JWT
# JWT.EXPIRATION → tiempo de expiración del token (en milisegundos)

# Compilar y ejecutar
./mvnw spring-boot:run
```

---

## Documentación interactiva

Una vez levantada la aplicación, podés consultar todos los endpoints y probarlos directamente desde la interfaz de **Swagger UI** (provista por OpenAPI):

```
http://localhost:8080/swagger-ui/index.html
```

El contrato OpenAPI en formato JSON está disponible en:

```
http://localhost:8080/v3/api-docs
```

Desde Swagger UI podés ver los DTOs esperados, los parámetros de cada endpoint, los códigos de respuesta y autenticarte para probar rutas protegidas.

---

## Autenticación

Los endpoints protegidos usan **JWT** + **Spring Security** con `@PreAuthorize`. Para obtener un token, usá `POST /api/auth/login` con las credenciales. El token debe enviarse en el header `Authorization: Bearer <token>`.

| Rol | Descripción |
|-----|-------------|
| `ROLE_USER` | Usuario estándar de la plataforma |
| `ROLE_ORGANIZATOR` | Organizador, puede crear y gestionar eventos públicos |
| `ROLE_ADMIN` | Administrador con acceso total |

---

## Endpoints

> La documentación completa de cada endpoint, incluyendo parámetros, cuerpos de solicitud y respuestas, está disponible en la interfaz OpenAPI (ver [Documentación interactiva](#documentación-interactiva)).

### Auth — `/api/auth`

| Método | Ruta | Descripción | Rol requerido |
|--------|------|-------------|---------------|
| `POST` | `/api/auth/register` | Registrar nuevo usuario y obtener datos | — |
| `POST` | `/api/auth/login` | Autenticarse y obtener token JWT | — |

---

### Users — `/users`

| Método | Ruta                               | Descripción | Rol requerido |
|--------|------------------------------------|-------------|-------------|
| `GET` | `/api/users`                       | Listar usuarios (filtros: `username`, `email`) | `ROLE_USER` |
| `PUT` | `/api/users/update`                    | Actualizar username y contraseña (usuario autenticado) | `ROLE_USER` |
| `DELETE` | `/api/users/{id}`                      | Eliminar usuario por ID (admin) | `ROLE_ADMIN` |
| `DELETE` | `/api/users`                           | Eliminar cuenta propia | `ROLE_USER` |
| `GET` | `/api/users/follows`                   | Listar usuarios seguidos | `ROLE_USER` |
| `PATCH` | `/api/users/follow/user/{idOtherUser}` | Seguir / dejar de seguir un usuario | `ROLE_USER` |
| `GET` | `/api/users/followers`                 | Listar seguidores | `ROLE_USER` |
| `GET` | `/api/users/myParties`                 | Listar eventos creados por el usuario | `ROLE_USER` |
| `GET` | `/api/users/followParties`             | Listar eventos seguidos | `ROLE_USER` |
| `PATCH` | `/api/users/follow/party/{idParty}`    | Seguir / dejar de seguir un evento | `ROLE_USER` |
| `GET` | `/api/users/myTickets`                 | Listar tickets del usuario | `ROLE_USER` |
| `GET` | `/api/users/myReceipts`                | Listar recibos del usuario | `ROLE_USER` |
| `PATCH` | `/api/users/verify/{idExternal}`       | Verificar / promover usuario | `ROLE_ADMIN` |

---

### Parties — `/api/parties`

| Método | Ruta | Descripción | Rol requerido |
|--------|------|-------------|--------------|
| `GET` | `/api/parties` | Listar eventos (filtros: `partyId`, `organizerId`, `title`, `isPublic`, `city`) | — |
| `GET` | `/api/parties/{id}` | Obtener evento por ID | — |
| `POST` | `/api/parties/public` | Crear evento público | `ROLE_ORGANIZATOR` |
| `POST` | `/api/parties/private` | Crear evento privado | `ROLE_USER` |
| `PUT` | `/api/parties/{id}` | Actualizar evento existente | `ROLE_ORGANIZATOR` o `ROLE_USER` |
| `POST` | `/api/parties/{id}/tag` | Agregar etiqueta a un evento | `ROLE_ORGANIZATOR` |
| `DELETE` | `/api/parties/{id}` | Eliminar evento | `ROLE_ORGANIZATOR` o `ROLE_ADMIN` |

---

### Tickets — `/api/tickets`

| Método | Ruta | Descripción | Rol requerido |
|--------|------|-------------|---------------|
| `GET` | `/api/tickets/admin` | Listar todos los tickets (filtros: `externalId`, `isConfirmed`, `title`, `usernameOrganizer`, `usernameUser`) | `ROLE_ADMIN` |
| `GET` | `/api/tickets/organizer` | Listar tickets de los eventos propios (filtros: `externalId`, `isConfirmed`, `title`, `usernameUser`) | `ROLE_ORGANIZATOR` |
| `GET` | `/api/tickets` | Listar tickets propios (filtros: `externalId`, `isConfirmed`, `title`, `usernameOrganizer`) | `ROLE_USER` |
| `POST` | `/api/tickets` | Comprar tickets para evento público | `ROLE_USER` |
| `POST` | `/api/tickets/private` | Obtener tickets para evento privado | `ROLE_USER` |
| `PATCH` | `/api/tickets/transferTicket/{ticketId}` | Transferir ticket a otro usuario (`newUserExtId` como query param) | `ROLE_USER` |
| `PATCH` | `/api/tickets/confirmTickets/{receiptExtId}` | Confirmar compra de tickets | `ROLE_ADMIN` |
| `DELETE` | `/api/tickets/returnTicket/{ticketId}` | Devolver un ticket | `ROLE_USER` |
| `DELETE` | `/api/tickets/rejectPurchase/{receiptId}` | Rechazar y anular una compra | `ROLE_ADMIN` |

---

### Receipts — `/api/receipts`

| Método | Ruta | Descripción | Rol requerido |
|--------|------|-------------|---------------|
| `GET` | `/api/receipts/admin` | Listar todos los recibos (filtros: `externalId`, `userExtId`, `paymentMethod`, `minPrice`, `maxPrice`, `minFinalPrice`, `maxFinalPrice`, `from`, `to`, `minQuantity`, `maxQuantity`) | `ROLE_ADMIN` |
| `GET` | `/api/receipts` | Listar recibos propios (mismos filtros, sin `userExtId`) | `ROLE_USER` |
| `DELETE` | `/api/receipts/{receiptExtId}/{userExtId}` | Eliminar recibo | `ROLE_ADMIN` |

---

### Tags — `/tags`

| Método | Ruta | Descripción | Rol requerido |
|--------|------|-------------|---------------|
| `GET` | `/api/tags` | Listar etiquetas (filtro opcional: `name`) | — |
| `POST` | `/api/api/tags` | Crear nueva etiqueta | `ROLE_ADMIN` |
| `PUT` | `/api/tags/{oldName}` | Actualizar nombre de etiqueta | `ROLE_ADMIN` |
| `DELETE` | `/api/tags` | Eliminar etiqueta (nombre en el body) | `ROLE_ADMIN` |

---

## DTOs principales

### `NewUserDto`
```json
{
  "email": "usuario@ejemplo.com",
  "username": "juanperez",
  "password": "Pass@1234",
  "birthdate": "2000-05-15"
}
```

### `PartyReqDTO` (evento público)
```json
{
  "title": "Fiesta de verano",
  "description": "Una gran fiesta al aire libre",
  "city": "Mar del Plata",
  "adress": "Av. Colón 1234",
  "dateTime": "2025-12-31T22:00:00",
  "guestLimit": 100,
  "partyAccesibility": true
}
```

### `TicketRequestDTO` (evento público)
```json
{
  "price": 2000.00,
  "paymentMethod": "CREDIT_CARD",
  "quantity": 2,
  "partyIdExternal": "123e4567-e89b-12d3-a456-426614174000"
}
```

### `TagsDTO`
```json
{
  "name": "electrónica"
}
```

---

## Validaciones

| Campo | Regla |
|-------|-------|
| `password` | 8–20 caracteres, al menos una mayúscula, minúscula, número y carácter especial (`@#$%^&+=!`) |
| `email` | Formato de email válido |
| `birthdate` | Debe ser una fecha pasada |
| `quantity` (tickets) | Entre 1 y 5 |
| `price` | Mayor o igual a 0 |
| `dateTime` (evento) | Debe ser una fecha futura |
| `guestLimit` | Mayor a 0 |
| `name` (tag) | No vacío, máximo 30 caracteres |

---

## Integrantes

| Nombre             |
|--------------------|
| Collovati, Mariano |
| Palos, Lucia       |
| Sanchez, Alejandro |
| Schub, Marcos      |

---

## Grupo 12 — Voy 🎟️