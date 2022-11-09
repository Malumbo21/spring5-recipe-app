package com.springosyrs.spring5recipeapp.controllers;

import com.springosyrs.spring5recipeapp.commands.RecipeCommand;
import com.springosyrs.spring5recipeapp.services.ImageService;
import com.springosyrs.spring5recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {
    @Mock
    ImageService imageService;
    @Mock
    RecipeService recipeService;

    ImageController imageController;
    MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageController = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void handleImagePost() throws Exception {
        var multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                "Malumbo Sinkamba".getBytes());
        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));
        verify(imageService).saveImageFile(anyLong(), any());
    }

    @Test
    public void renderImageFromDB() throws Exception {
        var command = new RecipeCommand();
        command.setId(1L);

        var s = "fake image data";
        var byteArray = new Byte[s.getBytes().length];
        int i = 0;
        for (var b : s.getBytes()) {
            byteArray[i++] = b;
        }
        command.setImage(byteArray);
        when(recipeService.findCommandById(anyLong()))
                .thenReturn(command);
        var response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseBytes = response.getContentAsByteArray();
        assertEquals(s.getBytes().length, responseBytes.length);
    }

    @Test
    public void testGetImageNumberFormatException() throws Exception {
        mockMvc.perform(get("/recipe/abc/recipeimage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}