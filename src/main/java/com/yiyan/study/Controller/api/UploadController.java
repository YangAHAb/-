package com.yiyan.study.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.yiyan.study.utils.userlogutil.UserLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final Path fileStorageLocation = Paths.get("transfer", "uploaded-files");

    public UploadController() throws IOException {
        Files.createDirectories(fileStorageLocation);
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
            Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            response.put("status", "success");
            response.put("message", "File uploaded successfully");
            response.put("filename", file.getOriginalFilename());
        } catch (IOException e) {
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
            Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // log
            UserLog.setLogFileName(userId, taskId);
            UserLog.info(String.format("File upload: user %s uploads file %s in the task %s.", userId,
                    file.getOriginalFilename(), taskId));

            response.put("status", "success");
            response.put("message", "File uploaded successfully");
            response.put("filename", file.getOriginalFilename());
            response.put("user_id", userId);
            response.put("task_id", taskId);
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Failed to upload file: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
