# PROJECT_MAP.md — Library Management System

---

## TECH_STACK

| Layer | Technology | Version | Notes |
|-------|-----------|---------|-------|
| Language | Java | 21 | LTS, virtual threads support |
| Framework | Spring Boot | 4.0.6 | Via `spring-boot-starter-parent` |
| ORM | Spring Data JPA / Hibernate | managed by Boot 4.0.6 | `spring-boot-starter-data-jpa` |
| Security | Spring Security | managed by Boot 4.0.6 | `spring-boot-starter-security` |
| Validation | Jakarta Validation (Hibernate Validator) | managed by Boot 4.0.6 | `spring-boot-starter-validation` |
| Web | Spring WebMVC | managed by Boot 4.0.6 | `spring-boot-starter-webmvc` |
| Database | MySQL | any 8.x | Connector: `mysql-connector-j` (runtime scope) |
| Build | Maven | 3.9.15 | Wrapper configured |
| Boilerplate | Lombok | managed by Boot 4.0.6 | `optional` scope, annotation processor configured |
| DevTools | Spring Boot DevTools | managed by Boot 4.0.6 | runtime scope, optional |

**Test dependencies:** `spring-boot-starter-data-jpa-test`, `spring-boot-starter-security-test`, `spring-boot-starter-validation-test`, `spring-boot-starter-webmvc-test` (all test scope).

**Note:** No frontend framework. This is a pure REST API backend. No Swagger/OpenAPI dependency configured.

---

## ARCHITECTURE_OVERVIEW

The application follows a **Layered Architecture** with **REST API exposure**:

```
HTTP Request
    │
    ▼
[Controller Layer]    — REST endpoints, input validation (@Valid)
    │
    ▼
[Service Layer]       — Business logic, @Transactional
    │
    ▼
[Repository Layer]    — Spring Data JPA interfaces (extends JpaRepository)
    │
    ▼
[Database (MySQL)]   — schema.sql + Hibernate DDL auto-update
```

Cross-cutting concerns:
- **Security:** Spring Security filter chain intercepts all requests before controllers.
- **Exception Handling:** `@RestControllerAdvice` catches exceptions from all layers.
- **Soft Delete:** `BaseEntity` `@MappedSuperclass` provides `deleted` flag; `@SQLRestriction("deleted = false")` on entities.

The architecture is **not** hexagonal — there are no ports/adapters. DTOs are used minimally (only for create input). Entities are returned directly in responses (no response DTOs).

---

## FOLDER_STRUCTURE

```
.
├── pom.xml                              # Maven POM (single module, Spring Boot 4.0.6, Java 21)
├── README.md                            # Project README, team workflow, MCD diagram
├── description.md                       # Functional concept document (in French)
├── 24014.md                             # Task spec for Member & Borrow (matricule 24014)
├── 24068.md                             # Task spec for Security, Exceptions (leader)
├── 24139.md                             # Task spec for Reference Entities (Category, Author, Publisher)
├── 24157.md                             # Task spec for Reservation & Queue (matricule 24157)
├── 24238.md                             # Task spec for Book, BookItem, BookAuthor (matricule 24238)
├── mvnw / mvnw.cmd                      # Maven wrapper scripts
└── src/
    ├── main/
    │   ├── java/supnum/projet/Library/
    │   │   ├── LibraryApplication.java          # @SpringBootApplication entry point
    │   │   ├── data/
    │   │   │   └── entities/
    │   │   │       ├── BaseEntity.java           # Abstract @MappedSuperclass (soft delete)
    │   │   │       └── enums/
    │   │   │           └── ReservationStatus.java # PENDING, READY, CANCELLED, COMPLETED
    │   │   ├── exceptions/
    │   │   │   ├── GlobalExceptionHandler.java    # @RestControllerAdvice (3 handlers)
    │   │   │   └── ResourceNotFoundException.java  # Custom RuntimeException
    │   │   └── security/
    │   │       └── SecurityConfig.java            # Spring Security (in-memory users, Basic Auth)
    │   └── resources/
    │       ├── application.properties             # DB, JPA, server config
    │       └── schema.sql                         # DDL for all 10 tables
    └── test/
        └── java/supnum/projet/Library/
            └── LibraryApplicationTests.java       # Context load smoke test
```

