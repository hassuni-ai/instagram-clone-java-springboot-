# Instagram Clone REST API Design

A simple Java backend project

## Tech Stack

- **Framework:** Spring Boot 3.x
- **Database:** sqlite
- **Auth:** JWT
- **Serialization:** Jackson

## Base URL

```
http://localhost:8080/api/v1
```

---

## 10 API Endpoints

### Authentication

#### 1. Register User
```
POST /auth/register
```

**Request:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "secret123",
  "fullName": "John Doe"
}
```

**Response:** `201 Created`
```json
{
  "id": "user-001",
  "username": "john_doe",
  "email": "john@example.com",
  "fullName": "John Doe",
  "createdAt": "2024-01-15T10:00:00Z"
}
```

---

#### 2. Login
```
POST /auth/login
```

**Request:**
```json
{
  "email": "john@example.com",
  "password": "secret123"
}
```

**Response:** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "id": "user-001",
    "username": "john_doe"
  }
}
```

---

### Posts

#### 3. Create Post
```
POST /posts
```
*Requires: Bearer Token*

**Request:**
```json
{
  "imageUrl": "https://storage.example.com/image.jpg",
  "caption": "Beautiful sunset 🌅"
}
```

**Response:** `201 Created`
```json
{
  "id": "post-001",
  "imageUrl": "https://storage.example.com/image.jpg",
  "caption": "Beautiful sunset 🌅",
  "author": {
    "id": "user-001",
    "username": "john_doe",
    "avatarUrl": null
  },
  "likesCount": 0,
  "commentsCount": 0,
  "isLiked": false,
  "createdAt": "2024-01-15T12:00:00Z"
}
```

---

#### 4. Get Feed (List Posts)
```
GET /posts?page=0&size=10
```
*Requires: Bearer Token*

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "post-001",
      "imageUrl": "https://storage.example.com/image.jpg",
      "caption": "Beautiful sunset 🌅",
      "author": {
        "id": "user-001",
        "username": "john_doe",
        "avatarUrl": "https://..."
      },
      "likesCount": 42,
      "commentsCount": 5,
      "isLiked": true,
      "createdAt": "2024-01-15T12:00:00Z"
    }
  ],
  "page": 0,
  "totalPages": 5
}
```

---

#### 5. Get Single Post
```
GET /posts/{postId}
```
*Requires: Bearer Token*

**Response:** `200 OK`
```json
{
  "id": "post-001",
  "imageUrl": "https://storage.example.com/image.jpg",
  "caption": "Beautiful sunset 🌅",
  "author": {
    "id": "user-001",
    "username": "john_doe",
    "avatarUrl": "https://..."
  },
  "likesCount": 42,
  "commentsCount": 5,
  "isLiked": true,
  "createdAt": "2024-01-15T12:00:00Z"
}
```

---

#### 6. Delete Post
```
DELETE /posts/{postId}
```
*Requires: Bearer Token (owner only)*

**Response:** `204 No Content`

---

### Likes

#### 7. Like Post
```
POST /posts/{postId}/like
```
*Requires: Bearer Token*

**Response:** `200 OK`
```json
{
  "postId": "post-001",
  "likesCount": 43
}
```

---

#### 8. Unlike Post
```
DELETE /posts/{postId}/like
```
*Requires: Bearer Token*

**Response:** `200 OK`
```json
{
  "postId": "post-001",
  "likesCount": 42
}
```

---

### Comments

#### 9. Add Comment
```
POST /posts/{postId}/comments
```
*Requires: Bearer Token*

**Request:**
```json
{
  "text": "Amazing photo! 😍"
}
```

**Response:** `201 Created`
```json
{
  "id": "comment-001",
  "text": "Amazing photo! 😍",
  "author": {
    "id": "user-002",
    "username": "jane_doe"
  },
  "createdAt": "2024-01-15T13:00:00Z"
}
```

---

#### 10. Get Comments
```
GET /posts/{postId}/comments?page=0&size=20
```
*Requires: Bearer Token*

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "comment-001",
      "text": "Amazing photo! 😍",
      "author": {
        "id": "user-002",
        "username": "jane_doe",
        "avatarUrl": "https://..."
      },
      "createdAt": "2024-01-15T13:00:00Z"
    }
  ],
  "page": 0,
  "totalPages": 1
}
```

