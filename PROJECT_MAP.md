# 🗺️ PROJECT_MAP — Système de Gestion de Bibliothèque

---

## 🛠️ TECH_STACK

| Couche | Technologie | Version | Notes |
|-------|-----------|---------|-------|
| Langage | Java | 21 | LTS, support threads virtuels |
| Framework | Spring Boot | 4.0.6 | Via `spring-boot-starter-parent` |
| ORM | Spring Data JPA / Hibernate | géré par Boot 4.0.6 | `spring-boot-starter-data-jpa` |
| Sécurité | Spring Security | géré par Boot 4.0.6 | `spring-boot-starter-security` |
| Validation | Jakarta Validation (Hibernate Validator) | géré par Boot 4.0.6 | `spring-boot-starter-validation` |
| Web | Spring WebMVC | géré par Boot 4.0.6 | `spring-boot-starter-webmvc` |
| Base de données | MySQL | 8.x | Connecteur : `mysql-connector-j` (scope runtime) |
| Build | Maven | 3.9.15 | Wrapper configuré |
| Lombok | Lombok | géré par Boot 4.0.6 | Scope `optional`, annotation processor configuré |
| DevTools | Spring Boot DevTools | géré par Boot 4.0.6 | Scope runtime, optionnel |

**Dépendances de test :** `spring-boot-starter-data-jpa-test`, `spring-boot-starter-security-test`, `spring-boot-starter-validation-test`, `spring-boot-starter-webmvc-test` (scope test).

**Note :** Aucun framework frontend. API REST pure. Pas de dépendance Swagger/OpenAPI configurée.

---

## 🏗️ ARCHITECTURE_OVERVIEW

L'application suit une **Architecture en Couches** avec une **exposition REST** :

```
Requête HTTP
    │
    ▼
[Couche Controller]    — Endpoints REST, validation des entrées (@Valid)
    │
    ▼
[Couche Service]       — Logique métier, @Transactional
    │
    ▼
[Couche DAO]           — @Repository, encapsule les appels JPA, gère ResourceNotFoundException
    │
    ▼
[Couche Repository]    — Interfaces Spring Data JPA (extends JpaRepository)
    │
    ▼
[Base de données (MySQL)] — schema.sql + Hibernate DDL auto-update
```

Préoccupations transversales :
- **Sécurité :** La chaîne de filtres Spring Security intercepte toutes les requêtes avant les contrôleurs.
- **Gestion des exceptions :** `@RestControllerAdvice` capture les exceptions de toutes les couches.
- **Soft Delete :** `BaseEntity` `@MappedSuperclass` fournit le champ `deleted` ; `@SQLRestriction("deleted = false")` sur les entités.

L'architecture **n'est pas** hexagonale — il n'y a pas de ports/adaptateurs. Les DTOs sont utilisés minimalement (uniquement pour la création). Les entités sont retournées directement dans les réponses.

---

## 📁 STRUCTURE_DU_PROJET

