# Journal des Modifications

Tous les changements notables de ce projet seront documentés dans ce fichier.

Le format est basé sur [Keep a Changelog](https://keepachangelog.com/fr/1.1.0/),
et ce projet suit [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2026-05-28

### Corrigé
- **DTOs de réponse** : les contrôleurs retournent des DTOs (BookResponse, UserResponse, etc.) au lieu des entités JPA → plus de LazyInitializationException, `User.password` n'est plus exposé
- **Exceptions métier** : `DuplicateResourceException` (409 Conflict) et `BusinessException` (400 Bad Request) remplacent les `RuntimeException` génériques
- **Pagination** : `Pageable` intégré sur tous les endpoints findAll
- **Statut HTTP 201 Created** pour toutes les créations (POST)
- **Messages en français** : uniformisation de tous les messages (validation, erreurs, réponses)
- **Builder pattern** : `@Builder` Lombok sur toutes les entités, plus de `new Entity()` + setters dans les services

### Ajouté
- Package `dto/response/` avec 7 DTOs de réponse
- `GlobalExceptionHandler` enrichi avec gestion de `DuplicateResourceException` et `BusinessException`
- `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` Lombok sur 12 entités

## [1.0.0] - 2026-05-22

### Ajouté
- Première version du Système de Gestion de Bibliothèque
- API REST pour gérer les livres, membres, emprunts et réservations
- Intégration Spring Security (JWT)
- Suppression logicielle (soft-delete)
- File d'attente FIFO pour les réservations
- Quotas et limites d'emprunt
- Verrouillage optimiste pour BookItem
