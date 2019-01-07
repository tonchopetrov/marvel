package com.heroes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.heroes.model.HeroPictureData;
import com.heroes.model.HeroUrl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeroDataDTO {

    private String id;
    private String name;
    private String description;
    private String powerDescription;
    private HeroPictureData thumbnail;
    private List<HeroUrl> urls;
}
