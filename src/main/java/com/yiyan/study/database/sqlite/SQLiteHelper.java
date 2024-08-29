package com.yiyan.study.database.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteHelper {
    private Connection connection;

    // 带数据库URL的构造函数
    // "jdbc:sqlite:F:\\zdlRepository\\test\\sqlite_db\\single_table.db"
    public SQLiteHelper(String url) {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("数据库连接失败: " + e.getMessage());
        }
    }

    // 读取数据库的所有表，返回List
    public List<String> getAllTables() {
        List<String> tables = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            System.out.println("读取所有表失败: " + e.getMessage());
        }
        return tables;
    }

    // 对于指定的表，返回所有列名，返回List
    public List<String> getColumns(String tableName) {
        List<String> columns = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getColumns(null, null, tableName, "%")) {
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            System.out.println("读取列名失败: " + e.getMessage());
        }
        return columns;
    }

    // 对于指定的表，返回所有列名+数据，返回Map<String,List>
    public Map<String, List<Object>> getTableData(String tableName) {
        Map<String, List<Object>> tableData = new HashMap<>();
        List<String> columns = getColumns(tableName);

        // 初始化结果Map
        for (String column : columns) {
            tableData.put(column, new ArrayList<>());
        }

        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                for (String column : columns) {
                    tableData.get(column).add(rs.getObject(column));
                }
            }
        } catch (SQLException e) {
            System.out.println("读取表数据失败: " + e.getMessage());
        }
        return tableData;
    }

    // 获取数据库中的所有表及其数据
    // Map<String,Map> allTableData = {
    //  table_name1: {
    //      column1:List
    //      column2:List
    //      ...
    //    }
    //  table_name2: {
    //      column1:List
    //      column2:List
    //      ...
    //    }
    //    ...
    // }
    public Map<String, Map<String, List<Object>>> getAllTableData() {
        Map<String, Map<String, List<Object>>> allTablesData = new HashMap<>();
        List<String> tables = getAllTables();

        for (String table : tables) {
            Map<String, List<Object>> tableData = getTableData(table);
            allTablesData.put(table, tableData);
        }
        return allTablesData;
    }

    // 创建新数据库并复制表结构和数据
    // newDatabaseUrl = "jdbc:sqlite:/path/to/new_database.db";
    public void createDatabaseWithSameStructure( Map<String, Map<String, List<Object>>> newDatabaseData, String newDatabaseUrl) {
        try (Connection newConnection = DriverManager.getConnection(newDatabaseUrl)) {
            if (newConnection != null) {
                System.out.println("新数据库创建成功。");

                // 获取所有表
                List<String> tables = getAllTables();

                for (String table : tables) {
                    // 获取表结构
                    String createTableSQL = getCreateTableSQL(table);
                    if (createTableSQL != null) {
                        // 在新数据库中创建表
                        try (Statement stmt = newConnection.createStatement()) {
                            stmt.execute(createTableSQL);
                            System.out.println("表 " + table + " 创建成功。");
                        }

                    }

                    copyTableData(newDatabaseData.get(table), table, newConnection);
                     System.out.println("表 " + table + " 数据复制成功。");
                }
            }
        } catch (SQLException e) {
            System.out.println("创建新数据库失败: " + e.getMessage());
        }
    }

    // 获取创建表的SQL语句
    private String getCreateTableSQL(String tableName) {
        String createTableSQL = null;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT sql FROM sqlite_master WHERE type='table' AND name='" + tableName + "'")) {
            if (rs.next()) {
                createTableSQL = rs.getString("sql");
            }
        } catch (SQLException e) {
            System.out.println("获取创建表SQL失败: " + e.getMessage());
        }
        return createTableSQL;
    }

    // 复制表数据，从给定的 Map 中
    private void copyTableData(Map<String, List<Object>> tableData, String tableName, Connection newConnection) {
        try (Statement newStmt = newConnection.createStatement()) {
            List<String> columns = getColumns(tableName);

            // 确保 tableData 中包含所有需要的列
            if (tableData.keySet().containsAll(columns)) {
                for (int i = 0; i < tableData.get(columns.get(0)).size(); i++) { // 遍历每一行
                    StringBuilder sb = new StringBuilder("INSERT INTO " + tableName + " (");
                    StringBuilder values = new StringBuilder(" VALUES (");

                    for (String column : columns) {
                        sb.append(column).append(", ");
                        Object value = tableData.get(column).get(i);
                        if (value != null) {
                            values.append("'").append(value.toString().replace("'", "''")).append("', ");
                        } else {
                            values.append("NULL, ");
                        }
                    }

                    sb.setLength(sb.length() - 2); // 移除最后的逗号和空格
                    values.setLength(values.length() - 2); // 移除最后的逗号和空格

                    sb.append(")").append(values).append(");");

                    newStmt.execute(sb.toString());
                }
            } else {
                System.out.println("tableData 中的列与表结构不匹配。");
            }
        } catch (SQLException e) {
            System.out.println("复制表数据失败: " + e.getMessage());
        }
    }


    // 关闭连接
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("关闭连接失败: " + e.getMessage());
        }
    }
}
