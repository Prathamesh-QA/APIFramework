package com.qa.api.jsonutils;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import net.minidev.json.JSONArray;

public class JsonUtils {

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    @Step("Verify if the jsonString is valid json or not")
    public static boolean isJSONValid(String jsonString) {
        try {
            new JSONObject(jsonString);
        } catch (JSONException e) {
            return false;
        }
        Allure.step("Json String " + jsonString + " is valid json");
        logger.info("Json String {} is valid json", jsonString);
        return true;
    }

    @Step("Get the json node count from json string")
    public static int getJsonNodeCount(String jsonString, String jsonXPath) {

        try {
            int nodeCount = -1;

            if (jsonString == null || jsonString.equalsIgnoreCase("")) {
                return nodeCount;
            }

            final DocumentContext documentContext = JsonPath.parse(jsonString);
            final JsonPath jsonPath = JsonPath.compile(jsonXPath);
            final Object read = documentContext.read(jsonPath);
            if (read instanceof JSONArray) {
                List<Object> data = (List<Object>) read;
                nodeCount = data.size();
            } else {
                nodeCount = 1;
            }

            Allure.step("jsonString:- " + jsonString);
            Allure.step("jsonXPath:- " + jsonXPath);
            Allure.step("Json Node Count:- " + nodeCount);

            logger.info("jsonString {}", jsonString);
            logger.info("jsonXPath {}", jsonXPath);
            logger.info("Json Node Count is {}", nodeCount);
            return nodeCount;
        } catch (Exception e) {
            Allure.step("Exception occured while fetching node count from json string. Exception:- " + e.getMessage());
            logger.info("Exception occured while fetching node count from json string. Exception:- {}", e.getMessage());
        }

        return -1;
    }

    @Step("Get the json list from json string")
    public static List<String> getJsonNodeList(String jsonString, String jsonXPath) {

        try {
            if (jsonString == null || jsonString.equalsIgnoreCase("")) {
                return null;
            }
            DocumentContext documentContext = JsonPath.parse(jsonString);
            JsonPath jsonPath = JsonPath.compile(jsonXPath);
            Object read = documentContext.read(jsonPath);
            Allure.step("jsonString:- " + jsonString);
            Allure.step("jsonXPath:- " + jsonXPath);
            Allure.step("JsonNodeList:- " + (List<String>) read);

            logger.info("jsonString {}", jsonString);
            logger.info("jsonXPath {}", jsonXPath);
            logger.info("Json node list is {}", (List<String>) read);

            return (List<String>) read;
        } catch (Exception e) {
            Allure.step("Exception occured while fetching list from json. Exception:- " + e.getMessage());
            logger.info("Exception occured while fetching list from json. Exception:- {}", e.getMessage());
        }
        return null;
    }

    @Step("Get the json node value from json string")
    public static String getJsonNodeValue(String jsonString, String jsonXPath) {

        try {
            if (jsonString == null || jsonString.equalsIgnoreCase("")) {
                return null;
            }

            String jsonNodeValue;
            DocumentContext documentContext = JsonPath.parse(jsonString);
            JsonPath jsonPath = JsonPath.compile(jsonXPath);
            Object read = documentContext.read(jsonPath);

            if (read instanceof JSONArray) {
                List<Object> data = (List<Object>) read;
                jsonNodeValue = data.get(0).toString();
            } else {
                jsonNodeValue = read.toString();
            }

            Allure.step("jsonString:- " + jsonString);
            Allure.step("jsonXPath:- " + jsonXPath);
            Allure.step("JsonNodeValue:- " + jsonNodeValue);

            logger.info("jsonString {}", jsonString);
            logger.info("jsonXPath {}", jsonXPath);
            logger.info("Json node value is {}", jsonNodeValue);
            return jsonNodeValue;
        } catch (Exception e) {
            logger.error("Exception occurred while fetching node. Exception:- {}", e.getMessage());
            Allure.step("Exception occurred while fetching node. Exception:- " + e.getMessage());
        }

        return null;
    }

    @Step("Update the json node value in json string")
    public static String updateJsonNodeValue(String jsonString, String jsonXPath, String valueToBeUpdated) {

        try {
            String jsonKey = jsonXPath.substring(jsonXPath.lastIndexOf(".") + 1);
            String jsonPath = jsonXPath.substring(0, jsonXPath.lastIndexOf("."));
            DocumentContext documentContext = JsonPath.parse(jsonString);

            Object newValue = null;
            if (valueToBeUpdated == null || valueToBeUpdated.equals("") || valueToBeUpdated.equalsIgnoreCase("null")) {
                newValue = valueToBeUpdated;
            } else {
                newValue = JsonPath.read(valueToBeUpdated, "$");
            }

            documentContext.put(jsonPath, jsonKey, newValue);
            Allure.step("json node is updated successfully. jsonPath " + jsonXPath + " and valueToBeUpdated "
                    + valueToBeUpdated, Status.PASSED);
            logger.info("json node is updated successfully. jsonPath {} and valueToBeUpdated {}", jsonXPath,
                    valueToBeUpdated);
            return documentContext.jsonString().toString();
        } catch (Exception e) {
            logger.error("Exception occured while updating jsonPath {} in json string. Exception:- {}", jsonXPath,
                    e.getMessage());
            Allure.step("Exception occured while updating jsonPath " + jsonXPath + " in json string. Exception:- "
                    + e.getMessage(), Status.FAILED);
        }
        return null;
    }

    @Step("Delete json node value in json string")
    public static String deleteJsonNode(String jsonString, String jsonXPath) {
        try {
            DocumentContext documentContext = JsonPath.parse(jsonString);
            documentContext.delete(jsonXPath);
            Allure.step("jsonPath " + jsonXPath + " is removed from json string", Status.PASSED);
            logger.info("jsonPath {} is removed from json string", jsonXPath);
            return documentContext.jsonString();
        } catch (Exception e) {
            Allure.step("Exception occured while removing jsonPath " + jsonXPath + " from json string. Exception:- "
                    + e.getMessage(), Status.FAILED);
            logger.error("Exception occured while removing jsonPath {} from json string. Exception:- {}", jsonXPath,
                    e.getMessage());
        }
        return null;
    }

}