```
.
├── pom.xml                              # POM Maven (module unique, Spring Boot 4.0.6, Java 21)
├── README.md                            # README du projet, workflow d'équipe, diagramme MCD
├── description.md                       # Document fonctionnel (en français)
├── 24014.md                             # Spécification des tâches : Membre & Emprunt (matricule 24014)
├── 24068.md                             # Spécification des tâches : Sécurité, Exceptions (leader)
├── 24139.md                             # Spécification des tâches : Entités de référence (Category, Author, Publisher)
├── 24157.md                             # Spécification des tâches : Réservation & File d'attente (matricule 24157)
├── 24238.md                             # Spécification des tâches : Book, BookItem, BookAuthor (matricule 24238)
├── mvnw / mvnw.cmd                      # Scripts Maven wrapper
└── src/
    ├── main/
    │   ├── java/supnum/projet/Library/
    │   │   ├── LibraryApplication.java          # Point d'entrée @SpringBootApplication
    │   │   ├── dao/
    │   │   │   ├── AuthorDao.java                 # DAO Author (encapsule AuthorRepository)
    │   │   │   ├── BookDao.java                   # DAO Book (encapsule BookRepository)
    │   │   │   ├── BookAuthorDao.java             # DAO BookAuthor (encapsule BookAuthorRepository)
    │   │   │   ├── BookItemDao.java               # DAO BookItem (encapsule BookItemRepository)
    │   │   │   ├── BorrowDao.java                 # DAO Borrow (encapsule BorrowRepository)
    │   │   │   ├── CategoryDao.java               # DAO Category (encapsule CategoryRepository)
    │   │   │   ├── LanguageDao.java               # DAO Language (encapsule LanguageRepository)
    │   │   │   ├── MemberDao.java                 # DAO Member (encapsule MemberRepository)
    │   │   │   ├── NationalityDao.java            # DAO Nationality (encapsule NationalityRepository)
    │   │   │   ├── PublisherDao.java              # DAO Publisher (encapsule PublisherRepository)
    │   │   │   ├── ReservationDao.java            # DAO Reservation (encapsule ReservationRepository)
    │   │   │   └── UserDao.java                   # DAO User (encapsule UserRepository)
    │   │   ├── data/
    │   │   │   └── entities/
    │   │   │       ├── BaseEntity.java           # @MappedSuperclass abstraite (soft delete)
    │   │   │       └── enums/
    │   │   │           └── ReservationStatus.java # PENDING, READY, CANCELLED, COMPLETED
    │   │   ├── exceptions/
    │   │   │   ├── GlobalExceptionHandler.java    # @RestControllerAdvice (3 handlers)
    │   │   │   └── ResourceNotFoundException.java  # RuntimeException personnalisée
    │   │   └── security/
    │   │       └── SecurityConfig.java            # Spring Security (JWT, BCrypt, filtre Bearer)
    │   └── resources/
    │       ├── application.properties             # Configuration DB, JPA, serveur
    │       └── schema.sql                         # DDL pour les 10 tables
    └── test/
        └── java/supnum/projet/Library/
            └── LibraryApplicationTests.java       # Test de chargement du contexte
```

**Packages actifs dans le projet :**
- `dao/` — 12 DAOs (AuthorDao, BookDao, BookAuthorDao, BookItemDao, BorrowDao, CategoryDao, LanguageDao, MemberDao, NationalityDao, PublisherDao, ReservationDao, UserDao)
- `data/entities/` — 11 entités (BaseEntity + Language, Nationality, Category, Publisher, Author, Book, BookItem, BookAuthor, Member, Borrow, Reservation, User)
- `data/entities/enums/` — BookItemStatus, AuthorRole, MemberType, BorrowStatus, ReservationStatus
- `data/repositories/` — 12 interfaces repository (Spring Data JPA)
- `dto/` — CategoryDTO, AuthorDTO, BookDTO, BookItemDTO, MemberDTO, PublisherDTO, ReservationDTO, UserRegistrationDTO
- `services/` — 10 services (AuthorService, BookAuthorService, BookItemService, BookService, BorrowService, CategoryService, MemberService, PublisherService, ReservationService, UserService)
- `controllers/` — 12 contrôleurs (AuthController, AuthorController, BookAuthorController, BookController, BookItemController, BorrowController, CategoryController, LanguageController, MemberController, NationalityController, PublisherController, ReservationController, SeedController, AdminController)

---

## 🔄 FLUX_SYSTÈME & FLUX_UI

### Flux de données (Requête → Réponse)

```
Client (HTTP)
  │
  │ POST/GET/PUT/DELET sur /api/*
  │ Avec en-tête Basic Auth (username:password)
  ▼
SecurityFilterChain
  │
  ├── /api/public/** → permitAll (sans authentification)
  ├── /api/admin/**  → hasRole("ADMIN")
  └── anyRequest()   → authenticated
  │
  ▼
DispatcherServlet → Controller
  │
  ├── @Valid valide le corps de la requête → en cas d'échec, MethodArgumentNotValidException → GlobalExceptionHandler retourne 400
  │
  ▼
Service (avec @Transactional)
  │
  ├── Règles métier appliquées
  ├── Appels aux méthodes Repository
  │
  ▼
Repository (JPA)
  │
  ├── Hibernate génère le SQL
  ├── @SQLRestriction("deleted = false") filtre les lignes supprimées logiquement
  │
  ▼
Base de données MySQL (library_db)
  │
  └── La réponse remonte à travers les couches
```

