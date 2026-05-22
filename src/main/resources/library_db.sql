-- phpMyAdmin SQL Dump
-- version 5.2.2deb2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : ven. 22 mai 2026 à 17:14
-- Version du serveur : 8.4.8-0ubuntu0.25.10.1
-- Version de PHP : 8.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `library_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `author`
--

CREATE TABLE `author` (
  `id` bigint NOT NULL,
  `deleted` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `nationality_code` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `author`
--

INSERT INTO `author` (`id`, `deleted`, `name`, `nationality_code`) VALUES
(1, b'1', 'Victor Hugo', 'FR'),
(2, b'0', 'Author2', 'FR'),
(3, b'1', 'Victor Hugo', 'FR'),
(4, b'0', 'Mokhtar Ould Daddah', 'MR');

-- --------------------------------------------------------

--
-- Structure de la table `book`
--

CREATE TABLE `book` (
  `id` bigint NOT NULL,
  `deleted` bit(1) NOT NULL,
  `isbn` varchar(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `category_id` bigint DEFAULT NULL,
  `language_code` varchar(10) DEFAULT NULL,
  `publisher_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `book`
--

INSERT INTO `book` (`id`, `deleted`, `isbn`, `title`, `category_id`, `language_code`, `publisher_id`) VALUES
(1, b'1', '978-2070612758', 'Le Petit Prince (Édition spéciale)', 3, 'FR', 3),
(2, b'0', '978-2-07-036362-4', 'L\'Aventure ambiguë', 3, 'FR', 3);

-- --------------------------------------------------------

--
-- Structure de la table `book_author`
--

