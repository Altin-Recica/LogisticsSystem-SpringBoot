package be.kdg.sa.land.service;

import be.kdg.sa.land.domain.WeighbridgeTicket;
import be.kdg.sa.land.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Optional<WeighbridgeTicket> createTicket(String licencePlate, double tareWeight, double grossWeight, String type, long clientId) {
        try {
            if (tareWeight >= grossWeight) {
                logger.error("Invalid weights: tareWeight {} must be less than grossWeight {}", tareWeight, grossWeight);
                throw new IllegalArgumentException("Tare weight must be less than gross weight");
            }

            double netWeight = grossWeight - tareWeight;
            LocalDateTime timestamp = LocalDateTime.now();
            WeighbridgeTicket ticket = new WeighbridgeTicket(tareWeight, grossWeight, netWeight, licencePlate, timestamp, type, clientId);
            logger.info("Creating ticket: {}", ticket);
            ticketRepository.save(ticket);
            return Optional.of(ticket);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating ticket for licence plate {}: {}", licencePlate, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating ticket for licence plate {}: {}", licencePlate, e.getMessage());
            throw new RuntimeException("Failed to create ticket", e);
        }
    }

    public Collection<WeighbridgeTicket> getAllTickets() {
        try {
            Collection<WeighbridgeTicket> tickets = ticketRepository.findAll();
            logger.info("Retrieved {} tickets", tickets.size());
            return tickets;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all tickets: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve tickets", e);
        }
    }

}
