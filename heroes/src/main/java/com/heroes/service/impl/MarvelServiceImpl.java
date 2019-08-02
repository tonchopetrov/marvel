package com.heroes.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heroes.dto.HeroDataDTO;
import com.heroes.dto.HeroResponseDTO;
import com.heroes.dto.HeroWithPowerDTO;
import com.heroes.exception.HeroNotFoundException;
import com.heroes.model.Hero;
import com.heroes.model.HeroesData;
import com.heroes.repository.HeroRepository;
import com.heroes.service.MarvelService;
import com.heroes.service.TranslatorService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@Slf4j
@Service
public class MarvelServiceImpl implements MarvelService {

    @Value("${marvel.key.private}")
    private String marvelPrivateKey;

    @Value("${marvel.key.public}")
    private String marvelPublicKay;

    @Value("${marvel.heroUrl}")
    private String heroUrl;

    @Value("${marvel.batchSize}")
    private int batchSize;

    @Value("${encoding}")
    private String encoding;

    @Value("${hash.algorithm}")
    private String algorithm;

    private HeroRepository repository;
    private TranslatorService translatorService;
    private ObjectMapper objectMapper;


    @Autowired
    public MarvelServiceImpl(HeroRepository repository, TranslatorService translatorService, ObjectMapper objectMapper) {
        this.repository = repository;
        this.translatorService = translatorService;
        this.objectMapper = new ObjectMapper();
    }

//  MAX 3000 request per day
//    @PostConstruct
    private void init() throws IOException {

        //get first 100 heroes
        HeroesData batch = getNextBatch(0);

        batch.getData().getResults().forEach(h ->{
            Hero hero = new Hero();
            BeanUtils.copyProperties(h,hero);
            hero.setWikiUrl(getHeroWikiUrl(h));

            repository.save(hero);

        });

        int totalSize = batch.getData().getTotal();

        int batchesLeft =  Double.valueOf(Math.ceil(1.0 * totalSize / batchSize)).intValue() - 1;
        AtomicInteger atomicBatchSize = new AtomicInteger();
        atomicBatchSize.set(batchesLeft);
        IntStream.rangeClosed(1,batchesLeft).parallel().forEach( i -> {

            try {
                getNextBatch(i * batchSize).getData().getResults().forEach(h ->{
                    Hero hero = new Hero();
                    BeanUtils.copyProperties(h,hero);
                    hero.setWikiUrl(getHeroWikiUrl(h));

                    repository.save(hero);
                });
            } catch (IOException e) {
                log.error("Save hero exc: {}",e.getMessage());
            }

        });
    }

    @Override
    public List<String> getHeroesId() throws Exception {

        List<String>  heroesIdList = new ArrayList<>();

        List<Hero> heroes = repository.findAll();

        if(heroes != null && heroes.size() > 0){
            heroes.forEach(h -> {
                heroesIdList.add(h.getId());
            });
        }

        log.debug("Heroes size: {}",heroes.size());

        return heroesIdList;
    }

    @Override
    public HeroResponseDTO getHeroById(String id) throws Exception {

        HeroesData data = getHero(id);
        HeroResponseDTO responseDTO = new HeroResponseDTO();
        BeanUtils.copyProperties(data.getData().getResults().get(0), responseDTO);

        log.debug("Hero id: {}", responseDTO.getId());

        return responseDTO;
    }

