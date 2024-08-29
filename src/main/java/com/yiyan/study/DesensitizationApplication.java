package com.yiyan.study;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yiyan.study.database.sqlite.SQLiteHelper;
import com.yiyan.study.database.opengauss.OpengaussHelper;
import com.yiyan.study.model.DesensitizationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 启动类
 */
@SpringBootApplication
@Slf4j
@RestController
public class DesensitizationApplication {

    private static final DesensitizationDTO dto = com.yiyan.study.model.DesensitizationDTO.builder()
            .username("呼延泽")
            .phoneNumber("19855362406")
            .email("3114077197@qq.com")
            .address("安徽省芜湖市")
            .note("！ＣＸ＠Ｘ＃Ｘ")
            .idCardNum("340204200304073219")
            .build();

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(DesensitizationApplication.class, args);

        // ObjectMapper mapper = new ObjectMapper();
        // String s = mapper.writeValueAsString(dto);
        // log.info("Json : {}", s);

        // _test();
    }

    private static void _test() {
        // _sqliteTest();
        _opengaussTest();
    }

    private static void _opengaussTest() {
        (new OpengaussHelper()).test();
    }

    private static void _sqliteTest() {
        // sqlite test
        SQLiteHelper dbHelper = new SQLiteHelper("jdbc:sqlite:F:\\zdlRepository\\test\\sqlite_db\\single_table.db");

        // 获取所有表
        List<String> tables = dbHelper.getAllTables();
        System.out.println("Tables: " + tables);

        // 获取指定表的所有列名
        String tableName = tables.get(0);
        List<String> columns = dbHelper.getColumns(tableName);
        System.out.println("Columns in " + tableName + ": " + columns);

        // 获取指定表的所有列名和数据
        Map<String, List<Object>> tableData = dbHelper.getTableData(tableName);
        System.out.println("Data in " + tableName + ":");
        for (Map.Entry<String, List<Object>> entry : tableData.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // test: create new db
        dbHelper.createDatabaseWithSameStructure(dbHelper.getAllTableData(),
                "jdbc:sqlite:F:\\zdlRepository\\test\\sqlite_db\\new_single_table.db");

        // 关闭连接
        dbHelper.close();
    }

    @GetMapping("/test")
    public DesensitizationDTO desensitizationTest() {
        return dto;
    }
}
