package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUom();
}
