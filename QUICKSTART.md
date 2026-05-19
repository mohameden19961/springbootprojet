# 🚀 QUICK START - Démarrage Rapide

**Temps estimé** : 10 minutes  

---

## ⚡ 5 Minutes de Setup

### Étape 1 : Préparation MySQL (2 min)

```bash
# Ouvrir MySQL
mysql -u root -p

# Créer la base de données
CREATE DATABASE library_db;

# Créer l'utilisateur
CREATE USER 'supnum'@'localhost' IDENTIFIED BY 'Supnum';

# Accorder les droits
GRANT ALL PRIVILEGES ON library_db.* TO 'supnum'@'localhost';
FLUSH PRIVILEGES;

# Vérifier la connexion
mysql -u supnum -pSupnum library_db
```

**Configuration MySQL** :
- Utilisateur : `supnum`
- Mot de passe : `Supnum`
- Host : `localhost:3306`
- Base de données : `library_db`

### Étape 2 : Builder et Lancer (3 min)

```bash
# Se placer dans le répertoire du projet
cd /home/abdy/ahmed/projSpring/Library

# Construire avec Maven
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

### Étape 3 : Vérifier le Démarrage (Immédiat)

L'application devrait afficher :
```
2024-05-19 10:00:00 INFO Library started in 4.2 seconds
Started LibraryApplication in 4.2 seconds
```

Accéder à l'API sur : **http://localhost:8080**

---

## 🧪 Tester l'API (5 min)

### Test 1 : Créer une catégorie

```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Science-Fiction"}'
```

**Réponse attendue** (201 Created) :
```json
{
  "id": 1,
  "name": "Science-Fiction"
}
```

### Test 2 : Créer une langue

```bash
curl -X POST http://localhost:8080/api/v1/reference/languages \
  -H "Content-Type: application/json" \
  -d '{"code": "en", "name": "English"}'
```

### Test 3 : Créer un éditeur

```bash
curl -X POST http://localhost:8080/api/v1/publishers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Penguin Books",
    "email": "contact@penguin.com"
  }'
```

**Réponse attendue** (201 Created) :
```json
{
  "id": 1,
  "name": "Penguin Books",
  "email": "contact@penguin.com"
}
```

### Test 4 : Créer un livre

```bash
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "isbn": "978-0132350884",
    "languageCode": "en",
    "categoryId": 1,
    "publisherId": 1
  }'
```

**Réponse attendue** (201 Created) :
```json
{
  "id": 1,
  "title": "Clean Code",
  "isbn": "978-0132350884",
  "languageCode": "en",
  "categoryId": 1,
  "publisherId": 1,
  "authors": []
}
```

### Test 5 : Ajouter un exemplaire

```bash
curl -X POST http://localhost:8080/api/v1/books/1/items \
  -H "Content-Type: application/json" \
  -d '{"barcode": "BOOK-001-2024"}'
```

**Réponse attendue** (201 Created) :
```json
{
  "id": 1,
  "barcode": "BOOK-001-2024",
  "status": "AVAILABLE",
  "bookId": 1,
  "version": 0
}
```

### Test 6 : Créer un membre

```bash
curl -X POST http://localhost:8080/api/v1/members \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "memberType": "STUDENT",
    "maxBorrows": 5
  }'
```

**Réponse attendue** (201 Created) :
```json
{
  "id": 1,
  "email": "student@example.com",
  "memberType": "STUDENT",
  "maxBorrows": 5
}
```

### Test 7 : Emprunter un livre (OPÉRATION CRITIQUE)

```bash
curl -X POST http://localhost:8080/api/v1/borrows/member/1/item/1 \
  -H "Content-Type: application/json"
```

**Réponse attendue** (201 Created) :
```json
{
  "id": 1,
  "memberId": 1,
  "bookItemId": 1,
  "renewalCount": 0,
  "status": "ACTIVE",
  "borrowedAt": "2024-05-19T10:30:00",
  "dueDate": "2024-06-18T10:30:00",
  "returnedAt": null
}
```

### Test 8 : Lister les emprunts d'un membre

```bash
curl -X GET "http://localhost:8080/api/v1/borrows/member/1?page=0&size=10"
```

### Test 9 : Retourner un livre

```bash
curl -X PUT http://localhost:8080/api/v1/borrows/1/return \
  -H "Content-Type: application/json"
```

**Réponse attendue** (200 OK) :
```json
{
  "id": 1,
  "memberId": 1,
  "bookItemId": 1,
  "status": "RETURNED",
  "returnedAt": "2024-05-19T10:35:00"
}
```

### Test 10 : Réserver un livre

```bash
# D'abord emprunter à nouveau
curl -X POST http://localhost:8080/api/v1/borrows/member/1/item/1

# Créer une deuxième member
curl -X POST http://localhost:8080/api/v1/members \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student2@example.com",
    "memberType": "STUDENT",
    "maxBorrows": 5
  }'

