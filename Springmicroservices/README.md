# Architecture Microservices

Ce projet est une démonstration d'une architecture microservices basée sur Spring Boot et Spring Cloud. Il simule une application e-commerce simple avec des services pour les acheteurs, les produits et les commandes.

## Schéma de l'Architecture

Le schéma ci-dessous illustre les différents composants de l'architecture et leurs interactions.

```mermaid
graph TD
    subgraph "Client"
        U[Utilisateur / Client]
    end

    subgraph "Infrastructure"
        GW[API Gateway<br/>(gateway-service:8888)]
        DS[Discovery Service<br/>(discovery-service:8761<br/>Eureka)]
        CS[Config Server<br/>(config-service:9999)]
        K[Message Broker<br/>(Kafka)]
    end

    subgraph "Services Métier"
        AS[Acheteur Service<br/>(acheteur-service:9001)]
        PS[Produit Service<br/>(produit-service:9004)]
        CSVC[Commande Service<br/>(commande-service:9003)]
    end
    
    subgraph "Bases de Données"
        DB_A[DB Acheteur<br/>(PostgreSQL)]
        DB_P[DB Produit<br/>(PostgreSQL)]
        DB_C[DB Commande<br/>(PostgreSQL)]
    end

    U -- Requête HTTP --> GW

    GW -- Route --> AS
    GW -- Route --> PS
    GW -- Route --> CSVC

    AS -- Enregistrement & Découverte --> DS
    PS -- Enregistrement & Découverte --> DS
    CSVC -- Enregistrement & Découverte --> DS
    GW -- Découverte --> DS

    AS -- Récupère Configuration --> CS
    PS -- Récupère Configuration --> CS
    CSVC -- Récupère Configuration --> CS
    GW -- Récupère Configuration --> CS

    AS -- Appel REST Synchrone<br/>(Validation stock) --> PS
    
    AS -- Publie Événement<br/>(AchatEffectueEvent) --> K
    K -- Consomme Événement --> CSVC
    K -- Consomme Événement --> PS

    AS -- CRUD --> DB_A
    PS -- CRUD --> DB_P
    CSVC -- CRUD --> DB_C
```
## Description des Services

### Services d'Infrastructure
*   **API Gateway (`gateway-service`)**: Point d'entrée unique pour toutes les requêtes des clients. Il gère le routage dynamique vers les services métier en s'appuyant sur le Discovery Service.
*   **Discovery Service (`discovery-service`)**: Annuaire de services (Eureka) où chaque microservice s'enregistre. Cela permet une découverte dynamique des instances de service.
*   **Config Server (`config-service`)**: Centralise la gestion de la configuration pour tous les microservices. Les configurations sont stockées dans le répertoire `micro-config`.
*   **Message Broker (Kafka)**: Utilisé pour la communication asynchrone et événementielle entre les services, ce qui permet de les découpler.

### Services Métier
*   **Acheteur Service (`acheteur-service`)**: Gère les informations des acheteurs (création, lecture, mise à jour, suppression). Il initie le processus d'achat.
*   **Produit Service (`produit-service`)**: Gère le catalogue des produits, y compris les informations sur le stock.
*   **Commande Service (`commande-service`)**: Gère la création et le suivi des commandes.

## Flux de Communication

Le système utilise deux principaux modes de communication :

1.  **Communication Synchrone (REST)**: Utilisée pour les requêtes qui nécessitent une réponse immédiate. Par exemple, lorsque `acheteur-service` vérifie le stock disponible auprès du `produit-service` avant de valider un achat. Cette communication est facilitée par OpenFeign.
2.  **Communication Asynchrone (Événementielle avec Kafka)**: Utilisée pour découpler les services. Lorsqu'un achat est effectué, `acheteur-service` publie un événement `AchatEffectueEvent`. Le `commande-service` et le `produit-service` consomment cet événement pour respectivement créer une commande et décrémenter le stock.

## Démarrage

1.  **Prérequis**: Docker, Docker Compose, JDK 17, Maven.
2.  **Lancer l'infrastructure**:
    ```bash
    docker-compose up -d
    ```
    Cette commande démarre Zookeeper et Kafka.
3.  **Lancer les services**: Démarrez chaque service Spring Boot (Config, Discovery, Gateway, et les services métier) via votre IDE ou en utilisant Maven :
    ```bash
    # Pour chaque service
    mvn spring-boot:run
    ```
    **Ordre de démarrage conseillé** :
    1. `config-service`
    2. `discovery-service`
    3. `gateway-service`
    4. Les autres services (`acheteur-service`, `produit-service`, `commande-service`)

</rewritten_file> 
