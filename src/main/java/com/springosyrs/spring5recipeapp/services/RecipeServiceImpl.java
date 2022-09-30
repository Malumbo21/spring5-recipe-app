package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.commands.RecipeCommand;
import com.springosyrs.spring5recipeapp.converters.RecipeCommandToRecipe;
import com.springosyrs.spring5recipeapp.converters.RecipeToRecipeCommand;
import com.springosyrs.spring5recipeapp.domain.Recipe;
import com.springosyrs.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("i'm in a service");
        var data = new HashSet<Recipe>();
        recipeRepository.findAll().iterator().forEachRemaining(data::add);
        return data;
    }

    @Override
    public Recipe findById(Long id) {
        var recipeOptional = recipeRepository.findById(id);
        return recipeOptional.orElseThrow(() -> new RuntimeException("recipe not found"));
    }

    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        var detached = recipeCommandToRecipe.convert(command);
        var savedRecipe = recipeRepository.save(detached);
        log.debug("Saved Recipe Id:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

}