---

## Endpoint Summary

| # | Method | Endpoint | Description |
|---|--------|----------|-------------|
| 1 | POST | `/auth/register` | Register new user |
| 2 | POST | `/auth/login` | Login user |
| 3 | POST | `/posts` | Create new post |
| 4 | GET | `/posts` | Get feed/list posts |
| 5 | GET | `/posts/{id}` | Get single post |
| 6 | DELETE | `/posts/{id}` | Delete post |
| 7 | POST | `/posts/{id}/like` | Like a post |
| 8 | DELETE | `/posts/{id}/like` | Unlike a post |
| 9 | POST | `/posts/{id}/comments` | Add comment |
| 10 | GET | `/posts/{id}/comments` | Get comments |

---

## Error Response Format

```json
{
  "error": "NOT_FOUND",
  "message": "Post not found",
  "timestamp": "2024-01-15T10:00:00Z"
}
```

| Status | Error Code | When |
|--------|------------|------|
| 400 | BAD_REQUEST | Invalid input |
| 401 | UNAUTHORIZED | Missing/invalid token |
| 403 | FORBIDDEN | Not owner of resource |
| 404 | NOT_FOUND | Resource doesn't exist |
| 409 | CONFLICT | Already liked/exists |

---

## Java Models

```java
// Domain Models
public class User {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private String avatarUrl;
    private Instant createdAt;

    // Constructors, getters, setters
}

public class Post {
    private String id;
    private String authorId;
    private String imageUrl;
    private String caption;
    private Instant createdAt;

    // Constructors, getters, setters
}

public class Comment {
    private String id;
    private String postId;
    private String authorId;
    private String text;
    private Instant createdAt;

    // Constructors, getters, setters
}

public class Like {
    private String userId;
    private String postId;
    private Instant createdAt;

    // Constructors, getters, setters
}
```

```java
// Generic class for API results
public class ApiResult<T> {
    private T data;
    private String errorCode;
    private String errorMessage;
    private boolean success;

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> result = new ApiResult<>();
        result.data = data;
        result.success = true;
        return result;
    }

    public static <T> ApiResult<T> error(String code, String message) {
        ApiResult<T> result = new ApiResult<>();
        result.errorCode = code;
        result.errorMessage = message;
        result.success = false;
        return result;
    }
}

// Mapper method example
public PostResponse toResponse(Post post, User author, int likesCount, boolean isLiked) {
    return new PostResponse(
        post.getId(),
        post.getImageUrl(),
        post.getCaption(),
        author.toSummary(),
        likesCount,
        isLiked,
        post.getCreatedAt()
    );
}
```

---

## Project Structure

```
src/main/java/com/example/instagram/
├── Application.java
├── config/
│   └── DatabaseConfig.java
├── model/
│   ├── User.java
│   ├── Post.java
│   ├── Comment.java
│   └── Like.java
├── repository/
│   ├── UserRepository.java
│   ├── PostRepository.java
│   ├── CommentRepository.java
│   └── LikeRepository.java
├── service/
│   ├── AuthService.java
│   ├── PostService.java
│   └── CommentService.java
├── controller/
│   ├── AuthController.java
│   ├── PostController.java
│   └── CommentController.java
└── dto/
    ├── Request.java
    └── Response.java
```

---

## Database Schema

```sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    avatar_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE posts (
    id VARCHAR(36) PRIMARY KEY,
    author_id VARCHAR(36) REFERENCES users(id),
    image_url VARCHAR(500) NOT NULL,
    caption TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE likes (
    user_id VARCHAR(36) REFERENCES users(id),
    post_id VARCHAR(36) REFERENCES posts(id),
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, post_id)
);

CREATE TABLE comments (
    id VARCHAR(36) PRIMARY KEY,
    post_id VARCHAR(36) REFERENCES posts(id),
    author_id VARCHAR(36) REFERENCES users(id),
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);
```ss