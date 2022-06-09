package com.qa.api.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.ITestAnnotation;

public class RetryListener extends TestListenerAdapter implements IAnnotationTransformer {

    private static Logger logger = LoggerFactory.getLogger(RetryListener.class);

    @Override
    public void transform(ITestAnnotation annotation, Class clas, Constructor constructor, Method method) {
        IRetryAnalyzer retry = annotation.getRetryAnalyzer();
        if (retry == null) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Inside onFinish method");
        Iterator<ITestResult> skippedTestCases = context.getSkippedTests().getAllResults().iterator();
        while (skippedTestCases.hasNext()) {
            ITestResult skippedTestCase = skippedTestCases.next();
            ITestNGMethod method = skippedTestCase.getMethod();
            if (context.getSkippedTests().getResults(method).size() > 0) {
                logger.info("Removing skipped test case from result count count {}", method.getMethodName().toString());
                skippedTestCases.remove();
            }
        }
    }
}
