package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.constant.UploadFileConstant;
import com.example.quanlybanhang.service.UploadFileService;
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

@RestController
@CrossOrigin("*")
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadFileController {

    private final UploadFileService uploadFileService;

    @PostMapping("/uploadInProject")
    public ResponseEntity<?> uploadInProject(@RequestParam("file") MultipartFile image) {
        String fileName = image.getOriginalFilename();
        uploadFileService.directoryCreateInProject();
        fileName = uploadFileService.convertFileName(fileName);
        try {
            File uploadFile = new File(UploadFileConstant.SRC_IMAGE_PROJECT + fileName);
            FileCopyUtils.copy(image.getBytes(), uploadFile);
            return new ResponseEntity<>(uploadFile, HttpStatus.OK);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/uploadInDriveStorage")
    public ResponseEntity<?> uploadInDriveStorage(@RequestParam("file") MultipartFile image) {
        String fileName = image.getOriginalFilename();
        uploadFileService.directoryCreateInHardDrive();
        fileName = uploadFileService.convertFileName(fileName);
        try {
            FileCopyUtils.copy(image.getBytes(), new File(UploadFileConstant.SRC_IMAGE_HARD_DRIVE + fileName));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable("fileName") String fileName) {
        try {
            File file = new File(UploadFileConstant.SRC_IMAGE_HARD_DRIVE + fileName);
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
