package com.springosyrs.spring5recipeapp.repositories;

import com.springosyrs.spring5recipeapp.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    Optional<Ingredient> findByRecipeIdAndId(Long recipe, Long id);
}
