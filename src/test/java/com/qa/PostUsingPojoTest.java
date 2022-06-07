package com.qa;

import com.github.javafaker.*;
import com.qa.pojo.*;
import io.restassured.http.*;
import io.restassured.module.jsv.*;
import io.restassured.path.json.*;
import io.restassured.response.*;
import org.apache.http.*;
import org.testng.*;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.*;

public class PostUsingPojoTest {

    //POJO : Plain Old Java Object - Way of representing an object
    //Rule:
    // 1. For every { } new POJO class has to be created
    // 2. For every [ ] new List needs to be created List<Type>


    @Test
    public void pojoTest(){
        //creating a pojo object to pass as request body
        List<String> dinner = new ArrayList<String>();
        dinner.add("dosa");
        dinner.add("sambhar");
        List<String> jobs = new ArrayList<String>();
        jobs.add("Automation Test Specialist");
        jobs.add("Trainer");
        List<Marks> marks = new ArrayList<>();
        marks.add(new Marks(90,65,59));
        marks.add(new Marks(83,75,68));
        Faker mockedData = new Faker();

        Employee employee = new Employee(mockedData.number().numberBetween(100,1000),mockedData.name().firstName(),mockedData.name().lastName(),mockedData.name().username(),
                            jobs,marks, new FavFood(mockedData.food().dish(),mockedData.food().vegetable(),dinner));
        String uri = "https://reqres.in/api/users";
        Response response = given().header("Content-Type", ContentType.JSON)
                                    .body(employee)
                                    .log().all()
                                    .post(uri);
        System.out.println(response.getStatusCode());
        System.out.println(response.prettyPrint());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED);
        Assert.assertEquals(response.header("Content-Type"),"application/json; charset=utf-8");

        System.out.println(response.jsonPath().getString("email"));
        System.out.println(response.jsonPath().getList("jobs"));

        //how to de-serialize the json response into a pojo class
        Employee deserializedEmployee = response.as(Employee.class);
        System.out.println(deserializedEmployee.getFavFood().getBreakfast());
        System.out.println(deserializedEmployee.getMarks());

        // how to validate the json schema received from server
        response.then().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir") + "/src/test/resources/schema/Employee.json")));
    }

}
