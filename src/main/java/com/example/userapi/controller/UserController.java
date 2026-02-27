package com.example.userapi.controller;

import com.example.userapi.UserService;
import com.example.userapi.dto.CreateUserRequest;
import com.example.userapi.dto.UpdateUserRequest;
import com.example.userapi.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
//@Tag(name = "Users API", description = "CRUD operations for users db")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ===================== CREATE OPERATIONS ======================

    /**
     * Create a new user
     */
    @PostMapping
    //@Operation(summary="Create a new User", description = "Creates a new user")
    //@ApiResponses
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDTO createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // ===================== READ OPERATIONS ======================

    /**
     * Get all users
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // ===================== UPDATE OPERATIONS ======================

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Integer id, @Valid @RequestBody UpdateUserRequest request) {
        UserDTO updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }


}
