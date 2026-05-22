# Journal des Modifications

Tous les changements notables de ce projet seront documentés dans ce fichier.

Le format est basé sur [Keep a Changelog](https://keepachangelog.com/fr/1.1.0/),
et ce projet suit [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-05-22

### Ajouté
- Première version du Système de Gestion de Bibliothèque
- API REST pour gérer les livres, membres, emprunts et réservations
- Intégration Spring Security
- Suppression logicielle (soft-delete)
- File d'attente FIFO pour les réservations
- Quotas et limites d'emprunt
- Verrouillage optimiste pour BookItem
