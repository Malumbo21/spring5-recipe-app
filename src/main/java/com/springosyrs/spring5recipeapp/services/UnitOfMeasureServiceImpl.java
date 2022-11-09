package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.springosyrs.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.springosyrs.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand convertor;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, UnitOfMeasureToUnitOfMeasureCommand convertor) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.convertor = convertor;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUom() {
        return StreamSupport.stream(unitOfMeasureRepository.findAll()
                        .spliterator(), false)
                .map(convertor::convert)
                .collect(Collectors.toSet());
    }
}
