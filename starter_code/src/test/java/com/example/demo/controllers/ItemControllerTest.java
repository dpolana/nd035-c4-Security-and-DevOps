package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_find_item_by_name_200_OK(){
        List<Item> testItems = loadItems();
        String itemTestName = "FirstItem";
        when(itemRepository.findByName(itemTestName)).thenReturn(testItems);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(itemTestName);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void verify_find_item_by_name_404_NotFound(){
        List<Item> testItems = new ArrayList<>();
        String itemTestName = "FirstItem";
        when(itemRepository.findByName(itemTestName)).thenReturn(testItems);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(itemTestName);

        assertNull(responseEntity.getBody());
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void verify_find_item_by_id_200_OK(){
        Item testItem = loadItem();
        Long id = 1L;
        when(itemRepository.findById(id)).thenReturn(Optional.of(testItem));
        ResponseEntity<Item> responseEntity = itemController.getItemById(id);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    private static Item loadItem(){
        Item firstItem = new Item();
        firstItem.setId(1L);
        firstItem.setName("FirstItem");
        firstItem.setPrice(new BigDecimal("2.99"));
        firstItem.setDescription("This is the first item for testing");

        return firstItem;
    }

    private static List<Item> loadItems(){
        Item firstItem = new Item();
        firstItem.setId(1L);
        firstItem.setName("FirstItem");
        firstItem.setPrice(new BigDecimal("2.99"));
        firstItem.setDescription("This is the first item for testing");

        Item secondItem = new Item();
        firstItem.setId(2L);
        firstItem.setName("FirstItem");
        firstItem.setPrice(new BigDecimal("4.99"));
        firstItem.setDescription("This is the first item again but with more qualities, hence the expensive");

        return Arrays.asList(firstItem, secondItem);
    }
}
