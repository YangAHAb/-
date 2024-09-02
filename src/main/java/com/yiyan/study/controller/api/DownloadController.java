package com.yiyan.study.controller.api;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.yiyan.study.utils.userlogutil.UserLog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class DownloadController {

    private final Path fileStorageLocation = Paths.get("transfer", "downloaded_files");

    public DownloadController() throws IOException {
        Files.createDirectories(fileStorageLocation);
    }

    // 文件下载接口
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam("user_id") String userId,
            @RequestParam("task_id") String taskId) throws MalformedURLException {

        // 根据请求参数动态构建文件名或路径
        String fileName = userId + "_" + taskId + "_masked.db"; // 示例：文件名根据 user_id 和 task_id 生成
        Path filePath = fileStorageLocation.resolve(fileName).normalize();

        // log
        UserLog.setLogFileName(userId);

        // 确保文件存在
        if (!Files.exists(filePath)) {
            UserLog.info(String.format(
                    "File download failed: The file user %s downloaded in the task %s doesn't exist.", userId, taskId));

            return ResponseEntity.notFound().build(); // 如果文件不存在，返回 404
        }
        UserLog.info(String.format(
                "File download: user %s downloads file %s in the task %s.", userId, filePath,
                taskId));

        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
