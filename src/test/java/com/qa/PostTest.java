package com.qa;

import io.restassured.http.*;
import io.restassured.response.*;
import org.testng.*;
import org.testng.annotations.*;

import java.io.*;

import static io.restassured.RestAssured.*;

public class PostTest {

    //1. passing json body as string not recomended
    // pros - easy to copy paste ---> can be used to check whether the API is working
    // cons - not recommended for larger json or dynamic json

    @Test
    public void postRequestFromVariableTest(){
        String payload = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";
        String uri = "https://reqres.in/api/users";
        Response response = given().header("Content-Type", ContentType.JSON)
                                    .body(payload)
                                    .log().all()
                                    .post(uri);

        response.prettyPrint();
        System.out.println("Response Code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(),201);

    }

    @Test
    public void postRequestFromExternalFile(){
        String uri = "https://reqres.in/api/users";
        String testdataPath = System.getProperty("user.dir") + "/src/test/resources/testdata/posttest.json";
        File payload = new File(testdataPath);
        Response response =  given().header("Content-Type",ContentType.JSON)
                .body(payload)
                .log().all()
                .post(uri);
        response.prettyPrint();
        System.out.println(response.getStatusCode());
    }

}
