package com.heroes.service;

import com.heroes.dto.HeroResponceDTO;
import com.heroes.dto.HeroWithPowerDTO;

import java.util.List;

public interface MarvelService {

    List<String> getHeroesId() throws Exception;

    HeroResponceDTO getHeroById(String id) throws Exception;

    HeroWithPowerDTO extractCharacterPower(String id, String language) throws Exception;

}
