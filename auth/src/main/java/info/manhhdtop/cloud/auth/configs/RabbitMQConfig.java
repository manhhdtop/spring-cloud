package info.manhhdtop.cloud.auth.configs;

import info.manhhdtop.cloud.common.messaging.constants.RabbitMQConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(RabbitMQConstant.User.EXCHANGE);
    }

    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange(RabbitMQConstant.Role.EXCHANGE);
    }

    @Bean
    public Queue userSyncQueue() {
        return QueueBuilder.durable(RabbitMQConstant.User.SYNC_QUEUE).build();
    }

    @Bean
    public Queue userCreatedQueue() {
        return QueueBuilder.durable(RabbitMQConstant.User.CREATED_QUEUE).build();
    }

    @Bean
    public Queue userUpdatedQueue() {
        return QueueBuilder.durable(RabbitMQConstant.User.UPDATED_QUEUE).build();
    }

    @Bean
    public Queue userLockedQueue() {
        return QueueBuilder.durable(RabbitMQConstant.User.LOCKED_QUEUE).build();
    }

    @Bean
    public Queue userUnlockedQueue() {
        return QueueBuilder.durable(RabbitMQConstant.User.UNLOCKED_QUEUE).build();
    }

    @Bean
    public Queue userDeletedQueue() {
        return QueueBuilder.durable(RabbitMQConstant.User.DELETED_QUEUE).build();
    }

    @Bean
    public Queue userStatusChangedQueue() {
        return QueueBuilder.durable(RabbitMQConstant.User.STATUS_CHANGED_QUEUE).build();
    }

    @Bean
    public Binding userSyncBinding() {
        return BindingBuilder
                .bind(userSyncQueue())
                .to(userExchange())
                .with(RabbitMQConstant.User.SYNC_ROUTING_KEY);
    }

    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder
                .bind(userCreatedQueue())
                .to(userExchange())
                .with(RabbitMQConstant.User.CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding userUpdatedBinding() {
        return BindingBuilder
                .bind(userUpdatedQueue())
                .to(userExchange())
                .with(RabbitMQConstant.User.UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding userLockedBinding() {
        return BindingBuilder
                .bind(userLockedQueue())
                .to(userExchange())
                .with(RabbitMQConstant.User.LOCKED_ROUTING_KEY);
    }

    @Bean
    public Binding userUnlockedBinding() {
        return BindingBuilder
                .bind(userUnlockedQueue())
                .to(userExchange())
                .with(RabbitMQConstant.User.UNLOCKED_ROUTING_KEY);
    }

    @Bean
    public Binding userDeletedBinding() {
        return BindingBuilder
                .bind(userDeletedQueue())
                .to(userExchange())
                .with(RabbitMQConstant.User.DELETED_ROUTING_KEY);
    }

    @Bean
    public Binding userStatusChangedBinding() {
        return BindingBuilder
                .bind(userStatusChangedQueue())
                .to(userExchange())
                .with(RabbitMQConstant.User.STATUS_CHANGED_ROUTING_KEY);
    }

    // Role queues and bindings
    @Bean
    public Queue roleCreateRequestQueue() {
        return QueueBuilder.durable(RabbitMQConstant.Role.CREATE_REQUEST_QUEUE).build();
    }

    @Bean
    public Binding roleCreateRequestBinding() {
        return BindingBuilder
                .bind(roleCreateRequestQueue())
                .to(authExchange())
                .with(RabbitMQConstant.Role.CREATE_REQUEST_ROUTING_KEY);
    }

    // Permission queues and bindings
    @Bean
    public Queue permissionCreateRequestQueue() {
        return QueueBuilder.durable(RabbitMQConstant.Permission.CREATE_REQUEST_QUEUE).build();
    }

    @Bean
    public Binding permissionCreateRequestBinding() {
        return BindingBuilder
                .bind(permissionCreateRequestQueue())
                .to(authExchange())
                .with(RabbitMQConstant.Permission.CREATE_REQUEST_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}

