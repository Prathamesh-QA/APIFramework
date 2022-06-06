package com.qa;

import com.qa.pojo.*;
import io.restassured.http.*;
import io.restassured.response.*;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;
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

        Employee employee = new Employee(341,"Amuthan","Shaktivel","a.shaktivel@gmail.com",
                            jobs,marks, new FavFood("Oats Pancake","Dal Rice",dinner));
        String uri = "https://reqres.in/api/users";
        Response response = given().header("Content-Type", ContentType.JSON)
                                    .body(employee)
                                    .log().all()
                                    .post(uri);
        System.out.println(response.getStatusCode());
    }


}
