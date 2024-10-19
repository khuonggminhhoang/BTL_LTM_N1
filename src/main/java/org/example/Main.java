package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import System.Config;

public class Main {
    public static void main(String[] args) {
        try {
            // Tạo kết nối
            Connection connection = DriverManager.getConnection(Config.JDBC_URL, Config.JDBC_USER, Config.JDBC_PASSWORD);
            System.out.println("Kết nối thành công!");


            // Đóng kết nối
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}