package com.yiyan.study.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import com.yiyan.study.database.opengauss.OpengaussHelper;
import com.yiyan.study.utils.userlogutil.UserLog;

import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final Path fileStorageLocation = Paths.get("transfer", "uploaded_files");
    private final OpengaussHelper opengaussHelper;

    @Autowired
    public UploadController(OpengaussHelper opengaussHelper) throws IOException {
        Files.createDirectories(fileStorageLocation);
        this.opengaussHelper = opengaussHelper;
    }

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    // 文件上传接口
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> response = new HashMap<>();
        try {
            // 生成唯一文件名
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path targetLocation = fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation);

            // 获取文件信息
            String filePath = targetLocation.toString();
            String fileType = Files.probeContentType(targetLocation);
            long fileSize = file.getSize();

            // 插入文件信息到数据库
            String sql = "INSERT INTO file (file_name, file_path, file_type, file_size, file_status) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = opengaussHelper.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, uniqueFileName);
                pstmt.setString(2, filePath);
                pstmt.setString(3, fileType);
                pstmt.setLong(4, fileSize);
                pstmt.setString(5, "uploaded");
                pstmt.executeUpdate();
            }

            response.put("status", "success");
            response.put("message", "File uploaded successfully");
            response.put("filename", uniqueFileName);
        } catch (IOException | SQLException e) {
            response.put("status", "error");
            response.put("message", "Failed to upload file: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    // 多文件上传接口
    @PostMapping("/uploadMultiple")
    public ResponseEntity<Map<String, Object>> uploadMultipleFiles(@RequestParam("file") MultipartFile[] files)
            throws IOException {
        Map<String, Object> response = new HashMap<>();
        StringBuilder uploadedFiles = new StringBuilder();
        try {
            for (MultipartFile file : files) {
                Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                uploadedFiles.append(file.getOriginalFilename()).append(" ");
            }
            response.put("status", "success");
            response.put("message", "Files uploaded successfully");
            response.put("files", uploadedFiles.toString().trim());
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Failed to upload files: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    // 文件和其他数据上传接口
    @PostMapping("/uploadWithData")
    public ResponseEntity<Map<String, Object>> uploadFileWithData(@RequestParam("file") MultipartFile file,
            @RequestParam("user_id") String userId, @RequestParam("task_id") String taskId) throws IOException {
        Map<String, Object> response = new HashMap<>();
        try {
            // 生成唯一文件名
            // String originalFileName = file.getOriginalFilename();
            // String uniqueFileName = UUID.randomUUID().toString() + "_" +
            // originalFileName;
            // Path targetLocation = fileStorageLocation.resolve(uniqueFileName);

            String targetFileName = userId + "_" + taskId + ".db";
            Path targetLocation = fileStorageLocation.resolve(targetFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 获取文件信息
            String filePath = targetLocation.toString();
            String fileType = Files.probeContentType(targetLocation);
            long fileSize = file.getSize();

            System.out.println("fileType:" + fileType);

            // 插入文件信息到数据库
            String sql = "INSERT INTO file (file_name, file_path, file_type, file_size,file_status) VALUES (?, ?, ?, ?, ?)";

            // log
            UserLog.setLogFileName(userId);
            UserLog.info(String.format("File upload: user %s uploads file %s in the task %s.", userId,
                    targetFileName, taskId));

            try (Connection conn = opengaussHelper.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, targetFileName);
                pstmt.setString(2, filePath);
                pstmt.setString(3, fileType);
                pstmt.setLong(4, fileSize);
                pstmt.setString(5, "uploaded");
                pstmt.executeUpdate();
            }

            response.put("status", "success");
            response.put("message", "File uploaded successfully");
            response.put("filename", targetFileName);
        } catch (IOException | SQLException e) {
            // } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Failed to upload file: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
