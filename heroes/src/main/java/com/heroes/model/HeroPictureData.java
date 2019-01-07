package com.heroes.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@Document(collection = "hero_picture_date")
public class HeroPictureData{

    @Id
    private String path;
    private String extension;
}
