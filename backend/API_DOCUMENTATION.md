# Documentation API - Projet 3 ChâTop

## Configuration

### Base de données (MAMP MySQL)
- **Port**: 8889
- **Database**: rental_db
- **Username**: root
- **Password**: root

### Serveur
- **Port**: 3001
- **Base URL**: http://localhost:3001

### Upload d'images
- **Dossier**: `uploads/` (créé automatiquement)
- **Taille max**: 10MB
- **URL d'accès**: http://localhost:3001/uploads/{filename}

---

## Routes API

### 🔓 Routes publiques (sans authentification)

#### GET /api/rentals
Récupère la liste de tous les rentals.

**Réponse 200 OK**:
```json
{
  "rentals": [
    {
      "id": 1,
      "name": "test house 1",
      "surface": 432.0,
      "price": 300.0,
      "picture": "https://...",
      "description": "Lorem ipsum...",
      "owner_id": 1,
      "created_at": "2012/12/02",
      "updated_at": "2014/12/02"
    }
  ]
}
```

#### GET /api/rentals/{id}
Récupère un rental par son ID.

**Réponse 200 OK**:
```json
{
  "id": 1,
  "name": "test house 1",
  "surface": 432.0,
  "price": 300.0,
  "picture": "https://...",
  "description": "Lorem ipsum...",
  "owner_id": 1,
  "created_at": "2012/12/02",
  "updated_at": "2014/12/02"
}
```

**Réponse 404**: Rental non trouvé

---

### 🔑 Routes d'authentification

#### POST /api/auth/register
Inscription d'un nouvel utilisateur.

**Body (JSON)**:
```json
{
  "name": "Test User",
  "email": "test@test.com",
  "password": "password123"
}
```

**Réponse 200 OK**:
```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9..."
}
```

**Réponse 400**: Email déjà utilisé
```json
{}
```

#### POST /api/auth/login
Connexion d'un utilisateur.

**Body (JSON)**:
```json
{
  "email": "test@test.com",
  "password": "password123"
}
```

**Réponse 200 OK**:
```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9..."
}
```

**Réponse 401**: Identifiants incorrects

#### GET /api/auth/me
Récupère les informations de l'utilisateur connecté.

**Header requis**:
```
Authorization: Bearer {token}
```

**Réponse 200 OK**:
```json
{
  "id": 1,
  "name": "Test User",
  "email": "test@test.com",
  "created_at": "2025/10/28",
  "updated_at": "2025/10/28"
}
```

**Réponse 401**: Non authentifié

---

### 🔒 Routes protégées (authentification requise)

**Header requis pour toutes ces routes**:
```
Authorization: Bearer {token}
```

#### POST /api/rentals
Créer un nouveau rental avec upload d'image.

**Content-Type**: `multipart/form-data`

**Paramètres**:
- `name` (String, requis): Nom du rental
- `surface` (Number, requis): Surface en m²
- `price` (Number, requis): Prix
- `picture` (File, requis): Image du rental
- `description` (String, optionnel): Description

**Exemple avec cURL**:
```bash
curl -X POST http://localhost:3001/api/rentals \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "name=Ma maison" \
  -F "surface=100" \
  -F "price=500" \
  -F "picture=@/path/to/image.jpg" \
  -F "description=Belle maison"
```

**Réponse 200 OK**:
```json
{
  "message": "Rental created !"
}
```

**Réponse 401**: Non authentifié
**Réponse 400**: Erreur de validation

#### PUT /api/rentals/{id}
Modifier un rental existant.

**Content-Type**: `multipart/form-data`

