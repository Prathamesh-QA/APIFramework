package com.qa;

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

}
