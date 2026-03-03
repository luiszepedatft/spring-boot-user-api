package com.example.userapi.service;

import com.example.userapi.dto.CreateUserRequest;
import com.example.userapi.dto.UpdateUserRequest;
import com.example.userapi.dto.UserDTO;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Get all Users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }

    /**
     * Get a user by id
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return userMapper.toDTO(user);
    }

    /**
     * Create a new user
     */
    public UserDTO createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    /**
     * Update a user
     */
    public UserDTO updateUser(int id, UpdateUserRequest request) {
        User existingUser = userRepository.findById(id).orElseThrow();
        userMapper.updateEntityFromRequest(request, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Deactivate a user
     */
    public UserDTO deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setActive(false);
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Delete a user by ID
     */
    public void deleteUserById(int id) {
        User existingUser = userRepository.findById(id).orElseThrow();
        userRepository.deleteById(existingUser.getId());
    }


}
