package com.qa.api.listener;

import com.qa.api.automationcontext.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class);
    private int count = 0;
    private int maxCount;

    @Override
    public boolean retry(ITestResult iTestResult) {

        maxCount = AutomationContext.getConfigprop().getProperty("api.retry.count") == null ? 2
                : Integer.parseInt(AutomationContext.getConfigprop().getProperty("api.retry.count"));

        logger.info("Retry parameter count is {}", maxCount);
        if (!iTestResult.isSuccess()) {
            if (count < maxCount) {
                count++;
                //iTestResult.setStatus(ITestResult.SUCCESS_PERCENTAGE_FAILURE);
                iTestResult.setStatus(ITestResult.SUCCESS);
                String message = Thread.currentThread().getName() + " error in class:- " + iTestResult.getTestClass()
                        + " method:- " + iTestResult.getName() + " with status " + iTestResult.getStatus()
                        + " retrying " + count + " times";
                logger.info(message);
                return true;
            }else {
                iTestResult.setStatus(ITestResult.FAILURE);
            }
        }
        return false;
    }

}
