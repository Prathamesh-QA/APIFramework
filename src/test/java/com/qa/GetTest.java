package com.qa;

import io.restassured.http.*;
import io.restassured.response.*;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;
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

}
