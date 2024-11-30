package com.example.quanlybanhang.service;

import com.example.quanlybanhang.constant.UploadFileConstant;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UploadFileService {

    public void directoryCreateInHardDrive() {
        File dir = new File(UploadFileConstant.SRC_IMAGE_HARD_DRIVE);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Directory already exists");
                System.out.println("Failed to create directory!");
            }
        }
    }

    public void directoryCreateInProject() {
        File dir = new File(UploadFileConstant.SRC_IMAGE_PROJECT);
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
