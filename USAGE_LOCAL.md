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
  "username": "abdy",
  "password": "24068Supnum"
}
```

### Avec curl

```bash
curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"abdy","password":"24068Supnum"}'
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

| Username | Password | Role |
|---|---|---|
| `abdy` | `24068Supnum` | ADMIN |
| `hassen` | `24238Supnum` | USER |
| `baba` | `24157Supnum` | USER |
| `haja` | `24014Supnum` | USER |
| `abdselam` | `24139Supnum` | USER |

## 6. Exemple complet

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"abdy","password":"24068Supnum"}' | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")

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
