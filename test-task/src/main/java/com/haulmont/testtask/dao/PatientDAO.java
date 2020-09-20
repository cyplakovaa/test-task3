package com.haulmont.testtask.dao;

import com.haulmont.testtask.database.DbException;
import com.haulmont.testtask.entity.Patient;

import java.sql.SQLException;
import java.util.List;

public interface PatientDAO extends GeneralDAO<Patient> {

    @Override
    void add(Patient object) throws SQLException;

    @Override
    List<Patient> getAll() throws SQLException;

    @Override
    Patient getById(Long id) throws SQLException;

    @Override
    void update(Patient object) throws SQLException;

    @Override
    void remove(Patient object) throws SQLException, DbException;
}
