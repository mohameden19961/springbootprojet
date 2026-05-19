# Système de Gestion de Bibliothèque (Backend REST API)

Bienvenue sur le projet de gestion de bibliothèque ! Ce projet consiste à développer un backend moderne avec **Java, Spring Boot et PostgreSQL/MySQL**.

Pour comprendre les concepts et règles métiers clés du projet, vous pouvez consulter la [📄 Documentation Fonctionnelle](./description.md).


## 👥 Équipe de Développement

Ce projet est découpé et organisé en tâches réparties entre 5 membres de l'équipe :
- **Matricule 24068 (Leader)** : Architecture, Sécurité (Spring Security), Gestion Globale des Exceptions.
- **Matricule 24139** : Entités de références (Category, Author, Publisher) et CRUD basique.
- **Matricule 24238** : Gestion des Livres (Book) et Exemplaires (BookItem), Optimistic Locking.
- **Matricule 24014** : Gestion des Membres (Member) et des Emprunts (Borrow), Limites métiers.
- **Matricule 24157** : Gestion des Réservations (Reservation), File d'attente, Validations complexes.

Chaque membre possède un fichier `[Matricule].md` dans ce dépôt qui détaille le code et les tâches qu'il doit accomplir.

## 📊 Modèle Conceptuel de Données (MCD)

Le diagramme suivant représente la structure de la base de données et les relations entre les tables :

```mermaid
erDiagram
    LANGUAGE {
        string code PK
        string name
    }
    NATIONALITY {
        string code PK
        string name
    }
    CATEGORY {
        bigint id PK
        string name
        boolean deleted
    }
    PUBLISHER {
        bigint id PK
        string name
        string email
        boolean deleted
    }
    AUTHOR {
        bigint id PK
        string name
        string nationality_code FK
        boolean deleted
    }
    BOOK {
        bigint id PK
        string title
        string isbn
        string language_code FK
        bigint category_id FK
        bigint publisher_id FK
        boolean deleted
    }
    BOOK_ITEM {
        bigint id PK
        string barcode
        string status
        bigint version
        boolean deleted
    }
    MEMBER {
        bigint id PK
        string email
        string member_type
        int max_borrows
        boolean deleted
    }
    BOOK_AUTHOR {
        bigint book_id PK, FK
        bigint author_id PK, FK
        string role
    }
    RESERVATION {
        bigint id PK
        bigint member_id FK
        bigint book_id FK
        int queue_position
        string status
    }
    BORROW {
        bigint id PK
        bigint member_id FK
        bigint book_item_id FK
        int renewal_count
        string status
    }

    LANGUAGE ||--o{ BOOK : "possède"
    NATIONALITY ||--o{ AUTHOR : "a"
    CATEGORY ||--o{ BOOK : "contient"
    PUBLISHER ||--o{ BOOK : "publie"
    BOOK ||--o{ BOOK_AUTHOR : "rédigé par"
    AUTHOR ||--o{ BOOK_AUTHOR : "a rédigé"
    BOOK ||--o{ BOOK_ITEM : "contient exemplaires"
    MEMBER ||--o{ BORROW : "effectue"
    BOOK_ITEM ||--o{ BORROW : "est associé à"
    MEMBER ||--o{ RESERVATION : "fait"
    BOOK ||--o{ RESERVATION : "est concerné par"
```

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

## Contributions & Workflow Git

Salut l'équipe 👋

Voici comment on va travailler ensemble sur le projet. Lisez bien avant de commencer à coder.

──────────────────────────
🔧 WORKFLOW À SUIVRE
──────────────────────────

1️⃣ Récupérer le projet
```bash
git clone <lien-du-repo>
cd <nom-du-repo>
```

2️⃣ Créer votre branche (OBLIGATOIRE)
```bash
git checkout -b feature/votre-nom
```
Exemple : `git checkout -b feature/taches-24139`

2️⃣ bis — Se mettre à jour depuis main (important !)

Faites ça régulièrement pour éviter les conflits avec le travail des autres :
```bash
git checkout main
git pull origin main
git checkout feature/votre-branche
git merge main
```

3️⃣ Coder + committer
```bash
git add .
git commit -m "Description de ce que vous avez fait"
```

4️⃣ Pusher votre branche
```bash
git push origin feature/votre-nom
```

5️⃣ Ouvrir une Pull Request sur GitHub
→ Allez sur le repo GitHub
→ Cliquez sur "Compare & pull request"
→ Décrivez ce que vous avez fait
→ Attendez mon approbation

──────────────────────────
⛔ RÈGLES IMPORTANTES
──────────────────────────

❌ Ne jamais pusher directement sur `main`
❌ Ne jamais merger vous-mêmes
✅ Toujours travailler sur votre propre branche
✅ Une PR par fonctionnalité
✅ Se synchroniser avec `main` régulièrement

Si vous avez des questions, contactez le leader.
