package com.yiyan.study.database.opengauss;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OpengaussHelper {

    // 数据库连接信息
    String url = "jdbc:opengauss://127.0.0.1:15432/test_db";
    String user = "gaussdb";
    String password = "openGauss@123";

    // JDBC 连接对象
    Connection conn = null;
    Statement stmt = null;

    public void test() {
        try {
            // 加载JDBC驱动程序
            Class.forName("org.opengauss.Driver");

            // 创建连接
            conn = DriverManager.getConnection(url, user, password);

            // 创建Statement对象来执行SQL查询
            stmt = conn.createStatement();
            String sql = "SELECT * FROM test_table";

            // 执行查询
            ResultSet rs = stmt.executeQuery(sql);

            // 处理结果
            while (rs.next()) {
                System.out.println("id: " + rs.getString("id"));
                // 根据实际表结构和需求处理查询结果
            }

            // 关闭结果集
            rs.close();
            // 关闭Statement
            stmt.close();
            // 关闭连接
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}