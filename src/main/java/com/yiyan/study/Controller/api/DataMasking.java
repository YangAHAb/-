package com.yiyan.study.Controller.api;

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

import com.yiyan.study.Controller.Desensitization.identifyController;
import com.yiyan.study.database.sqlite.SQLiteHelper;

@RestController
@RequestMapping("/api")
public class DataMasking {

    @GetMapping("/tuomin")
    public ResponseEntity<String> tuomin(
            @RequestParam("user_id") String userId,
            @RequestParam("task_id") String taskId) {
        // 根据user_id和task_id开始脱敏
        try {
            performDataMasking(userId, taskId);
            return ResponseEntity.ok("脱敏任务已完成");
        } catch (Exception e) {
            // 处理异常情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("脱敏任务失败: " + e.getMessage());
        }
    }

    private void performDataMasking(String userId, String taskId) {
        // 读取上传的数据库文件
        String dbName = userId + "_" + taskId + ".db";
        Path path = Paths.get("transfer", "uploaded-files", dbName);
        String url = "jdbc:sqlite:" + path.toString();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(url);
        Map<String, Map<String, List<Object>>> dbData = sqLiteHelper.getAllTableData();

        // 脱敏处理
        Map<String, Map<String, List<Object>>> maskedDbData = maskData(dbData);

        // 保存脱敏后的数据到新的数据库文件
        String maskedDbName = "De-identified_" + userId + "_" + taskId + ".db";
        Path maskedPath = Paths.get("transfer", "downloaded-files", maskedDbName);
        String maskedUrl = "jdbc:sqlite:" + maskedPath.toString();
        sqLiteHelper.createDatabaseWithSameStructure(maskedDbData, maskedUrl);

        // close db
        sqLiteHelper.close();
    }

    private Map<String, Map<String, List<Object>>> maskData(Map<String, Map<String, List<Object>>> dbData) {
        Map<String, Map<String, List<Object>>> maskedDbData = new HashMap<>();
        for (Map.Entry<String, Map<String, List<Object>>> tableEntry : dbData.entrySet()) {
            String tableName = tableEntry.getKey();
            Map<String, List<Object>> tableData = tableEntry.getValue();
            Map<String, List<Object>> maskedTableData = new HashMap<>();
            // 以列为单位进行处理
            for (Map.Entry<String, List<Object>> columnEntry : tableData.entrySet()) {
                String columnName = columnEntry.getKey();
                List<Object> columnData = columnEntry.getValue();
                // 对列数据进行脱敏处理
                List<Object> maskedColumnData = maskColumnData(columnData);
                maskedTableData.put(columnName, maskedColumnData);
            }
            maskedDbData.put(tableName, maskedTableData);
        }
        return maskedDbData;
    }

    private List<Object> maskColumnData(List<Object> columnData) {
        // System.out.println(columnData.toString());
        List<Object> ans = identifyController.maskList(columnData);
        System.out.println(ans.toString());
        return ans;
    }
}
