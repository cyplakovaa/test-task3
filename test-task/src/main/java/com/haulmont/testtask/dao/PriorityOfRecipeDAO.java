package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.PriorityOfRecipe;

import java.sql.SQLException;
import java.util.List;

public interface PriorityOfRecipeDAO extends GeneralDAO<PriorityOfRecipe> {

    @Override
    void add(PriorityOfRecipe object);

    @Override
    List<PriorityOfRecipe> getAll() throws SQLException;

    @Override
    PriorityOfRecipe getById(Long id) throws SQLException;

    @Override
    void update(PriorityOfRecipe object);

    @Override
    void remove(PriorityOfRecipe object);


}
