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

The architecture is **not** hexagonal — there are no ports/adapters. Input DTOs are used for `@Valid` create/update bodies. **Response DTOs** are now used in all 6 controllers — no entity is leaked across the REST boundary.

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
12. **Reference Entities Domain (task #24139)** — `Language`, `Nationality` (string-PK reference tables, no soft delete); `Category`, `Publisher`, `Author` (Long-PK, soft-deletable via `BaseEntity` + `@SQLRestriction`); `Author.nationality` `@ManyToOne(LAZY)` to `Nationality`. Repositories `LanguageRepository`, `NationalityRepository`, `CategoryRepository` (with `findByName`), `PublisherRepository` (with `findByName`), `AuthorRepository`. DTOs `CategoryDTO`, `AuthorDTO`, `PublisherDTO` (with `@Email` and `@Size`). Services `CategoryService`, `PublisherService`, `AuthorService` exposing `findAll`/`create`/`delete` with name-uniqueness checks and nationality FK resolution; soft-delete via `setDeleted(true)`. Controllers `CategoryController` (`/api/categories`), `PublisherController` (`/api/publishers`), `AuthorController` (`/api/authors`) — `GET`, `POST` (with `@Valid`), `DELETE /{id}`. `./mvnw test` passes against H2 with all reference entities registered.
13. **Member & Borrow Domain (task #24014)** — `MemberType` (`STUDENT`, `TEACHER`, `EXTERNAL`) and `BorrowStatus` (`ACTIVE`, `RETURNED`, `OVERDUE`, `LOST`) enums. `Member` entity (Long-PK, soft-deletable; unique email; `memberType`; `maxBorrows`). `Borrow` entity (Long-PK, **no `BaseEntity` / no soft delete**, matches schema; `@ManyToOne` to `Member` and `BookItem`; `renewalCount` default 0; `status` default `ACTIVE`; no temporal columns by spec). `MemberRepository.findByEmail`; `BorrowRepository.countByMemberAndStatus`. `MemberDTO` with `@Email`, `@NotBlank`, `@NotNull`, `@Positive` validations. `BorrowService` enforces three business rules: (1) `BookItem.status == AVAILABLE` before checkout, (2) member active-borrow count `< maxBorrows`, (3) max 3 renewals; transitions `BookItem.status` between `AVAILABLE` and `BORROWED` on checkout/return. `BorrowController` exposes `POST /api/borrows/checkout?memberId=&barcode=`, `POST /api/borrows/{id}/return`, `POST /api/borrows/{id}/renew`. `./mvnw test` passes against H2.
14. **Reservation & Queue Domain (task #24157)** — `Reservation` entity (Long-PK, **no `BaseEntity`** to match schema; `@ManyToOne(LAZY)` to `Member` and `Book` (parent title, not `BookItem`); `queuePosition` integer; `status` default `PENDING`). Reuses the pre-existing `ReservationStatus` enum (`PENDING`, `READY`, `CANCELLED`, `COMPLETED`). `ReservationRepository.findByBookAndStatusOrderByQueuePositionAsc` (FIFO ordering) and `findMaxQueuePositionForBook` (JPQL with `COALESCE(MAX, 0)`). `ReservationDTO` with `@NotNull` on `memberId` and `bookId`. `ReservationService` computes next queue position atomically inside `@Transactional` `reserve`; `cancel` rejects non-`PENDING` reservations; `getQueueForBook` returns ordered active queue. `ReservationController` exposes `POST /api/reservations`, `POST /api/reservations/{id}/cancel`, `GET /api/reservations/queue/{bookId}`. `./mvnw test` passes — full 11-entity context boots on H2.
15. **P0 Architecture Hardening (fix/p0-architecture-hardening)** — Three audit-driven mitigations applied surgically:
    - **N+1 fix via `@EntityGraph`:** `BookRepository.findAll()` overridden with `@EntityGraph(attributePaths = {"language","category","publisher"})`; `AuthorRepository.findAll()` with `@EntityGraph(attributePaths = {"nationality"})`; `ReservationRepository.findByBookAndStatusOrderByQueuePositionAsc(...)` with `@EntityGraph(attributePaths = {"member","book"})`. `CategoryRepository`/`PublisherRepository` left untouched (no lazy relationships on those entities); `BorrowRepository` left untouched (no list endpoint exists).
    - **Response DTOs (no entity leaking):** Six new DTOs in `dto/` — `BookResponseDTO`, `AuthorResponseDTO`, `CategoryResponseDTO`, `PublisherResponseDTO`, `BorrowResponseDTO`, `ReservationResponseDTO` — each with a static `from(Entity)` factory. All 6 controllers now return `List<*ResponseDTO>` or `ResponseEntity<*ResponseDTO>` instead of raw JPA entities; services keep their entity-returning signatures and mapping happens at the controller boundary.
    - **GlobalExceptionHandler hardening:** Added `@ExceptionHandler` for `DataIntegrityViolationException` → 409 Conflict and for `OptimisticLockingFailureException` / `jakarta.persistence.OptimisticLockException` → 409 Conflict. Response bodies are generic French messages — raw SQL / stack traces are never echoed back. The pre-existing `ResourceNotFoundException`, `MethodArgumentNotValidException`, and generic-`Exception` handlers are unchanged.
    - **Verification:** `./mvnw clean compile` green after Step 1; `./mvnw compile` green after Step 2 (55 sources, was 49); `./mvnw test` green after Step 3 (`Tests run: 1, Failures: 0, Errors: 0`).
16. **P1 Security Gating (fix/p0-architecture-hardening)** — Audit-driven RBAC + schema-drift hardening:
    - **Method security enabled:** `@EnableMethodSecurity` added to `SecurityConfig` alongside the existing `@EnableWebSecurity`. The filter-chain rule `.anyRequest().authenticated()` is retained as the fallback so any endpoint without a `@PreAuthorize` still requires authentication.
    - **Endpoint-level RBAC (`@PreAuthorize`):** All state-changing endpoints (`POST`, `DELETE`) on `BookController`, `AuthorController`, `CategoryController`, `PublisherController` are restricted to `hasRole('ADMIN')`. `BorrowController` (`/checkout`, `/{id}/return`, `/{id}/renew`) and `ReservationController` (`POST /api/reservations`, `POST /{id}/cancel`) require `hasAnyRole('ADMIN','USER')` so standard members can self-service. Read endpoints (`GET /api/books`, `GET /api/authors`, `GET /api/categories`, `GET /api/publishers`, `GET /api/reservations/queue/{bookId}`) remain just-authenticated via the fallback rule.
    - **Schema-drift fix:** `application.properties` flipped from `spring.jpa.hibernate.ddl-auto=update` to `validate`. Hibernate now strictly checks the live MySQL schema against entity mappings at boot and fails fast on drift instead of silently mutating production tables. The test profile (`application-test.properties`) keeps `create-drop` so unit tests stay portable on H2.
    - **Verification:** `./mvnw clean compile` green after Step 1; `./mvnw compile` green after Step 2; `./mvnw test` green after Step 3 (`Tests run: 1, Failures: 0, Errors: 0`).

---

## ORPHANS & PENDING

All domain modules specified across the four task files (#24238, #24139, #24014, #24157) are now implemented on disk. **The core domain layer is functionally complete.** No domain-layer Java source files remain pending.

### Spec-honored gaps (intentional, not pending):
- **No `MemberService` / `MemberController`.** Task #24014 defines `MemberDTO` but no service or controller. Members can currently only be persisted via direct repository access — there is no REST endpoint to create or list them. This is the spec's scope, not an omission. If a task later requires Member CRUD, `MemberDTO` is already ready.
- **No `BookItem` CRUD endpoint.** Task #24238 defines the `BookItem` entity and repository but neither service nor controller. New copies can only be persisted via `Book`'s cascade or direct repository access. Spec-honored.
- **No `Language` / `Nationality` CRUD endpoints.** Task #24139 defines entities and repositories only — by design, reference data is treated as seeded.

### Cross-cutting work that is out of scope for any single spec file:
- `SecurityConfig.java` defines `/api/public/**` and `/api/admin/**` route rules, but none of the implemented controllers live under those paths (all are at `/api/<resource>` and fall through to `authenticated()`). If route-level role enforcement is desired, controller mappings need to move.
- No reservation-to-borrow handoff logic — when a `BookItem` returns to `AVAILABLE`, the queue position #1 reservation is not auto-promoted to `READY`. Not in any spec; could be a future task.
- No overdue detection — no `dueDate` / `returnDate` columns in `borrow` (matches schema), so `BorrowStatus.OVERDUE` is unreachable without a schema migration.
- `BookService` exposes only `findAll` + `create`; no `update` / `delete` endpoints (per spec).

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
| **Flat role model (any authenticated user could POST/DELETE)** | RESOLVED | `@EnableMethodSecurity` is active and every write endpoint carries `@PreAuthorize`. Reference-data CRUD is ADMIN-only; Borrow and Reservation actions are ADMIN+USER. See item #16. |
| **Schema drift via `ddl-auto=update`** | RESOLVED | Production property is now `spring.jpa.hibernate.ddl-auto=validate`; Hibernate verifies and refuses to start on drift instead of silently mutating tables. The test profile overrides this to `create-drop` for portable H2 testing. |
| **In-memory users** | HIGH | `InMemoryUserDetailsManager` — users are hardcoded and lost on restart. No user persistence, registration, or password recovery. |
| **Hardcoded credentials** | MEDIUM | `admin`/`admin123` and `user`/`user123` are in plaintext source. Anyone with repo access sees them. |
| **CSRF disabled** | HIGH | `csrf.disable()` — no CSRF protection. Acceptable for REST API with Basic Auth, but risky if a browser client is added. |
| **HTTP Basic Auth only** | MEDIUM | No JWT, OAuth2, or token-based auth. Credentials sent in every request (base64 encoded, not encrypted without HTTPS). |
| **Entities as response objects** | RESOLVED | All 6 controllers now return dedicated `*ResponseDTO` types; entities no longer cross the REST boundary. See item #15 in IMPLEMENTED_FEATURES. |
| **Password stored in source** | LOW | `application.properties` contains `spring.datasource.password=Supnum` in plaintext. |

### Code design risks

| Risk | Type | Details |
|------|------|---------|
| **N+1 query problem** | RESOLVED | `BookRepository.findAll()`, `AuthorRepository.findAll()`, and `ReservationRepository.findByBookAndStatusOrderByQueuePositionAsc(...)` carry `@EntityGraph` annotations forcing eager fetch of `language`/`category`/`publisher`, `nationality`, and `member`/`book` respectively. Combined with response DTOs that read only the fields they need, lazy-load avalanches on list endpoints are eliminated. |
| **No pagination on list endpoints** | Performance | `BookController.getAll()` and `CategoryController.getAll()` return `List<T>` with no pagination. With thousands of records, this will cause OOM or slow responses. (Global pageable default is set in properties but unused.) |
| **RuntimeException for business rules** | Design | Business rule violations (quota exceeded, ISBN duplicate, non-available item) throw plain `RuntimeException` — caught by `GlobalExceptionHandler` as 500. These should be domain-specific exceptions mapping to 409 Conflict or 400 Bad Request. |
| **`@Transactional` on whole service classes** | Design | All services annotated with `@Transactional` at class level. Too broad; should be at method level for read-only operations (`@Transactional(readOnly = true)`). |
| **Missing `dueDate`/`returnDate`** | Domain | The `borrow` table has no temporal columns. Overdue detection and renewal limits cannot be enforced without dates. |
| **No uniqueness validation at DB level on `Member.email`** | Data | Schema has `UNIQUE` on email, but JPA entity does not handle `DataIntegrityViolationException` — a duplicate email will cause an unhandled 500. |
| **Missing response DTOs** | RESOLVED | Six `*ResponseDTO`s introduced; all 6 controllers translate at the boundary. |
| **Optimistic locking not surfaced to client** | RESOLVED | `OptimisticLockingFailureException` / `jakarta.persistence.OptimisticLockException` now map to 409 Conflict in `GlobalExceptionHandler` with a safe generic message. `BorrowService` still does not pre-empt the race (no retry loop), but a concurrent double-borrow now produces a clean 409 instead of a 500 with a stack trace. |

### Risk mitigations in place
- `ResourceNotFoundException` properly returns 404.
- `MethodArgumentNotValidException` handled for `@Valid` failures → 400 with field-level error map.
- Soft delete with `@SQLRestriction` prevents accidental exposure of deleted records.
- BCrypt password encoding for in-memory users (passwords are not stored in plaintext in the security layer, though they are in source code as encoded strings — the raw `admin123`/`user123` are what's passed through the encoder).
- **Test-environment MySQL dependency — MITIGATED.** `LibraryApplicationTests` previously required a running local MySQL with the production `supnum`/`Supnum` credentials, causing `mvn test` to fail on any clean checkout. Resolution: H2 added as a `test`-scoped dependency in `pom.xml`; `src/test/resources/application-test.properties` overrides only the datasource and JPA dialect to use an in-memory H2 database (MODE=MySQL) with `ddl-auto=create-drop` and `spring.sql.init.mode=never` (since the production `schema.sql` uses MySQL-only DDL); `LibraryApplicationTests` carries `@ActiveProfiles("test")` to bind the profile. Production `application.properties` and `schema.sql` are unchanged, so deployments still target MySQL.

---

## SUMMARY

**Completion status:** ~100% of the specified domain layer. All four task files (#24238, #24139, #24014, #24157) are implemented and verified. `./mvnw test` is green against H2.

**Project totals on disk:**
- 11 JPA entities (`BaseEntity` + 10 concrete: `Language`, `Nationality`, `Category`, `Publisher`, `Author`, `Book`, `BookItem`, `BookAuthor`, `Member`, `Borrow`, `Reservation`)
- 7 enums (`ReservationStatus`, `BookItemStatus`, `AuthorRole`, `MemberType`, `BorrowStatus`)
- 10 repositories
- 6 input DTOs (`BookDTO`, `CategoryDTO`, `AuthorDTO`, `PublisherDTO`, `MemberDTO`, `ReservationDTO`) — note `MemberDTO` is unused at the controller layer (spec-honored gap)
- 6 response DTOs (`BookResponseDTO`, `CategoryResponseDTO`, `AuthorResponseDTO`, `PublisherResponseDTO`, `BorrowResponseDTO`, `ReservationResponseDTO`)
- 6 services (`BookService`, `CategoryService`, `AuthorService`, `PublisherService`, `BorrowService`, `ReservationService`)
- 6 controllers (`BookController`, `CategoryController`, `AuthorController`, `PublisherController`, `BorrowController`, `ReservationController`)

**Recommended next actions (outside the spec'd task scope, all noted earlier in this map):**
1. `Pageable` on all `GET /api/<resource>` list endpoints.
2. Member CRUD endpoints (uses the existing `MemberDTO`).
3. Replace business-rule `RuntimeException`s (ISBN duplicate, name duplicates, unavailable item, quota exceeded, renewal limit, non-PENDING cancel) with domain exceptions → 409 Conflict (the generic-`Exception` handler currently maps them to 500).
4. Migrate from in-memory auth to a persistent `UserDetailsService`.
5. Schema migration if overdue detection is needed (add `dueDate` / `returnDate` to `borrow`).
6. Reservation-to-borrow handoff (auto-promote queue position #1 to `READY` when a `BookItem` returns to `AVAILABLE`).
