package com.example.market_management.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://103.200.22.212:3306/tmquangt_supermarket", "tmquangt_supermarket", "Matkhau123@@");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}