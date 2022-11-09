package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.commands.IngredientCommand;
import com.springosyrs.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.springosyrs.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.springosyrs.spring5recipeapp.domain.Ingredient;
import com.springosyrs.spring5recipeapp.domain.Recipe;
import com.springosyrs.spring5recipeapp.repositories.RecipeRepository;
import com.springosyrs.spring5recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final RecipeRepository recipeRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, UnitOfMeasureRepository unitOfMeasureRepository, RecipeRepository recipeRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        var recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()) log.error("recipe id not found id :" + recipeId);

        var recipe = recipeOptional.get();
        var ingredientOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();
        if (ingredientOptional.isEmpty()) log.error("Ingredient Id not found: " + ingredientId);

        return ingredientOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());
        if (recipeOptional.isEmpty()) {
            //todo toss error if not found
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        }
        var recipe = recipeOptional.get();

        var ingredientOptional = recipe
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findAny();
        if (ingredientOptional.isPresent()) {
            var ingredient = ingredientOptional.get();
            ingredient.setAmount(command.getAmount());
            ingredient.setDescription(command.getDescription());
            ingredient.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
                    //todo address this
                    .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))
            );
        } else {
            Ingredient ingredient = ingredientCommandToIngredient.convert(command);
            recipe.addIngredient(requireNonNull(ingredient));
        }
        var savedRecipe = recipeRepository.save(recipe);
        var savedIngredientOptional = savedRecipe
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();
        if (savedIngredientOptional.isEmpty()) {
            savedIngredientOptional = savedRecipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
                    .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                    .filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }
        return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        log.debug("Deleting ingredient: " + recipeId + ":" + ingredientId);

        var recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            var recipe = recipeOptional.get();
            log.debug("Found recipe");
            var ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();
            if (ingredientOptional.isPresent()) {
                log.debug("found ingredient");
                var ingredient = ingredientOptional.get();
                ingredient.setRecipe(null);
                recipe.getIngredients().remove(ingredient);
                recipeRepository.save(recipe);

            }
        } else {
            log.debug("RecipeId not found: " + recipeId);
        }
    }

}
