package be.kdg.sa.land.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitTopology {
    public static final String TOPIC_EXCHANGE = "demo-exchange";
    public static final String TOPIC_WEIGHING_EVENT = "be.weighing.event";

    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Queue topicQueueWeighing(){
        return new Queue(TOPIC_WEIGHING_EVENT,false);
    }



    @Bean
    public Binding topicTicketBinding(TopicExchange topicExchange, Queue topicQueueWeighing){
        return BindingBuilder.bind(topicQueueWeighing).to(topicExchange).with("be.weighing.event");
    }
}
