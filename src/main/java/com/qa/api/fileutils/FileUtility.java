package com.qa.api.fileutils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;

public class FileUtility {

    private static Logger logger = LoggerFactory.getLogger(FileUtility.class);

    @Step("reading file")
    public static String readFile(String filePath) {
        try {
            String text = new String(Files.readAllBytes(Paths.get(filePath)));
            return text;
        } catch (IOException e) {
            Allure.step("Exception occurred while reading file. Exception:- " + e.getMessage(), Status.FAILED);
            logger.error("ERROR: Exception occurred while reading file. Exception:- {}", e.getMessage());
        }
        return null;
    }

    @Step("reading file from resource folder")
    public static String readFileFromResource(String filePath) {

        try {
            FileUtility instance = new FileUtility();
            ClassLoader classLoader = instance.getClass().getClassLoader();

            InputStream fileInputStream = classLoader.getResourceAsStream(filePath);
            return IOUtils.toString(fileInputStream);
        } catch (Exception e) {
            Allure.step("Exception occurred while reading file from resource. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error("ERROR: Exception occurred while reading file from resource. Exception:- {}", e.getMessage());
        }
        return null;
    }

    @Step("reading file from resource folder")
    public static InputStream readFileFromResourceAsStream(String filePath) {

        try {
            FileUtility instance = new FileUtility();
            ClassLoader classLoader = instance.getClass().getClassLoader();
            InputStream fileInputStream = classLoader.getResourceAsStream(filePath);
            return fileInputStream;
        } catch (Exception e) {
            Allure.step("Exception occurred while reading file from resource. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error("ERROR: Exception occurred while reading file from resource. Exception:- {}", e.getMessage());
        }
        return null;
    }
}
