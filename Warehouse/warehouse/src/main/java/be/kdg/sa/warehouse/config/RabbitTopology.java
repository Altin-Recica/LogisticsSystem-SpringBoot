package be.kdg.sa.warehouse.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopology {

    public static final String TOPIC_EXCHANGE = "demo-exchange";

    public static final String TOPIC_QUEUE_TICKET = "ticket-queue";


    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }


    @Bean
    public Queue topicQueueTicket(){
        return new Queue(TOPIC_QUEUE_TICKET,false);
    }



    @Bean
    public Binding topicTicketBinding(TopicExchange topicExchange, Queue topicQueueTicket){
        return BindingBuilder.bind(topicQueueTicket).to(topicExchange).with("be.weighbridgeticket");
    }
}
