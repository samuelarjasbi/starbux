package com.starbux.starbuxapi;
import com.starbux.starbuxapi.entity.AddToCart;
import com.starbux.starbuxapi.entity.Drinks;
import com.starbux.starbuxapi.entity.Toppings;
import com.starbux.starbuxapi.repository.AddToCartRepository;
import com.starbux.starbuxapi.service.ToppingsService;
import org.json.JSONObject;
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



import com.starbux.starbuxapi.security.TestSecurityConfig;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReportsControllerTest  extends StarbuxapiApplicationTests{


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
    private AddToCartRepository addToCartRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void ReportsOrderTest() throws Exception {


        mockMvc.perform(get("/api/reports/orders")
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());



    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void ToppingssOrderTest() throws Exception {


        mockMvc.perform(get("/api/reports/toppings")
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());



    }


}