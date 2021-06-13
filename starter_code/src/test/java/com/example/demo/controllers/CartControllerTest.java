package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void test_add_to_cart_200_OK(){
        User testUser = createUser();
        Item testItem = createItem();

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("TestUser");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(testUser);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(testItem));

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void test_add_to_cart_User_404_NotFound(){
        Item testItem = createItem();

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("TestUser");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(null);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(testItem));

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNull(responseEntity.getBody());
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void test_add_to_cart_Item_404_NotFound(){
        User testUser = createUser();

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("TestUser");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(testUser);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.empty());

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNull(responseEntity.getBody());
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    private static User createUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setPassword("TestPassword");
        user.setCart(new Cart());

        return user;
    }

    private static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("TestItem1");
        item.setPrice(new BigDecimal("3.66"));
        item.setDescription("The first test item");

        return item;
    }
}