### Parcours utilisateur existants (d'après le schéma + code planifié)

**État actuel (seule l'infrastructure existe) :**
1. Aucun parcours utilisateur réel — les contrôleurs, services, entités (sauf BaseEntity) ne sont pas encore créés.

**Parcours planifiés (d'après les spécifications .md) :**

1. **CRUD Catégorie :** `GET /api/categories` → lister → `POST /api/categories` → créer → `DELETE /api/categories/{id}` → soft-delete
2. **CRUD Livre :** `GET /api/books` → lister → `POST /api/books` → créer (résout les références Language, Category, Publisher)
3. **Flux Emprunt :** `POST /api/borrows/checkout?memberId=&barcode=` → emprunter (vérifie disponibilité, quota membre) → `POST /api/borrows/{id}/return` → retourner → `POST /api/borrows/{id}/renew` → renouveler (max 3)
4. **Flux Réservation :** `POST /api/reservations` → réserver (file FIFO) → `POST /api/reservations/{id}/cancel` → annuler → `GET /api/reservations/queue/{bookId}` → voir la file

---

## 📊 GESTION_D'ÉTAT

- **Backend :** Entièrement **REST sans état**. Aucune session côté serveur.
- **Authentification :** HTTP Basic Auth → chaque requête transporte les identifiants ; `InMemoryUserDetailsManager` stocke les utilisateurs en mémoire (pas de persistance, perdu au redémarrage).
- **Contrôle de concurrence :** Optimistic locking via `@Version` sur `BookItem.version` (planifié, pas encore implémenté).
- **État Soft Delete :** Champ booléen `deleted` sur toutes les entités supprimables logiquement ; `@SQLRestriction` filtre au moment des requêtes.
- **Pas de couche de cache** (pas de Redis, pas de @Cacheable configuré).

---

## ✅ FONCTIONNALITÉS_IMPLÉMENTÉES

Ces fonctionnalités sont **100% complètes et fonctionnelles** (les fichiers existent sur disque) :

1. **Bootstrap de l'application Spring Boot** — `LibraryApplication.java` démarre sur le port 8081.
2. **Classe de base Soft Delete** — `BaseEntity` `@MappedSuperclass` avec champ booléen `deleted`.
3. **Enum ReservationStatus** — `PENDING, READY, CANCELLED, COMPLETED`.
4. **Configuration Spring Security** — Utilisateurs in-memory (`admin`/`admin123` avec rôle ADMIN, `user`/`user123` avec rôle USER), encodage BCrypt, CSRF désactivé, Basic Auth.
5. **Autorisation basée sur les routes** — `/api/public/**` ouvert, `/api/admin/**` réservé ADMIN, tout le reste authentifié.
6. **ResourceNotFoundException personnalisée** — Étend `RuntimeException`.
7. **Gestionnaire global d'exceptions** — `@RestControllerAdvice` gérant `ResourceNotFoundException` (404), `MethodArgumentNotValidException` (400), `Exception` générique (500).
8. **Schéma DDL de la base de données** — `schema.sql` crée les 10 tables (`language`, `nationality`, `category`, `publisher`, `author`, `book`, `book_item`, `member`, `book_author`, `reservation`, `borrow`) avec clés étrangères.
9. **Propriétés de l'application** — Configuration MySQL (`library_db`, utilisateur `supnum`/`Supnum`, port 3306), Hibernate `ddl-auto=update`, mode SQL init `always`, valeurs par défaut de pagination (taille 20, max 100), port serveur 8081.
10. **Test de chargement du contexte** — Test smoke `@SpringBootTest` basique.
11. **Domaine Livre (tâche #24238)** — Entités `Book`, `BookItem` (avec `@Version` optimiste), `BookAuthor` (avec `@EmbeddedId`) ; enums `BookItemStatus` et `AuthorRole` ; `BookRepository`, `BookItemRepository` ; `BookDTO` (`@Valid`) ; `BookService` (`findAll`, `create` avec vérification ISBN dupliqué et résolution FK) ; `BookController` exposant `GET /api/books` et `POST /api/books`. Code de production + tests compilent avec `./mvnw clean test-compile`.
12. **Domaine Entités de Référence (tâche #24139)** — `Language`, `Nationality` (PK string, pas de soft delete) ; `Category`, `Publisher`, `Author` (PK Long, soft-deletable via `BaseEntity` + `@SQLRestriction`) ; `Author.nationality` `@ManyToOne(LAZY)`. Repositories `LanguageRepository`, `NationalityRepository`, `CategoryRepository` (avec `findByName`), `PublisherRepository` (avec `findByName`), `AuthorRepository`. DTOs `CategoryDTO`, `AuthorDTO`, `PublisherDTO` (avec `@Email` et `@Size`). Services `CategoryService`, `PublisherService`, `AuthorService` exposant `findAll`/`create`/`delete` avec vérifications d'unicité et résolution FK. Controllers `CategoryController` (`/api/categories`), `PublisherController` (`/api/publishers`), `AuthorController` (`/api/authors`). `./mvnw test` passe sur H2.
13. **Domaine Membre & Emprunt (tâche #24014)** — Enums `MemberType` (`STUDENT`, `TEACHER`, `EXTERNAL`) et `BorrowStatus` (`ACTIVE`, `RETURNED`, `OVERDUE`, `LOST`). Entités `Member` (PK Long, soft-deletable) et `Borrow` (pas de soft delete). `BorrowService` applique 3 règles métier. `BorrowController` expose les endpoints de checkout, retour et renouvellement. `./mvnw test` passe sur H2.
14. **Domaine Réservation & File d'attente (tâche #24157)** — Entité `Reservation` (PK Long, pas de soft delete). `ReservationService` calcule la position FIFO. `ReservationController` expose les endpoints de réservation, annulation et file d'attente. `./mvnw test` passe sur H2.

---

## ⏳ ÉLÉMENTS_ORPHELINS & EN_ATTENTE

Tous les modules domaine spécifiés dans les quatre fichiers de tâches (#24238, #24139, #24014, #24157) sont maintenant implémentés sur disque. **Le cœur du domaine est fonctionnellement complet.** Aucun fichier source Java du domaine n'est en attente.

### Écarts honorés par la spécification (intentionnels, pas en attente) :
- **Pas de `MemberService` / `MemberController`.** La tâche #24014 définit `MemberDTO` mais pas de service ni contrôleur. Les membres ne peuvent être persistés que via un accès direct au repository.
- **Pas d'endpoint CRUD `BookItem`.** La tâche #24238 définit l'entité et le repository mais ni service ni contrôleur.
- **Pas d'endpoints CRUD `Language` / `Nationality`.** La tâche #24139 définit uniquement les entités et repositories — les données de référence sont traitées comme pré-remplies.

### Travail transversal hors scope :
- `SecurityConfig.java` définit les règles de route `/api/public/**` et `/api/admin/**`, mais aucun des contrôleurs implémentés ne se trouve sous ces chemins.
- Pas de logique de transition réservation→emprunt.
- Pas de détection de retard — pas de colonnes `dueDate` / `returnDate` dans `borrow`.
- `BookService` expose uniquement `findAll` + `create` ; pas d'endpoints `update` / `delete`.

---

## 🔗 DÉPENDANCES_ENTRE_MODULES

Le graphe de dépendances (selon les fichiers .md de tâches) :

```
Category ──┐
Publisher ─┼──► Book ──┐
Language ──┘            │
                  Book ──┼──► BookItem
                  Author─┤
                        │
                        ├──► BookAuthor (table de jonction, N-N avec rôle)
                        │
Member ──► Borrow ──► BookItem
Member ──► Reservation ──► Book
```

Règles de dépendances clés :
- **Book** dépend de Language, Category, Publisher (tous `@ManyToOne`).
- **BookItem** dépend de Book (`@ManyToOne`).
- **BookAuthor** relie Book et Author avec une `@EmbeddedId` et un attribut `role`.
- **Borrow** dépend de Member et BookItem (`@ManyToOne` chacun).
- **Reservation** dépend de Member et Book (`@ManyToOne` chacun).
- **Author** dépend de Nationality (`@ManyToOne`).
- Category, Publisher, Language, Nationality sont des tables de référence autonomes.

**Risque de dépendance circulaire :** Absent — toutes les arêtes sont unidirectionnelles.

---

## 🔒 SÉCURITÉ & RISQUES

### Zones sensibles à la sécurité

| Problème | Sévérité | Détails |
|-------|----------|---------|
| **Utilisateurs in-memory** | HAUTE | `InMemoryUserDetailsManager` — utilisateurs codés en dur et perdus au redémarrage. |
| **Identifiants codés en dur** | MOYENNE | `admin`/`admin123` et `user`/`user123` en clair dans le code source. |
| **CSRF désactivé** | HAUTE | `csrf.disable()` — acceptable pour API REST avec Basic Auth, mais risqué avec un client navigateur. |
| **HTTP Basic Auth uniquement** | MOYENNE | Pas de JWT, OAuth2 ou token. Identifiants envoyés en chaque requête. |
| **Entités comme objets de réponse** | HAUTE | Les entités sont retournées directement par les contrôleurs, exposant la structure interne. |
| **Mot de passe dans le code source** | BASSE | `application.properties` contient `spring.datasource.password=Supnum` en clair. |

### Risques de conception

| Risque | Type | Détails |
|------|------|---------|
| **Problème N+1** | Performance | Tous les `@ManyToOne` utilisent `FetchType.LAZY`, mais `findAll()` peut déclencher N+1 lors de la sérialisation Jackson. |
| **Pas de pagination** | Performance | Les endpoints de liste retournent `List<T>` sans pagination. |
| **RuntimeException pour règles métier** | Conception | Les violations de règles métier lèvent des `RuntimeException` → erreur 500 au lieu de 409. |
| **`@Transactional` sur toute la classe** | Conception | `@Transactional` au niveau classe, trop large. |
| **Absence de `dueDate`/`returnDate`** | Domaine | La table `borrow` n'a pas de colonnes temporelles. |
| **Pas de validation d'unicité DB sur `Member.email`** | Données | Le schéma a `UNIQUE` sur email, mais JPA ne gère pas `DataIntegrityViolationException`. |
| **Absence de DTOs de réponse** | Maintenabilité | Les entités exposées directement dans les réponses REST. |
| **Optimistic locking non branché dans le service** | Concurrence | `@Version` déclaré sur `BookItem`, mais `BorrowService.borrowBook()` ne gère pas `OptimisticLockException`. |

### Atténuations en place
- `ResourceNotFoundException` retourne correctement 404.
- `MethodArgumentNotValidException` gérée pour les échecs `@Valid` → 400 avec carte d'erreurs.
- Soft delete avec `@SQLRestriction` empêche l'exposition accidentelle des enregistrements supprimés.
- Encodage BCrypt pour les utilisateurs in-memory.
- H2 ajouté comme dépendance de test pour permettre les tests sans MySQL.

---

## 📋 RÉSUMÉ

**État d'avancement :** ~100% de la couche domaine spécifiée. Les quatre fichiers de tâches (#24238, #24139, #24014, #24157) sont implémentés et vérifiés. `./mvnw test` est vert sur H2.

**Totaux du projet sur disque :**
- 11 entités JPA (`BaseEntity` + 10 concrètes)
- 7 enums
- 12 repositories
- 12 DAOs
- 8 DTOs
- 10 services
- 14 contrôleurs

**Actions recommandées (hors scope des tâches spécifiées) :**
1. `Pageable` sur tous les endpoints de liste `GET /api/<resource>`.
2. DTOs de réponse / `@EntityGraph` pour les risques N+1.
3. Endpoints CRUD Membre (utilise le `MemberDTO` existant).
4. Gérer `OptimisticLockException` dans `BorrowService.borrowBook()` → retourner 409.
5. Remplacer les `RuntimeException` métier par des exceptions domaine → 409 Conflict.
6. Migrer de l'auth in-memory vers un `UserDetailsService` persistant.
7. Migration de schéma pour la détection de retard (ajouter `dueDate` / `returnDate` à `borrow`).
8. Transition réservation→emprunt (promouvoir automatiquement la position #1 à `READY`).
