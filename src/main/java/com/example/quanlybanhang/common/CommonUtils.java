package com.example.quanlybanhang.common;

import com.example.quanlybanhang.constant.SalesManagementConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CommonUtils {

    public static byte[] convertStringImageToByte(String image) throws IOException {
        if (StringUtils.isNotEmpty(image)) {
            return Files.readAllBytes(Paths.get(image));
        } else {
            String defaultImageURL = SalesManagementConstants.SRC_IMAGE + SalesManagementConstants.DEFAULT_NO_IMAGE;
            return Files.readAllBytes(Paths.get(defaultImageURL));
        }
    }
}
