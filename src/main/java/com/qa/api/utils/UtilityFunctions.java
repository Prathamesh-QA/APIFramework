package com.qa.api.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qa.api.automationcontext.*;
import com.qa.api.fileutils.*;
import org.apache.commons.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Step;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.MultiPartSpecification;


public class UtilityFunctions {

    private static Logger logger = LoggerFactory.getLogger(UtilityFunctions.class);

    public static String getTemplateRequest(String templateName) {
        return FileUtility.readFile(
                "src/test/resources" + File.separator + "request-template" + File.separator + templateName + ".json");
    }

    @Step("Get the application name")
    public static String getApplicationName(String contextUrl) {

        final Optional<String> first = AutomationContext.getApplicationContextMapping().entrySet().stream()
                .filter(entry -> contextUrl.startsWith(entry.getValue())).map(Map.Entry::getKey).findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            return null;
        }
    }

    @Step("Creating MultiPartSpecification")
    public static MultiPartSpecification createMultiPartSpecification(String fileContent, String controlName,
                                                                      String mimeType) {

        try {
            MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(IOUtils.toInputStream(fileContent))
                    .controlName(controlName).mimeType(mimeType).build();
            return multiPartSpecification;
        } catch (Exception e) {
            logger.info("Exception occured while creating MultiPartSpecification. Exception:- {}", e.getMessage());
        }
        return null;
    }

    @Step("Generating unique string from date time stamp")
    public static String generateUniqueString() {
        SimpleDateFormat ft = new SimpleDateFormat("ddMMyyyyhhmmssSSSa");
        String uniqueString = ft.format(new Date());
        logger.info("Unique string {} from date time stamp is generated", uniqueString);
        return uniqueString;
    }

    @Step("Generating unique string from UUID")
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @Step("Generating unique string from UUID")
    public static String generateEndPointUrl(Map map, String endPointUrl) {

        Pattern r = Pattern.compile("\\$\\{\\w+\\}");
        Matcher m = r.matcher(endPointUrl);
        while (m.find()) {
            String group = m.group();
            String key = group.replace("$", "").replace("{", "").replace("}", "");
            endPointUrl = endPointUrl.replace(group, String.valueOf(map.get(key)));
        }
        return endPointUrl;
    }
}

