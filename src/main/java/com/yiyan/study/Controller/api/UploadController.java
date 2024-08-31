package com.yiyan.study.Controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> response = new HashMap<>();
        try {
            Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation);
            response.put("status", "success");
            response.put("message", "File uploaded successfully");
            response.put("filename", file.getOriginalFilename());
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Failed to upload file: " + e.getMessage());
        }
        return response;
    }

    // 多文件上传接口
    @PostMapping("/uploadMultiple")
    public Map<String, Object> uploadMultipleFiles(@RequestParam("file") MultipartFile[] files) throws IOException {
        Map<String, Object> response = new HashMap<>();
        StringBuilder uploadedFiles = new StringBuilder();
        try {
            for (MultipartFile file : files) {
                Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(), targetLocation);
                uploadedFiles.append(file.getOriginalFilename()).append(" ");
            }
            response.put("status", "success");
            response.put("message", "Files uploaded successfully");
            response.put("files", uploadedFiles.toString().trim());
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Failed to upload files: " + e.getMessage());
        }
        return response;
    }

    // 文件和其他数据上传接口
    @PostMapping("/uploadWithData")
    public Map<String, Object> uploadFileWithData(@RequestParam("file") MultipartFile file,
            @RequestParam("username") String username) throws IOException {
        Map<String, Object> response = new HashMap<>();
        try {
            Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation);
            response.put("status", "success");
            response.put("message", "File uploaded successfully");
            response.put("filename", file.getOriginalFilename());
            response.put("username", username);
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Failed to upload file: " + e.getMessage());
        }
        return response;
    }
}
