package com.qa.api.automationcontext;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.qa.api.restassuredutils.*;

public class AutomationContext {
    private static final ThreadLocal<RestAssuredUtil> RESTASSUREDHANDLER = new InheritableThreadLocal<RestAssuredUtil>();
    private static LinkedHashMap<String, String> applicationContextMapping = new LinkedHashMap<String, String>();
    private static Map<String, String> tokenMapping = new HashMap<String, String>();
    private static Set<String> applicationList = new HashSet<String>();
    private static Properties prop = new Properties();
    private static Properties xpathprop = new Properties();
    private static Properties configprop = new Properties();
    private static Properties excelprop = new Properties();
    private static int tokenRefreshTime = 999999999;

    public static RestAssuredUtil getRestAssuredHandler() {
        return RESTASSUREDHANDLER.get();
    }

    public static void setRestAssuredHandler(RestAssuredUtil restAssuredUtil) {
        RESTASSUREDHANDLER.set(restAssuredUtil);
    }

    public static void removeRestAssuredHandler() {
        RESTASSUREDHANDLER.remove();
    }

    public static LinkedHashMap<String, String> getApplicationContextMapping() {
        return applicationContextMapping;
    }

    protected static void setApplicationContextMapping(LinkedHashMap<String, String> applicationContextMapping) {
        AutomationContext.applicationContextMapping.putAll(applicationContextMapping);
    }

    public static Map<String, String> getTokenMapping() {
        return tokenMapping;
    }

    public static void setTokenMapping(String applicationName, String token) {
        AutomationContext.tokenMapping.put(applicationName, token);
    }

    public static Set<String> getApplicationList() {
        return applicationList;
    }

    protected static void setApplicationList(Set<String> applicationList) {
        AutomationContext.applicationList.addAll(applicationList);
    }

    public static Properties getProp() {
        return prop;
    }

    protected static void setProp(InputStream fs_prop) {
        try {
            AutomationContext.prop.load(fs_prop);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Properties getXpathprop() {
        return xpathprop;
    }

    protected static void setXpathprop(InputStream xpathprop) {
        try {
            AutomationContext.xpathprop.load(xpathprop);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Properties getConfigprop() {
        return configprop;
    }

    protected static void setConfigprop(InputStream configprop) {
        try {
            AutomationContext.configprop.load(configprop);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Properties getExcelprop() {
        return excelprop;
    }

    protected static void setExcelprop(InputStream excelprop) {
        try {
            AutomationContext.excelprop.load(excelprop);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int getTokenRefreshTime() {
        return tokenRefreshTime;
    }

    public static void setTokenRefreshTime(int tokenRefreshTime) {
        AutomationContext.tokenRefreshTime = tokenRefreshTime;
    }

}