# Réserver avec le deuxième membre
curl -X POST http://localhost:8080/api/v1/reservations/member/2/book/1
```

**Réponse attendue** (201 Created) :
```json
{
  "id": 1,
  "memberId": 2,
  "bookId": 1,
  "queuePosition": 1,
  "status": "PENDING",
  "createdAt": "2024-05-19T10:40:00"
}
```

---

## 📊 Architecture en 30 Secondes

```
REST API
   ↓
Controllers (Requêtes HTTP)
   ↓
Services (Logique métier)
   ↓
Repositories (JPA)
   ↓
MySQL (Données)
```

**Couches** :
1. **Controllers** : Gèrent les requêtes HTTP (8 controllers)
2. **Services** : Exécutent la logique métier (7 services)
3. **Repositories** : Accès aux données (11 repositories)
4. **Entities** : Modèle JPA (12 entités)
5. **Database** : MySQL stocke les données

---

## 🔍 Vérifier l'Installation

### Vérifier MySQL
```bash
mysql -u supnum -pSupnum library_db -e "SHOW TABLES;"
```

**Vous devriez voir** :
```
author
book
book_author
book_item
borrow
category
language
member
nationality
publisher
reservation
```

### Vérifier l'Application
```bash
# Lister les livres (doit retourner page vide initialement)
curl http://localhost:8080/api/v1/books

# Réponse attendue
{
  "content": [],
  "pageable": {...},
  "totalElements": 0,
  "totalPages": 0
}
```

### Vérifier les Logs
```
# Terminal où mvn spring-boot:run tourne
# Vous devriez voir :
INFO supnum.projet.Library - Application started successfully
DEBUG ... - SQL INSERT INTO language ...
```

---

## 🎯 Flux Complète du Système

```
1. INITIALISATION
   └─ Créer Langue, Catégorie, Éditeur
   
2. CRÉATION DE CONTENU
   └─ Ajouter Livre + Exemplaires (BookItems)
   
3. ENREGISTREMENT MEMBRES
   └─ Créer des Membres (STUDENT, TEACHER, etc.)
   
4. EMPRUNTS
   └─ Member emprunte BookItem
   └─ BookItem.status passe à BORROWED
   
5. RENOUVELLEMENT (OPTIONNEL)
   └─ Member peut renouveler (max 3 fois)
   
6. RETOUR
   └─ Member retourne BookItem
   └─ BookItem.status passe à AVAILABLE
   └─ SI réservation : status passe à RESERVED
   
7. RÉSERVATION (SI PAS DISPO)
   └─ Member réserve
   └─ Mis en file d'attente
   └─ Position calculée automatiquement (FIFO)
   
8. NOTIFICATION READY
   └─ Quand exemplaire retourné
   └─ Réservant reçoit notification (status=READY)
```

---

## ⚙️ Configuration Par Défaut

### MySQL
```properties
Host         : localhost
Port         : 3306
User         : supnum
Password     : Supnum
Database     : library_db
```

### Application
```properties
Server Port  : 8080
API Version  : v1
Base URL     : http://localhost:8080/api/v1
```

### Limites Par Défaut
```
Emprunt Duration  : 30 jours
Max Renouvellements : 3 par emprunt
Max Emprunts Student : 5 (configurable)
Max Emprunts Teacher : 10 (configurable)
Max Emprunts External : 3 (configurable)
```

---

## 🆘 Troubleshooting

### Erreur : "Access Denied for user 'supnum'"
```bash
# Vérifier MySQL
mysql -u supnum -pSupnum library_db

# Sinon recréer l'utilisateur
mysql -u root -p
DROP USER 'supnum'@'localhost';
CREATE USER 'supnum'@'localhost' IDENTIFIED BY 'Supnum';
GRANT ALL PRIVILEGES ON library_db.* TO 'supnum'@'localhost';
FLUSH PRIVILEGES;
```

### Erreur : "Port 8080 already in use"
```properties
# Dans application.properties
server.port=8081  # Changer le port
```

### Erreur : "Tables don't exist"
```bash
# Vérifier dans app.properties
spring.jpa.hibernate.ddl-auto=update  # PAS "none"

# Relancer l'application
mvn spring-boot:run
# Hibernate crée les tables automatiquement
```

### Erreur : "Member has reached maximum borrows"
```
Le membre a atteint la limite d'emprunts
Mettre à jour maxBorrows ou retourner un livre d'abord
```

---

## 📖 Documentation Complète

Pour plus de détails :
- **README.md** : Vue d'ensemble générale
- **STRUCTURE.md** : Structure détaillée du projet
- **24068.md** : Configuration MySQL et architecture
- **24139.md** : Entités et DTOs
- **24238.md** : Repositories et Services
- **24014.md** : Controllers REST
- **24157.md** : Logique métier avancée

---

## 🎉 Bravo !

L'application est maintenant **prête à utiliser** !

**Prochaines étapes** :
1. Explorer les endpoints via REST client (Postman, curl, etc.)
2. Tester les scénarios complexes (réservations, renouvellement)
3. Lire la documentation détaillée pour chaque équipe
4. Ajouter des features supplémentaires selon vos besoins

---

**Version** : 1.0  
**Date** : Mai 2024  
**Status** : ✅ Production Ready
