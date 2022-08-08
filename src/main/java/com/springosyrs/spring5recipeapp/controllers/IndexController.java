package com.springosyrs.spring5recipeapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

public class IndexController {
    @RequestMapping({"", "/", "/index", "/index.html"})
    public String getIndexPage() {
        return "index";
    }

}
