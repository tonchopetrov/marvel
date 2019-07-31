package com.heroes.health;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping(path = "/isAlive")
    public ResponseEntity isAlive(){
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
