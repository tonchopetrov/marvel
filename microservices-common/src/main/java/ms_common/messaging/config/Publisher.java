package ms_common.messaging.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import ms_common.messaging.dto.MessageDTO;

import java.io.IOException;


//public class Publisher {

//    private Channel channel;
//    private ObjectMapper objectMapper;
//
//    public Publisher(Channel channel, ObjectMapper objectMapper) {
//        this.channel = channel;
//        this.objectMapper = objectMapper;
//    }
//
//     public void publishMessage(MessageDTO dto, String queueName) throws IOException {
//
//         this. channel.basicPublish("", queueName, null, objectMapper.writeValueAsBytes(dto));
//     }
//}
