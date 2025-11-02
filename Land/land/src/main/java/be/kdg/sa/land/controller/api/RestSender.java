package be.kdg.sa.land.controller.api;

import be.kdg.sa.land.config.RabbitTopology;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestSender {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public RestSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendTicket(long clientId, String materialType, String licencePlate, double weight, LocalDateTime timestamp) throws JsonProcessingException {
        Map<String, Object> ticketMap = new HashMap<>();
        ticketMap.put("clientId", clientId);
        ticketMap.put("licencePlate", licencePlate);
        ticketMap.put("type", materialType);
        ticketMap.put("weight", weight);
        ticketMap.put("timestamp", timestamp);

        String ticketJson = objectMapper.writeValueAsString(ticketMap);

        rabbitTemplate.convertAndSend(RabbitTopology.TOPIC_EXCHANGE, "be.weighbridgeticket", ticketJson);
    }

}

