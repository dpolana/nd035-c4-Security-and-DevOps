package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private CartController cartController;

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        cartController = new CartController();
    }

    @Test
    public void test_submit_cart_200_OK(){
        User testUser = loadUser();
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        ResponseEntity<UserOrder> responseEntity = orderController.submit(testUser.getUsername());

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void test_submit_cart_404_user_not_found(){
        User testUser = loadUser();
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(null);
        ResponseEntity<UserOrder> responseEntity = orderController.submit(testUser.getUsername());

        assertNull(responseEntity.getBody());
        assertEquals(404, responseEntity.getStatusCodeValue());
    }


    private static User loadUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setPassword("TestPassword");
        user.setCart(fillCart());

        return user;
    }

    private static Item loadItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("TestItem1");
        item.setPrice(new BigDecimal("3.66"));
        item.setDescription("The first test item");

        return item;
    }

    private static Cart fillCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(Collections.singletonList(loadItem()));
        cart.setTotal(new BigDecimal("3.66"));
        return cart;
    }
}
