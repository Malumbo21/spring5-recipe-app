package com.springosyrs.spring5recipeapp.controllers;

import com.springosyrs.spring5recipeapp.domain.Recipe;
import com.springosyrs.spring5recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class RecipeControllerTest {
    @Mock
    RecipeService service;
    RecipeController controller;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new RecipeController(service);
    }

    @Test
    public void findById() throws Exception {
        var recipe = new Recipe();
        recipe.setId(1L);
        var mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        when(service.findById(anyLong())).thenReturn(recipe);
        mockMvc.perform(get("/recipe/show/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"));
    }
}
