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
import lombok.extern.slf4j.Slf4j;
import ms_common.messaging.config.RabbitConstants;
import ms_common.messaging.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Slf4j
@Service
public class TranslatorServiceImpl implements TranslatorService {

    @Value("${google.credentials.path}")
    private String googleCredentialsPath;

    private static final String EN = "en";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Connection connection;

    @PostConstruct
    public void init()  {

        handleDelivery();
    }

    private void handleDelivery()  {

     try{
         StringRpcServer server = new StringRpcServer(connection.createChannel(), RabbitConstants.REQUEST_QUEUE_NAME) {
             public String handleStringCall(String request) {

                 String translatedPower = null;
                 try {
                     MessageDTO messageDTO = objectMapper.readValue(request, new TypeReference<MessageDTO>() {
                     });

                     if(messageDTO != null){
                         translatedPower = translate(messageDTO.getLanguage(), messageDTO.getText());
                     }

                 } catch (IOException e) {
                     log.error("TranslatorService delivery message error: ",e.getMessage());
                 }
                 return translatedPower;
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

    private Translate setGoogleCredentials() throws IOException {

        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(TranslatorServiceImpl.class.getResourceAsStream(googleCredentialsPath));

        TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(credentials).build();

        return  translateOptions.getDefaultInstance().getService();
    }


}
