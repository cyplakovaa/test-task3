package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Recipe;

import java.sql.SQLException;
import java.util.List;

public interface RecipeDAO extends GeneralDAO<Recipe>{


    void add(Recipe object) throws SQLException;

    List<Recipe> getAll() throws SQLException;


    Recipe getById(Long id) throws SQLException;


    void update(Recipe object) throws SQLException;


    void remove(Recipe object) throws SQLException;
}
