package com.qa.api.restassuredutils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qa.api.automationcontext.*;
import com.qa.api.jsonutils.*;
import com.qa.api.utils.*;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;

public class RestAssuredUtil {

    private static final Logger logger = LoggerFactory.getLogger(RestAssuredUtil.class);

    private int statusCode;
    private String response;

    public String sendRequest(String endPointUrl, String body, Method method, Map<String, String> queryParams,
                              Map<String, String> headers, List<MultiPartSpecification> multiPartFile) {

        RestAssuredConfig config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig().setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 30000));

        RequestSpecification requestSpecification;

        String applicationName = UtilityFunctions.getApplicationName(endPointUrl.split(":\\d+")[1]);
        String accessToken = AutomationContext.getTokenMapping().get(applicationName);

        if (headers == null || headers.isEmpty()) {
            headers = new HashMap<>();
        }

        if (multiPartFile != null && !multiPartFile.isEmpty() && !headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "multipart/form-data");
        } else if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "application/json");
        }

        headers.put("Authorization", "Bearer " + accessToken);

        if (queryParams != null) {
            requestSpecification = RestAssured.given().queryParams(queryParams);
        } else {
            requestSpecification = RestAssured.given();
        }

        requestSpecification = requestSpecification.headers(headers).config(config);

        if (method.equals(Method.GET)) {
            executeGetRequest(endPointUrl, requestSpecification);
        } else if (method.equals(Method.POST)) {
            executePostRequest(endPointUrl, requestSpecification, body, multiPartFile);
        } else if (method.equals(Method.PUT)) {
            executePutRequest(endPointUrl, requestSpecification, body);
        } else if (method.equals(Method.DELETE)) {
            executeDeleteRequest(endPointUrl, requestSpecification);
        } else if (method.equals(Method.PATCH)) {
            executePatchRequest(endPointUrl, requestSpecification, body);
        }
        return this.response;
    }

    public String sendRequestWithTestData(String endPointUrl, String body, Method method, Map<String, String> queryParams,
                                          Map<String, String> headers, List<MultiPartSpecification> multiPartFile, Map testData) {

        endPointUrl = UtilityFunctions.generateEndPointUrl(testData, endPointUrl);

        RestAssuredConfig config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig().setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 30000));

        RequestSpecification requestSpecification;

        String applicationName = UtilityFunctions.getApplicationName(endPointUrl.split(":\\d+")[1]);
        String accessToken = AutomationContext.getTokenMapping().get(applicationName);

        if (headers == null || headers.isEmpty()) {
            headers = new HashMap<>();
        }

        if (multiPartFile != null && !multiPartFile.isEmpty() && !headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "multipart/form-data");
        } else if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "application/json");
        }

        headers.put("Authorization", "Bearer " + accessToken);

        if (queryParams != null) {
            requestSpecification = RestAssured.given().queryParams(queryParams);
        } else {
            requestSpecification = RestAssured.given();
        }

        requestSpecification = requestSpecification.headers(headers).config(config);

        if (method.equals(Method.GET)) {
            executeGetRequest(endPointUrl, requestSpecification);
        } else if (method.equals(Method.POST)) {
            executePostRequest(endPointUrl, requestSpecification, body, multiPartFile);
        } else if (method.equals(Method.PUT)) {
            executePutRequest(endPointUrl, requestSpecification, body);
        } else if (method.equals(Method.DELETE)) {
            executeDeleteRequest(endPointUrl, requestSpecification);
        } else if (method.equals(Method.PATCH)) {
            executePatchRequest(endPointUrl, requestSpecification, body);
        }
        return this.response;
    }

    @Step("Execute Patch Call")
    private void executePatchRequest(String endPointUrl, RequestSpecification requestSpecification, String body) {
        try {
            final Response response = requestSpecification.body(body).filter(new RequestFilter()).patch(endPointUrl);
            this.response = response.asString();
            this.statusCode = response.getStatusCode();
            Allure.step("Request Body:- " + body, Status.PASSED);
            Allure.step("Response from server:- " + this.response, Status.PASSED);
            Allure.step("Status code from server:- " + this.statusCode, Status.PASSED);
        } catch (Exception e) {
            Allure.step("Exception occurred while executing PATCH call:- " + e.getMessage(), Status.FAILED);
            logger.error("ERROR: Exception occurred while executing PATCH call:- {}", e.getMessage());
        }
    }

    @Step("Executing DELETE Call")
    private void executeDeleteRequest(String endPointUrl, RequestSpecification requestSpecification) {
        try {
            final Response response = requestSpecification.body("").filter(new RequestFilter()).delete(endPointUrl);
            this.response = response.asString();
            this.statusCode = response.getStatusCode();
            Allure.step("Response from server:- " + this.response, Status.PASSED);
            Allure.step("Status code from server:- " + this.statusCode, Status.PASSED);
        } catch (Exception e) {
            Allure.step("Exception occurred while executing DELETE call:- " + e.getMessage(), Status.FAILED);
            logger.error("ERROR: Exception occurred while executing DELETE call:- {}", e.getMessage());
        }
    }

    @Step("Executing PUT Call")
    private void executePutRequest(String endPointUrl, RequestSpecification requestSpecification, String body) {
        try {
            final Response response = requestSpecification.body(body).filter(new RequestFilter()).put(endPointUrl);
            this.response = response.asString();
            this.statusCode = response.getStatusCode();
            Allure.step("Request Body:- " + body, Status.PASSED);
            Allure.step("Response from server:- " + this.response, Status.PASSED);
            Allure.step("Status code from server:- " + this.statusCode, Status.PASSED);
        } catch (Exception e) {
            Allure.step("Exception occurred while executing PUT call:- " + e.getMessage(), Status.FAILED);
            logger.error("ERROR: Exception occurred while executing PUT call:- {}", e.getMessage());
        }
    }

    @Step("Executing POST Call")
    private void executePostRequest(String endPointUrl, RequestSpecification requestSpecification, String body,
                                    List<MultiPartSpecification> multiPartFile) {

        try {
            if (!(multiPartFile == null || multiPartFile.isEmpty())) {
                for (MultiPartSpecification multiPartSpecification : multiPartFile) {
                    requestSpecification.multiPart(multiPartSpecification);
                }
            } else {
                requestSpecification = requestSpecification.body(body);
            }

            final Response response = requestSpecification.filter(new RequestFilter()).when().post(endPointUrl);

            this.response = response.asString();
            this.statusCode = response.getStatusCode();
            Allure.step("Request Body:- " + body, Status.PASSED);
            Allure.step("Response from server:- " + this.response, Status.PASSED);
            Allure.step("Status code from server:- " + this.statusCode, Status.PASSED);
        } catch (Exception e) {
            Allure.step("Exception occurred while executing POST call:- " + e.getMessage(), Status.FAILED);
            logger.error("ERROR: Exception occurred while executing POST call:- {}", e.getMessage());
        }

    }

    @Step("Executing POST Call")
    private void executePostRequest(String endPointUrl, RequestSpecification requestSpecification, String body,
                                    Map<String, String> multiPartFile) {
        try {

            if (multiPartFile != null) {

                for (Map.Entry<String, String> entry : multiPartFile.entrySet()) {

                    if (entry.getKey().equalsIgnoreCase("file")) {
                        MultiPartSpecification multiPartSpecification = this.getMultiPartSpecification(entry.getKey(),
                                entry.getValue());

                        requestSpecification.multiPart(multiPartSpecification);
                    } else if (JsonUtils.isJSONValid(entry.getValue())) {
                        MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(entry.getValue())
                                .controlName(entry.getKey()).mimeType(ContentType.JSON.toString()).build();

                        requestSpecification.multiPart(multiPartSpecification);
                    } else {
                        MultiPartSpecification multiPartSpecification = this.getMultiPartSpecification(entry.getKey(),
                                entry.getValue());

                        requestSpecification.multiPart(multiPartSpecification);
                    }
                }

            } else {
                requestSpecification = requestSpecification.body(body);
            }

            final Response response = requestSpecification.filter(new RequestFilter()).when().post(endPointUrl);

            this.response = response.asString();
            this.statusCode = response.getStatusCode();
            Allure.step("Request Body:- " + body, Status.PASSED);
            Allure.step("Response from server:- " + this.response, Status.PASSED);
            Allure.step("Status code from server:- " + this.statusCode, Status.PASSED);
        } catch (Exception e) {
            Allure.step("Exception occurred while executing POST call:- " + e.getMessage(), Status.FAILED);
            logger.error("ERROR: Exception occurred while executing POST call:- {}", e.getMessage());
        }
    }

    @Step("Executing GET Call")
    private void executeGetRequest(String endPointUrl, RequestSpecification requestSpecification) {
        try {
            final Response response = requestSpecification.body("").filter(new RequestFilter()).get(endPointUrl);
            this.response = response.asString();
            this.statusCode = response.getStatusCode();
            Allure.step("Response from server:- " + this.response);
            Allure.step("Status code from server:- " + this.statusCode);
        } catch (Exception e) {
            Allure.step("Exception occurred while executing GET call:- " + e.getMessage(), Status.FAILED);
            logger.error("ERROR: Exception occurred while executing GET call:- {}", e.getMessage());
        }
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getResponse() {
        return this.response;
    }

    private MultiPartSpecification getMultiPartSpecification(String controlName, String filePath) {

        try {
            String contentType;
            InputStream fileContent = this.getClass().getClassLoader().getResourceAsStream(filePath);

            if (filePath.endsWith("dtazv") || filePath.endsWith("json")) {
                contentType = ContentType.JSON.toString();
            } else {
                contentType = ContentType.JSON.toString();
            }

            MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(fileContent)
                    .controlName(controlName).mimeType(contentType).build();
            return multiPartSpecification;

        } catch (Exception e) {
            logger.info("Exception occured while creating MultiPartSpecification. Exception:- {}", e.getMessage());
        }
        return null;
    }
}
