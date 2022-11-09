package com.springosyrs.spring5recipeapp.services;

import com.springosyrs.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.springosyrs.spring5recipeapp.domain.UnitOfMeasure;
import com.springosyrs.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand
            = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureService service;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void listAllUom() {
        //given
        var unitOfMeasures = new HashSet<UnitOfMeasure>();
        var uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        unitOfMeasures.add(uom1);
        var uom2 = new UnitOfMeasure();
        uom2.setId(1L);
        unitOfMeasures.add(uom2);
        when(unitOfMeasureRepository.findAll())
                .thenReturn(unitOfMeasures);
        //when
        var commands = service.listAllUom();
        //then
        assertEquals(2, commands.size());
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}