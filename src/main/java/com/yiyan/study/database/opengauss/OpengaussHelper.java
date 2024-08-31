package com.yiyan.study.database.opengauss;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OpengaussHelper {

    private HikariDataSource dataSource;

    public OpengaussHelper() {
        _initDataSource("127.0.0.1", 15432, "postgres", "gaussdb", "openGauss@123");
        _initializeTables();
    }

    public OpengaussHelper(String host, int port, String dbName, String username, String password) {
        _initDataSource(host, port, dbName, username, password);
        _initializeTables();
    }

    private void _initDataSource(String host, int port, String dbName, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:opengauss://%s:%d/%s", host, port, dbName));
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.opengauss.Driver");

        // 配置连接池参数
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);

        dataSource = new HikariDataSource(config);
    }

    // 初始化数据库表
    private void _initializeTables() {
        for (String sql : SQLQueries.createTableQueries) {
            execute(sql);
        }
    }

    // 获取连接
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 执行 SQL 语句
    public void execute(String sql) {
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 执行查询
    public ResultSet executeQuery(String sql) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 执行更新
    public int executeUpdate(String sql) {
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 关闭连接池
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public void test() {
        try {
            execute("INSERT INTO \"user\" (username, password) VALUES ('testUser', 'testPass');");
            ResultSet rs = executeQuery("SELECT * FROM \"user\";");
            while (rs.next()) {
                System.out.println("User: " + rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
