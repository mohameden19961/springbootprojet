# Guide de développement local

## 1. Prérequis

- Java 21
- Docker
- Maven (ou `./mvnw`)
- Postman (ou tout client HTTP)

## 2. Lancer la base de données

```bash
docker-compose up -d
```

Démarre MySQL 8 + phpMyAdmin.

## 3. Lancer l'API

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

L'API démarre sur le port **8081**.

## 4. Accès

| Service | URL | Identifiants |
|---|---|---|
| phpMyAdmin | http://localhost:8080 | root / root |
| API | http://localhost:8081 | Basic Auth |
| Base de données | localhost:3306 | supnum / Supnum |

## 5. Authentification

L'API utilise HTTP Basic Auth :

| Utilisateur | Mot de passe | Rôle |
|---|---|---|
| admin | admin123 | ADMIN |
| user | user123 | USER |

## 6. Exemples de requêtes

```bash
# Lister les livres
curl -u "user:user123" http://localhost:8081/api/books

# Créer une catégorie
curl -u "admin:admin123" -X POST http://localhost:8081/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Fiction"}'

# Créer un livre
curl -u "admin:admin123" -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0140449266","title":"The Odyssey","publicationYear":1999,"categoryId":1,"publisherId":1,"languageCode":"en"}'
```

## 7. Arrêter

```bash
docker-compose down
```
