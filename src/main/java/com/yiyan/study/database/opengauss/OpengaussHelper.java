package com.yiyan.study.database.opengauss;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpengaussHelper {

    // 数据库连接信息
    String url = "jdbc:opengauss://127.0.0.1:15432/test_db";
    String user = "gaussdb";
    String password = "openGauss@123";

    // JDBC 连接对象
    Connection conn = null;
    Statement stmt = null;

    public OpengaussHelper() {
        _init();
    }

    public OpengaussHelper(String host, int port, String dbName, String username, String password) {
        if (host.isEmpty())
            host = "127.0.0.1";
        url = String.format("jdbc:opengauss://%s:%d/%s", host, port, dbName);
        user = username;
        this.password = password;

        _init();
    }

    private void _init() {
        try {
            // 创建连接
            conn = getConnection();
            // 创建Statement对象来执行SQL查询
            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            // 加载JDBC驱动程序
            Class.forName("org.opengauss.Driver");

            // 创建并返回新的连接
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 执行sql
    public void execute(String sql) {
        try {
            // 执行查询
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 执行查询
    public ResultSet excuteQuery(String sql) {
        try {
            // 执行查询
            return stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 执行更新
    public int executeUpdate(String sql) {
        try {
            // 执行查询
            return stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 查询所有列名, 返回List<String>
    public List<String> getColumns(String tableName, String schema) {
        if (schema.isEmpty())
            schema = "public";
        List<String> columns = new ArrayList<>();
        try {
            ResultSet rs = conn.getMetaData().getColumns(tableName, schema, null, null);
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columns;
    }

    // 对于指定的表，返回所有列名+数据，返回Map<String,List>
    public Map<String, List<Object>> getTableData(String tableName) {
        Map<String, List<Object>> tableData = new HashMap<>();
        List<String> columns = getColumns(tableName, "public");

        // 初始化结果Map
        for (String column : columns) {
            tableData.put(column, new ArrayList<>());
        }

        try {
            // 执行查询
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            while (rs.next()) {
                for (String column : columns) {
                    tableData.get(column).add(rs.getObject(column));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableData;
    }

    // 关闭连接
    public void close() {
        try {
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        try {
            // 执行查询
            List<String> columns = getColumns("test_table", "public");
            System.out.println("opengauss columnNames: " + columns);
            Map<String, List<Object>> tableData = getTableData("test_table");
            System.out.println("opengauss tableData: " + tableData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}