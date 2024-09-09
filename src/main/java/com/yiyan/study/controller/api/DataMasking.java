package com.yiyan.study.controller.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiyan.study.controller.desensitization.identifyController;
import com.yiyan.study.database.sqlite.SQLiteHelper;
import com.yiyan.study.utils.userlogutil.UserLog;

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
            Path path = Paths.get("transfer", "uploaded_files", dbName);
            String url = "jdbc:sqlite:" + path.toString();
            SQLiteHelper sqLiteHelper = new SQLiteHelper(url);

            // 数据库表中各个表的列名，以及这些列是否可以进行脱敏处理的标志
            Map<String, List<String>> columnNames = sqLiteHelper.getAllColumns();
            Map<String, List<Integer>> ColumnType = sqLiteHelper.getAllColumnsType();

            System.out.println(columnNames);
            System.out.println(ColumnType);

            // log
            UserLog.setLogFileName(userId);
            UserLog.info(String.format("Identify: user %s identifies file %s in the task %s.", userId,
                    dbName, taskId));
            UserLog.endLog();

            // close db
            sqLiteHelper.close();

            response.put("status", "success");
            response.put("message", "识别任务已完成");
            response.put("userId", userId);
            response.put("taskId", taskId);
            response.put("columnNames", columnNames);
            response.put("columnType", ColumnType);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("识别错误：" + e.getMessage());

            // 处理异常情况
            response.put("error", "识别任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @RequestMapping(value = "/mask", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> mask(
            @RequestBody Map<String, Object> requestBody) {
        /**
         * requestBody={
         * user_id:
         * task_id:
         * rule:
         * }
         */
        Map<String, Object> response = new HashMap<>();
        String userId = requestBody.get("user_id").toString();
        String taskId = requestBody.get("task_id").toString();

        @SuppressWarnings("unchecked")
        Map<String, List<Integer>> rule = (Map<String, List<Integer>>) requestBody.get("rule");
        System.out.println("rule:" + rule);
        @SuppressWarnings("unchecked")
        Map<String, List<String>> columnNames = (Map<String, List<String>>) requestBody.get("columnNames");
        System.out.println("columnNames:" + columnNames);

        try {
            // 读取上传的数据库文件
            String dbName = userId + "_" + taskId + ".db";
            Path path = Paths.get("transfer", "uploaded_files", dbName);
            String url = "jdbc:sqlite:" + path.toString();
            SQLiteHelper sqLiteHelper = new SQLiteHelper(url);
            Map<String, Map<String, List<Object>>> dbData = sqLiteHelper.getAllTableData();

            // 脱敏处理
            Map<String, Map<String, List<Object>>> maskedDbData = maskData(dbData, rule, columnNames);

            // 保存脱敏后的数据到新的数据库文件
            String maskedDbName = userId + "_" + taskId + "_masked.db";
            Path maskedPath = Paths.get("transfer", "downloaded_files", maskedDbName);
            String maskedUrl = "jdbc:sqlite:" + maskedPath.toString();
            sqLiteHelper.createDatabaseWithSameStructure(maskedDbData, maskedUrl);

            // log
            UserLog.setLogFileName(userId);
            UserLog.info(String.format("Mask: user %s masks file %s in the task %s, result stored as %s.", userId,
                    dbName, taskId, maskedDbName));
            UserLog.endLog();

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
            Map<String, Map<String, List<Object>>> dbData,
            Map<String, List<Integer>> rule,
            Map<String, List<String>> columnNames) {

        Map<String, Map<String, List<Object>>> maskedDbData = new HashMap<>();

        // db
        for (Map.Entry<String, Map<String, List<Object>>> tableEntry : dbData.entrySet()) {
            // table
            String tableName = tableEntry.getKey();
            Map<String, List<Object>> tableData = tableEntry.getValue();
            Map<String, List<Object>> maskedTableData = new HashMap<>();
            List<Integer> tableRule = rule.get(tableName);
            List<String> columns = columnNames.get(tableName);
            // System.out.println("tableRule:" + tableRule);

            // int idx = 0;
            for (Map.Entry<String, List<Object>> columnEntry : tableData.entrySet()) {
                // column
                String columnName = columnEntry.getKey();
                List<Object> columnData = columnEntry.getValue();

                int idx = columns.indexOf(columnName);
                List<Object> maskedColumnData = maskColumnData(columnData, tableRule.get(idx));
                maskedTableData.put(columnName, maskedColumnData);
                // idx++;
            }
            maskedDbData.put(tableName, maskedTableData);
        }
        return maskedDbData;
    }

    private List<Object> maskColumnData(List<Object> columnData, int type) {
        // System.out.println("current rule:" + type);
        try {
            return identifyController.maskList(columnData, type);
        } catch (Exception e) {
            e.printStackTrace();
            return columnData;
        }
    }
}
