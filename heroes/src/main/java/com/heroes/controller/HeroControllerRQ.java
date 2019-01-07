package com.heroes.controller;

public interface HeroControllerRQ {

    String PATH = "/characters";
    String ID = "characterId";
    String ID_PATH = "/{" + ID + "}";
    String POWERS = "/powers";
    String LANGUAGE = "languageCode";
    String LANGUAGE_PATH = "/{" + LANGUAGE + "}";
}
