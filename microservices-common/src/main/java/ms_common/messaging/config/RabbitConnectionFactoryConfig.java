package ms_common.messaging.config;

import com.rabbitmq.client.*;
import ms_common.messaging.config.RabbitConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Configuration
public class RabbitConnectionFactoryConfig {


    @Bean
    public Connection connectionFactory() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitConstants.HOST);
        return factory.newConnection();
    }
}