CREATE TABLE `book_author` (
  `role` enum('CO_AUTHOR','EDITOR','MAIN_AUTHOR') NOT NULL,
  `author_id` bigint NOT NULL,
  `book_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `book_author`
--

INSERT INTO `book_author` (`role`, `author_id`, `book_id`) VALUES
('MAIN_AUTHOR', 3, 1),
('MAIN_AUTHOR', 4, 2);

-- --------------------------------------------------------

--
-- Structure de la table `book_item`
--

CREATE TABLE `book_item` (
  `id` bigint NOT NULL,
  `deleted` bit(1) NOT NULL,
  `barcode` varchar(50) NOT NULL,
  `status` enum('AVAILABLE','BORROWED','DAMAGED','LOST','RESERVED') NOT NULL,
  `version` bigint DEFAULT NULL,
  `book_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `book_item`
--

INSERT INTO `book_item` (`id`, `deleted`, `barcode`, `status`, `version`, `book_id`) VALUES
(1, b'1', '978-2070612758-002', 'DAMAGED', 2, 1),
(2, b'0', 'MR-2024-ADV-003', 'BORROWED', 4, 1),
(3, b'0', 'MR-2024-ADV-005', 'BORROWED', 4, 2),
(4, b'1', 'MR-2024-ADV-002', 'AVAILABLE', 1, 2);

-- --------------------------------------------------------

--
-- Structure de la table `borrow`
--

CREATE TABLE `borrow` (
  `id` bigint NOT NULL,
  `renewal_count` int NOT NULL,
  `status` enum('ACTIVE','LOST','OVERDUE','RETURNED') NOT NULL,
  `book_item_id` bigint NOT NULL,
  `member_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `borrow`
--

INSERT INTO `borrow` (`id`, `renewal_count`, `status`, `book_item_id`, `member_id`) VALUES
(1, 0, 'RETURNED', 2, 2),
(2, 0, 'RETURNED', 3, 2),
(3, 1, 'ACTIVE', 2, 2),
(4, 0, 'ACTIVE', 3, 2);

-- --------------------------------------------------------

--
-- Structure de la table `category`
--

CREATE TABLE `category` (
  `id` bigint NOT NULL,
  `deleted` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `category`
--

INSERT INTO `category` (`id`, `deleted`, `name`) VALUES
(1, b'1', 'Fantasy'),
(2, b'1', 'Roman'),
(3, b'0', 'Histoire de la Mauritanie depuis 1960'),
(4, b'1', 'Histoire de la france');

-- --------------------------------------------------------

--
-- Structure de la table `language`
--

CREATE TABLE `language` (
  `code` varchar(10) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `language`
--

INSERT INTO `language` (`code`, `name`) VALUES
('AR', 'Arabe'),
('EN', 'English'),
('FR', 'Français');

-- --------------------------------------------------------

--
-- Structure de la table `member`
--

CREATE TABLE `member` (
  `id` bigint NOT NULL,
  `deleted` bit(1) NOT NULL,
  `email` varchar(100) NOT NULL,
  `max_borrows` int NOT NULL,
  `member_type` enum('EXTERNAL','STUDENT','TEACHER') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `member`
--

INSERT INTO `member` (`id`, `deleted`, `email`, `max_borrows`, `member_type`) VALUES
(1, b'1', 'nouveau@example.com', 10, 'TEACHER'),
(2, b'0', 'borrower@test.com', 5, 'STUDENT'),
(3, b'1', 'nouveauX@example.com', 10, 'TEACHER'),
(4, b'0', 'teacher@library.mr', 10, 'TEACHER');

-- --------------------------------------------------------

--
-- Structure de la table `nationality`
--

CREATE TABLE `nationality` (
  `code` varchar(10) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `nationality`
--

INSERT INTO `nationality` (`code`, `name`) VALUES
('DZ', 'Algérienne'),
('EN', 'Anglaise'),
('FR', 'Française'),
('MR', 'Mauritania');

-- --------------------------------------------------------

--
-- Structure de la table `publisher`
--

CREATE TABLE `publisher` (
  `id` bigint NOT NULL,
  `deleted` bit(1) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `publisher`
--

INSERT INTO `publisher` (`id`, `deleted`, `email`, `name`) VALUES
(1, b'1', 'c@g.fr', 'Gallimard'),
(2, b'1', 'c@folio.fr', 'Folio ED'),
(3, b'0', 'contact@daralfikr.com', 'Dar Al Fikr 1'),
(4, b'1', 'contact2@daralfikr.com', 'Dar Al Fikr 5');

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

CREATE TABLE `reservation` (
  `id` bigint NOT NULL,
  `queue_position` int NOT NULL,
  `status` enum('CANCELLED','COMPLETED','PENDING','READY') NOT NULL,
  `book_id` bigint NOT NULL,
  `member_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `reservation`
--

INSERT INTO `reservation` (`id`, `queue_position`, `status`, `book_id`, `member_id`) VALUES
(1, 1, 'CANCELLED', 1, 2),
(2, 1, 'CANCELLED', 2, 2),
(3, 1, 'CANCELLED', 2, 2),
(4, 2, 'PENDING', 2, 4);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `author`
--
ALTER TABLE `author`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhecil72yu01knf8xf7ucws7lp` (`nationality_code`);

--
-- Index pour la table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKehpdfjpu1jm3hijhj4mm0hx9h` (`isbn`),
  ADD KEY `FKhopohgrhspo3kjrqsni38g0hj` (`language_code`);

--
-- Index pour la table `book_author`
--
ALTER TABLE `book_author`
  ADD PRIMARY KEY (`author_id`,`book_id`);

--
-- Index pour la table `book_item`
--
ALTER TABLE `book_item`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKakfd0wbja0kw4fwxl14qf7h54` (`barcode`),
  ADD KEY `FK8rv4lky70oknrbd01ph1w9adw` (`book_id`);

--
-- Index pour la table `borrow`
--
ALTER TABLE `borrow`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK46ccwnsi9409t36lurvtyljak` (`name`);

--
-- Index pour la table `language`
--
ALTER TABLE `language`
  ADD PRIMARY KEY (`code`);

--
-- Index pour la table `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKmbmcqelty0fbrvxp1q58dn57t` (`email`);

--
-- Index pour la table `nationality`
--
ALTER TABLE `nationality`
  ADD PRIMARY KEY (`code`);

--
-- Index pour la table `publisher`
--
ALTER TABLE `publisher`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKh9trv4xhmh6s68vbw9ba6to70` (`name`),
  ADD UNIQUE KEY `UKtq31gshjc2w4bjif7cw51o25` (`email`);

--
-- Index pour la table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `author`
--
ALTER TABLE `author`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `book`
--
ALTER TABLE `book`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `book_item`
--
ALTER TABLE `book_item`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `borrow`
--
ALTER TABLE `borrow`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `category`
--
ALTER TABLE `category`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `member`
--
ALTER TABLE `member`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `publisher`
--
ALTER TABLE `publisher`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `author`
--
ALTER TABLE `author`
  ADD CONSTRAINT `FKhecil72yu01knf8xf7ucws7lp` FOREIGN KEY (`nationality_code`) REFERENCES `nationality` (`code`);

--
-- Contraintes pour la table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `FKhopohgrhspo3kjrqsni38g0hj` FOREIGN KEY (`language_code`) REFERENCES `language` (`code`);

--
-- Contraintes pour la table `book_item`
--
ALTER TABLE `book_item`
  ADD CONSTRAINT `FK8rv4lky70oknrbd01ph1w9adw` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
