package com.heroes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeroWithPowerDTO {

    private int id;
    private String name;
    private String power;
}
