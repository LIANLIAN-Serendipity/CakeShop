package cn.smxy.zhouxuelian9.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private static final String UPLOAD_DIR = "D:/upload/";
    private static final String BASE_URL = "http://192.168.131.210:8089/zhouxuelian9/upload/";

    @PostMapping("/uploadFile")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        if (file.isEmpty()) {
            result.put("code", 4000);
            result.put("msg", "上传文件不能为空");
            return result;
        }

        try {
            // 获取原始文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 生成唯一文件名
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            Path uploadPath = Paths.get(UPLOAD_DIR);

            // 检查上传目录是否存在，不存在则创建
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 构建访问URL
            String imageUrl = BASE_URL + uniqueFileName;

            result.put("code", 2000);
            result.put("msg", "文件上传成功");
            result.put("dataobject", imageUrl);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            result.put("code", 5000);
            result.put("msg", "文件上传失败: " + e.getMessage());
        }

        return result;
    }
}