# Utilisation en local

## 1. Prérequis

- Java 21
- Docker (pour MySQL)
- Maven (ou utiliser `./mvnw`)

## 2. Démarrer MySQL

```bash
docker compose up -d mysql
```

## 3. Lancer l'application

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

L'API est accessible sur : `http://localhost:8081`

## 4. Se connecter (obtenir un token JWT)

```bash
POST http://localhost:8081/api/auth/login

Body (JSON):
{
  "username": "<votre-username>",
  "password": "<votre-mot-de-passe>"
}
```

### Avec curl

```bash
curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"<votre-username>","password":"<votre-mot-de-passe>"}'
```

### Réponse

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "abdy",
  "role": "ADMIN"
}
```

## 5. Utilisateurs disponibles

| Username | Role |
|---|---|
| `abdy` | ADMIN |
| `hassen` | USER |
| `baba` | USER |
| `haja` | USER |
| `abdselam` | USER |

Chaque utilisateur connaît son mot de passe personnel.

## 6. Exemple complet

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"<votre-username>","password":"<votre-mot-de-passe>"}' | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")

# 2. Utiliser le token pour une requête
curl -s http://localhost:8081/api/categories \
  -H "Authorization: Bearer $TOKEN" | python3 -m json.tool

# 3. Créer une catégorie (admin seulement)
curl -s -X POST http://localhost:8081/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Science-Fiction"}' | python3 -m json.tool
```

## 7. Seed des données de référence

```bash
curl -s -X POST http://localhost:8081/api/admin/seed \
  -H "Authorization: Bearer $TOKEN"
```
