package com.yiyan.study.Controller.api;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class DownloadController {

    private final Path fileStorageLocation = Paths.get("transfer","downloaded-files");

    
    public DownloadController() throws IOException {
        Files.createDirectories(fileStorageLocation);
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
