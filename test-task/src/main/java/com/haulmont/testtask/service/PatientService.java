package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.database.DbConnection;
import com.haulmont.testtask.database.DbException;
import com.haulmont.testtask.entity.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientService extends DbConnection implements PatientDAO {

    private  Connection connection = getConnection();

    @Override
    public void add(Patient object) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO patients_tbl (firstName_fld, lastName_fld, " +
                "patronymic_fld, phoneNumber_fld) VALUES (?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, object.getFirstName());
            preparedStatement.setString(2, object.getLastName());
            preparedStatement.setString(3, object.getPatronymic());
            preparedStatement.setString(4, object.getPhoneNumber());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
    }


    @Override
    public List<Patient> getAll() throws SQLException {
        List<Patient> patientList = new ArrayList<>();
        String sql = "SELECT patient_id, firstName_fld, lastName_fld, patronymic_fld, phoneNumber_fld FROM patients_tbl";

        PreparedStatement preparedStatement = null;
        try {
             preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Patient patient = new Patient();
                patient.setId(resultSet.getLong("patient_id"));
                patient.setFirstName(resultSet.getString("firstName_fld"));
                patient.setLastName(resultSet.getString("lastName_fld"));
                patient.setPatronymic(resultSet.getString("patronymic_fld"));
                patient.setPhoneNumber(resultSet.getString("phoneNumber_fld"));
                patientList.add(patient);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
        return patientList;
    }

    @Override
    public Patient getById(Long id) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "SELECT patient_id, firstName_fld, lastName_fld, patronymic_fld, " +
                "phoneNumber_fld FROM patients_tbl WHERE patient_id = ?";
        Patient patient = new Patient();

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                patient.setId(resultSet.getLong("patient_id"));
                patient.setFirstName(resultSet.getString("firstName_fld"));
                patient.setLastName(resultSet.getString("lastName_fld"));
                patient.setPatronymic(resultSet.getString("patronymic_fld"));
                patient.setPhoneNumber(resultSet.getString("phoneNumber_fld"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
        return patient;
    }

    @Override
    public void update(Patient object) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE patients_tbl SET firstName_fld = ?, lastName_fld = ?, " +
                "patronymic_fld = ?, phoneNumber_fld = ? WHERE patient_id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, object.getFirstName());
            preparedStatement.setString(2, object.getLastName());
            preparedStatement.setString(3, object.getPatronymic());
            preparedStatement.setString(4, object.getPhoneNumber());
            preparedStatement.setLong(5, object.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
    }

    @Override
    public void remove(Patient object) throws SQLException, DbException {
        PreparedStatement preparedStatement = null;
        String sql = "DELETE FROM patients_tbl WHERE patient_id = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, object.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DbException(throwables);

        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
    }
}
