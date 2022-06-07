package com.qa;

import com.github.javafaker.*;
import com.qa.pojo.*;
import io.restassured.http.*;
import io.restassured.response.*;
import org.testng.annotations.*;
import java.util.*;
import static io.restassured.RestAssured.*;

public class UpdateTest {

    @Test
    public void updateResourceTest(){
        List<String> dinner = new ArrayList<String>();
        dinner.add("dosa");
        dinner.add("sambhar");
        List<String> jobs = new ArrayList<String>();
        jobs.add("Automation Test Specialist");
        jobs.add("Engineering Leader");
        List<Marks> marks = new ArrayList<>();
        marks.add(new Marks(90,65,59));
        marks.add(new Marks(83,75,68));
        Faker mockedData = new Faker();

        Employee employee = new Employee(mockedData.number().numberBetween(100,1000),mockedData.name().firstName(),mockedData.name().lastName(),mockedData.name().username(),
                jobs,marks, new FavFood(mockedData.food().dish(),mockedData.food().vegetable(),dinner));
        String uri = "https://reqres.in/api/users/{id}";
        Response response = given().header("Content-Type", ContentType.JSON)
                            .pathParam("id",mockedData.number().numberBetween(1,1000))
                            .body(employee)
                            .log().all()
                            .put(uri);
        System.out.println(response.getStatusCode());
    }


}