    @Override
    public HeroWithPowerDTO extractCharacterPower(String id, String language) throws Exception {

        Hero hero = repository.findOne(id);

        if(hero == null){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(hero.getWikiUrl());
        sb.append(URLEncoder.encode(hero.getName(), encoding));

        Document doc = null;
       try{
                doc = Jsoup.connect(sb.toString())
                   .get();
       }catch (Exception e){
           log.error("Error getting  html document: {}",e.getMessage());
       }

        Elements powerGrid = doc.getElementsByClass("power-grid");

        sb = new StringBuilder();

        for (Element power : powerGrid) {
            Elements powerCircle = power.getElementsByClass("power-circle__wrapper");
            for(Element e : powerCircle){
                String label = e.getElementsByClass("power-circle__label").text();
                String rating = e.getElementsByClass("power-circle__rating").text();
                sb.append(label);
                sb.append(": "+rating+ " ");
            }
        }

        HeroWithPowerDTO resultDto = new HeroWithPowerDTO();
        BeanUtils.copyProperties(hero,resultDto);

        String translatedPower = translatorService.translate(language, sb.toString());

        if(!StringUtils.isEmpty(translatedPower)){

            resultDto.setPower(translatedPower);
            return resultDto;
        }

        return resultDto;
    }

//    private String translatePower(String language, String text) throws IOException, TimeoutException {
//
//        MessageDTO messageDTO = new MessageDTO();
//        messageDTO.setText(text);
//        messageDTO.setLanguage(language);
//
//        channel = connection.createChannel();
//        rpcClient = new RpcClient(channel, "",RabbitConstants.REQUEST_QUEUE_NAME);
//
//        String power = rpcClient.stringCall(objectMapper.writeValueAsString(messageDTO));
//
//        log.debug("Translated power: {}",power);
//
//        return power;
//    }

    private String getHeroWikiUrl(HeroDataDTO hero){
        AtomicReference<String> heroWikiUrl = new AtomicReference<>();
        hero.getUrls().forEach(u -> {
            if("wiki".equals(u.getType())){
                heroWikiUrl.set(u.getUrl());
            }
        });
        return heroWikiUrl.get();
    }

    private String createHashKey(String randomString, String publicKey, String privateKey){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String keys = randomString+privateKey+publicKey;

        md.update(keys.getBytes());
        byte[] digest = md.digest();
        String result = DatatypeConverter
                .printHexBinary(digest).toLowerCase();

        return result;
    }

    private String generateHeroesUrl(String id, int offset){

        String randomString = Long.toHexString(Double.doubleToLongBits(Math.random()));

        String hashKey = createHashKey(randomString,marvelPublicKay,marvelPrivateKey);

        StringBuilder sb = new StringBuilder();
        sb.append(heroUrl);

        if(id != null && !id.equals("")){
            sb.append("/"+id);
        }

        sb.append("?ts=");
        sb.append(randomString);
        sb.append("&apikey=");
        sb.append(marvelPublicKay);
        sb.append("&hash=");
        sb.append(hashKey);
        sb.append("&offset=");
        sb.append(offset);

        log.debug("Hero URL= {}",sb.toString());

        return sb.toString();
    }

    private HeroesData getNextBatch(int offset) throws IOException {

        String stringUrl  =   generateHeroesUrl("",offset);

        return parseHeroesData(stringUrl);
    }

    private HeroesData getHero(String id) throws IOException, HeroNotFoundException {
        String stringUrl =   generateHeroesUrl(id,1);

        return parseHeroesData(stringUrl);
    }

    private HeroesData parseHeroesData(String stringUrl) throws IOException, HeroNotFoundException {

        String data  = readDataFromUrl(stringUrl);

        return objectMapper.readValue(data, new TypeReference<HeroesData>() {
        });
    }

    private String readDataFromUrl(String stringUrl) throws IOException, HeroNotFoundException {

        InputStream inStream;
        try {

            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            inStream = connection.getInputStream();

        } catch (FileNotFoundException e) {
            log.error("Hero not found exception !");
            // if hero wasn't found Marvel return FileNotFoundException
            throw new HeroNotFoundException();
        }
        return streamToString(inStream);
    }

    private  String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, encoding).useDelimiter("\\Z").next();
        return text;
    }

//    private void publishMessage(String language,String message) throws IOException, TimeoutException {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost(RabbitConstants.HOST);
//        connection = factory.newConnection();
//        channel = connection.createChannel();
//
//        channel.queueDeclare(RabbitConstants.REQUEST_QUEUE_NAME, false, false, false, null);
////        channel.queueDeclare(RabbitConstants.REPLY_QUEUE_NAME, false, false, false, null);
//
//        MessageDTO messageDTO = new MessageDTO();
//        messageDTO.setText(message);
//        messageDTO.setLanguage(language);
//
//        Publisher publisher = new Publisher(connection.createChannel(),objectMapper);
//        publisher.publishMessage(messageDTO,RabbitConstants.REQUEST_QUEUE_NAME);
//
//    }

}
