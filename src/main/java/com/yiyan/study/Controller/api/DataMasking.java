package com.yiyan.study.controller.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yiyan.study.database.opengauss.OpengaussHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiyan.study.controller.desensitization.identifyController;
import com.yiyan.study.database.sqlite.SQLiteHelper;

import java.sql.PreparedStatement;

@RestController
@RequestMapping("/api")
public class DataMasking {

private final OpengaussHelper opengaussHelper;

@Autowired
public DataMasking(OpengaussHelper opengaussHelper) {
    this.opengaussHelper = opengaussHelper;
}
@GetMapping("/identify")
public ResponseEntity<Map<String, Object>> identify(
        @RequestParam("user_id") String userId,
        @RequestParam("task_id") String taskId) {
    Map<String, Object> response = new HashMap<>();
    try {
        // 读取上传的数据库文件
        String dbName = userId + "_" + taskId + ".db";
        Path path = Paths.get("transfer", "uploaded_files", dbName);
        String url = "jdbc:sqlite:" + path.toString();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(url);

        // 进行识别处理
        Map<String, List<String>> columnNames = sqLiteHelper.getAllColumns();
        Map<String, List<Boolean>> canMaskColumnNames = sqLiteHelper.getAllColumnsCanMask();
        
        // 生成识别报告
        String reportFileName = userId + "_" + taskId + "_report.txt";
        Path reportPath = Paths.get("transfer", "downloaded_files", reportFileName);
        Files.write(reportPath, columnNames.toString().getBytes());
        
        // 记录到数据库
        String filePath = reportPath.toString();
        String fileType = Files.probeContentType(reportPath);
        long fileSize = Files.size(reportPath);

        String sqlFileInsert = "INSERT INTO file (file_name, file_path, file_type, file_size, file_status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = opengaussHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlFileInsert)) {
            pstmt.setString(1, reportFileName);
            pstmt.setString(2, filePath);
            pstmt.setString(3, fileType);
            pstmt.setLong(4, fileSize);
            pstmt.setString(5, "generated");
            pstmt.executeUpdate();
        }

        String sqlResultInsert = "INSERT INTO desen_result (task_id, report_file_id) VALUES (?, (SELECT file_id FROM file WHERE file_name = ?))";
        try (Connection conn = opengaussHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlResultInsert)) {
            pstmt.setString(1, taskId);
            pstmt.setString(2, reportFileName);
            pstmt.executeUpdate();
        }

        sqLiteHelper.close();

        response.put("status", "success");
        response.put("message", "识别任务已完成");
        response.put("userId", userId);
        response.put("taskId", taskId);
        response.put("reportFile", reportFileName);

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        response.put("error", "识别任务失败: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


@GetMapping("/mask")
public ResponseEntity<Map<String, Object>> mask(
        @RequestParam("user_id") String userId,
        @RequestParam("task_id") String taskId) {
    Map<String, Object> response = new HashMap<>();
    try {
        // 读取上传的数据库文件
        String dbName = userId + "_" + taskId + ".db";
        Path path = Paths.get("transfer", "uploaded_files", dbName);
        String url = "jdbc:sqlite:" + path.toString();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(url);
        Map<String, Map<String, List<Object>>> dbData = sqLiteHelper.getAllTableData();

        // 脱敏处理
        Map<String, Map<String, List<Object>>> maskedDbData = maskData(dbData);

        // 保存脱敏后的数据到新的数据库文件
        String maskedDbName = userId + "_" + taskId + "_masked.db";
        Path maskedPath = Paths.get("transfer", "downloaded_files", maskedDbName);
        String maskedUrl = "jdbc:sqlite:" + maskedPath.toString();
        sqLiteHelper.createDatabaseWithSameStructure(maskedDbData, maskedUrl);

        // 记录到数据库
        String filePath = maskedPath.toString();
        String fileType = Files.probeContentType(maskedPath);
        long fileSize = Files.size(maskedPath);

        String sqlFileInsert = "INSERT INTO file (file_name, file_path, file_type, file_size, file_status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = opengaussHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlFileInsert)) {
            pstmt.setString(1, maskedDbName);
            pstmt.setString(2, filePath);
            pstmt.setString(3, fileType);
            pstmt.setLong(4, fileSize);
            pstmt.setString(5, "masked");
            pstmt.executeUpdate();
        }

        // String sqlResultInsert = "INSERT INTO desen_result (task_id, desen_file_id) VALUES (?, (SELECT file_id FROM file WHERE file_name = ?))";
        // try (Connection conn = opengaussHelper.getConnection();
        //         PreparedStatement pstmt = conn.prepareStatement(sqlResultInsert)) {
        //     pstmt.setString(1, taskId);
        //     pstmt.setString(2, maskedDbName);
        //     pstmt.executeUpdate();
        // }
String sqlResultUpdate = "UPDATE desen_result SET desen_file_id = (SELECT file_id FROM file WHERE file_name = ?) WHERE task_id = ?";
String sqlResultInsert = "INSERT INTO desen_result (task_id, desen_file_id) SELECT ?, (SELECT file_id FROM file WHERE file_name = ?) WHERE NOT EXISTS (SELECT 1 FROM desen_result WHERE task_id = ?)";

try (Connection conn = opengaussHelper.getConnection()) {
    // 尝试更新记录
    try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlResultUpdate)) {
        pstmtUpdate.setString(1, maskedDbName);
        pstmtUpdate.setString(2, taskId);
        int rowsUpdated = pstmtUpdate.executeUpdate();

        // 如果没有记录被更新，则执行插入
        if (rowsUpdated == 0) {
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlResultInsert)) {
                pstmtInsert.setString(1, taskId);
                pstmtInsert.setString(2, maskedDbName);
                pstmtInsert.setString(3, taskId);
                pstmtInsert.executeUpdate();
            }
        }
    }
}

        sqLiteHelper.close();

        response.put("status", "success");
        response.put("message", "脱敏任务已完成");
        response.put("userId", userId);
        response.put("taskId", taskId);
        response.put("maskedFile", maskedDbName);

        return ResponseEntity.ok(response);
    } catch (Exception e) {
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
