package com.qa;

import io.restassured.http.*;
import io.restassured.response.*;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

public class GetTest {

    @Test
    public void getTest(){
        Response response = given().when().get("https://reqres.in/api/users/2");
        System.out.println(response.getStatusCode());
        System.out.println(response.getContentType());
        System.out.println(response.getTime());
        System.out.println(response.getTimeIn(TimeUnit.SECONDS));
        System.out.println(response.prettyPrint());
    }

    @Test
    public void getWithQueryParamTest(){
        String uri = "https://reqres.in/api/users";
        Response response = given().header("Content-Type", ContentType.JSON)
                                    .queryParam("page","2")
                                    .log().all()
                                    .get(uri);
        System.out.println(response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    // Here we can log the API response into a file using java.nio.files library
    @Test
    public void getResponseLoggedInFile() {
        String uri = "https://reqres.in/api/users";
        Response response = given().header("Content-Type", ContentType.JSON)
                .queryParam("page", "2")
                .log().all()
                .get(uri);
        try {
            Files.write(Paths.get(System.getProperty("user.dir") + "/src/test/resources/response/employee-response.json")
                            ,response.asByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Design Patterns ---> Never ever force fit a design pattern
    // design patterns are used to overcome some problem such as structural problems
    // different design patterns --> creational (Builder pattern), structural, behavioural



}
