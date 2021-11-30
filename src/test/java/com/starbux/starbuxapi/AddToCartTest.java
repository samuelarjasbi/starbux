package com.starbux.starbuxapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbux.starbuxapi.entity.Drinks;
import com.starbux.starbuxapi.entity.Toppings;
import com.starbux.starbuxapi.repository.AddToCartRepository;
import com.starbux.starbuxapi.repository.DrinksRepository;
import com.starbux.starbuxapi.repository.ToppingsRepository;
import com.starbux.starbuxapi.repository.UserRepository;
import com.starbux.starbuxapi.security.TestSecurityConfig;
import org.json.JSONArray;
import org.json.JSONObject;
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
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
public class AddToCartTest extends StarbuxapiApplicationTests{


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

        String BeforeData = "{\"username\":\"test\",\"email\":\"test@test.com\",\"password\":\"test1234\",\"role\":[\"ADMIN\",\"USER\"]}";

        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(BeforeData))
                .andExpect(status().isOk());

        // Create Drink

        Drinks drinks = new Drinks(0,"test",1,1);

        mockMvc.perform(post("/api/products/add/drink")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinks)))
                .andExpect(status().isOk());

        Drinks drinkEntity = drinksRepository.findByName("test");

        // Create topping

        Toppings toppings = new Toppings(0,"test",1,1,1);

        mockMvc.perform(post("/api/products/add/topping")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toppings)))
                .andExpect(status().isOk());

        Toppings toppingsEntity = toppingsRepository.findByName("test");


        // Create Json object

        JSONObject addToCart = new JSONObject();

        addToCart.put("userId",Long.toString(userRepository.findByEmail("test@test.com").getId()));
        addToCart.put("qty","3");
        addToCart.put("drinks_id",Integer.toString(drinkEntity.getId()));
        addToCart.put("toppings_id",Integer.toString(toppingsEntity.getId()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/addtocart/addProduct")
                .contentType("application/json")
                .content(addToCart.toString());

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(),response.getStatus());

        JSONObject data = new JSONObject();
        JSONArray jsonArr = new JSONArray(response.getContentAsString());
        JSONObject jsonObj = new JSONObject();
        for (int i = 0; i < jsonArr.length(); i++) { jsonObj = jsonArr.getJSONObject(i); }
        String id = jsonObj.getString("id");
        data.put("userId","3");
        data.put("cartId",id);

        mockMvc.perform(delete("/api/addtocart/removeProductFromCart")
                .contentType("application/json")
                .content(data.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/products/delete/Drink/{id}",drinksRepository.findByName("test").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/auth/delete/user/{id}",userRepository.findByEmail("test@test.com").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/products/delete/topping/{id}", toppingsRepository.findByName("test").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());




    }


    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void ProductCartUpdateQtyTest() throws Exception {

        // Create User

        String BeforeData = "{\"username\":\"test3\",\"email\":\"test3@test.com\",\"password\":\"test1234\",\"role\":[\"ADMIN\",\"USER\"]}";

        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(BeforeData))
                .andExpect(status().isOk());

        // Create Drink

        Drinks drinks = new Drinks(0,"test3",1,1);

        mockMvc.perform(post("/api/products/add/drink")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinks)))
                .andExpect(status().isOk());

        Drinks drinkEntity = drinksRepository.findByName("test3");

        // Create topping

        Toppings toppings = new Toppings(0,"test3",1,1,1);

        mockMvc.perform(post("/api/products/add/topping")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toppings)))
                .andExpect(status().isOk());

        Toppings toppingsEntity = toppingsRepository.findByName("test3");


        // Create Json object

        JSONObject addToCart = new JSONObject();

        addToCart.put("userId",Long.toString(userRepository.findByEmail("test3@test.com").getId()));
        addToCart.put("qty","3");
        addToCart.put("drinks_id",Integer.toString(drinkEntity.getId()));
        addToCart.put("toppings_id",Integer.toString(toppingsEntity.getId()));


        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/addtocart/addProduct")
                .contentType("application/json")
                .content(addToCart.toString());

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(),response.getStatus());

        JSONObject data = new JSONObject();
        JSONArray jsonArr = new JSONArray(response.getContentAsString());
        JSONObject jsonObj = new JSONObject();
        System.out.println(jsonArr);
        for (int i = 0; i < jsonArr.length(); i++) { jsonObj = jsonArr.getJSONObject(i); }
        String id = jsonObj.getString("id");
        System.out.println(id);
        data.put("userId",Long.toString(userRepository.findByEmail("test3@test.com").getId()));
        data.put("cartId",id);
        data.put("qty","10");
        mockMvc.perform(put("/api/addtocart/updateQtyForCart")
                .contentType("application/json")
                .content(data.toString()))
                .andExpect(status().isOk());

        data.remove("qty");

        mockMvc.perform(delete("/api/addtocart/removeProductFromCart")
                .contentType("application/json")
                .content(data.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/products/delete/Drink/{id}",drinksRepository.findByName("test3").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/auth/delete/user/{id}",userRepository.findByEmail("test3@test.com").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/products/delete/topping/{id}", toppingsRepository.findByName("test3").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void GetCartByUserIdTest() throws Exception {


        // Create User

        String BeforeData = "{\"username\":\"test5\",\"email\":\"test5@test.com\",\"password\":\"test1234\",\"role\":[\"ADMIN\",\"USER\"]}";

        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(BeforeData))
                .andExpect(status().isOk());

        // Create Drink

        Drinks drinks = new Drinks(0,"test5",1,1);

        mockMvc.perform(post("/api/products/add/drink")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(drinks)))
                .andExpect(status().isOk());

        Drinks drinkEntity = drinksRepository.findByName("test5");

        // Create topping

        Toppings toppings = new Toppings(0,"test5",1,1,1);

        mockMvc.perform(post("/api/products/add/topping")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toppings)))
                .andExpect(status().isOk());

        Toppings toppingsEntity = toppingsRepository.findByName("test5");


        // Create Json object

        JSONObject addToCart = new JSONObject();

        addToCart.put("userId",Long.toString(userRepository.findByEmail("test5@test.com").getId()));
        addToCart.put("qty","3");
        addToCart.put("drinks_id",Integer.toString(drinkEntity.getId()));
        addToCart.put("toppings_id",Integer.toString(toppingsEntity.getId()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/addtocart/addProduct")
                .contentType("application/json")
                .content(addToCart.toString());

        MvcResult addresult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = addresult.getResponse();
        assertEquals(HttpStatus.OK.value(),response.getStatus());


        JSONObject findFromCart = new JSONObject();
        findFromCart.put("userId",Long.toString(userRepository.findByEmail("test5@test.com").getId()));



        RequestBuilder findrequestBuilder = MockMvcRequestBuilders.post("/api/addtocart/getCartsByUserId")
                .contentType("application/json")
                .content(findFromCart.toString());

        MvcResult result = mockMvc.perform(findrequestBuilder).andReturn();

        MockHttpServletResponse findCartResponse = result.getResponse();
        assertEquals(HttpStatus.OK.value(), findCartResponse.getStatus());

        JSONObject data = new JSONObject();
        JSONArray jsonArr = new JSONArray(findCartResponse.getContentAsString());
        JSONObject jsonObj = new JSONObject();
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonObj = jsonArr.getJSONObject(i);
        }
        System.out.println(jsonObj);
        String id = jsonObj.getString("id");
        data.put("userId", "3");
        data.put("cartId", id);

        mockMvc.perform(delete("/api/addtocart/removeProductFromCart")
                .contentType("application/json")
                .content(data.toString()))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/api/products/delete/Drink/{id}",drinksRepository.findByName("test5").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/auth/delete/user/{id}",userRepository.findByEmail("test5@test.com").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/products/delete/topping/{id}", toppingsRepository.findByName("test5").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

    }

}
