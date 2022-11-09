package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.commands.IngredientCommand;
import com.springosyrs.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.springosyrs.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.springosyrs.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.springosyrs.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.springosyrs.spring5recipeapp.domain.Ingredient;
import com.springosyrs.spring5recipeapp.domain.Recipe;
import com.springosyrs.spring5recipeapp.repositories.RecipeRepository;
import com.springosyrs.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;
    IngredientService ingredientService;

    //init converters
    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, ingredientCommandToIngredient, unitOfMeasureRepository, recipeRepository);
    }

    @Test
    public void findByRecipeIdAndId() throws Exception {
    }

    @Test
    public void findByRecipeIdAndRecipeIdHappyPath() throws Exception {
        //given
        var recipe = new Recipe();
        recipe.setId(1L);

        var ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        var ingredient2 = new Ingredient();
        ingredient2.setId(1L);

        var ingredient3 = new Ingredient();
        ingredient3.setId(3L);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        var recipeOptional = Optional.of(recipe);

        when(
                recipeRepository
                        .findById(anyLong())
        )
                .thenReturn(recipeOptional);

        //then
        var ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);

        //when
        assertEquals(Long.valueOf(3L), ingredientCommand.getId());
        assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByRecipeIdAndIngredientId() {
    }

    @Test
    void saveIngredientCommand() {
        //given
        var command = new IngredientCommand();
        command.setId(3L);
        command.setRecipeId(2L);
        var recipeOptional = Optional.of(new Recipe());
        var savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId(3L);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);
        //when
        var savedCommand = ingredientService.saveIngredientCommand(command);
        //then
        assertEquals(3L, savedCommand.getId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void deleteById() {
        var recipe = new Recipe();
        var ingredient = new Ingredient();
        ingredient.setId(3L);
        recipe.addIngredient(ingredient);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        //when
        ingredientService.deleteById(1L, 3L);

        //then
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));
    }
}