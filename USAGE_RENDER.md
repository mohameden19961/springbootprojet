# Utilisation sur Render

## 1. URL de base

Remplacer `https://ton-app.onrender.com` par l'URL de ton service Render.

## 2. Se connecter (obtenir un token JWT)

```bash
POST https://ton-app.onrender.com/api/auth/login

Body (JSON):
{
  "username": "abdy",
  "password": "24068Supnum"
}
```

### Avec curl

```bash
curl -s -X POST https://ton-app.onrender.com/api/auth/login \
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

## 3. Utilisateurs disponibles

| Username | Role |
|---|---|
| `abdy` | ADMIN |
| `hassen` | USER |
| `baba` | USER |
| `haja` | USER |
| `abdselam` | USER |

Chaque utilisateur connaît son mot de passe personnel.

## 4. Exemple complet

```bash
# 1. Login
TOKEN=$(curl -s -X POST https://ton-app.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"abdy","password":"24068Supnum"}' | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")

# 2. Lister les membres
curl -s https://ton-app.onrender.com/api/members \
  -H "Authorization: Bearer $TOKEN" | python3 -m json.tool

# 3. Créer un livre (admin)
curl -s -X POST https://ton-app.onrender.com/api/books \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"Dune","isbn":"978-0-441-17271-9"}' | python3 -m json.tool
```

## 5. Seed des données de référence

```bash
curl -s -X POST https://ton-app.onrender.com/api/admin/seed \
  -H "Authorization: Bearer $TOKEN"
```

## 6. Redéploiement

Pour redéployer après un push :

1. Aller sur [dashboard.render.com](https://dashboard.render.com)
2. Cliquer sur le service
3. **Manual Deploy** → **Deploy latest commit**
