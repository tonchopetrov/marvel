package com.heroes.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface TranslatorService {

    String translate(String targetLanguage, String text) throws IOException, TimeoutException;
}
