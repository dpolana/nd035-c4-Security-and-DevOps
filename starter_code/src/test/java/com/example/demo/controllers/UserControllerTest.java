package com.example.demo.controllers;

import com.example.demo.TestUtilis;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtilis.injectObjects(userController, "userRepository", userRepository);
        TestUtilis.injectObjects(userController, "cartRepository", cartRepository);
        TestUtilis.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void test_create_user_happy_path(){
        when(encoder.encode("TestPassword")).thenReturn("hashedTestPassword");
        CreateUserRequest cru = new CreateUserRequest();
        cru.setUsername("TestUser");
        cru.setPassword("TestPassword");
        cru.setConfirmPassword("TestPassword");

        ResponseEntity<User> responseEntity = userController.createUser(cru);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User userCreated = responseEntity.getBody();
        assertNotNull(userCreated);
        assertEquals("TestUser", userCreated.getUsername());
        assertEquals("hashedTestPassword", userCreated.getPassword());
    }

    @Test
    public void test_find_by_username(){
        when(encoder.encode("TestPassword")).thenReturn("hashedTestPassword");
        when(userRepository.findByUsername("TestUser")).thenReturn(simulateUser());
        CreateUserRequest cru = new CreateUserRequest();
        cru.setUsername("TestUser");
        cru.setPassword("TestPassword");
        cru.setConfirmPassword("TestPassword");

        ResponseEntity<User> responseEntity = userController.createUser(cru);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        User userCreated = responseEntity.getBody();

        ResponseEntity<User> userResponseEntity = userController.findByUserName(userCreated.getUsername());
        User userSearched = userResponseEntity.getBody();
        assertNotNull(userSearched);
        assertEquals("TestUser", userSearched.getUsername());
    }

    private User simulateUser(){
        User user = new User();
        user.setUsername("TestUser");
        user.setPassword("TestPassword");

        return user;
    }
}
