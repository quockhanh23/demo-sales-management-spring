package com.example.quanlybanhang.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadFileController {

    // Phải tạo thư mục lưu trữ trước
    @PostMapping("/upload2")
    public ResponseEntity<?> uploadInProject(@RequestParam("file") MultipartFile image) {
        String fileName = image.getOriginalFilename();
        directoryCreateInProject();
        try {
            File uploadFile = new File("src/main/resources/static/images/" + convertFileName(fileName));
            FileCopyUtils.copy(image.getBytes(), uploadFile);
            return new ResponseEntity<>(uploadFile, HttpStatus.OK);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile image) {
        String fileName = image.getOriginalFilename();
        directoryCreate();
        try {
            FileCopyUtils.copy(image.getBytes(), new File("E:/images/" + fileName));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Hiển thị ảnh trên localHost
    // Tên file phải có cả đuôi file ví dụ: image.jpg
    @GetMapping("/images/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable("fileName") String fileName) {
        try {
            // Thao tác với các tệp tin và thư mục trên hệ thống tệp tin của máy tính
            File file = new File("E:/images/" + fileName);
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public void directoryCreate() {
        File dir = new File("E:/images/");
        if (!dir.exists()) {
            // Nếu thư mục chưa tồn tại
            if (dir.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Directory already exists");
                System.out.println("Failed to create directory!");
            }
        }
    }

    public void directoryCreateInProject() {
        File dir = new File("src/main/resources/static/images/");
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Directory already exists");
                System.out.println("Failed to create directory!");
            }
        }
    }

    public String convertFileName(String fileName) {
        if (null == fileName) return "";
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        return sdf.format(new Date()) + fileExtension;
    }
}
