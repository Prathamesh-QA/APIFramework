package com.qa;

import io.restassured.http.*;
import io.restassured.response.*;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;

public class DeleteTest {

    @Test
    public void deleteResourceTest(){

        String uri = "https://reqres.in/api/users/{id}";

        Response response = given().header("Content-Type", ContentType.JSON)
                                    .pathParam("id","2")
                                    .log().all()
                                    .delete(uri);
        System.out.println(response.getStatusCode());
        System.out.println(response.prettyPrint());
    }
}
