package com.springosyrs.spring5recipeapp.repositories;

import com.springosyrs.spring5recipeapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
