package com.qa.api.restassuredutils;

import java.util.List;

import io.qameta.allure.Allure;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.MultiPartSpecification;

public class RequestFilter extends RequestLoggingFilter {

    public Response filter(FilterableRequestSpecification filterableRequestSpecification,
                           FilterableResponseSpecification filterableResponseSpecification, FilterContext filterContext) {
        String uri = filterableRequestSpecification.getURI();
        String accessToken = filterableRequestSpecification.getHeaders().get("Authorization").toString();
        Allure.step("Endpoint URL:- " + uri);
        Allure.step("accessToken:- " + accessToken);
        Allure.step("Http Method:- " + filterableRequestSpecification.getMethod());
        List<MultiPartSpecification> multiPartParams = filterableRequestSpecification.getMultiPartParams();
        multiPartParams.stream().forEach(c -> {
            Allure.step(c.toString());
        });

        return filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
    }
}
