# Logistiek Systeem

Dit project is een logistiek platform, genaamd `Mineral Flow`, ontwikkeld voor de Krystal distributie Groep (KdG). Het doel is om hun verouderde systeem voor de distributie van mineralen (zoals gips, ijzererts en cement) te moderniseren. De applicatie optimaliseert de volledige materiaalstroom, van de planning en aankomst van vrachtwagens (landzijde) tot de opslag in magazijnen en het laden van schepen voor kopers (waterzijde). Het systeem berekent opslagkosten voor leveranciers en commissies op verkopen, en beheert complexe logistieke processen zoals afspraken, wachtrijen en inspecties.

## Kernfunctionaliteiten

- **Magazijn Systeem:** Beheert voorraadniveaus, opslagcapaciteit en berekent de dagelijkse facturen (opslagkosten en commissies).

- **Landzijde Systeem:** Verwerkt de aankomst van vrachtwagens via een afsprakensysteem en een FIFO-wachtrij. Registreert wegingen en beheert de levering aan magazijnen. Inclusief een webinterface.

- **Waterzijde Systeem:** Coördineert het laden van schepen op basis van aankooporders (POs). Plant en volgt verplichte operaties zoals inspecties (IO) en bunkeren (BO).

## Technische Architectuur

- **Backend:** Java Spring Boot
- **Database:** PostgreSQL (met aparte schema's per service: `Land`, `Water`, `Warehouse`)
- **Messaging:** RabbitMQ (voor asynchrone communicatie tussen services)
- **Security:** Keycloak (voor authenticatie en autorisatie van de REST endpoints)
- **Frontend (Landzijde):** Spring MVC met Thymeleaf
- **Infrastructuur:** Docker & Docker Compose

## Auteur

**Altin Recica** – Studieproject KDG Hogeschool