**Planned packages (specified in .md task files, NOT yet created on disk):**
- `data/entities/` — Language, Nationality, Category, Publisher, Author, Book, BookItem, BookAuthor, Member, Borrow, Reservation
- `data/entities/enums/` — BookItemStatus, AuthorRole, MemberType, BorrowStatus
- `data/repositories/` — 10 repository interfaces
- `dto/` — CategoryDTO, AuthorDTO, BookDTO, MemberDTO, ReservationDTO
- `services/` — CategoryService, BookService, BorrowService, ReservationService
- `controllers/` — CategoryController, BookController, BorrowController, ReservationController

---

## SYSTEM_FLOW & UI_FLOW

### Data Flow (Request → Response)

```
Client (HTTP)
  │
  │ POST/GET/PUT/DELETE at /api/*
  │ With Basic Auth header (username:password)
  ▼
SecurityFilterChain
  │
  ├── /api/public/** → permitAll (no auth)
  ├── /api/admin/**  → hasRole("ADMIN")
  └── anyRequest()   → authenticated
  │
  ▼
DispatcherServlet → Controller
  │
  ├── @Valid validates request body → if fails, MethodArgumentNotValidException → GlobalExceptionHandler returns 400
  │
  ▼
Service (with @Transactional)
  │
  ├── Business rules applied
  ├── Calls Repository methods
  │
  ▼
Repository (JPA)
  │
  ├── Hibernate generates SQL
  ├── @SQLRestriction("deleted = false") filters soft-deleted rows
  │
  ▼
MySQL Database (library_db)
  │
  └── Response flows back through the layers
```

### Existing User Journeys (from schema + planned code)

**As-is (only infrastructure exists):**
1. No actual user journeys exist — controllers, services, entities (except BaseEntity) are not yet created.

**Planned journeys (from .md specs):**

1. **Category CRUD:** `GET /api/categories` → list all → `POST /api/categories` → create → `DELETE /api/categories/{id}` → soft-delete
2. **Book CRUD:** `GET /api/books` → list all → `POST /api/books` → create (resolves Language, Category, Publisher references)
3. **Borrow flow:** `POST /api/borrows/checkout?memberId=&barcode=` → borrow a book (checks availability, member quota) → `POST /api/borrows/{id}/return` → return → `POST /api/borrows/{id}/renew` → renew (max 3)
4. **Reservation flow:** `POST /api/reservations` → reserve (FIFO queue) → `POST /api/reservations/{id}/cancel` → cancel → `GET /api/reservations/queue/{bookId}` → view queue

---

## STATE_MANAGEMENT

- **Backend:** Entirely **stateless REST**. No session state on the server.
- **Authentication:** HTTP Basic Auth → each request carries credentials; `InMemoryUserDetailsManager` stores users in memory (no persistence, lost on restart).
- **Concurrency control:** Optimistic locking via `@Version` on `BookItem.version` field (planned, not yet implemented).
- **Soft Delete state:** Boolean `deleted` column on all soft-deletable entities; `@SQLRestriction` filters at query time.
- **No caching layer** (no Redis, no @Cacheable configured).

---

## IMPLEMENTED_FEATURES

These are the features **100% complete and functional** (files exist on disk):

