package com.heroes.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heroes.service.TranslatorService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.RpcClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms_common.messaging.dto.MessageDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslatorServiceImpl implements TranslatorService {

    private Channel channel;
    private Connection connection;
    private RpcClient rpcClient;
    private ObjectMapper objectMapper;

    private final ReplyingKafkaTemplate<String,MessageDTO,MessageDTO> kafkaTemplate;


//    @Autowired
//    public TranslatorServiceImpl(Connection connection, ObjectMapper objectMapper) {
//        this.connection = connection;
//        this.objectMapper = new ObjectMapper();
//    }

    @Override
    public String translate(String language, String text) throws IOException, TimeoutException, ExecutionException, InterruptedException {

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setText(text);
        messageDTO.setLanguage(language);

        String topic1 = "t1";
        String topic2 = "t2";

        // create producer record
        ProducerRecord<String, MessageDTO> record = new ProducerRecord<String, MessageDTO>(topic1, messageDTO);
        // set reply topic in header
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, topic2.getBytes()));
        // post in kafka topic
        RequestReplyFuture<String, MessageDTO, MessageDTO> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        // confirm if producer produced successfully
        SendResult<String, MessageDTO> sendResult = sendAndReceive.getSendFuture().get();

        sendResult.getProducerRecord().headers().forEach(header ->
                System.out.println(header.key() + ":" + Arrays.toString(header.value()))
        );

        ConsumerRecord<String, MessageDTO> consumerRecord = sendAndReceive.get();

        return consumerRecord.value().getText();

//
//        channel = connection.createChannel();
//        rpcClient = new RpcClient(channel, "", BrockerConstants.REQUEST_QUEUE_NAME);
//
//        messageDTO = objectMapper.readValue(rpcClient.stringCall(objectMapper.writeValueAsString(messageDTO))
//                ,new TypeReference<MessageDTO>() {});
//
//        if(messageDTO != null && !messageDTO.getStatus().equals(MessageDTO.MessageStatus.ERROR)){
//            log.debug("Translated power: {}",messageDTO.getText());
//            return messageDTO.getText();
//        }else {
//            log.debug("Message status ERROR");
//            return null;
//        }

    }

//    @PreDestroy
    public void stop() throws IOException, TimeoutException {
        channel.close();
        connection.close();
        rpcClient.close();
    }
}
