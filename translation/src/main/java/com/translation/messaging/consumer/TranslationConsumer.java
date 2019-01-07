//package com.translation.messaging.consumer;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rabbitmq.client.*;
//import com.translation.service.TranslatorService;
//import ms_common.messaging.config.MessageDTOConsumer;
//import ms_common.messaging.config.RabbitConstants;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//
////@Component
//public class TranslationConsumer  {
//
//    private TranslatorService translatorService;
//    private ObjectMapper objectMapper;
//    private Channel channel;
//
//    @Autowired
//    public TranslationConsumer(TranslatorService translatorService, ObjectMapper objectMapper, Connection connection) throws IOException {
//        this.channel = connection.createChannel();
//        this.translatorService = translatorService;
//        this.objectMapper = objectMapper;
//    }
//
//    @PostConstruct
//    public void init() throws IOException {
////        MessageDTOConsumer consumer = new MessageDTOConsumer(channel,translatorService::translate,objectMapper);
////
////            try {
////                consumer.startOnQueue(RabbitConstants.REQUEST_QUEUE_NAME);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//
//
//
//    }
//
//}
