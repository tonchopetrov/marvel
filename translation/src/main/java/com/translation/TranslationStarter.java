package com.translation;

import lombok.extern.slf4j.Slf4j;
import ms_common.messaging.MicroservicesCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import javax.annotation.PreDestroy;

@Slf4j
@Import(MicroservicesCommonConfig.class)
@SpringBootApplication
public class TranslationStarter {

    public static void main(String[] args) {
        log.info("Service is  Starting !!!");
        SpringApplication.run(TranslationStarter.class, args);
        log.info("Service Started !!!");
    }

    @PreDestroy
    private void stop(){
        log.info("Service stopped !");
    }
}
