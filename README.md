# Logistics System

This project is a logistics platform called `Mineral Flow`, developed for the Krystal Distribution Group (KdG). The goal is to modernize their outdated system for distributing minerals (such as gypsum, iron ore, and cement). The application optimizes the entire material flow, from planning and truck arrivals (land side) to warehouse storage and loading ships for buyers (water side). The system calculates storage costs for suppliers and sales commissions, and manages complex logistics processes such as appointments, queues, and inspections.

## Key Features

- **Warehouse System:** Manages inventory levels, storage capacity, and calculates daily invoices (storage costs and commissions).

- **Land Side System:** Processes truck arrivals via an appointment system and a FIFO queue. Records weighings and manages deliveries to warehouses. Includes a web interface.

- **Water Side System:** Coordinates ship loading based on purchase orders (POs). Plans and tracks mandatory operations such as inspections (IO) and bunkering (BO).

## Technical Architecture

- **Backend:** Java Spring Boot
- **Database:** PostgreSQL (with separate schemas per service: `Land`, `Water`, `Warehouse`)
- **Messaging:** RabbitMQ (for asynchronous communication between services)
- **Security:** Keycloak (for authentication and authorization of REST endpoints)
- **Frontend (Land Side):** Spring MVC with Thymeleaf
- **Infrastructure:** Docker & Docker Compose

## Author

**Altin Recica** – Student Project, KDG University of Applied Sciences
