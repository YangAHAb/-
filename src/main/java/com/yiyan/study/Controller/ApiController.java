package com.yiyan.study.Controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final Path fileStorageLocation = Paths.get("uploaded-files");

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    public ApiController() throws IOException {
        Files.createDirectories(fileStorageLocation);
    }

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

    // 文件下载接口
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam("user_id") String userId,
            @RequestParam("task_id") String task_id) throws MalformedURLException {

        // 根据请求参数动态构建文件名或路径
        String fileName = "De-identified_" + userId + "_" + task_id + ".db"; // 示例：文件名根据 user_id 和 task_id 生成
        Path filePath = fileStorageLocation.resolve(fileName).normalize();

        // 确保文件存在
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build(); // 如果文件不存在，返回 404
        }

        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
