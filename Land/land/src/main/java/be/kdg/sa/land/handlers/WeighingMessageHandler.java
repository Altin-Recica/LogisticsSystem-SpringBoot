package be.kdg.sa.land.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static be.kdg.sa.land.config.RabbitTopology.TOPIC_WEIGHING_EVENT;

@Component
public class WeighingMessageHandler {
    private final ObjectMapper objectMapper;

    private String licencePlate;
    private double weight;
    private LocalDateTime timestamp;

    public WeighingMessageHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = TOPIC_WEIGHING_EVENT)
    public void receiveMessage(String message) throws JsonProcessingException {
        Map<String, Object> ticketData = objectMapper.readValue(message, Map.class);
        System.out.println("Received message: " + ticketData);

        handleReceivedWeighingEvent(ticketData);
    }

    private void handleReceivedWeighingEvent(Map<String, Object> ticketData) {
        this.licencePlate = (String) ticketData.get("licencePlate");
        this.weight = (double) ticketData.get("weight");
        this.timestamp = LocalDateTime.parse((String) ticketData.get("timestamp"));
    }


    public String getLicencePlate() {
        return licencePlate;
    }

    public double getWeight() {
        return weight;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


}
