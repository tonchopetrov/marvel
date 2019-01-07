//package ms_common.messaging.config;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rabbitmq.client.AMQP;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.DefaultConsumer;
//import com.rabbitmq.client.Envelope;
//import lombok.extern.slf4j.Slf4j;
//import ms_common.messaging.dto.MessageDTO;
//
//import java.io.IOException;
//
//@Slf4j
//public class SimpleConsumer {
////        extends DefaultConsumer {
//
////    private  ObjectMapper objectMapper;
////    private SimpleCommand command;
////    private Channel channel;
////
////    public SimpleConsumer(Channel channel, SimpleCommand command, ObjectMapper objectMapper) throws IOException {
////        super(channel);
////        this.objectMapper =objectMapper;
////        this.command = command;
////        this.channel = channel;
////    }
////
////    @Override
////    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
////
////        MessageDTO messageDTO = objectMapper.readValue(body, new TypeReference<MessageDTO>() {
////        });
////        try {
////            if(messageDTO != null){
////
////                command.execute(messageDTO.getText());
////
////            }
////        }catch (Exception e){
////            log.debug("Consumer exc: {}",e.getMessage());
////        }
////
////    }
////
////    public void startOnQueue(String queueName) throws IOException {
////        String tag = channel.basicConsume(queueName, true, this);
////    }
////
////    public interface SimpleCommand {
////        void execute( String b) ;
////    }
//
//}
