# Instagram Clone REST API

A fully-featured Instagram-like REST API built with Java 17 and Spring Boot 3. This backend service provides user authentication, post management, social interactions (likes/comments), and a paginated feed system.

## Features

- **Authentication & Authorization** - JWT-based authentication with secure password hashing (BCrypt)
- **User Management** - Registration with email/username validation and login functionality
- **Post Management** - Create, view, and delete posts with image URLs and captions
- **Social Feed** - Paginated feed displaying posts ordered by newest first
- **Likes System** - Like/unlike posts with real-time like count tracking
- **Comments** - Add and retrieve paginated comments on posts
- **API Documentation** - Interactive Swagger UI for exploring endpoints

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.0 |
| Security | Spring Security + JWT (JJWT 0.12.3) |
| Database | SQLite with Hibernate ORM |
| Build Tool | Gradle (Kotlin DSL) |
| Documentation | SpringDoc OpenAPI / Swagger UI 2.3.0 |

## Project Structure

```
src/main/java/com/example/instagram/
├── config/          # Security and JWT configuration
├── controller/      # REST API endpoints
├── service/         # Business logic layer
├── repository/      # Data access layer (JPA)
├── model/           # JPA entities
├── dto/             # Request/Response objects
└── exception/       # Custom exceptions and handlers
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 8.5+ (or use the included wrapper)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/instagram-clone-java-springboot-.git
   cd instagram-clone-java-springboot-
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The server will start at `http://localhost:8080`

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register a new user |
| POST | `/api/v1/auth/login` | Login and receive JWT token |

### Posts

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/posts` | Create a new post |
| GET | `/api/v1/posts` | Get paginated feed |
| GET | `/api/v1/posts/{id}` | Get a specific post |
| DELETE | `/api/v1/posts/{id}` | Delete a post (owner only) |

### Likes

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/posts/{id}/like` | Like a post |
| DELETE | `/api/v1/posts/{id}/like` | Unlike a post |

### Comments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/posts/{postId}/comments` | Add a comment |
| GET | `/api/v1/posts/{postId}/comments` | Get paginated comments |

## API Documentation

Interactive API documentation is available via Swagger UI:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## Usage Examples

### Register a User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "securepass123",
    "fullName": "John Doe"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securepass123"
  }'
```

### Create a Post (Authenticated)

```bash
curl -X POST http://localhost:8080/api/v1/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "imageUrl": "https://example.com/image.jpg",
    "caption": "My first post!"
  }'
```

## Configuration

Application settings can be modified in `src/main/resources/application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8080 | Server port |
| `jwt.secret` | (configured) | JWT signing secret |
| `jwt.expiration` | 86400000 | Token expiration (24 hours) |

## Error Handling

The API returns standardized error responses:

```json
{
  "error": "ERROR_CODE",
  "message": "Human-readable error message",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

Common HTTP status codes:
- `400` - Bad Request (validation errors)
- `401` - Unauthorized (invalid/missing token)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found (resource doesn't exist)
- `409` - Conflict (duplicate resource)

## License

This project is open source and available under the [MIT License](LICENSE).
