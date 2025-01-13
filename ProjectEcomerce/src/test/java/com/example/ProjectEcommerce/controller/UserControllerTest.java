package com.example.ProjectEcommerce.controller;

import com.example.ProjectEcommerce.dto.UserDto;
import com.example.ProjectEcommerce.model.User;
import com.example.ProjectEcommerce.request.CreateUserRequest;
import com.example.ProjectEcommerce.request.UserUpdateRequest;
import com.example.ProjectEcommerce.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private CreateUserRequest createUserRequest;
    private UserUpdateRequest userUpdateRequest;
    private User userCreateResponse;

    @BeforeEach
    void initData(){
        createUserRequest = CreateUserRequest.builder()
                .email("minh@gmail.com")
                .firstName("Chi")
                .lastName("Thui")
                .password("12345678")
                .build();
        userCreateResponse = User.builder()
                .email("minh@gmail.com")
                .firstName("Chi")
                .lastName("Thui")
                .build();

    }

    @Test
    @WithMockUser
    void createUser_validRequest_succes() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content  = objectMapper.writeValueAsString(createUserRequest);
        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn((userCreateResponse));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Create User Success!"));



    }
}
