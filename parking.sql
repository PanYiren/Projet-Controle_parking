-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : ven. 15 avr. 2022 à 16:41
-- Version du serveur :  5.7.31
-- Version de PHP : 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `parking`
--

-- --------------------------------------------------------

--
-- Structure de la table `captureimmatriculation`
--

DROP TABLE IF EXISTS `captureimmatriculation`;
CREATE TABLE IF NOT EXISTS `captureimmatriculation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idImma` varchar(10) NOT NULL,
  `TimeSpamp` timestamp NOT NULL,
  `lien` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idImma` (`idImma`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `liste`
--

DROP TABLE IF EXISTS `liste`;
CREATE TABLE IF NOT EXISTS `liste` (
  `idImma` varchar(10) NOT NULL,
  `IsResident` tinyint(1) NOT NULL,
  `IsVisiteur` tinyint(1) NOT NULL,
  PRIMARY KEY (`idImma`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `liste`
--

INSERT INTO `liste` (`idImma`, `IsResident`, `IsVisiteur`) VALUES
('AA-564-JI', 1, 0),
('FJ-225-QS', 1, 0),
('FM-598-DD', 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `login` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `IsGardien` tinyint(1) NOT NULL,
  `IsPolice` tinyint(1) NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`login`, `password`, `IsGardien`, `IsPolice`) VALUES
('gardien', 'gardien', 1, 0),
('police', 'police', 0, 1);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `captureimmatriculation`
--
ALTER TABLE `captureimmatriculation`
  ADD CONSTRAINT `captureimmatriculation_ibfk_1` FOREIGN KEY (`idImma`) REFERENCES `liste` (`idImma`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
