package com.springosyrs.spring5recipeapp.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImageFile(Long id, MultipartFile file);
}
