package com.heroes.controller;

import com.heroes.dto.error.ErrorDTO;
import com.heroes.exception.HeroNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ControllerValidationHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO processGeneralException(Exception e){

        log.error("General backend exception: {}",e);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setDeveloperMessage(e.getMessage());
        errorDTO.setErrorMsgKey("GENERAL_ERROR");

        return errorDTO;
    }

    @ExceptionHandler(HeroNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO processHeroNotFoundException(Exception e) {

        log.error("Hero not found: {}", e);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setDeveloperMessage(e.getMessage());
        errorDTO.setErrorMsgKey("Hero not found.");

        return errorDTO;
    }
}
