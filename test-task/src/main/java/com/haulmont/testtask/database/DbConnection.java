package com.haulmont.testtask.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String DB_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
    private static final String DB_URL = "jdbc:hsqldb:file:/Users/aleksandrcyplakov" +
            "/Desktop/Education/Java/test-task3/polyclinic_db/aleksandrcyplakov";
//    private static final String DB_URL = "jdbc:hsqldb:file:~test-task3/polyclinic_db/aleksandrcyplakov";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "";

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connection created successfully");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}