# 🎉 Voy API

API REST para la gestión de eventos, tickets, recibos, usuarios y etiquetas de la plataforma **Voy**.

---

## 📋 Tabla de contenidos

- [Tecnologías](#tecnologías)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Documentación interactiva](#documentación-interactiva)
- [Autenticación](#autenticación)
- [Endpoints](#endpoints)
  - [Users](#users---voy-users)
  - [Parties](#parties---apiparties)
  - [Tickets](#tickets---voytickets)
  - [Receipts](#receipts---voyreceipts)
  - [Tags](#tags---tags)
- [DTOs principales](#dtos-principales)
- [Validaciones](#validaciones)

---

## Tecnologías

- **Java 17+**
- **Spring Boot 3**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **SpringDoc OpenAPI (Swagger UI)**
- **Lombok**

---

## Instalación y ejecución

```bash
# Clonar el repositorio
git clone https://github.com/grupo12/voy.git
cd voy

# Configurar variables de entorno en application.properties o application.yml
# DB_URL, DB_USERNAME, DB_PASSWORD

# Compilar y ejecutar
./mvnw spring-boot:run
```

---

## Documentación interactiva

Una vez levantada la aplicación, la documentación Swagger UI está disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

El archivo OpenAPI en formato JSON se puede obtener en:

```
http://localhost:8080/v3/api-docs
```

---

## Autenticación

Algunos endpoints requieren autenticación mediante rol. Se utiliza **Spring Security** con `@PreAuthorize`.

| Rol | Descripción |
|-----|-------------|
| `ROLE_USER` | Usuario estándar de la plataforma |
| `ROLE_ADMIN` | Administrador con acceso a endpoints de confirmación y rechazo de compras |

---

## Endpoints

### Users — `/users`

| Método | Ruta | Descripción | Rol requerido |
|--------|------|-------------|---------------|
| `GET` | `/users` | Listar usuarios (filtros: `username`, `email`) | `ROLE_USER` |
| `GET` | `/users/{idExternal}` | Buscar usuario por ID externo | — |
| `GET` | `/users/{email}` | Buscar usuario por email | — |
| `POST` | `/users` | Crear nuevo usuario | — |
| `PUT` | `/users/{idExternal}` | Actualizar username y contraseña | — |
| `DELETE` | `/users/{id}` | Eliminar usuario | — |
| `GET` | `/users/{idExternal}/follows` | Listar usuarios seguidos | — |
| `PATCH` | `/users/{idUser}/follow/user/{idOtherUser}` | Seguir / dejar de seguir un usuario | — |
| `GET` | `/users/{idExternal}/followers` | Listar seguidores | — |
| `GET` | `/users/{idExternal}/myParties` | Listar eventos creados por el usuario | — |
| `GET` | `/users/{idExternal}/followParties` | Listar eventos seguidos | — |
| `PATCH` | `/users/{idExternal}/follow/party/{idParty}` | Seguir / dejar de seguir un evento | — |
| `GET` | `/users/{idExternal}/myTickets` | Listar tickets del usuario | — |
| `GET` | `/users/{idExternal}/myReceipts` | Listar recibos del usuario | — |

---

### Parties — `/api/parties`

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/parties` | Listar eventos (filtros: `partyId`, `organizerId`, `title`, `isPublic`, `city`) |
| `POST` | `/api/parties` | Crear nuevo evento |
| `PUT` | `/api/parties/{id}` | Actualizar evento existente |
| `POST` | `/api/parties/{id}/tag` | Agregar etiqueta a un evento |
| `DELETE` | `/api/parties/{id}/tag` | Quitar etiqueta de un evento |
| `DELETE` | `/api/parties/{id}` | Eliminar evento |

---

### Tickets — `/voy/tickets`

| Método | Ruta | Descripción | Rol requerido |
|--------|------|-------------|---------------|
| `GET` | `/voy/tickets` | Listar tickets (filtros: `externalId`, `isConfirmed`, `title`, `usernameOrganizer`, `usernameUser`) | — |
| `POST` | `/voy/tickets` | Comprar tickets (máx. 5 por compra) | — |
| `PATCH` | `/voy/tickets/transferTicket/{userId}/{ticketId}` | Transferir ticket a otro usuario | — |
| `PATCH` | `/voy/tickets/confirmTickets/{receiptExtId}` | Confirmar compra de tickets | `ROLE_ADMIN` |
| `DELETE` | `/voy/tickets/returnTicket/{userId}/{ticketId}` | Devolver un ticket | — |
| `DELETE` | `/voy/tickets/rejectPurchase/{receiptId}` | Rechazar y anular una compra | `ROLE_ADMIN` |

---

### Receipts — `/voy/receipts`

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/voy/receipts` | Listar recibos (filtros: `externalId`, `paymentMethod`, `minPrice`, `maxPrice`, `minFinalPrice`, `maxFinalPrice`, `from`, `to`, `minQuantity`, `maxQuantity`) |
| `POST` | `/voy/receipts` | Crear nuevo recibo |
| `DELETE` | `/voy/receipts/{receiptExtId}/{userExtId}` | Eliminar recibo |

---

### Tags — `/tags`

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/tags` | Listar etiquetas (filtro opcional: `name`) |
| `GET` | `/tags/{name}` | Buscar etiqueta por nombre |
| `POST` | `/tags` | Crear nueva etiqueta |
| `PUT` | `/tags/{oldName}` | Actualizar nombre de etiqueta |
| `DELETE` | `/tags` | Eliminar etiqueta (nombre en el body) |

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

### `PartyReqDTO`
```json
{
  "idOrganizer": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Fiesta de verano",
  "description": "Una gran fiesta al aire libre",
  "city": "Mar del Plata",
  "adress": "Av. Colón 1234",
  "dateTime": "2025-12-31T22:00:00",
  "guestLimit": 100,
  "partyAccesibility": true
}
```

### `TicketRequestDTO`
```json
{
  "price": 2000.00,
  "paymentMethod": "CREDIT_CARD",
  "quantity": 2,
  "userIdExternal": "550e8400-e29b-41d4-a716-446655440000",
  "partyIdExternal": "123e4567-e89b-12d3-a456-426614174000"
}
```

### `ReceiptRequestDTO`
```json
{
  "price": 1500.00,
  "paymentMethod": "CREDIT_CARD",
  "quantity": 2,
  "user": {
    "externalId": "550e8400-e29b-41d4-a716-446655440000"
  }
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
| `quantity` (tickets/receipts) | Entre 1 y 5 |
| `price` | Mayor o igual a 0 |
| `dateTime` (evento) | Debe ser una fecha futura |
| `guestLimit` | Mayor a 0 |
| `name` (tag) | No vacío, máximo 30 caracteres |

---

## Grupo 12 — Voy 🎟️
