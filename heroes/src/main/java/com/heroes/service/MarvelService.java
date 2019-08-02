package com.heroes.service;

import com.heroes.dto.HeroResponseDTO;
import com.heroes.dto.HeroWithPowerDTO;

import java.util.List;

public interface MarvelService {

    List<String> getHeroesId() throws Exception;

    HeroResponseDTO getHeroById(String id) throws Exception;

    HeroWithPowerDTO extractCharacterPower(String id, String language) throws Exception;

}
