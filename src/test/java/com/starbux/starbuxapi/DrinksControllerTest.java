package com.starbux.starbuxapi;



import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbux.starbuxapi.entity.Drinks;
import com.starbux.starbuxapi.repository.DrinksRepository;
import com.starbux.starbuxapi.security.TestSecurityConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DrinksControllerTest extends StarbuxapiApplicationTests{


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before()
    public void setup()
    {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Autowired
    private DrinksRepository drinksRepository;


    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void DrinkAddTest() throws Exception {



        Drinks drinks = new Drinks(0,"test",1,1);

        mockMvc.perform(post("/api/products/add/drink")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinks)))
                .andExpect(status().isOk());

        Drinks drinkEntity = drinksRepository.findByName("test");
        assertEquals(drinkEntity.getName(),"test");

    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void DrinksAddTest() throws Exception {



        Drinks drinks1 = new Drinks(0,"test1",1,1);
        Drinks drinks2 = new Drinks(0,"test2",1,1);

        List<Drinks> drinks = Lists.newArrayList(drinks1,drinks2);


        mockMvc.perform(post("/api/products/add/drinks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinks)))
                .andExpect(status().isOk());

        Drinks drinkEntity = drinksRepository.findByName("test1");
        assertEquals(drinkEntity.getName(),"test1");
        Drinks drinkEntity2 = drinksRepository.findByName("test2");
        assertEquals(drinkEntity2.getName(),"test2");



    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void DrinkUpdateTest() throws Exception {
        Drinks drinksBefore = new Drinks(0,"test4",1,1);

        mockMvc.perform(post("/api/products/add/drink")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinksBefore)))
                .andExpect(status().isOk());


        Drinks drinkEntityBefore = drinksRepository.findByName("test4");
        Drinks drinks = new Drinks(drinkEntityBefore.getId(),"test4",10,1);

        mockMvc.perform(put("/api/products/update/drink")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinks)))
                .andExpect(status().isOk());

        Drinks drinkEntityAfter = drinksRepository.findByName("test4");
        assertEquals(drinkEntityAfter.getQuantity(),10);

        mockMvc.perform(delete("/api/products/delete/Drink/{id}", drinksRepository.findByName("test4").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());



    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void DrinkFindByNameAndIdTest() throws Exception {


        Drinks drinkEntityBefore = drinksRepository.findByName("test1");

        Drinks drinks = new Drinks(0,"test1",1,1);

        mockMvc.perform(get("/api/products/DrinksById/{id}", drinkEntityBefore.getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/products/Drinks/{name}", drinkEntityBefore.getName())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        Drinks drinkEntity = drinksRepository.findByName("test1");
        assertEquals(drinkEntity.getName(),"test1");

        mockMvc.perform(delete("/api/products/delete/Drink/{id}", drinksRepository.findByName("test1").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/products/delete/Drink/{id}", drinksRepository.findByName("test2").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void RemoveDrinkTest() throws Exception {


        mockMvc.perform(delete("/api/products/delete/Drink/{id}",drinksRepository.findByName("test").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void GetAllDrinksTest() throws Exception {


        Drinks drinks = new Drinks(0,"test",1,1);

        mockMvc.perform(post("/api/products/add/drink")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinks)))
                .andExpect(status().isOk());

        Drinks drinkEntity = drinksRepository.findByName("test");



        mockMvc.perform(get("/api/products/Drinks")
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());



        mockMvc.perform(delete("/api/products/delete/Drink/{id}", drinksRepository.findByName("test").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());


    }



    }


