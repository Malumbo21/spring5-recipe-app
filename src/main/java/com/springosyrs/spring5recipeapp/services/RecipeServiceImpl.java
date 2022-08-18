package com.springosyrs.spring5recipeapp.services;

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

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("i'm in a service");
        var data = new HashSet<Recipe>();
        recipeRepository.findAll().iterator().forEachRemaining(data::add);
        return data;
    }
}
