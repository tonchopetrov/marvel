package com.heroes.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heroes.service.TranslatorService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.RpcClient;
import lombok.extern.slf4j.Slf4j;
import ms_common.messaging.config.RabbitConstants;
import ms_common.messaging.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class TranslatorServiceImpl implements TranslatorService {

    private Channel channel;
    private Connection connection;
    private RpcClient rpcClient;
    private ObjectMapper objectMapper;

    @Autowired
    public TranslatorServiceImpl(Connection connection, ObjectMapper objectMapper) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String translate(String language, String text) throws IOException, TimeoutException {

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setText(text);
        messageDTO.setLanguage(language);

        channel = connection.createChannel();
        rpcClient = new RpcClient(channel, "", RabbitConstants.REQUEST_QUEUE_NAME);

        messageDTO = objectMapper.readValue(rpcClient.stringCall(objectMapper.writeValueAsString(messageDTO))
                ,new TypeReference<MessageDTO>() {});

        if(messageDTO != null && !messageDTO.getStatus().equals(MessageDTO.MessageStatus.ERROR)){
            log.debug("Translated power: {}",messageDTO.getText());
            return messageDTO.getText();
        }else {
            log.debug("Message status ERROR");
            return null;
        }
    }

    @PreDestroy
    public void stop() throws IOException, TimeoutException {
        channel.close();
        connection.close();
        rpcClient.close();
    }
}
