package com.example.userapi.repository;

import com.example.userapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
@ActiveProfiles("test")  // Uses application-test.yaml (disables Liquibase)
public class UserRepositoryTest {

    @Autowired
    private org.springframework.boot.jpa.test.autoconfigure.TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        /**
         * Positive creation user
         */
        user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setActive(true);
        user1.setEmail("user1@gmai.com");
        entityManager.persist(user1);
        /**
         * Negative creation user
         */
        user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setActive(false);
        user2.setEmail("invalid.email");
        entityManager.persist(user2);
        /**
         * Other user
         */
        user3 = new User();
        user3.setUsername("user3");
        user3.setPassword("password3");
        user3.setActive(false);
        user3.setEmail("user3@outlook.com");
        entityManager.persist(user3);

        entityManager.flush();
    }

    // ==================== BASIC CRUD TESTS ====================

    @Test
    @DisplayName("Should find user by ID")
    void findById_ExistingId_ReturnsUser() {
        // Act
        Optional<User> found = userRepository.findById(user1.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo(user1.getUsername());
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void findById_NotExistingId_ReturnsEmpty() {
        Optional<User> found = userRepository.findById(999);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should create a new user")
    void save_NewUser_ReturnSavedUser() {

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("password1");
        newUser.setActive(true);
        newUser.setEmail("newuser@email.com");

        User userSaved = userRepository.save(newUser);

        assertThat(userSaved.getId()).isNotNull();
        assertThat(userSaved.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(userSaved.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should delete product by ID")
    void deleteById_ExistingId_RemovesUser() {

        userRepository.deleteById(user1.getId());
        entityManager.flush();

        Optional<User> found = userRepository.findById(user1.getId());
        assertThat(found).isEmpty();
    }

    // ==================== CUSTOM JPQL QUERY TESTS ====================

    @Test
    @DisplayName("Should find all emails")
    void findAllEmails_existingUsers_ReturnsAllEmails() {

        List<String> emails = userRepository.findAllEmails();

        assertThat(emails).hasSize(3);
    }
}
