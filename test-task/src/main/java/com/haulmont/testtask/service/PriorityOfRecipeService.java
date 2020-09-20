package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.PriorityOfRecipeDAO;
import com.haulmont.testtask.database.DbConnection;
import com.haulmont.testtask.entity.PriorityOfRecipe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PriorityOfRecipeService extends DbConnection implements PriorityOfRecipeDAO {

    private Connection connection = getConnection();

    @Override
    public void add(PriorityOfRecipe object) {

    }

    @Override
    public List<PriorityOfRecipe> getAll() throws SQLException {
        List<PriorityOfRecipe> priorityOfRecipeList = new ArrayList<>();

        String sql = "SELECT * FROM priorityOfRecipe_tbl";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                PriorityOfRecipe priorityOfRecipe = new PriorityOfRecipe();
                priorityOfRecipe.setId(resultSet.getLong("priority_id"));
                priorityOfRecipe.setPriorityName(resultSet.getString("priorityName_fld"));
                priorityOfRecipeList.add(priorityOfRecipe);
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
        return priorityOfRecipeList;

    }
    @Override
    public PriorityOfRecipe getById(Long id) throws SQLException {

        PreparedStatement preparedStatement = null;
        String sql = "SELECT * FROM priorityOfRecipe_tbl WHERE priority_id = ?";
        PriorityOfRecipe priorityOfRecipe = new PriorityOfRecipe();

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                priorityOfRecipe.setId(resultSet.getLong("priority_id"));
                priorityOfRecipe.setPriorityName(resultSet.getString("priorityName_fld"));
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
        return priorityOfRecipe;
    }

    @Override
    public void update(PriorityOfRecipe object) {

    }

    @Override
    public void remove(PriorityOfRecipe object) {

    }
}