1. **Spring Boot Application bootstrap** — `LibraryApplication.java` starts on port 8081.
2. **Soft Delete base class** — `BaseEntity` `@MappedSuperclass` with `deleted` boolean field.
3. **ReservationStatus enum** — `PENDING, READY, CANCELLED, COMPLETED`.
4. **Spring Security configuration** — In-memory users (`admin`/`admin123` with ADMIN role, `user`/`user123` with USER role), BCrypt password encoding, CSRF disabled, Basic Auth.
5. **Route-based authorization** — `/api/public/**` open, `/api/admin/**` ADMIN-only, all else authenticated.
6. **Custom ResourceNotFoundException** — Extends `RuntimeException`.
7. **Global exception handler** — `@RestControllerAdvice` handling `ResourceNotFoundException` (404), `MethodArgumentNotValidException` (400), generic `Exception` (500).
8. **Database schema DDL** — `schema.sql` creates all 10 tables (`language`, `nationality`, `category`, `publisher`, `author`, `book`, `book_item`, `member`, `book_author`, `reservation`, `borrow`) with foreign keys.
9. **Application properties** — MySQL datasource config (`library_db`, user `supnum`/`Supnum`, port 3306), Hibernate `ddl-auto=update`, SQL init mode `always`, pagination defaults (page size 20, max 100), server port 8081.
10. **Context load test** — Basic `@SpringBootTest` smoke test.
11. **Book Domain (task #24238)** — `Book`, `BookItem` (with `@Version` optimistic locking), `BookAuthor` (with `@EmbeddedId`) entities; `BookItemStatus` and `AuthorRole` enums; `BookRepository`, `BookItemRepository`; `BookDTO` (`@Valid`); `BookService` (`findAll`, `create` with ISBN-duplicate check and FK resolution); `BookController` exposing `GET /api/books` and `POST /api/books`. Production code + tests compile cleanly via `./mvnw clean test-compile`; the existing `LibraryApplicationTests.contextLoads` smoke test requires a running local MySQL (pre-existing environmental dependency, unchanged by this task).

---

## ORPHANS & PENDING

The following features are **documented in task files (.md)** but **NOT implemented** (no Java files on disk):

### Completely missing (no files exist):

| Domain | Files Missing | Count |
|--------|--------------|-------|
| **Reference Entities** | `Nationality.java` (STUB on disk: `Language.java`, `Category.java`, `Publisher.java`, `Author.java` — minimal `@Entity` + `@Id` only, awaiting task #24139) | 1 |
| **Member Domain** | `Member.java` | 1 |
| **Transaction Entities** | `Borrow.java`, `Reservation.java` | 2 |
| **Enums** | `MemberType.java`, `BorrowStatus.java` | 2 |
| **Repositories** | `NationalityRepository`, `AuthorRepository`, `MemberRepository`, `BorrowRepository`, `ReservationRepository` (STUB on disk: `LanguageRepository`, `CategoryRepository`, `PublisherRepository` — empty `JpaRepository` extensions to satisfy `BookService` DI) | 5 |
| **DTOs** | `CategoryDTO.java`, `AuthorDTO.java`, `MemberDTO.java`, `ReservationDTO.java` | 4 |
| **Services** | `CategoryService.java`, `BorrowService.java`, `ReservationService.java` | 3 |
| **Controllers** | `CategoryController.java`, `BorrowController.java`, `ReservationController.java` | 3 |

**Total pending files: 21 Java source files** (down from 39 — task #24238 contributed 10 files; 8 additional files placed as minimal stubs for FK targets).

### Dead code / incomplete references:
- `schema.sql` references all tables — but the corresponding JPA `@Entity` classes are missing, so Hibernate `ddl-auto=update` cannot generate/validate them.
- `SecurityConfig.java` defines `/api/public/**` and `/api/admin/**` route rules — but no controllers or endpoints exist behind these paths yet.

### Missing business logic (identified in specs but not in code):
- `Author` and `Publisher` services/controllers (spec says "develop on the same model" but no code provided).
- `BookItem.status` transitions (AVAILABLE → BORROWED → AVAILABLE) are referenced by BorrowService but BookItem entity is not yet on disk.
- No reservation-to-borrow handoff logic (when a reserved book becomes available, auto-assign to queue position #1).
- No overdue detection logic (no `dueDate` or `returnDate` fields in any schema or entity).
- `BookService` has no `update` or `delete` endpoints (only `findAll` and `create`).

---

## MODULE_DEPENDENCIES

The dependency graph (as specified in the task .md files):

```
Category ──┐
Publisher ─┼──► Book ──┐
Language ──┘            │
                  Book ──┼──► BookItem
                  Author─┤
                        │
                        ├──► BookAuthor (join table, N-N with role)
                        │
Member ──► Borrow ──► BookItem
Member ──► Reservation ──► Book
```

Key dependency rules:
- **Book** depends on Language, Category, Publisher (all `@ManyToOne`).
- **BookItem** depends on Book (`@ManyToOne`).
- **BookAuthor** joins Book and Author with an `@EmbeddedId` and a `role` attribute.
- **Borrow** depends on Member and BookItem (`@ManyToOne` each).
- **Reservation** depends on Member and Book (`@ManyToOne` each).
- **Author** depends on Nationality (`@ManyToOne`).
- Category, Publisher, Language, Nationality are standalone reference tables (no foreign keys pointing to them).

**Circular dependency risk:** Not present — all edges are unidirectional.

---

## SECURITY & RISKS

### Security-sensitive areas

| Issue | Severity | Details |
|-------|----------|---------|
| **In-memory users** | HIGH | `InMemoryUserDetailsManager` — users are hardcoded and lost on restart. No user persistence, registration, or password recovery. |
| **Hardcoded credentials** | MEDIUM | `admin`/`admin123` and `user`/`user123` are in plaintext source. Anyone with repo access sees them. |
| **CSRF disabled** | HIGH | `csrf.disable()` — no CSRF protection. Acceptable for REST API with Basic Auth, but risky if a browser client is added. |
| **HTTP Basic Auth only** | MEDIUM | No JWT, OAuth2, or token-based auth. Credentials sent in every request (base64 encoded, not encrypted without HTTPS). |
| **Entities as response objects** | HIGH | `Category`, `Book`, `Borrow`, etc. are returned directly from controllers. No response DTOs. This exposes internal field structure and can leak `deleted` flag, `version`, internal IDs. |
| **Password stored in source** | LOW | `application.properties` contains `spring.datasource.password=Supnum` in plaintext. |

### Code design risks

| Risk | Type | Details |
|------|------|---------|
| **N+1 query problem** | Performance | All `@ManyToOne` relationships use `FetchType.LAZY` (correct), but `findAll()` in services will trigger N+1 if lazy fields are accessed during serialization. No `@EntityGraph` or `JOIN FETCH` used anywhere. |
| **No pagination on list endpoints** | Performance | `BookController.getAll()` and `CategoryController.getAll()` return `List<T>` with no pagination. With thousands of records, this will cause OOM or slow responses. (Global pageable default is set in properties but unused.) |
| **RuntimeException for business rules** | Design | Business rule violations (quota exceeded, ISBN duplicate, non-available item) throw plain `RuntimeException` — caught by `GlobalExceptionHandler` as 500. These should be domain-specific exceptions mapping to 409 Conflict or 400 Bad Request. |
| **`@Transactional` on whole service classes** | Design | All services annotated with `@Transactional` at class level. Too broad; should be at method level for read-only operations (`@Transactional(readOnly = true)`). |
| **Missing `dueDate`/`returnDate`** | Domain | The `borrow` table has no temporal columns. Overdue detection and renewal limits cannot be enforced without dates. |
| **No uniqueness validation at DB level on `Member.email`** | Data | Schema has `UNIQUE` on email, but JPA entity does not handle `DataIntegrityViolationException` — a duplicate email will cause an unhandled 500. |
| **Missing response DTOs** | Maintainability | Entities are exposed directly in REST responses. Any schema change leaks to the API contract. |
| **Optimistic locking not wired into service** | Concurrency | `@Version` is declared on `BookItem` (planned), but `BorrowService.borrowBook()` does not handle `OptimisticLockException`. Concurrent requests could create double-borrows. |

### Risk mitigations in place
- `ResourceNotFoundException` properly returns 404.
- `MethodArgumentNotValidException` handled for `@Valid` failures → 400 with field-level error map.
- Soft delete with `@SQLRestriction` prevents accidental exposure of deleted records.
- BCrypt password encoding for in-memory users (passwords are not stored in plaintext in the security layer, though they are in source code as encoded strings — the raw `admin123`/`user123` are what's passed through the encoder).
- **Test-environment MySQL dependency — MITIGATED.** `LibraryApplicationTests` previously required a running local MySQL with the production `supnum`/`Supnum` credentials, causing `mvn test` to fail on any clean checkout. Resolution: H2 added as a `test`-scoped dependency in `pom.xml`; `src/test/resources/application-test.properties` overrides only the datasource and JPA dialect to use an in-memory H2 database (MODE=MySQL) with `ddl-auto=create-drop` and `spring.sql.init.mode=never` (since the production `schema.sql` uses MySQL-only DDL); `LibraryApplicationTests` carries `@ActiveProfiles("test")` to bind the profile. Production `application.properties` and `schema.sql` are unchanged, so deployments still target MySQL.

---

## SUMMARY

**Completion status:** ~30% (infrastructure layer in place; Book Domain (task #24238) complete; reference entities Language/Category/Publisher/Author exist as compile-only stubs awaiting task #24139; Member/Borrow/Reservation domains still missing).

**Next critical actions:**
1. Task #24139 — flesh out reference entities (Nationality, Language, Category, Publisher, Author) with full fields, repositories, DTOs, services, controllers.
2. Task #24014 — Member & Borrow domain.
3. Task #24157 — Reservation & queue domain.
4. Add `Pageable` support to `BookController.getAll()` (currently returns full `List<Book>`).
5. Fix N+1 risks by adding `@EntityGraph` or `JOIN FETCH` queries on Book lookups.
6. Migrate from in-memory auth to persistent user storage.
7. Add response DTOs to prevent entity exposure (currently `Book` is returned directly).
8. Replace business-rule `RuntimeException` (ISBN duplicate) in `BookService.create` with a domain exception mapped to 409 Conflict.
