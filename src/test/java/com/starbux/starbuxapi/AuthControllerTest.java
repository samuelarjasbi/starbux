package com.starbux.starbuxapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbux.starbuxapi.repository.UserRepository;
import com.starbux.starbuxapi.security.TestSecurityConfig;
import com.starbux.starbuxapi.security.jwt.JwtUtils;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest extends StarbuxapiApplicationTests{


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
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "username", roles={"ADMIN"})
    @Test
    public void CreateUser() throws Exception{

        String data = "{\"username\":\"test2\",\"email\":\"test2@test.com\",\"password\":\"test1234\",\"role\":[\"ADMIN\",\"USER\"]}";

        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(data))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/api/auth/delete/user/{id}",userRepository.findByEmail("test2@test.com").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());


    }

    @WithMockUser(username = "username", roles={"ADMIN"})
    @Test
    public void SignInUserTest() throws Exception{

        String beforeData = "{\"username\":\"test2\",\"email\":\"test2@test.com\",\"password\":\"test1234\",\"role\":[\"ROLE_ADMIN\",\"ROLE_USER\"]}";

        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(beforeData))
                .andExpect(status().isOk());


        String data = "{\"username\":\"test2\",\"password\":\"test1234\"}";

        mockMvc.perform(post("/api/auth/signin")
                .contentType("application/json")
                .content(data))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/auth/delete/user/{id}",userRepository.findByEmail("test2@test.com").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void TokenTest() throws Exception{

        String beforeData = "{\"username\":\"test2\",\"email\":\"test2@test.com\",\"password\":\"test1234\",\"role\":[\"ADMIN\",\"USER\"]}";

        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(beforeData))
                .andExpect(status().isOk());


        String data = "{\"username\":\"test2\",\"password\":\"test1234\"}";


        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/signin")
                .contentType("application/json")
                .content(data);

        MvcResult addresult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = addresult.getResponse();
        assertEquals(HttpStatus.OK.value(),response.getStatus());

        JSONObject jsonObject = new JSONObject(response.getContentAsString());

        boolean TokenCheck = jwtUtils.validateJwtToken(jsonObject.getString("accessToken"));
        assertEquals(TokenCheck,true);


        mockMvc.perform(delete("/api/auth/delete/user/{id}",userRepository.findByEmail("test2@test.com").getId())
                .contentType("application/json")
                .content(""))
                .andExpect(status().isOk());

    }






}
