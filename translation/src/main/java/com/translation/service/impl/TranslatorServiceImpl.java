package com.translation.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import com.rabbitmq.client.*;
import com.translation.service.TranslatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms_common.messaging.config.BrockerConstants;
import ms_common.messaging.config.kafka.Listener;
import ms_common.messaging.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class TranslatorServiceImpl implements TranslatorService {

    @Value("${google.credentials.path}")
    private String googleCredentialsPath;

    private static final String EN = "en";

    private final ObjectMapper objectMapper;
    private final Listener listener;

    private Connection connection;

//    @PostConstruct
    public void init()  {

        handleDelivery();
    }

    private void handleDelivery()  {

     try{
         StringRpcServer server = new StringRpcServer(connection.createChannel(), BrockerConstants.REQUEST_QUEUE_NAME) {
             public String handleStringCall(String request) {

                 String translatedPower = null;
                 MessageDTO messageDTO = null;
                 try {
                     messageDTO = objectMapper.readValue(request, new TypeReference<MessageDTO>() {
                     });

                     if(messageDTO != null){
                         translatedPower = translate(messageDTO.getLanguage(), messageDTO.getText());
                         if(translatedPower != null){
                             messageDTO.setText(translatedPower);
                             messageDTO.setStatus(MessageDTO.MessageStatus.SUCCESS);

                             return objectMapper.writeValueAsString(messageDTO);
                         }else {
                             messageDTO.setStatus(MessageDTO.MessageStatus.ERROR);
                             return objectMapper.writeValueAsString(messageDTO);
                         }
                     }

                 } catch (IOException e) {
                     log.error("TranslatorService delivery message error: ",e.getMessage());
                 }
                 return null;
             }
         };
         server.mainloop();
     }catch (Exception e){
        log.error("Error rpc call; {}",e.getMessage());
     }

    }

    @Override
    public String translate( String targetLanguage, String text) throws IOException {

        Translate translate = setGoogleCredentials();
        Translation translation =
                translate.translate(
                        text,
                        TranslateOption.sourceLanguage(EN),
                        TranslateOption.targetLanguage(targetLanguage));

        String result = translation.getTranslatedText();
        log.debug("Original text: {} translated text: {}",text,result);

        if(result != null && !result.isEmpty()){
            return result;
        }

        return null;
    }


    @KafkaListener(topics = "t1")
    @SendTo
    public MessageDTO translatorListener(MessageDTO messageDTO){
        try {
//            String result = translate(messageDTO.getLanguage(),messageDTO.getText());
            String result = "it`s translated !!!!!";
            if(!StringUtils.isEmpty(result)){
                messageDTO.setText(result);
                messageDTO.setStatus(MessageDTO.MessageStatus.SUCCESS);

                return messageDTO;
            }
        } catch (Exception e) {
            messageDTO.setStatus(MessageDTO.MessageStatus.ERROR);
            return messageDTO;
        }
        return null;
    }

    private Translate setGoogleCredentials() throws IOException {

        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(TranslatorServiceImpl.class.getResourceAsStream(googleCredentialsPath));

        TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(credentials).build();

        return  translateOptions.getDefaultInstance().getService();
    }
}
