package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Doctor;

import java.sql.SQLException;
import java.util.List;

public interface DoctorDAO extends GeneralDAO<Doctor> {
    @Override
    void add(Doctor object) throws SQLException;

    @Override
    List<Doctor> getAll() throws SQLException;

    @Override
    Doctor getById(Long id) throws SQLException;

    @Override
    void update(Doctor object) throws SQLException;

    @Override
    void remove(Doctor object) throws SQLException;

}
