//package ms_common.messaging.config;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rabbitmq.client.AMQP;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.DefaultConsumer;
//import com.rabbitmq.client.Envelope;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import ms_common.messaging.config.RabbitConstants;
//import ms_common.messaging.dto.MessageDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//import java.util.concurrent.Callable;
//
//@Slf4j
//public class MessageDTOConsumer extends DefaultConsumer {
//
//    private  ObjectMapper objectMapper;
//    private Command command;
//    private Channel channel;
//
//    public MessageDTOConsumer(Channel channel, Command command, ObjectMapper objectMapper) throws IOException {
//        super(channel);
//        this.objectMapper =objectMapper;
//        this.command = command;
//        this.channel = channel;
//    }
//
////    @Override
////    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
////
////        MessageDTO messageDTO = objectMapper.readValue(body, new TypeReference<MessageDTO>() {
////        });
////        try {
////            if(messageDTO != null){
////
////                String translatedMessage = command.execute(messageDTO.getLanguage(), messageDTO.getText());
////
////                Publisher publisher = new Publisher(channel,objectMapper);
////                MessageDTO result = new MessageDTO();
////                result.setText(translatedMessage);
////
////                publisher.publishMessage(result,RabbitConstants.REPLY_QUEUE_NAME);
////            }
////        }catch (Exception e){
////            log.debug("Consumer exc: {}",e.getMessage());
////        }
////
////    }
//
//    public void startOnQueue(String queueName) throws IOException {
//        String tag = channel.basicConsume(queueName, true, this);
//    }
//
//    public interface Command {
//        String execute(String s, String b) throws IOException;
//    }
//
//}