**Paramètres** (tous optionnels):
- `name` (String): Nom du rental
- `surface` (Number): Surface en m²
- `price` (Number): Prix
- `picture` (File): Nouvelle image (l'ancienne sera supprimée)
- `description` (String): Description

**Exemple avec cURL**:
```bash
curl -X PUT http://localhost:3001/api/rentals/1 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "name=Maison modifiée" \
  -F "price=600"
```

**Réponse 200 OK**:
```json
{
  "message": "Rental updated !"
}
```

**Réponse 401**: Non authentifié
**Réponse 404**: Rental non trouvé

#### POST /api/messages
Envoyer un message concernant un rental.

**Body (JSON)**:
```json
{
  "rental_id": 1,
  "user_id": 1,
  "message": "Je suis intéressé par cette location"
}
```

**Réponse 200 OK**:
```json
{
  "message": "Message send with success"
}
```

**Réponse 401**: Non authentifié
**Réponse 400**: Validation échouée ou rental/user non trouvé

#### GET /api/user/{id}
Récupère les informations d'un utilisateur par son ID.

**Réponse 200 OK**:
```json
{
  "id": 1,
  "name": "Test User",
  "email": "test@test.com",
  "created_at": "2025/10/28",
  "updated_at": "2025/10/28"
}
```

**Réponse 401**: Non authentifié
**Réponse 404**: Utilisateur non trouvé

---

## Gestion des images

### Upload
- Les images sont uploadées via `multipart/form-data`
- Stockées dans le dossier `uploads/` avec un nom UUID unique
- URL générée: `/uploads/{uuid}.{extension}`

### Accès
Les images sont accessibles publiquement via:
```
http://localhost:3001/uploads/{filename}
```

### Suppression
- Automatique lors de la mise à jour d'un rental avec une nouvelle image
- Les anciennes images sont supprimées du système de fichiers

---

## Sécurité

### JWT (JSON Web Token)
- **Algorithme**: HS384
- **Expiration**: 24 heures (86400000 ms)
- **Format du header**: `Authorization: Bearer {token}`

### Mots de passe
- **Encodage**: BCrypt
- Stockés hashés en base de données

### CORS
- Activé pour toutes les origines (`*`)
- Méthodes autorisées: GET, POST, PUT, DELETE, OPTIONS

---

## Structure du projet

```
backend/
├── src/main/java/com/openclassroom/projet3/
│   ├── config/
│   │   ├── SecurityConfig.java          # Configuration Spring Security + JWT
│   │   ├── JwtAuthenticationFilter.java # Filtre d'authentification JWT
│   │   ├── WebConfig.java               # Configuration CORS
│   │   └── FileUploadConfig.java        # Configuration upload/accès fichiers
│   ├── controller/
│   │   ├── AuthController.java          # Routes auth (register, login, me)
│   │   ├── RentalController.java        # Routes rentals (CRUD)
│   │   ├── MessageController.java       # Routes messages
│   │   └── UserController.java          # Routes user
│   ├── dto/
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── AuthResponse.java
│   │   ├── UserDto.java
│   │   ├── RentalDto.java
│   │   ├── RentalListResponseDto.java
│   │   ├── MessageRequest.java
│   │   └── MessageResponse.java
│   ├── entities/
│   │   ├── User.java                    # Table USERS
│   │   ├── Rental.java                  # Table RENTALS
│   │   └── Message.java                 # Table MESSAGES
│   ├── repositories/
│   │   ├── UserRepository.java
│   │   ├── RentalRepository.java
│   │   └── MessageRepository.java
│   └── service/
│       ├── JwtService.java              # Génération et validation JWT
│       ├── UserService.java             # Logique métier users
│       ├── RentalService.java           # Logique métier rentals
│       ├── MessageService.java          # Logique métier messages
│       └── FileStorageService.java      # Gestion upload/suppression fichiers
└── uploads/                              # Dossier des images uploadées
```

---

## Lancer le projet

### Prérequis
- Java 17
- Maven
- MySQL (MAMP ou autre)

### Étapes

1. **Démarrer MAMP MySQL** (port 8889)

2. **Créer la base de données** (déjà fait):
```sql
CREATE DATABASE rental_db;
```

3. **Configuration** dans `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:8889/rental_db
spring.datasource.username=root
spring.datasource.password=root

jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000

upload.path=uploads/
```

4. **Lancer l'application**:
```bash
cd backend
mvn spring-boot:run
```

5. **Accès**:
- API: http://localhost:3001
- Images: http://localhost:3001/uploads/{filename}

---

## Tests avec cURL

### Inscription
```bash
curl -X POST http://localhost:3001/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@test.com","password":"password"}'
```

### Connexion
```bash
curl -X POST http://localhost:3001/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password"}'
```

### Créer un rental (avec token)
```bash
curl -X POST http://localhost:3001/api/rentals \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "name=Test House" \
  -F "surface=50" \
  -F "price=500" \
  -F "picture=@image.jpg" \
  -F "description=Test description"
```

### Récupérer les rentals
```bash
curl http://localhost:3001/api/rentals
```

---

## Notes importantes

### Méthodes Repository utilisées
- `UserRepository`: `findByEmail()`, `existsByEmail()`, `findById()`
  - `findByEmail()` est nécessaire pour login et JWT (extrait l'email du token)
- `RentalRepository`: `findAll()`, `findById()`, `save()`
- `MessageRepository`: `save()`

### Format des dates
- Format de sortie: `yyyy/MM/dd` (ex: "2025/10/28")
- Gestion automatique via Jackson `@JsonFormat`

### Gestion des erreurs
- 400: Validation échouée / Ressource non trouvée
- 401: Non authentifié
- 404: Ressource non trouvée
- 200: Succès
