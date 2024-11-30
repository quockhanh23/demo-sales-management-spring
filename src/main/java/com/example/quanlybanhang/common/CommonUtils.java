package com.example.quanlybanhang.common;

import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.constant.UploadFileConstant;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CommonUtils {

    public static byte[] convertStringImageToByte(String image) throws IOException {
        if (StringUtils.isNotEmpty(image)) {
            return Files.readAllBytes(Paths.get(image));
        } else {
            String defaultImageURL = UploadFileConstant.SRC_IMAGE_PROJECT + SalesManagementConstants.DEFAULT_NO_IMAGE;
            return Files.readAllBytes(Paths.get(defaultImageURL));
        }
    }
}
