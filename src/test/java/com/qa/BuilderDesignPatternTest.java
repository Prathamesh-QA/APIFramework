package com.qa;

import com.github.javafaker.*;
import com.qa.pojo.*;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;

public class BuilderDesignPatternTest {

    @Test
    public void builderTest(){ //Immutability
        Faker mockedData = new Faker();
        // Using the parameterized constructor when there are large parameters to be passed from the constructor
        // In this case were it will be confusing for the user which is the field : Readability decreases
        // If you want to ignore some fields you will need to create multiple constructors to suffice for creating a
        // object with this no. of parameters

        Student student = new Student(mockedData.number().numberBetween(100, 1000), mockedData.name().firstName(), mockedData.name().lastName(), mockedData.name().username());



    }


}
