package org.jnu.unimart.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.access.path}")
    private String accessPath;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            Path path = Paths.get(uploadPath, filename);
            Files.copy(file.getInputStream(), path);

            // 添加日志
            System.out.println("物理路径: " + path.toString());
            System.out.println("访问路径: " + accessPath + filename);

            Map<String, String> response = new HashMap<>();
            response.put("url", "http://localhost:8080" + accessPath + filename);
            response.put("filename", filename);
            response.put("fullPath", path.toString());

            // 验证文件是否确实被保存
            if (Files.exists(path)) {
                System.out.println("文件成功保存到: " + path);
            } else {
                System.out.println("文件保存失败!");
            }

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace(); // 打印详细错误信息
            return ResponseEntity.badRequest().body("文件上传失败: " + e.getMessage());
        }
    }
} 