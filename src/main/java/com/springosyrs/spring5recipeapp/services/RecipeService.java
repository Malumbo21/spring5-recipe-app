package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
}
