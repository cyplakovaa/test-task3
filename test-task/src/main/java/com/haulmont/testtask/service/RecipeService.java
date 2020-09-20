package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.RecipeDAO;
import com.haulmont.testtask.database.DbConnection;
import com.haulmont.testtask.entity.Recipe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeService extends DbConnection implements RecipeDAO {

    private Connection connection = getConnection();

    @Override
    public void add(Recipe object) throws SQLException {
        Recipe recipe = new Recipe();
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO recipes_tbl(description_fld, patient_id_fld, doctor_id_fld, " +
                "creationDate_fld, validity_fld, priority_id_fld) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getDescription());
            preparedStatement.setLong(2, object.getPatient().getId());
            preparedStatement.setLong(3, object.getDoctor().getId());
            Date creationDate = new Date(object.getCreationDate().getTime());
            preparedStatement.setDate(4, creationDate);
            preparedStatement.setInt(5, object.getValidity());
            preparedStatement.setLong(6, object.getPriority().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public List<Recipe> getAll() throws SQLException {
        List<Recipe> recipeList = new ArrayList<>();
        String sql = "SELECT recipe_id, description_fld, patient_id_fld, doctor_id_fld, " +
                "creationDate_fld, validity_fld, priority_id_fld FROM recipes_tbl";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Recipe recipe = new Recipe();
                recipe.setId(resultSet.getLong("recipe_id"));
                recipe.setDescription(resultSet.getString("description_fld"));
                PatientService patientService = new PatientService();
                recipe.setPatient(patientService.getById(resultSet.getLong("patient_id_fld")));
                DoctorService doctorService = new DoctorService();
                recipe.setDoctor(doctorService.getById(resultSet.getLong("doctor_id_fld")));
                recipe.setCreationDate(resultSet.getDate("creationDate_fld"));
                recipe.setValidity(resultSet.getInt("validity_fld"));
                PriorityOfRecipeService priorityOfRecipeService = new PriorityOfRecipeService();
                recipe.setPriority(priorityOfRecipeService.getById(resultSet.getLong("priority_id_fld")));
                recipeList.add(recipe);
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
        return recipeList;
    }

    @Override
    public Recipe getById(Long id) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "SELECT recipe_id, description_fld, patient_id_fld, doctor_id_fld, " +
                "creationDate_fld, validity_fld, priority_id_fld FROM recipes_tbl WHERE recipe_id = ?";
        Recipe recipe = new Recipe();

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                recipe.setId(resultSet.getLong("recipe_id"));
                recipe.setDescription(resultSet.getString("description_fld"));
                PatientService patientService = new PatientService();
                recipe.setPatient(patientService.getById(resultSet.getLong("patient_id_fld")));
                DoctorService doctorService = new DoctorService();
                recipe.setDoctor(doctorService.getById(resultSet.getLong("doctor_id_fld")));
                recipe.setCreationDate(resultSet.getDate("creationDate_fld"));
                recipe.setValidity(resultSet.getInt("validity_fld"));
                PriorityOfRecipeService priorityOfRecipeService = new PriorityOfRecipeService();
                recipe.setPriority(priorityOfRecipeService.getById(resultSet.getLong("priority_id_fld")));
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
        return recipe;
    }

    @Override
    public void update(Recipe object) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE recipes_tbl SET description_fld = ?, patient_id_fld = ?, " +
                "doctor_id_fld = ?, creationDate_fld = ?, validity_fld = ?, " +
                "priority_id_fld = ? WHERE recipe_id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, object.getDescription());
            preparedStatement.setLong(2, object.getPatient().getId());
            preparedStatement.setLong(3, object.getDoctor().getId());
            Date creationDate = new Date(object.getCreationDate().getTime());
            preparedStatement.setDate(4, creationDate);
            preparedStatement.setInt(5, object.getValidity());
            preparedStatement.setLong(6, object.getPriority().getId());
            preparedStatement.setLong(7, object.getId());

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
    public void remove(Recipe object) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "DELETE FROM recipes_tbl WHERE recipe_id = ?";

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


    public List<Recipe> filterRecipe(Long patientId, Long priorityId, String description){
        List<Recipe> filteredRecipesList = new ArrayList<>();
        PreparedStatement preparedStatement = null;

        String patientFilterCondition = patientId > -1 ? " AND patient_id_fld = " + patientId : "";
        String priorityFilterCondition = priorityId > -1 ? " AND priority_id_fld = " + priorityId : "";
        String descriptionFilterCondition = description.length() > 0 ? " AND description_fld LIKE '%" + description + "%'" : "";

//        String sql = "SELECT * FROM recipes_tbl WHERE patient_id_fld = " + patientId + " AND priority_id_fld = " + priorityId + " AND description_fld LIKE '%" + description + "%'";
        String sql = "SELECT * FROM recipes_tbl WHERE TRUE " + patientFilterCondition + priorityFilterCondition + descriptionFilterCondition;

        try {
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Recipe recipe = new Recipe();
                recipe.setId(resultSet.getLong("recipe_id"));
                recipe.setDescription(resultSet.getString("description_fld"));
                PatientService patientService = new PatientService();
                recipe.setPatient(patientService.getById(resultSet.getLong("patient_id_fld")));
                DoctorService doctorService = new DoctorService();
                recipe.setDoctor(doctorService.getById(resultSet.getLong("doctor_id_fld")));
                recipe.setCreationDate(resultSet.getDate("creationDate_fld"));
                recipe.setValidity(resultSet.getInt("validity_fld"));
                PriorityOfRecipeService priorityOfRecipeService = new PriorityOfRecipeService();
                recipe.setPriority(priorityOfRecipeService.getById(resultSet.getLong("priority_id_fld")));
                filteredRecipesList.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredRecipesList;
    }
}
