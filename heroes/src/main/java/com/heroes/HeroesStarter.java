package com.heroes;

import com.heroes.config.AppConfig;
import com.heroes.config.MongoConfig;
import lombok.extern.slf4j.Slf4j;
import ms_common.messaging.MicroservicesCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import javax.annotation.PreDestroy;

@Slf4j
@Import(MicroservicesCommonConfig.class)
@SpringBootApplication(scanBasePackageClasses = {MongoConfig.class, AppConfig.class})
public class HeroesStarter {


	public static void main(String[] args)  {
		log.info("Service is  Starting !!!");
		SpringApplication.run(HeroesStarter.class, args);
		log.info("Service Started !!!");
    }

	@PreDestroy
	private void stop(){
		log.info("Service stopped !");
	}
}
