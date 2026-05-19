# 📚 Système de Gestion de Bibliothèque - Backend REST API

## Vue d'ensemble

Ce projet implémente un **backend complet pour un système de gestion de bibliothèque moderne** en utilisant Spring Boot 4.0.6, Spring Data JPA, Hibernate et MySQL.

Le système gère complètement les livres, les exemplaires physiques, les membres, les emprunts et les réservations avec une architecture professionnelle et maintenable.

---

## 🎯 Objectifs du Projet

✅ Gestion complète des livres et exemplaires  
✅ Gestion des auteurs, catégories et maisons d'édition  
✅ Système d'emprunts avec limites de durée  
✅ Système de réservations avec file d'attente FIFO  
✅ Renouvellement d'emprunts (max 3 fois)  
✅ Soft delete pour conserver l'historique  
✅ Optimistic locking pour éviter les conflits d'accès simultané  
✅ API REST professionnelle avec validation et gestion d'erreurs centralisée  

---

## 🏗️ Architecture et Structure

### Stack Technologique
- **Framework** : Spring Boot 4.0.6
- **ORM** : Spring Data JPA + Hibernate
- **Base de données** : MySQL
- **Validation** : Jakarta Validation
- **Build** : Maven
- **Java Version** : 21

### Structure des Répertoires
```
src/main/java/supnum/projet/Library/
├── controllers/           # REST Controllers
│   ├── BookController.java
│   ├── MemberController.java
│   ├── BorrowController.java
│   ├── ReservationController.java
│   ├── CategoryController.java
│   ├── PublisherController.java
│   ├── AuthorController.java
│   └── ReferenceController.java
├── services/             # Logique métier
│   ├── BookService.java
│   ├── MemberService.java
│   ├── BorrowService.java
│   ├── ReservationService.java
│   ├── CategoryService.java
│   ├── PublisherService.java
│   └── AuthorService.java
├── data/
│   ├── entities/         # Entités JPA
│   ├── repositories/     # Spring Data JPA Repositories
│   └── enums/           # Énumérations métier
├── dto/                 # Data Transfer Objects
├── exceptions/          # Gestion d'exceptions
└── LibraryApplication.java
```

---

## 📊 Modèle de Données

### Tables de Référence
- **language** : Langues des livres
- **nationality** : Nationalités des auteurs

### Tables Principales
- **category** : Catégories de livres
- **publisher** : Maisons d'édition
- **author** : Auteurs avec nationalités
- **book** : Livres intellectuels
- **book_item** : Exemplaires physiques avec versioning pour optimistic lock
- **member** : Membres de la bibliothèque

### Tables Transactionnelles
- **borrow** : Emprunts de livres (historique complet)
- **reservation** : Réservations avec gestion de file d'attente

### Relations
```
Language → Book (1-N)
Nationality → Author (1-N)
Category → Book (1-N)
Publisher → Book (1-N)
Book ↔ Author (N-N)
Book → BookItem (1-N)
Book → Reservation (1-N)
Member → Borrow (1-N)
Member → Reservation (1-N)
BookItem → Borrow (1-1)
```

---

## 🔧 Configuration

### Configuration MySQL

#### 1. Créer la base de données
```sql
CREATE DATABASE IF NOT EXISTS library_db;
```

#### 2. application.properties
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=supnum
spring.datasource.password=Supnum
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080
spring.application.name=Library
```

#### 3. Identifiants de connexion
- **Utilisateur** : `supnum`
- **Mot de passe** : `Supnum`
- **Port MySQL** : `3306`
- **Base de données** : `library_db`

---

## 📦 Énumérations Métier

### BookItemStatus
- `AVAILABLE` : Exemplaire disponible pour emprunt
- `BORROWED` : Exemplaire actuellement emprunté
- `RESERVED` : Exemplaire réservé
- `LOST` : Exemplaire perdu
- `DAMAGED` : Exemplaire endommagé

### BorrowStatus
- `ACTIVE` : Emprunt en cours
- `RETURNED` : Livre retourné
- `OVERDUE` : Emprunt en retard
- `LOST` : Exemplaire perdu

### ReservationStatus
- `PENDING` : Réservation en attente
- `READY` : Livre disponible pour le membre
- `CANCELLED` : Réservation annulée
- `COMPLETED` : Réservation complétée

### MemberType
- `STUDENT` : Étudiant
- `TEACHER` : Enseignant
- `EXTERNAL` : Membre externe

---

## 🔐 Règles Métier Principales

### 1. Emprunt de Livres
```
Avant d'emprunter :
✓ L'exemplaire doit être AVAILABLE
✓ Le membre ne doit pas avoir dépassé maxBorrows
✓ Pas de réservation prioritaire d'un autre membre
```

### 2. Retour de Livres
```
Lors du retour :
✓ Mettre à jour le statut du BookItem
✓ Vérifier les réservations en attente
✓ Attribuer le livre au premier réservant si applicable
```

### 3. Renouvellement
```
Un membre peut renouveler :
✓ Maximum 3 fois par emprunt
✓ Seulement si pas de réservation active
✓ Prolonge la date d'emprunt de 30 jours
```

### 4. Réservations
```
Conditions :
✓ Aucun exemplaire disponible
✓ File d'attente FIFO automatique
✓ Position en file calculée automatiquement
✓ Réorganisation automatique lors d'annulation
```

### 5. Soft Delete
```
Données supprimées logiquement :
✓ Pas de suppression physique
✓ Colonne 'deleted = true'
✓ Filtrage automatique en lectures
✓ Historique conservé
```

### 6. Optimistic Locking
```
Concurrence sur BookItem :
✓ Colonne @Version pour chaque exemplaire
✓ Prévention des surécritures simultanées
✓ Exception en cas de conflit
```

---

## 🚀 Installation et Démarrage

### Prérequis
- Java 21 ou supérieur
- Maven 3.6+
- MySQL 8.0+
- IDE recommandé : IntelliJ IDEA ou VS Code

### Étapes d'installation

1. **Cloner le projet**
```bash
git clone <url-repo>
cd Library
```

2. **Créer la base de données MySQL**
```sql
CREATE DATABASE library_db;
```

3. **Configurer application.properties**
- Vérifier les identifiants MySQL (supnum / Supnum)
- Adapter l'URL si nécessaire

4. **Construire le projet**
```bash
mvn clean install
```

5. **Démarrer l'application**
```bash
mvn spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

