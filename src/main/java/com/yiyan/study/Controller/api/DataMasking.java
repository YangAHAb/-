package com.yiyan.study.controller.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiyan.study.controller.desensitization.identifyController;
import com.yiyan.study.database.sqlite.SQLiteHelper;

@RestController
@RequestMapping("/api")
public class DataMasking {
    @GetMapping("/identify")
    public ResponseEntity<Map<String, Object>> identify(
            @RequestParam("user_id") String userId,
            @RequestParam("task_id") String taskId) {
        Map<String, Object> response = new HashMap<>();
        // 根据user_id和task_id开始识别
        try {
            // 读取上传的数据库文件
            String dbName = userId + "_" + taskId + ".db";
            Path path = Paths.get("transfer", "uploaded-files", dbName);
            String url = "jdbc:sqlite:" + path.toString();
            SQLiteHelper sqLiteHelper = new SQLiteHelper(url);

            // 数据库表中各个表的列名，以及这些列是否可以进行脱敏处理的标志
            Map<String, List<String>> columnNames = sqLiteHelper.getAllColumns();
            Map<String, List<Boolean>> canMaskColumnNames = sqLiteHelper.getAllColumnsCanMask();

            // close db
            sqLiteHelper.close();

            response.put("status", "success");
            response.put("message", "识别任务已完成");
            response.put("userId", userId);
            response.put("taskId", taskId);
            response.put("columnNames", columnNames);
            response.put("canMaskColumnNames", canMaskColumnNames);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 处理异常情况
            response.put("error", "识别任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/mask")
    public ResponseEntity<Map<String, Object>> mask(
            @RequestParam("user_id") String userId,
            @RequestParam("task_id") String taskId) {
        Map<String, Object> response = new HashMap<>();
        // 根据user_id和task_id开始脱敏
        try {
            // 读取上传的数据库文件
            String dbName = userId + "_" + taskId + ".db";
            Path path = Paths.get("transfer", "uploaded-files", dbName);
            String url = "jdbc:sqlite:" + path.toString();
            SQLiteHelper sqLiteHelper = new SQLiteHelper(url);
            Map<String, Map<String, List<Object>>> dbData = sqLiteHelper.getAllTableData();

            // 脱敏处理
            Map<String, Map<String, List<Object>>> maskedDbData = maskData(dbData);

            // 保存脱敏后的数据到新的数据库文件
            String maskedDbName = userId + "_" + taskId + "_masked.db";
            Path maskedPath = Paths.get("transfer", "downloaded-files", maskedDbName);
            String maskedUrl = "jdbc:sqlite:" + maskedPath.toString();
            sqLiteHelper.createDatabaseWithSameStructure(maskedDbData, maskedUrl);

            // close db
            sqLiteHelper.close();

            response.put("status", "success");
            response.put("message", "脱敏任务已完成");
            response.put("userId", userId);
            response.put("taskId", taskId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 处理异常情况
            response.put("error", "脱敏任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Map<String, List<Object>>> maskData(
            Map<String, Map<String, List<Object>>> dbData) {

        Map<String, Map<String, List<Object>>> maskedDbData = new HashMap<>();

        for (Map.Entry<String, Map<String, List<Object>>> tableEntry : dbData.entrySet()) {
            String tableName = tableEntry.getKey();
            Map<String, List<Object>> tableData = tableEntry.getValue();
            Map<String, List<Object>> maskedTableData = new HashMap<>();

            for (Map.Entry<String, List<Object>> columnEntry : tableData.entrySet()) {
                String columnName = columnEntry.getKey();
                List<Object> columnData = columnEntry.getValue();

                List<Object> maskedColumnData = maskColumnData(columnData);
                maskedTableData.put(columnName, maskedColumnData);
            }
            maskedDbData.put(tableName, maskedTableData);
        }
        return maskedDbData;
    }

    private List<Object> maskColumnData(List<Object> columnData) {
        try {
            //TODO: 脱敏算法选择
            return identifyController.maskList(columnData, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return columnData;
        }
    }
}
