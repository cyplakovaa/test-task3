package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.database.DbConnection;
import com.haulmont.testtask.entity.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorService extends DbConnection implements DoctorDAO {

    private Connection connection = getConnection();

    @Override
    public void add(Doctor object) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO doctors_tbl (firstName_fld, lastName_fld, " +
                "patronymic_fld, specialization_fld) " +
                "VALUES (?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, object.getFirstName());
            preparedStatement.setString(2, object.getLastName());
            preparedStatement.setString(3, object.getPatronymic());
            preparedStatement.setString(4, object.getSpecialization());

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
    public List<Doctor> getAll() throws SQLException {
        List<Doctor> doctorList = new ArrayList<>();
        String sql = "SELECT doctor_id, firstName_fld, lastName_fld, patronymic_fld, specialization_fld FROM doctors_tbl";

        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Doctor doctor = new Doctor();
                doctor.setId(resultSet.getLong("doctor_id"));
                doctor.setFirstName(resultSet.getString("firstName_fld"));
                doctor.setLastName(resultSet.getString("lastName_fld"));
                doctor.setPatronymic(resultSet.getString("patronymic_fld"));
                doctor.setSpecialization(resultSet.getString("specialization_fld"));

                doctorList.add(doctor);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
            return doctorList;
    }

    @Override
    public Doctor getById(Long id) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "SELECT doctor_id, firstName_fld, lastName_fld, " +
                "patronymic_fld, specialization_fld FROM doctors_tbl WHERE doctor_id = ?";
        Doctor doctor = new Doctor();

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                doctor.setId(resultSet.getLong("doctor_id"));
                doctor.setFirstName(resultSet.getString("firstName_fld"));
                doctor.setLastName(resultSet.getString("lastName_fld"));
                doctor.setPatronymic(resultSet.getString("patronymic_fld"));
                doctor.setSpecialization(resultSet.getString("specialization_fld"));
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
        return doctor;
    }

    @Override
    public void update(Doctor object) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE doctors_tbl SET firstName_fld = ?, lastName_fld = ?, " +
                "patronymic_fld = ?, specialization_fld = ? WHERE doctor_id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, object.getFirstName());
            preparedStatement.setString(2, object.getLastName());
            preparedStatement.setString(3, object.getPatronymic());
            preparedStatement.setString(4, object.getSpecialization());
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
    public void remove(Doctor object) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "DELETE FROM doctors_tbl WHERE doctor_id = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, object.getId());

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

    public Map<Long, Integer> getDoctorStatistic() throws SQLException {
        Map<Long, Integer> doctorStatisticMap = null;
        PreparedStatement preparedStatement = null;
        String sql = "SELECT doctor_id, COUNT(recipe_id) FROM doctors_tbl " +
                "INNER JOIN recipes_tbl ON doctor_id = doctor_id_fld GROUP BY doctor_id";

        try {
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            doctorStatisticMap = new HashMap<>();

            while(resultSet.next()){
                Long doctorId = resultSet.getLong("doctor_id");
                Integer stats = resultSet.getInt(2);
                doctorStatisticMap.put(doctorId, stats);
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
        return doctorStatisticMap;
    }

}

