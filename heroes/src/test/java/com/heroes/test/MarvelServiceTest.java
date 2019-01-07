package com.heroes.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heroes.controller.HeroControllerRQ;
import com.heroes.dto.HeroResponceDTO;
import com.heroes.dto.HeroWithPowerDTO;
import com.heroes.service.TranslatorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MarvelServiceTest {

    private static final String HERO_ID = "1011334";
    private static  final String TRANSLATED_RESULT = "издръжливост: 2 енергия: 0 борба умения: 3 интелигентност: 3 скорост: 3 сила: 3";
    private static  final String TRANSLATED_REQUEST = "durability: 2 energy: 0 fighting skills: 3 intelligence: 3 speed: 3 strength: 3 ";
    private static final String BG_LANGUAGE_CODE = "bg";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TranslatorService translatorService;

    @Before
    public void setup() throws IOException {
        Mockito.when(translatorService.translate(BG_LANGUAGE_CODE,TRANSLATED_REQUEST)).thenReturn(TRANSLATED_RESULT);
    }

    @Test
    public void getHeroesTest() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(HeroControllerRQ.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<String> heroIds = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<List<String>>(){});

        Assert.assertTrue(heroIds.size() > 0);
    }

    @Test
    public void getHeroById() throws Exception {

        String heroPath =HeroControllerRQ.PATH+ HeroControllerRQ.ID_PATH.replace("{characterId}",HERO_ID);

        MvcResult mvcResult = this.mockMvc.perform(get(heroPath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        HeroResponceDTO heroResponceDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<HeroResponceDTO>(){});

        Assert.assertNotNull(heroResponceDTO);
        Assert.assertEquals("3-D Man",heroResponceDTO.getName());
    }

    @Test()
    public void getHeroByIdNotFound() throws Exception {

        String heroId = "1111111111";
        String heroPath =HeroControllerRQ.PATH+ HeroControllerRQ.ID_PATH.replace("{characterId}",heroId);

        MvcResult mvcResult = this.mockMvc.perform(get(heroPath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
    }

    @Test
    public void extractHeroPowers() throws Exception {

        String heroPowerPathPath =HeroControllerRQ.PATH+HeroControllerRQ.ID_PATH.replace("{characterId}",HERO_ID)
                +HeroControllerRQ.POWERS+HeroControllerRQ.LANGUAGE_PATH.replace("{languageCode}",BG_LANGUAGE_CODE);

        MvcResult mvcResult = this.mockMvc.perform(get(heroPowerPathPath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        HeroWithPowerDTO heroResponceDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<HeroWithPowerDTO>(){});

        Assert.assertNotNull(heroResponceDTO);
        Assert.assertNotNull(heroResponceDTO.getPower());
        Assert.assertEquals(TRANSLATED_RESULT,heroResponceDTO.getPower());

    }
}
