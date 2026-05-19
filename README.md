# Système de Gestion de Bibliothèque (Backend REST API)

Bienvenue sur le projet de gestion de bibliothèque ! Ce projet consiste à développer un backend moderne avec **Java, Spring Boot et PostgreSQL/MySQL**.

## 👥 Équipe de Développement

Ce projet est découpé et organisé en tâches réparties entre 5 membres de l'équipe :
- **Matricule 24068 (Leader)** : Architecture, Sécurité (Spring Security), Gestion Globale des Exceptions.
- **Matricule 24139** : Entités de références (Category, Author, Publisher) et CRUD basique.
- **Matricule 24238** : Gestion des Livres (Book) et Exemplaires (BookItem), Optimistic Locking.
- **Matricule 24014** : Gestion des Membres (Member) et des Emprunts (Borrow), Limites métiers.
- **Matricule 24157** : Gestion des Réservations (Reservation), File d'attente, Validations complexes.

Chaque membre possède un fichier `[Matricule].md` dans ce dépôt qui détaille le code et les tâches qu'il doit accomplir.

## ⚙️ Configuration de la Base de Données

Le projet est configuré pour fonctionner avec **MySQL**. 
Dans le fichier `src/main/resources/application.properties`, vous trouverez la configuration suivante :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=supnum
spring.datasource.password=Supnum
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Initialisation du schéma via schema.sql
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
```

**Détails :**
- **URL** : Se connecte sur `localhost` au port `3306`. La base `library_db` est créée automatiquement si elle n'existe pas.
- **User** : `supnum`
- **Password** : `Supnum`
- **schema.sql** : Le fichier à la racine de vos `resources` contient la plus petite définition de la DB respectant l'intégralité de votre Modèle Conceptuel de Données (MCD).

## 🚀 Lancement du projet
1. Assurez-vous d'avoir MySQL installé et qu'un utilisateur `supnum` (mdp: `Supnum`) dispose des droits sur le serveur.
2. Démarrez l'application avec Maven : `./mvnw spring-boot:run`.
3. Vérifiez les logs pour confirmer l'exécution de `schema.sql`.

Bon développement à toute l'équipe ! Lisez vos fichiers Markdown personnels pour démarrer.

## 🤝 Contributions
Pour toute contribution :

- Créer une branche feature
- Respecter l'architecture en place
- Tester les modifications
- Créer une pull request
