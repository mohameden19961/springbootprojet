# Guide d'utilisation et de test de la branche `test`

Cette branche (`test`) regroupe le travail complet et intégré de tous les membres de l'équipe pour le projet de gestion de bibliothèque. Elle a été validée avec succès par des tests de compilation et d'API.

---

## 📋 Récapitulatif des fonctionnalités intégrées

1. **Sécurité et Exceptions (Membre 24068)** :
   * Configuration de Spring Security avec contrôle d'accès basé sur les rôles (Basic Auth).
   * Gestion globale des exceptions via un ControllerAdvice dédié.
2. **Tables de Référence (Membre 24139)** :
   * Entités et endpoints associés pour les tables de référence (`Language`, `Nationality`, `Category`, `Publisher`, `Author`).
3. **Livres et Exemplaires (Membre 24238)** :
   * Gestion des entités `Book`, `BookItem` (avec verrouillage optimiste `@Version` pour la concurrence) et `BookAuthor`.
   * Correction d'un bug d'infinité récursive lors de la sérialisation JSON grâce à l'ajout de `@JsonIgnore` sur la relation réciproque dans `BookItem`.
4. **Membres et Emprunts (Membre 24014)** :
   * Gestion des membres (`Member`) avec limitation du nombre maximal d'emprunts actifs.
   * Logique métier des emprunts, retours et renouvellements (limité à 3 fois maximum).
5. **Réservations et Files d'attente (Membre 24157)** :
   * Système de file d'attente FIFO (premier arrivé, premier servi) pour les réservations sur des livres indisponibles.
   * Endpoints de gestion de file d'attente et d'annulation.

---

## 🚀 Comment exécuter l'application

### Option A : Avec MySQL (Configuration par défaut)

1. Assurez-vous d'avoir un serveur MySQL en cours d'exécution sur le port `3306`.
2. Créez une base de données nommée `library_db`.
3. Configurez vos identifiants MySQL dans le fichier `src/main/resources/application.properties` :
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/library_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=VOTRE_UTILISATEUR
   spring.datasource.password=VOTRE_MOT_DE_PASSE
   ```
4. Lancez le projet :
   ```bash
   ./mvnw spring-boot:run
   ```

### Option B : Avec H2 (Base de données temporaire en mémoire pour tests rapides)

Si vous n'avez pas de base de données MySQL opérationnelle localement, vous pouvez basculer temporairement sur H2 :

1. Ajoutez la dépendance suivante dans `pom.xml` :
   ```xml
   <dependency>
       <groupId>com.h2database</groupId>
       <artifactId>h2</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```
2. Modifiez le fichier `src/main/resources/application.properties` :
   ```properties
   spring.datasource.url=jdbc:h2:mem:library_db;DB_CLOSE_DELAY=-1;MODE=MySQL
   spring.datasource.username=sa
   spring.datasource.password=
   spring.datasource.driver-class-name=org.h2.Driver
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
   ```
3. Lancez le projet :
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🧪 Comment lancer les tests d'API

### Test automatisé via Script
Un script de test automatisé nommé `test_api.sh` est disponible à la racine du projet. 
Il effectue les actions suivantes :
1. Lance l'application Spring Boot en arrière-plan (configurée sur le port `8081`).
2. Attend que l'application soit prête.
3. Exécute des requêtes `curl` avec authentification pour valider :
   * La sécurité (accès refusé sans authentification).
   * La création et la récupération de catégories.
   * La création et la liste des livres.
   * Le flux complet d'emprunt (emprunt, double emprunt refusé, renouvellement, retour).
   * Le flux complet de réservation (création de réservations en file d'attente FIFO, récupération de file d'attente, annulation).
4. Arrête le serveur proprement à la fin des tests.

Pour exécuter le script :
```bash
./test_api.sh
```

---

### Tests manuels avec `curl`

Les comptes de test configurés dans la sécurité sont :
* **Administrateur** : `admin` / `admin123` (Option curl : `-u admin:admin123`)
* **Utilisateur standard** : `user` / `user123` (Option curl : `-u user:user123`)

Voici les commandes principales pour tester manuellement chaque fonctionnalité :

#### 1. Créer une Catégorie (Admin uniquement)
```bash
curl -i -X POST http://localhost:8081/api/categories \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{"name": "Roman"}'
```

#### 2. Créer un Livre (Admin uniquement)
*Note : nécessite que la langue `FR`, la catégorie `1` et l'éditeur `1` existent (déjà configurés par défaut par le seeder).*
```bash
curl -i -X POST http://localhost:8081/api/books \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{"title": "Harry Potter", "isbn": "9780747532699", "languageCode": "FR", "categoryId": 1, "publisherId": 1}'
```

#### 3. Emprunter un exemplaire (Admin/User)
```bash
curl -i -X POST "http://localhost:8081/api/borrows/checkout?memberId=1&barcode=BC001" \
  -u admin:admin123
```

#### 4. Renouveler un emprunt
```bash
curl -i -X POST "http://localhost:8081/api/borrows/1/renew" \
  -u admin:admin123
```

#### 5. Retourner un exemplaire
```bash
curl -i -X POST "http://localhost:8081/api/borrows/1/return" \
  -u admin:admin123
```

#### 6. Réserver un livre (Création de file d'attente FIFO)
```bash
curl -i -X POST http://localhost:8081/api/reservations \
  -u user:user123 \
  -H "Content-Type: application/json" \
  -d '{"memberId": 1, "bookId": 1}'
```

#### 7. Consulter la file d'attente d'un livre
```bash
curl -i -X GET http://localhost:8081/api/reservations/queue/1 \
  -u user:user123
```

#### 8. Annuler une réservation
```bash
curl -i -X POST http://localhost:8081/api/reservations/1/cancel \
  -u user:user123
```
