package be.kdg.sa.land.service;

import be.kdg.sa.land.domain.WeighingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static be.kdg.sa.land.config.RabbitTopology.TOPIC_EXCHANGE;

@Service
public class WeighingEventService {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public WeighingEventService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendWeighingEvent(WeighingEvent event) throws JsonProcessingException {
        Map<String, Object> weightMap = new HashMap<>();
        weightMap.put("licencePlate", event.getKenteken());
        weightMap.put("weight", event.getGewicht());
        weightMap.put("timestamp", event.getTijdstip());

        String weightJson = objectMapper.writeValueAsString(weightMap);

        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, "be.weighing.event", weightJson);
    }

}
