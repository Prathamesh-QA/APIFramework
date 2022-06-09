package com.qa.api.token;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;

import com.qa.api.automationcontext.*;
import com.qa.api.jsonutils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TokenGeneration extends TimerTask {

    private static Logger logger = LoggerFactory.getLogger(TokenGeneration.class);

    @Override
    public void run() {
        Set<String> applicationList = AutomationContext.getApplicationList();

        for (String application : applicationList) {
            String[] clientIds = AutomationContext.getConfigprop().getProperty("api." + application + ".clientids")
                    .split(",");
            String clientId = clientIds[new Random().nextInt(clientIds.length)];
            String clientSecret = AutomationContext.getConfigprop()
                    .getProperty("api." + clientId.replaceAll("-", "").trim() + ".clientsecret");

            String response = this.getToken(application, clientId, clientSecret);
            String token = JsonUtils.getJsonNodeValue(response, "$.access_token");
            AutomationContext.setTokenMapping(application, token);
            logger.info("token fetched for {} service with {} client id", application, clientId);
        }
    }

    public void loadToken() {
        Set<String> applicationList = AutomationContext.getApplicationList();

        for (String application : applicationList) {
            String[] clientIds = AutomationContext.getConfigprop().getProperty("api." + application + ".clientids")
                    .split(",");
            String clientId = clientIds[new Random().nextInt(clientIds.length)];
            String clientSecret = AutomationContext.getConfigprop()
                    .getProperty("api." + clientId.replaceAll("-", "").trim() + ".clientsecret");

            String response = this.getToken(application, clientId, clientSecret);
            String token = JsonUtils.getJsonNodeValue(response, "$.access_token");
            AutomationContext.setTokenMapping(application, token);
            int expireIn = Integer.parseInt(JsonUtils.getJsonNodeValue(response, "$.expires_in"));

            if (expireIn < AutomationContext.getTokenRefreshTime()) {
                AutomationContext.setTokenRefreshTime(expireIn - 30);
            }
            logger.info("token fetched for {} service with {} client id", application, clientId);
        }
    }

    public String getToken(String applicationName, String clientId, String clientSecret) {

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("client_secret", clientSecret);
        queryParams.put("client_id", clientId);
        queryParams.put("grant_type", "client_credentials");
        queryParams.put("Scope", applicationName);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        Response response = RestAssured.given().queryParams(queryParams).headers(headers).body("")
                .post(AutomationContext.getConfigprop().getProperty("api.accesstokenurl"));

        return response.asString();
    }
}

