package com.starbux.starbuxapi;
import com.starbux.starbuxapi.entity.AddToCart;
import com.starbux.starbuxapi.entity.Drinks;
import com.starbux.starbuxapi.entity.Toppings;
import com.starbux.starbuxapi.repository.AddToCartRepository;
import com.starbux.starbuxapi.repository.DrinksRepository;
import com.starbux.starbuxapi.repository.ToppingsRepository;
import com.starbux.starbuxapi.repository.UserRepository;
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
public class OrderControllerTest extends StarbuxapiApplicationTests{

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DrinksRepository drinksRepository;

    @Autowired
    private ToppingsRepository toppingsRepository;

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void ProductAddToCartTest() throws Exception {



        // Create User

        String BeforeData = "{\"username\":\"test6\",\"email\":\"test6@test.com\",\"password\":\"test1234\",\"role\":[\"ADMIN\",\"USER\"]}";

        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(BeforeData))
                .andExpect(status().isOk());

        // Create Drink

        Drinks drinks = new Drinks(0,"test6",1,1);

        mockMvc.perform(post("/api/products/add/drink")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinks)))
                .andExpect(status().isOk());

        Drinks drinkEntity = drinksRepository.findByName("test6");

        // Create topping

        Toppings toppings = new Toppings(0,"test6",1,1,1);

        mockMvc.perform(post("/api/products/add/topping")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toppings)))
                .andExpect(status().isOk());

        Toppings toppingsEntity = toppingsRepository.findByName("test6");


        // Create Json object

        JSONObject addToCart = new JSONObject();

        addToCart.put("userId",Long.toString(userRepository.findByEmail("test6@test.com").getId()));
        addToCart.put("qty","3");
        addToCart.put("drinks_id",Integer.toString(drinkEntity.getId()));
        addToCart.put("toppings_id",Integer.toString(toppingsEntity.getId()));



        mockMvc.perform(post("/api/addtocart/addProduct")
                .contentType("application/json")
                .content(addToCart.toString()))
                .andExpect(status().isOk());


        JSONObject checkout = new JSONObject();
        checkout.put("userId",Long.toString(userRepository.findByEmail("test6@test.com").getId()));
        checkout.put("deliveryAddress","test St");

        mockMvc.perform(post("/api/order/checkout_order")
                .contentType("application/json")
                .content(checkout.toString()))
                .andExpect(status().isOk());

        JSONObject getCheckout = new JSONObject();
        checkout.put("userId",Long.toString(userRepository.findByEmail("test6@test.com").getId()));



        mockMvc.perform(post("/api/order/getOrdersByUserId")
                .contentType("application/json")
                .content(getCheckout.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/order/deleteOrderById/{id}",userRepository.findByEmail("test6@test.com").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/order/deleteOrderById/{id}",userRepository.findByEmail("test6@test.com").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/api/auth/delete/user/{id}",userRepository.findByEmail("test6@test.com").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/products/delete/topping/{id}", toppingsRepository.findByName("test6").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/reports/DeleteReportByUserId/{Id}",drinksRepository.findByName("test6").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());



    }


}
