package com.springosyrs.spring5recipeapp.controllers;

import com.springosyrs.spring5recipeapp.commands.IngredientCommand;
import com.springosyrs.spring5recipeapp.commands.RecipeCommand;
import com.springosyrs.spring5recipeapp.services.IngredientService;
import com.springosyrs.spring5recipeapp.services.RecipeService;
import com.springosyrs.spring5recipeapp.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;
    @Mock
    UnitOfMeasureService unitOfMeasureService;
    IngredientController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    public void testListIngredients() throws Exception {
        //given
        var recipeCommand = new RecipeCommand();
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        //when
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));
        //then
        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void testShowIngredient() throws Exception {
        //given
        var ingredientCommand = new IngredientCommand();
        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong()))
                .thenReturn(ingredientCommand);
        //then
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void testNewIngredientForm() throws Exception {
        //given
        var recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        //when
        when(recipeService.findCommandById(anyLong()))
                .thenReturn(recipeCommand);
        when(unitOfMeasureService.listAllUom()).thenReturn(new HashSet<>());
        //then
        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

    }

    @Test
    public void testUpdateIngredientForm() throws Exception {
        //given
        var ingredientCommand = new IngredientCommand();
        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong()))
                .thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAllUom()).thenReturn(new HashSet<>());
        //then
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        //given
        var command = new IngredientCommand();
        command.setId(3L);
        command.setRecipeId(2L);

        //when
        when(ingredientService.saveIngredientCommand(any())).thenReturn(command);
        //then
        mockMvc.perform(
                        post("/recipe/2/ingredient")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "")
                                .param("description", "some string")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));
    }

    @Test
    public void deleteRecipeIngredient() throws Exception {
        mockMvc.perform(get("/recipe/2/ingredient/3/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredients"));
        verify(ingredientService).deleteById(anyLong(), anyLong());
    }
}