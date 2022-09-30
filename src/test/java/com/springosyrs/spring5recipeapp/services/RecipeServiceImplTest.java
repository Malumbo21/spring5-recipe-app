package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.converters.RecipeCommandToRecipe;
import com.springosyrs.spring5recipeapp.converters.RecipeToRecipeCommand;
import com.springosyrs.spring5recipeapp.domain.Recipe;
import com.springosyrs.spring5recipeapp.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {
    RecipeServiceImpl recipeService;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }


    @Test
    public void getRecipes() {
        var recipe = new Recipe();
        var recipeData = new HashSet<Recipe>();
        recipeData.add(recipe);
        when(recipeRepository.findAll()).thenReturn(recipeData);
        var recipes = recipeService.getRecipes();
        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
    }


    @Test
    public void findById() {
        var recipe = new Recipe();
        recipe.setId(1L);
        var optional = Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(optional);
        var returnedRecipe = recipeService.findById(1L);
        assertNotNull("Null recipe returned", returnedRecipe);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }
}
