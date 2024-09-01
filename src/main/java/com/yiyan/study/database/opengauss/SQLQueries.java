package com.yiyan.study.database.opengauss;

public class SQLQueries {
    public static final String[] createTableQueries = {
            "CREATE TABLE IF NOT EXISTS \"user\" (" +
                    "user_id SERIAL PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL UNIQUE," +
                    "password VARCHAR(255) NOT NULL," +
                    "email VARCHAR(100)," +
                    "last_login TIMESTAMP," +
                    "last_ip VARCHAR(50)," +
                    "delete_flag BOOLEAN DEFAULT FALSE," +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "creater_id INTEGER," +
                    "updater_id INTEGER" +
                    ");",

            "CREATE TABLE IF NOT EXISTS file (" +
                    "file_id SERIAL PRIMARY KEY," +
                    "file_name VARCHAR(255)," +
                    "file_path VARCHAR(500)," +
                    "file_type VARCHAR(255)," +
                    "file_size INTEGER," +
                    "file_status VARCHAR(20)," +
                    "delete_flag BOOLEAN DEFAULT FALSE," +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "creater_id INTEGER," +
                    "updater_id INTEGER" +
                    ");",

            "CREATE TABLE IF NOT EXISTS desen_task (" +
                    "task_id SERIAL PRIMARY KEY," +
                    "file_id INTEGER REFERENCES file(file_id)," +
                    "rule_id VARCHAR(50)," +
                    "task_status VARCHAR(20)," +
                    "start_time TIMESTAMP," +
                    "end_time TIMESTAMP," +
                    "delete_flag BOOLEAN DEFAULT FALSE," +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "creater_id INTEGER," +
                    "updater_id INTEGER" +
                    ");"
    };
}
