package com.springosyrs.spring5recipeapp.controllers;

import com.springosyrs.spring5recipeapp.commands.IngredientCommand;
import com.springosyrs.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.springosyrs.spring5recipeapp.services.IngredientService;
import com.springosyrs.spring5recipeapp.services.RecipeService;
import com.springosyrs.spring5recipeapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/recipe")
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService uomService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService uomService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.uomService = uomService;
    }

    @GetMapping("/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredient list for recipe id: " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));
        return "recipe/ingredient/list";
    }

    @GetMapping("/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String id,
                                       Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId),
                Long.valueOf(id))
        );
        return "recipe/ingredient/show";
    }

    @GetMapping("/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id,
                                         Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId),
                Long.valueOf(id))
        );
        model.addAttribute("uomList", uomService.listAllUom());
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/{recipeId}/ingredient/new")
    public String newRecipeIngredient(@PathVariable String recipeId,
                                      Model model) {
        var recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));
        //TODO raise exception if recipe is null;
        var ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());
        ingredientCommand.setUom(new UnitOfMeasureCommand());
        model.addAttribute("ingredient", ingredientCommand);

        model.addAttribute("uomList", uomService.listAllUom());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        var savedCommand = ingredientService.saveIngredientCommand(command);
        log.debug("Saved Recipe Id: " + savedCommand.getRecipeId());
        log.debug("Saved Ingredient Id: " + savedCommand.getId());
        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/{recipeId}/ingredient/{id}/delete")
    public String deleteRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id,
                                         Model model) {
        log.debug("deleting ingredient id:" + id);
        ingredientService.deleteById(Long.valueOf(recipeId), Long.valueOf(id));
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
