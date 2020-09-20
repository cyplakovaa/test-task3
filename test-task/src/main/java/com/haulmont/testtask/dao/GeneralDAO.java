package com.haulmont.testtask.dao;

import com.haulmont.testtask.database.DbException;

import java.sql.SQLException;
import java.util.List;

public interface GeneralDAO <T>{

    // create
    void add (T object) throws SQLException;

    //read
    List<T> getAll() throws SQLException;

    T getById(Long id) throws SQLException;

    // Update
    void update (T object) throws SQLException;

    //delete
    void remove (T object) throws SQLException, DbException;
}
