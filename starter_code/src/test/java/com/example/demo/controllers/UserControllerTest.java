package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.util.TestUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
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
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "encoder", encoder);
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

    @Test
    public void test_create_user_400_bad_request(){
       // when(encoder.encode("TestPassword")).thenReturn("hashedTestPassword");
        CreateUserRequest cru = new CreateUserRequest();
        cru.setUsername("TestUser");
        cru.setPassword("TestPasswordTestUser");
        cru.setConfirmPassword("TestPasswordTestUser");

        ResponseEntity<User> responseEntity = userController.createUser(cru);
        assertNull(responseEntity.getBody());
        assertEquals(400, responseEntity.getStatusCodeValue());

    }

    private User simulateUser(){
        User user = new User();
        user.setUsername("TestUser");
        user.setPassword("TestPassword");

        return user;
    }
}
