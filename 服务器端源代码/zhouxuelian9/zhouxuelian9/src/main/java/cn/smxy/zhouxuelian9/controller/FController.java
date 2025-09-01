package cn.smxy.zhouxuelian9.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FController {
    @PostMapping("/uploadFile")
    public Map<String,Object> uploadFile(MultipartFile file) throws IOException {
        String oldFileName = file.getOriginalFilename();

        String newFileName = UUID.randomUUID().toString() +oldFileName;
        String fileSavaPath = "D:/upload/" +newFileName;

        File f = new File(fileSavaPath);
        file.transferTo(f );

        String imagePath = "http://192.168.131.210:8089/zhouxuelian9/upload/" +newFileName;

        Map<String,Object> map = new HashMap<>();
        map.put("code",2000);
        map.put("msg","文件上传成功");
        map.put("dataobject",imagePath);
        return map;
    }
}
