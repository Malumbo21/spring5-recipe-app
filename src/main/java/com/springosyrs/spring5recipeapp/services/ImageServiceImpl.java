package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(Long id, MultipartFile file) {
        try {
            var recipe = recipeRepository.findById(id).get();
            var byteObject = new Byte[file.getBytes().length];
            int i = 0;
            for (var b : file.getBytes()) {
                byteObject[i++] = b;
            }
            recipe.setImage(byteObject);
            recipeRepository.save(recipe);
        } catch (IOException e) {
            log.error("Error occurred", e);
            e.printStackTrace();
        }
    }
}
