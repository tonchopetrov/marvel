package ms_common.messaging.config;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Configuration
public class RabbitConnectionFactoryConfig {

    @Value("${rabbitmq.hostname}")
    private String rabbithost;

    @Bean
    public Connection connectionFactory() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbithost);
        return factory.newConnection();
    }
}
