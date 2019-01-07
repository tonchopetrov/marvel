package com.heroes.dto;

import com.heroes.model.HeroPictureData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeroResponceDTO {

    private String id;
    private String name;
    private String description;
    private HeroPictureData thumbnail;

}
