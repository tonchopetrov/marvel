package com.heroes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@Document(collection = "hero")
public class Hero {

    @Id
    private String id;
    private String name;
    private String description;
    private String powerDescription;
    private HeroPictureData  thumbnail;
    private String wikiUrl;


}
