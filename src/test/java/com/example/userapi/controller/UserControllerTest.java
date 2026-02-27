package com.example.userapi.controller;

import com.example.userapi.UserService;
import com.example.userapi.dto.CreateUserRequest;
import com.example.userapi.dto.UpdateUserRequest;
import com.example.userapi.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(controllers = {UserController.class})

public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = createObjectMapper();

    @MockitoBean
    private UserService userService;

    private UserDTO userDTO;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("user1");
        userDTO.setId(1);
        userDTO.setActive(true);
        userDTO.setEmail("user1@gmail.com");
        userDTO.setCreatedAt(LocalDateTime.now());
        userDTO.setUpdatedAt(LocalDateTime.now());

        createRequest = new CreateUserRequest();
        createRequest.setUsername("newUser1");
        createRequest.setEmail("newUser1@gmail.com");
        createRequest.setPassword("123456");

        updateRequest = new UpdateUserRequest();
        updateRequest.setUsername("updatedUser1");
        updateRequest.setEmail("updated@gmail.com");
    }

    // ==================== GET OPERATIONS ====================

    @Nested
    @DisplayName("GET /api/users Tests")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should return all users")
        void getAllProducts_ReturnsAllUsers() throws Exception {
            // Arrange
            List<UserDTO> users = Arrays.asList(userDTO);
            when(userService.getAllUsers()).thenReturn(users);

            // Act & Assert
            mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].username", is("user1")));

            verify(userService).getAllUsers();
        }

        @Test
        @DisplayName("Should return empty list when no users")
        void getAllUsers_NoUsers_ReturnsEmptyList() throws Exception {
            // Arrange
            when(userService.getAllUsers()).thenReturn(List.of());

            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/users/{id} Test")
    class GetUserByIdTests {

        @Test
        @DisplayName("Should return user")
        void getUsersById_ExistingId_ReturnsUser() throws Exception {
            // Arrange
            when(userService.getUserById(1)).thenReturn(userDTO);

            // Act & Assert
            mockMvc.perform(get("/api/users/1", 1))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.username", is("user1")))
                    .andExpect(jsonPath("$.email", is("user1@gmail.com")));
        }

        //@Test
        //@DisplayName("Should return 404 when user is not found")
        //void getUsersById_NonExistentId_Returns404() throws Exception {
        //    // Arrange
        //    when(userService.getUserById(999));
        //    // add type of exception
        //    // .thenThrow(new Exception());

        //    // Act & Assert
        //    mockMvc.perform(get("/api/users/999"))
        //            .andExpect(status().isNotFound())
        //            .andExpect(jsonPath("$.status", is(404)))
        //            .andExpect(jsonPath("$.error", is("NOT_FOUND")));
        //}
    }

    // ==================== POST OPERATIONS ====================

    @Nested
    @DisplayName("POST /api/users Tests")
    class CreateUserTests {

        @Test
        @DisplayName("Should return created user")
        void createUser_ValidRequest_Returns201() throws Exception {
            // Arrange
            when(userService.createUser(ArgumentMatchers.any(CreateUserRequest.class))).thenReturn(userDTO);

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.username", is("user1")));

            verify(userService).createUser(ArgumentMatchers.any(CreateUserRequest.class));
        }
    }

}
