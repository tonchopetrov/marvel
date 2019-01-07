package com.heroes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.heroes.dto.HeroDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeroesData {

    private HeroData data;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class HeroData{

        private int total;
        private List<HeroDataDTO> results;


    }

}