---

## 📡 Endpoints API

### 📚 Gestion des Livres
```
POST   /api/v1/books                      - Créer un livre
GET    /api/v1/books                      - Lister tous les livres
GET    /api/v1/books/{id}                 - Obtenir un livre
GET    /api/v1/books/isbn/{isbn}          - Rechercher par ISBN
GET    /api/v1/books/search/title         - Rechercher par titre
GET    /api/v1/books/filter/category/{id} - Filtrer par catégorie
GET    /api/v1/books/filter/author/{id}   - Filtrer par auteur
GET    /api/v1/books/filter/language/{code} - Filtrer par langue
PUT    /api/v1/books/{id}                 - Modifier un livre
DELETE /api/v1/books/{id}                 - Supprimer logiquement

POST   /api/v1/books/{bookId}/items           - Ajouter un exemplaire
GET    /api/v1/books/{bookId}/items           - Lister les exemplaires
GET    /api/v1/books/items/{itemId}           - Obtenir un exemplaire
GET    /api/v1/books/{bookId}/available-count - Compter exemplaires dispo
```

### 👥 Gestion des Membres
```
POST   /api/v1/members                - Enregistrer un membre
GET    /api/v1/members                - Lister tous les membres
GET    /api/v1/members/{id}           - Obtenir un membre
GET    /api/v1/members/email/{email}  - Rechercher par email
PUT    /api/v1/members/{id}           - Modifier un membre
DELETE /api/v1/members/{id}           - Supprimer logiquement
```

### 📖 Gestion des Emprunts
```
POST   /api/v1/borrows/member/{memberId}/item/{itemId} - Emprunter
PUT    /api/v1/borrows/{id}/return    - Retourner un livre
PUT    /api/v1/borrows/{id}/renew     - Renouveler un emprunt
GET    /api/v1/borrows/{id}           - Obtenir un emprunt
GET    /api/v1/borrows/member/{memberId}  - Emprunts d'un membre
GET    /api/v1/borrows/status/{status}    - Filtrer par statut
DELETE /api/v1/borrows/{id}           - Supprimer logiquement
```

### 🔔 Gestion des Réservations
```
POST   /api/v1/reservations/member/{memberId}/book/{bookId} - Réserver
GET    /api/v1/reservations/{id}              - Obtenir
GET    /api/v1/reservations/member/{memberId} - Réservations d'un membre
GET    /api/v1/reservations/book/{bookId}/queue - File d'attente
GET    /api/v1/reservations/status/{status}     - Filtrer par statut
PUT    /api/v1/reservations/{id}/cancel      - Annuler
DELETE /api/v1/reservations/{id}             - Supprimer
```

### 📋 Catégories et Éditeurs
```
POST   /api/v1/categories      - Créer une catégorie
GET    /api/v1/categories      - Lister les catégories
GET    /api/v1/categories/{id} - Obtenir une catégorie
PUT    /api/v1/categories/{id} - Modifier
DELETE /api/v1/categories/{id} - Supprimer

POST   /api/v1/publishers      - Créer un éditeur
GET    /api/v1/publishers      - Lister les éditeurs
GET    /api/v1/publishers/{id} - Obtenir un éditeur
PUT    /api/v1/publishers/{id} - Modifier
DELETE /api/v1/publishers/{id} - Supprimer
```

