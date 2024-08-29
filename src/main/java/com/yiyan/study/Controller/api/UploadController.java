package com.yiyan.study.Controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetLocation);
        return "File uploaded successfully: " + file.getOriginalFilename();
    }

    // 多文件上传接口
    @PostMapping("/uploadMultiple")
    public String uploadMultipleFiles(@RequestParam("file") MultipartFile[] files) throws IOException {
        StringBuilder response = new StringBuilder("Files uploaded successfully:");
        for (MultipartFile file : files) {
            Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation);
            response.append(" ").append(file.getOriginalFilename());
        }
        return response.toString();
    }

    // 文件和其他数据上传接口
    @PostMapping("/uploadWithData")
    public String uploadFileWithData(@RequestParam("file") MultipartFile file,
            @RequestParam("username") String username) throws IOException {
        Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetLocation);
        return "File uploaded successfully: " + file.getOriginalFilename() + ", Username: " + username;
    }
}
