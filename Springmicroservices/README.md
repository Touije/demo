# Springmicroservices

## Présentation
Ce projet illustre une architecture microservices basée sur Spring Boot. Il gère un système d’achats avec trois domaines principaux : acheteurs, commandes et produits. L’architecture inclut également une gateway, un service de configuration centralisée et un service de découverte.

## Architecture globale

```
[Client]
   |
   v
[Gateway Service] <-> [Discovery Service]
   |         |         |         |
   v         v         v         v
[Acheteur] [Commande] [Produit] [Config Service]
```

- **Gateway Service** : point d’entrée unique, routage des requêtes
- **Discovery Service** : enregistrement/découverte des microservices (Eureka)
- **Config Service** : configuration centralisée (Spring Cloud Config)
- **Acheteur/Commande/Produit** : services métiers

## Description des microservices

### Acheteur Service
- **Rôle** : gestion des acheteurs et des achats
- **Endpoints principaux** :
  - `POST /acheteurs` : créer un acheteur
  - `GET /acheteurs` : lister les acheteurs
  - `GET /acheteurs/{id}` : obtenir un acheteur
  - `PUT /acheteurs/{id}` : mettre à jour un acheteur
  - `DELETE /acheteurs/{id}` : supprimer un acheteur
  - `POST /acheteurs/achat` : effectuer un achat

### Commande Service
- **Rôle** : gestion des commandes
- **Endpoints principaux** :
  - `POST /commandes` : créer une commande
  - `GET /commandes` : lister les commandes
  - `GET /commandes/{id}` : obtenir une commande
  - `DELETE /commandes/{id}` : supprimer une commande
  - `PUT /commandes/{commandeId}/statut` : changer le statut

### Produit Service
- **Rôle** : gestion des produits et du stock
- **Endpoints principaux** :
  - `POST /produits` : créer un produit
  - `GET /produits` : lister les produits
  - `GET /produits/{id}` : obtenir un produit
  - `PUT /produits/{id}` : mettre à jour un produit
  - `DELETE /produits/{id}` : supprimer un produit
  - `POST /produits/{id}/decrementer-stock?quantite=x` : décrémenter le stock

### Gateway Service
- **Rôle** : point d’entrée unique, gestion du routage

### Config Service
- **Rôle** : centralisation de la configuration (Spring Cloud Config)

### Discovery Service
- **Rôle** : découverte des services (Eureka)

## Exemple de flux métier (Achat)
1. Un client effectue un achat via `POST /acheteurs/achat`.
2. Le service acheteur crée une commande via le service commande.
3. Le service commande réserve les produits via le service produit.
4. Le stock du produit est décrémenté.
5. Les statuts sont mis à jour et les informations sont renvoyées au client.

## Démarrage du projet

### Prérequis
- Java 17+
- Maven
- Docker & Docker Compose

### Lancement
1. Cloner le dépôt
2. Se placer dans le dossier `Springmicroservices`
3. Lancer la commande :
   ```bash
   docker-compose up --build
   ```
4. Les services seront accessibles via la gateway (par défaut sur le port 8080)

## Technologies utilisées
- Spring Boot
- Spring Cloud (Config, Eureka)
- Spring Data JPA
- Docker, Docker Compose
- Maven

## Annexes
- Chaque service possède ses propres tests unitaires et d’intégration (voir dossier `src/test` de chaque microservice).
- La configuration centralisée se trouve dans le dossier `micro-config`.
