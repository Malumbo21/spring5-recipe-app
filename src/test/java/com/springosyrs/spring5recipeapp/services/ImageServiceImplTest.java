package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.domain.Recipe;
import com.springosyrs.spring5recipeapp.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImageServiceImplTest {
    @Mock
    RecipeRepository recipeRepository;

    ImageService imageService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    public void saveImageFile() throws Exception {
        var id = 1L;
        var multipartFile = new MockMultipartFile("file", "testing.txt", "text/plain",
                "Malumbo Sinkamba".getBytes());
        var recipe = new Recipe();
        recipe.setId(id);
        var recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(anyLong()))
                .thenReturn(recipeOptional);
        var argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        //when
        imageService.saveImageFile(id, multipartFile);

        //then
        verify(recipeRepository).save(argumentCaptor.capture());
        var savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }

}