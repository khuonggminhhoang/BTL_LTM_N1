package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import System.Config;

public class DAO {
    protected Connection conn;

    public DAO() {
        try {
            this.conn = DriverManager.getConnection(Config.JDBC_URL, Config.JDBC_USER, Config.JDBC_PASSWORD);
            System.out.println("Connect to mysql successfully");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
