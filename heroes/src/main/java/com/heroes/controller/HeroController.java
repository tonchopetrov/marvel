package com.heroes.controller;

import com.heroes.dto.HeroResponceDTO;
import com.heroes.dto.HeroWithPowerDTO;
import com.heroes.service.MarvelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = HeroControllerRQ.PATH)
public class HeroController {


    private MarvelService marvelService;

    @Autowired
    public HeroController(MarvelService marvelService) {
        this.marvelService = marvelService;
    }


    @GetMapping(path = HeroControllerRQ.ID_PATH)
    public ResponseEntity getHero(@PathVariable(name = HeroControllerRQ.ID) String characterId) throws Exception {

        HeroResponceDTO responceDTO = marvelService.getHeroById(characterId);

        if(responceDTO != null){

            return new ResponseEntity<>(responceDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity getHeroes() throws Exception {

        List<String> heroesId = marvelService.getHeroesId();

        if(heroesId != null || heroesId.size() != 0){

            return new ResponseEntity<>(heroesId, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = HeroControllerRQ.ID_PATH+HeroControllerRQ.POWERS+HeroControllerRQ.LANGUAGE_PATH)
    public ResponseEntity getHeroPowers(
                                                @PathVariable(name = HeroControllerRQ.ID) String id,
                                                @PathVariable(name = HeroControllerRQ.LANGUAGE) String language) throws Exception {

        HeroWithPowerDTO dto = marvelService.extractCharacterPower(id,language);

        if(dto != null){
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }


}
