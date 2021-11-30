package com.starbux.starbuxapi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbux.starbuxapi.entity.Toppings;
import com.starbux.starbuxapi.repository.ToppingsRepository;
import com.starbux.starbuxapi.security.TestSecurityConfig;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ToppingsControllerTest extends StarbuxapiApplicationTests{


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
    private ToppingsRepository toppingsRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void ToppingAddTest() throws Exception {



        Toppings toppings = new Toppings(0,"test",1,1,1);

        mockMvc.perform(post("/api/products/add/topping")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toppings)))
                .andExpect(status().isOk());

        Toppings toppingsEntity = toppingsRepository.findByName("test");
        System.out.println(toppingsEntity.getName());
        assertEquals(toppingsEntity.getName(),"test");

        mockMvc.perform(delete("/api/products/delete/topping/{id}", toppingsRepository.findByName("test").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void ToppingsAddTest() throws Exception {



        Toppings toppings2 = new Toppings(0,"test2",1,1,0);
        Toppings toppings1 = new Toppings(0,"test1",1,1,0);

        List<Toppings> drinks = Lists.newArrayList(toppings1,toppings2);


        mockMvc.perform(post("/api/products/add/toppings")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinks)))
                .andExpect(status().isOk());

        Toppings toppingsEntity = toppingsRepository.findByName("test1");
        System.out.println(toppingsEntity.getName());
        assertEquals(toppingsEntity.getName(),"test1");
        Toppings toppingsEntity2 = toppingsRepository.findByName("test2");
        System.out.println(toppingsEntity2.getName());
        assertEquals(toppingsEntity2.getName(),"test2");


    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void ToppingUpdateTest() throws Exception {

        Toppings toppingsBefore = new Toppings(0,"test4",1,1,1);

        mockMvc.perform(post("/api/products/add/topping")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toppingsBefore)))
                .andExpect(status().isOk());


        Toppings toppingsEntityBefore = toppingsRepository.findByName("test4");
        Toppings toppings = new Toppings(toppingsEntityBefore.getId(),"test4",10,1,0);

        mockMvc.perform(put("/api/products/update/topping")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toppings)))
                .andExpect(status().isOk());

        Toppings toppingsEntityAfter = toppingsRepository.findByName("test4");
        assertEquals(toppingsEntityAfter.getQuantity(),10);

        mockMvc.perform(delete("/api/products/delete/topping/{id}", toppingsRepository.findByName("test4").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());



    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void ToppingFindByNameAndIdTest() throws Exception {


        Toppings toppingsEntityBefore = toppingsRepository.findByName("test1");

        Toppings toppings = new Toppings(0,"test1",1,1,0);

        mockMvc.perform(get("/api/products/toppingsById/{id}", toppingsEntityBefore.getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/products/topping/{name}", toppingsEntityBefore.getName())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        Toppings toppingsEntity = toppingsRepository.findByName("test1");
        assertEquals(toppingsEntity.getName(),"test1");

        mockMvc.perform(delete("/api/products/delete/topping/{id}", toppingsRepository.findByName("test1").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/products/delete/topping/{id}", toppingsRepository.findByName("test2").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void GetAllToppingsTest() throws Exception {
        Toppings toppings = new Toppings(0,"test",1,1,1);

        mockMvc.perform(post("/api/products/add/topping")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toppings)))
                .andExpect(status().isOk());



        mockMvc.perform(get("/api/products/toppings")
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        Toppings toppingsEntity = toppingsRepository.findByName("test");

        mockMvc.perform(delete("/api/products/delete/topping/{id}", toppingsRepository.findByName("test").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());


    }



}


