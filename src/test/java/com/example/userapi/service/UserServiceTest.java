package com.example.userapi.service;

import com.example.userapi.UserService;
import com.example.userapi.dto.CreateUserRequest;
import com.example.userapi.dto.UpdateUserRequest;
import com.example.userapi.dto.UserDTO;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Set up test data
        user = new User();
        user.setId(1);
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEmail("test@email.com");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());


        userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setUsername("testUsername");
        userDTO.setEmail("test@email.com");
        userDTO.setActive(true);

        createRequest = new CreateUserRequest();
        createRequest.setUsername("newUsername");
        createRequest.setPassword("newPassword");
        createRequest.setEmail("new@email.com");

        updateRequest = new UpdateUserRequest();
        updateRequest.setUsername("updatedUsername");
        updateRequest.setEmail("updated@email.com");
    }

    // ==================== GET OPERATIONS ====================

    @Nested
    @DisplayName("Get user test")
    class GetUserTest {

        @Test
        @DisplayName("Should return user when found")
        void getUserById_ExistingId_ReturnsUser() {
            // Arrange (mocking dependencies)
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            when(userMapper.toDTO(user)).thenReturn(userDTO);

            // Act (colling the actual service)
            UserDTO result = userService.getUserById(1);

            // Assert (Verify that we got what we were expecting)
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1);
            assertThat(result.getUsername()).isEqualTo("testUsername");

            // Verify that repository was called
            verify(userRepository).findById(1);
            verify(userMapper).toDTO(user);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void getUserById_NonExistentId_ThrowsException() {
            // Arrange
            when(userRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> userService.getUserById(999));
            // need to add type of exception here
            // need to add message containing here

            // Verify mapper was never called
            verify(userMapper, never()).toDTO(any());
        }
    }

    @Nested
    @DisplayName("Get all Users Tests")
    class GetAllProductsTests {

        @Test
        @DisplayName("Should return all user")
        void getAllProducts_ReturnsAllProducts() {
            // Arrange
            List<User> users = Arrays.asList(user, user);
            List<UserDTO> userDTOs = Arrays.asList(userDTO, userDTO);

            when(userRepository.findAll()).thenReturn(users);
            when(userMapper.toDTOList(users)).thenReturn(userDTOs);

            // Act
            List<UserDTO> result = userService.getAllUsers();

            // Assert
            assertThat(result).hasSize(2);
            verify(userRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no users")
        void getAllProducts_NoProducts_ReturnsEmptyList() {
            // Arrange
            when(userRepository.findAll()).thenReturn(List.of());
            when(userMapper.toDTOList(List.of())).thenReturn(List.of());

            // Act
            List<UserDTO> result = userService.getAllUsers();

            // Assert
            assertThat(result).isEmpty();
        }
    }
    // ==================== CREATE OPERATIONS ====================

    @Nested
    @DisplayName("Create User Tests")
    class CreateProductTests {

        @Test
        @DisplayName("Should create user successfully")
        void createUser_ValidRequest_ReturnCreatedUser() {
            //  Arrange
            User newUser = new User();
            newUser.setUsername("newUsername");

            //when(userRepository.existsById(1)).thenReturn(false);
            when(userMapper.toEntity(createRequest)).thenReturn(newUser);
            when(userRepository.save(newUser)).thenReturn(user);
            when(userMapper.toDTO(user)).thenReturn(userDTO);

            // Act
            UserDTO result = userService.createUser(createRequest);

            // Assert
            assertThat(result).isNotNull();
            //verify(userRepository).existsById(1);
            verify(userRepository).save(newUser);
        }
    }

    // ==================== UPDATE OPERATIONS ====================

    @Nested
    @DisplayName("Update User Test")
    class UpdateUserTests {

        @Test
        @DisplayName("Should update user successfully")
        void updateUser_ValidRequest_ReturnUpdatedUser() {
            // Arrange
            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            doNothing().when(userMapper).updateEntityFromRequest(updateRequest, user);
            when(userRepository.save(user)).thenReturn(user);
            when(userMapper.toDTO(user)).thenReturn(userDTO);

            // Act
            UserDTO result = userService.updateUser(1, updateRequest);

            // Assert
            assertThat(result).isNotNull();
            verify(userMapper).updateEntityFromRequest(updateRequest, user);
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void updateUser_NonExistentId_ThrowsException() {
            // Arrange
            when(userRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> userService.updateUser(999, updateRequest));
            // add type of exception

            verify(userRepository, never()).save(any());
        }
    }

    // ==================== DELETE OPERATIONS ====================

    @Nested
    @DisplayName("Delete User Test")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user successfully")
        void deleteUser_ExistingId_DeletesUser() {
            // Arrange
            when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
            doNothing().when(userRepository).deleteById(1);

            // Act
            userService.deleteUserById(1);

            // Assert
            verify(userRepository).deleteById(1);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void deleteUser_NonExistentId_ThrowsException() {
            // Arrange
            when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));

            // Act & Assert
            assertThatThrownBy(() -> userService.deleteUserById(999));
            // add type of exception here

            verify(userRepository, never()).deleteById(any());
        }
    }
}
