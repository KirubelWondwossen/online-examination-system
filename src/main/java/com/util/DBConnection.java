package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // Database credentials - TO BE CONFIGURED
    private static final String URL =
    "jdbc:mysql://localhost:3306/online_exam_system" +
    "?useSSL=false" +
    "&allowPublicKeyRetrieval=true" +
    "&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    // Load Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
