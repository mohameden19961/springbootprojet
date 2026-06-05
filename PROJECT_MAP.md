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
[Couche Repository]    — Interfaces Spring Data JPA (extends JpaRepository)
    │
    ▼
[Base de données (MySQL)] — Hibernate DDL auto-update
```

Préoccupations transversales :
- **Sécurité :** La chaîne de filtres Spring Security (JWT Bearer) intercepte toutes les requêtes avant les contrôleurs.
- **Gestion des exceptions :** `@RestControllerAdvice` capture les exceptions de toutes les couches (4 exceptions métier : ResourceNotFoundException → 404, DuplicateResourceException → 409, BusinessException → 400, générique → 500).
- **Soft Delete :** `BaseEntity` `@MappedSuperclass` fournit le champ `deleted` ; `@SQLRestriction("deleted = false")` sur les entités.
- **DTOs de réponse :** Les contrôleurs retournent des DTOs de réponse (`dto/response/`), pas les entités JPA directes.

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
    │   │   ├── controllers/                      # 14 contrôleurs REST
    │   │   ├── services/                         # 12 services métier
    │   │   ├── dto/
    │   │   │   ├── *.java                        # 9 DTOs requête (BookDTO, AuthorDTO, ...)
    │   │   │   └── response/
    │   │   │       └── *.java                    # 7 DTOs réponse (BookResponse, UserResponse, ...)
    │   │   ├── data/
    │   │   │   ├── entities/
    │   │   │   │   ├── BaseEntity.java           # @MappedSuperclass abstraite (soft delete)
    │   │   │   │   ├── *.java                    # 12 entités JPA concrètes
    │   │   │   │   └── enums/                    # 5 enums
    │   │   │   └── repositories/                 # 13 interfaces Spring Data JPA
    │   │   ├── exceptions/
    │   │   │   ├── GlobalExceptionHandler.java   # @RestControllerAdvice (4 handlers)
    │   │   │   ├── ResourceNotFoundException.java
    │   │   │   ├── DuplicateResourceException.java
    │   │   │   └── BusinessException.java
    │   │   └── security/
    │   │       ├── SecurityConfig.java           # Spring Security (JWT, BCrypt, filtre)
    │   │       ├── JwtUtil.java                  # Génération/validation JWT
    │   │       ├── JwtAuthenticationFilter.java  # Filtre Bearer token
    │   │       └── UserDetailsServiceImpl.java   # UserDetailsService persistant
    │   └── resources/
    │       ├── application.properties
    │       ├── application-dev.properties
    │       └── application-prod.properties
    └── test/
        └── java/supnum/projet/Library/
            └── LibraryApplicationTests.java       # Test de chargement du contexte

**Packages actifs dans le projet :**
- `data/entities/` — 13 entités (BaseEntity + 12 concrètes : Language, Nationality, Category, Publisher, Author, Book, BookItem, BookAuthor, Member, Borrow, Reservation, User)
- `data/entities/enums/` — BookItemStatus, AuthorRole, MemberType, BorrowStatus, ReservationStatus
- `data/repositories/` — 13 interfaces repository (Spring Data JPA)
- `dto/` — 9 DTOs requête + 7 DTOs réponse
- `services/` — 12 services (AuthorService, BookAuthorService, BookItemService, BookService, BorrowService, CategoryService, LanguageService, MemberService, NationalityService, PublisherService, ReservationService, UserService)
- `controllers/` — 14 contrôleurs (AdminController, AuthController, AuthorController, BookAuthorController, BookController, BookItemController, BorrowController, CategoryController, LanguageController, MemberController, NationalityController, PublisherController, ReservationController, SeedController)
- `exceptions/` — 4 classes (GlobalExceptionHandler + 3 exceptions métier)
- `security/` — 4 classes (Config, JwtUtil, JwtAuthFilter, UserDetailsServiceImpl)

---

## 🔄 FLUX_SYSTÈME & FLUX_UI

### Flux de données (Requête → Réponse)

```
Client (HTTP)
  │
  │ POST/GET/PUT/DELET sur /api/*
  │ Avec en-tête Authorization: Bearer <token>
  ▼
JwtAuthenticationFilter (avant chaque requête)
  │
  ├── Extrait le token Bearer de l'en-tête
  ├── Valide le token (JwtUtil)
  ├── Charge l'utilisateur (UserDetailsServiceImpl)
  └── Positionne le SecurityContext
  │
  ▼
SecurityFilterChain
  │
  ├── /api/auth/login → permitAll
  ├── /api/admin/**   → hasRole("ADMIN")
  └── anyRequest()    → authenticated
  │
  ▼
DispatcherServlet → Controller
  │
  ├── @Valid valide le corps de la requête → MethodArgumentNotValidException → 400
  │
  ▼
Service (@Transactional)
  │
  ├── Règles métier appliquées
  ├── Exceptions métier : ResourceNotFoundException (404), DuplicateResourceException (409), BusinessException (400)
  │
  ▼
Repository (Spring Data JPA)
  │
  ├── Hibernate génère le SQL
  ├── Pagination via Pageable sur tous les findAll
  ├── @SQLRestriction("deleted = false") filtre les lignes supprimées logiquement
  │
  ▼
Base de données MySQL/PostgreSQL/H2
  │
  └── La réponse (DTO, pas entité JPA) remonte à travers les couches
```

### Parcours utilisateur existants

1. **Authentification :** `POST /api/auth/login` → retourne un token JWT
2. **CRUD Catégorie :** `GET /api/categories?page=0&size=20` → lister paginé → `POST /api/categories` → créer (201) → `DELETE /api/categories/{id}` → soft-delete
3. **CRUD Livre :** `GET /api/books?page=0&size=20` → lister paginé → `POST /api/books` → créer (résout Language, Category, Publisher)
4. **CRUD Auteur :** `GET /api/authors?page=0&size=20` → `POST /api/authors` → créer (résout Nationality)
5. **CRUD Membre :** `GET /api/members?page=0&size=20` → `POST /api/members`
6. **CRUD Éditeur :** `GET /api/publishers?page=0&size=20` → `POST /api/publishers`
7. **Flux Emprunt :** `POST /api/borrows/checkout?memberId=&barcode=` → emprunter (vérifie disponibilité, quota membre) → `POST /api/borrows/{id}/return` → retourner → `POST /api/borrows/{id}/renew` → renouveler (max 3)
8. **Flux Réservation :** `POST /api/reservations` → réserver (file FIFO) → `POST /api/reservations/{id}/cancel` → annuler → `GET /api/reservations/queue/{bookId}` → voir la file
9. **Admin :** `POST /api/admin/users` → créer un utilisateur → `GET /api/admin/users` → lister les utilisateurs
10. **Seed :** `POST /api/admin/seed` → initialiser les langues et nationalités

---

## 📊 GESTION_D'ÉTAT

- **Backend :** Entièrement **REST sans état**. Aucune session côté serveur.
- **Authentification :** JWT Bearer Token → `POST /api/auth/login` retourne un token ; utilisateurs persistés en base via `User` entity et `UserDetailsServiceImpl`.
- **Contrôle de concurrence :** Optimistic locking via `@Version` sur `BookItem.version` — le champ est déclaré mais pas encore géré en cas de `OptimisticLockException`.
- **État Soft Delete :** Champ booléen `deleted` sur toutes les entités supprimables logiquement ; `@SQLRestriction` filtre au moment des requêtes.
- **Pagination :** Tous les endpoints `GET /api/*` acceptent `?page=0&size=20` via `Pageable` (taille par défaut : 20, max : 100).
- **Pas de couche de cache** (pas de Redis, pas de @Cacheable configuré).

---

## ✅ FONCTIONNALITÉS_IMPLÉMENTÉES

1. **Bootstrap Spring Boot 4.0.6 / Java 21** — `LibraryApplication.java`.
2. **Soft Delete** — `BaseEntity` avec champ `deleted` + `@SQLRestriction` sur 6 entités.
3. **5 enums** — `BookItemStatus`, `AuthorRole`, `MemberType`, `BorrowStatus`, `ReservationStatus`.
4. **Authentification JWT** — `POST /api/auth/login` → token JWT ; utilisateurs persistés en DB (`User` entity) ; BCrypt pour les mots de passe ; filtre Bearer.
5. **Sécurité par routes** — `/api/auth/login` public, `/api/admin/**` ADMIN, tout le reste authentifié.
6. **4 exceptions métier** — `ResourceNotFoundException` (404), `DuplicateResourceException` (409), `BusinessException` (400), fallback générique (500).
7. **DTOs de réponse** — 7 DTOs de réponse (`dto/response/`) : plus d'exposition directe des entités JPA. Fini les LazyInitializationException et les fuites de données (`User.password` masqué).
8. **Lombok** — `@Getter @Setter` + `@Builder` + `@NoArgsConstructor` + `@AllArgsConstructor` sur toutes les entités.
9. **Pagination** — `Pageable` sur tous les endpoints findAll (taille par défaut 20, max 100).
10. **201 Created** — Tous les `POST` retournent `201 CREATED` au lieu de `200 OK`.
11. **Messages en français** — Tous les messages d'erreur et de validation sont en français.
12. **Domaine Livre (#24238)** — `Book`, `BookItem` (`@Version`), `BookAuthor` (`@EmbeddedId`) ; CRUD complet avec DTOs réponse.
13. **Domaine Références (#24139)** — `Language`, `Nationality`, `Category`, `Publisher`, `Author` ; CRUD complet avec résolution FK.
14. **Domaine Membre & Emprunt (#24014)** — `Member`, `Borrow` ; 3 règles métier (disponibilité, quota, renouvellement max 3).
15. **Domaine Réservation (#24157)** — `Reservation` ; file FIFO avec calcul de position, annulation.
16. **Admin** — `GET/POST /api/admin/users` pour gérer les utilisateurs.
17. **Seed** — `POST /api/admin/seed` pour initialiser 18 langues et 40+ nationalités.
18. **3 profils** — `dev` (MySQL, port 8081), `prod` (PostgreSQL, port $PORT), `test` (H2 mémoire).
19. **Test de chargement du contexte** — `LibraryApplicationTests` passe.

---

## ⏳ ÉLÉMENTS_ORPHELINS & EN_ATTENTE

Tous les modules domaine spécifiés dans les quatre fichiers de tâches (#24238, #24139, #24014, #24157) sont implémentés. **Le cœur du domaine est fonctionnellement complet.**

### Travail transversal restant :
- Pas de logique de transition réservation→emprunt (promotion automatique de PENDING à READY).
- Pas de détection de retard — pas de colonnes `dueDate` / `returnDate` dans `borrow`.
- `OptimisticLockException` non gérée dans `BorrowService.borrowBook()` (devrait retourner 409 Conflict).
- Pas de `@PreAuthorize` pour la sécurité fine par méthode.

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
| **CSRF désactivé** | HAUTE | `csrf.disable()` — acceptable pour API REST avec JWT, mais risqué avec un client navigateur. |
| **Mot de passe dans le code source** | BASSE | `application-dev.properties` contient `spring.datasource.password=Supnum` en clair (local uniquement). |

### Risques de conception

| Risque | Type | Détails |
|------|------|---------|
| **Problème N+1** | Performance | Les `@ManyToOne` en LAZY peuvent déclencher N+1 lors de la sérialisation. |
| **`@Transactional` sur toute la classe** | Conception | `@Transactional` au niveau classe, trop large. |
| **Absence de `dueDate`/`returnDate`** | Domaine | La table `borrow` n'a pas de colonnes temporelles. |
| **Pas de validation d'unicité DB sur `Member.email`** | Données | Le schéma a `UNIQUE` sur email, mais JPA ne gère pas `DataIntegrityViolationException`. |
| **Optimistic locking non branché dans le service** | Concurrence | `@Version` déclaré sur `BookItem`, mais `BorrowService` ne gère pas `OptimisticLockException`. |
| **Transition réservation→emprunt absente** | Domaine | Pas de promotion automatique PENDING → READY quand un exemplaire est retourné. |

### Risques corrigés (résolus dans le refactoring récent)

| Risque | Correctif |
|------|---------|
| **Entités comme objets de réponse** | DTOs de réponse (`dto/response/`) — plus d'exposition directe des entités JPA. `User.password` n'est plus sérialisé. |
| **Absence de DTOs de réponse** | 7 DTOs de réponse créés (BookResponse, AuthorResponse, UserResponse, etc.). |
| **RuntimeException pour règles métier** | `DuplicateResourceException` (409), `BusinessException` (400) — plus de 500 pour les erreurs métier. |
| **Pas de pagination** | `Pageable` sur tous les endpoints findAll. |
| **Utilisateurs in-memory** | Utilisateurs persistés en base via `User` entity + `UserDetailsServiceImpl`. |
| **HTTP Basic Auth** | JWT Bearer token — plus d'envoi d'identifiants à chaque requête. |
| **Mot de passe dans le code** | `SecurityConfig` ne contient plus d'utilisateurs en dur. |
| **`new Entity()` + setters** | `@Builder` + builders dans tous les services. |
| **Messages d'erreur en anglais** | Tous les messages en français (validation, exceptions, réponses). |
| **200 OK pour les créations** | `201 Created` pour tous les `POST`. |

### Atténuations en place
- `ResourceNotFoundException` → 404, `DuplicateResourceException` → 409, `BusinessException` → 400.
- `MethodArgumentNotValidException` → 400 avec carte d'erreurs détaillée.
- Soft delete avec `@SQLRestriction` empêche l'exposition accidentelle des enregistrements supprimés.
- Encodage BCrypt pour les mots de passe utilisateurs.
- H2 ajouté comme dépendance de test pour permettre les tests sans MySQL.
- Lombok configuré (annotation processor + `@Builder` sur toutes les entités).

---

## 📋 RÉSUMÉ

**État d'avancement :** ~95% du domaine spécifié implémenté. `./mvnw test` est vert sur H2.

**Totaux du projet sur disque :**
- 13 entités JPA (`BaseEntity` + 12 concrètes)
- 5 enums
- 13 repositories
- 16 DTOs (9 requête + 7 réponse)
- 12 services
- 14 contrôleurs
- 4 exceptions
- 4 classes de sécurité

**Actions recommandées restantes :**
1. Gérer `OptimisticLockException` dans `BorrowService` → 409 Conflict.
2. Transition réservation→emprunt (promouvoir position #1 à `READY`).
3. Ajouter `dueDate` / `returnDate` à `Borrow` pour la détection de retard.
4. Interfaces pour les services (découplage, mocking facilité).
5. `@PreAuthorize` pour la sécurité fine par méthode.
6. Tests unitaires / d'intégration supplémentaires.