### ✍️ Auteurs et Références
```
POST   /api/v1/authors           - Créer un auteur
GET    /api/v1/authors           - Lister les auteurs
GET    /api/v1/authors/{id}      - Obtenir un auteur
GET    /api/v1/authors/search    - Rechercher par nom
PUT    /api/v1/authors/{id}      - Modifier
DELETE /api/v1/authors/{id}      - Supprimer

GET    /api/v1/reference/languages      - Lister les langues
GET    /api/v1/reference/nationalities  - Lister les nationalités
```

---

## 📝 Exemples d'Utilisation

### 1. Créer une catégorie
```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Science-Fiction"
  }'
```

### 2. Créer un livre
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

### 3. Ajouter un exemplaire
```bash
curl -X POST http://localhost:8080/api/v1/books/1/items \
  -H "Content-Type: application/json" \
  -d '{
    "barcode": "BOOK-001-2024"
  }'
```

### 4. Enregistrer un membre
```bash
curl -X POST http://localhost:8080/api/v1/members \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "memberType": "STUDENT",
    "maxBorrows": 5
  }'
```

### 5. Emprunter un livre
```bash
curl -X POST http://localhost:8080/api/v1/borrows/member/1/item/1 \
  -H "Content-Type: application/json"
```

### 6. Retourner un livre
```bash
curl -X PUT http://localhost:8080/api/v1/borrows/1/return \
  -H "Content-Type: application/json"
```

### 7. Renouveler un emprunt
```bash
curl -X PUT http://localhost:8080/api/v1/borrows/1/renew \
  -H "Content-Type: application/json"
```

### 8. Réserver un livre
```bash
curl -X POST http://localhost:8080/api/v1/reservations/member/2/book/1 \
  -H "Content-Type: application/json"
```

---

## 🧪 Validation des Données

Tous les endpoints utilisent **Jakarta Validation** pour valider les entrées :

- **Emails** : Format valide requis
- **ISBN** : Format ISBN valide
- **Barcode** : Unique dans le système
- **Nombres positifs** : maxBorrows > 0
- **Énums** : Valeurs acceptées uniquement
- **Champs obligatoires** : Vérifié automatiquement

---

## 🛡️ Gestion des Erreurs

Le système implémente une **gestion d'erreurs centralisée** avec GlobalExceptionHandler :

### Types d'Erreurs
- `404 Not Found` : Ressource non trouvée
- `409 Conflict` : Ressource dupliquée
- `400 Bad Request` : Violation de règle métier ou validation échouée
- `500 Internal Server Error` : Erreur serveur

### Réponse d'Erreur Standard
```json
{
  "timestamp": "2024-05-19T10:30:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Member has reached maximum number of active borrows",
  "path": "/api/v1/borrows/member/1/item/1",
  "validationErrors": null
}
```

---

## 👥 Équipe du Projet

| Matricule | Rôle | Responsabilités |
|-----------|------|-----------------|
| **24068** | Leader/Architecte | Configuration, Architecture, Orchestration |
| **24139** | Développeur Back | Entités & DTOs |
| **24238** | Développeur Back | Repositories & Services |
| **24014** | Développeur Back | Controllers REST |
| **24157** | Développeur Back | Logique Métier & Exceptions |

---

## 📚 Technologies Clés

- **Spring Boot 4.0.6** : Framework principal
- **Spring Data JPA** : Persistence
- **Hibernate** : ORM
- **MySQL Connector/J** : Driver MySQL
- **Jakarta Validation** : Validation des données
- **Lombok** : Réduction du boilerplate
- **Maven** : Build tool

---

## 🔄 Cycle de Vie des Donnees

### Soft Delete (Suppression Logique)
```
Toutes les suppressions sont logiques :
1. Un flag 'deleted' mis à true
2. Les requêtes filtrent automatiquement
3. Les données restent dans la BD
4. L'historique est conservé
```

### Optimistic Locking
```
Pour BookItem :
1. Chaque exemplaire a une colonne @Version
2. Hibernate l'incrémente à chaque modification
3. Détection automatique des conflits
4. Exception OptimisticLockingFailureException
```

---

## 📖 Documentation Supplémentaire

Chaque membre de l'équipe a une documentation détaillée :

- **24068.md** : Configuration et Architecture
- **24139.md** : Entités et DTOs
- **24238.md** : Repositories et Services
- **24014.md** : Controllers REST
- **24157.md** : Logique Métier et Gestion d'Erreurs

---

## 🤝 Contributions

Pour toute contribution :
1. Créer une branche feature
2. Respecter l'architecture en place
3. Tester les modifications
4. Créer une pull request

---

## 📄 Licence

Projet académique SUPNUM

---

## 📞 Support

Pour tout problème ou question, veuillez contacter :
- **Leader** : 24068
- **Équipe développement** : 24139, 24238, 24014, 24157

**Dernière mise à jour** : Mai 2024
**Version API** : 1.0
**Status** : En production
