package com.springosyrs.spring5recipeapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {
    Category category;

    @BeforeEach
    public void setup() {
        category = new Category();
    }

    @Test
    void getId() {
        category.setId(4L);
        assertEquals(4L, category.getId());
    }

    @Test
    void getDescription() {
    }

    @Test
    void getRecipes() {
    }
}
