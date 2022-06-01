package com.qa;

import com.github.javafaker.*;
import com.sun.xml.xsom.impl.scd.*;
import io.restassured.http.*;
import io.restassured.response.*;
import org.json.*;
import org.testng.*;
import org.testng.annotations.*;
import java.util.*;

import java.io.*;
import java.io.File;
import java.nio.file.*;

import static io.restassured.RestAssured.*;

public class PostTest {

    //1. passing json body as string not recomended
    // pros - easy to copy paste ---> can be used to check whether the API is working
    // cons - not recommended for larger json or dynamic json
    @Test
    public void postRequestFromVariableTest() {
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
        Assert.assertEquals(response.getStatusCode(), 201);

    }

    //this can be used for static post request
    // when we passing the request as a file
    @Test
    public void postRequestFromExternalFile() {
        String uri = "https://reqres.in/api/users";
        String testdataPath = System.getProperty("user.dir") + "/src/test/resources/testdata/posttest.json";
        File payload = new File(testdataPath);
        Response response = given().header("Content-Type", ContentType.JSON)
                .body(payload)
                .log().all()
                .post(uri);
        response.prettyPrint();
        System.out.println(response.getStatusCode());
    }

    // In this we can read testdata from the file
    // This approach can be used to print the request body in the logs once we convert it from file to string
    // This can be used to update few dynamic values in the request body
    //Cons This cannot be used for large no. of dynamic values
    @Test
    public void postRequestFromExternalFileConvertingToString() throws IOException {

        String testDataPath = System.getProperty("user.dir") + "/src/test/resources/testdata/posttest.json";
        byte[] bytes = Files.readAllBytes(Paths.get(testDataPath));
        String payload = new String(bytes);
        String uri = "https://reqres.in/api/users";
        //replacing payload read from file
        String updatedPayload = payload.replace("Shaktivel",new Faker().job().position());

        Response response = given().header("Content-Type", ContentType.JSON)
                .body(updatedPayload)
                .log().all()
                .post(uri);
        response.prettyPrint();
        System.out.println(response.getStatusCode());
    }
    // Using HashMap
    @Test
    public void postRequestUsingHashMap(){
        Map<String,Object> payload = new LinkedHashMap<>();
        payload.put("name",new Faker().name().firstName());
        payload.put("job",new Faker().job().position());
        //Construct request payload using list and Map Interface
        // { } -->  Use Map Interface
        // [ ] -->  List Interface
        // When we construct a request payload using map/list interface it is in the
        // form of java object and the server won't be able to understand the request
        // so in that case we can use serializers --> convert lang. specific objects
        // to byte stream which can be transfer over http protocol to server

        String uri = "https://reqres.in/api/users";
        Response response = given().header("Content-Type", ContentType.JSON)
                                    .body(payload)
                                    .log().all()
                                    .post(uri);
        response.prettyPrint();
        System.out.println(response.getStatusCode());


    }

    // Complex Payload using HashMap & Jackson Serializer
    @Test
    public void postRequestUsingHashMapComplexPayload(){

        //pros - we can construct a dynamic request payload
        //cons - verbose, complicated request construction
        //cons - generic type needs to mentioned as we have different
        //       type of data


        Map<String,Object> personalData = new LinkedHashMap<>();
        personalData.put("id",new Faker().number().numberBetween(100,1000));
        personalData.put("firstName",new Faker().name().firstName());
        personalData.put("lastName",new Faker().name().lastName());
        personalData.put("email","abc@incorp.com");

        List<String> jobs = new ArrayList<String>();
        jobs.add("Automation Tester");
        jobs.add("Trainer");

        personalData.put("jobs",jobs);

        Map<String,Object> food = new HashMap<>();
        food.put("breakfast",new Faker().food().fruit());
        food.put("lunch",new Faker().food().dish());
        List<String> dinner = new ArrayList<>();
        dinner.add("chapathi");
        dinner.add(new Faker().food().vegetable());
        food.put("dinner",dinner);

        personalData.put("favFoods",food);
        System.out.println(personalData);


        String uri = "https://reqres.in/api/users";
        Response response = given().header("Content-Type", ContentType.JSON)
                .body(personalData)
                .log().all()
                .post(uri);
        response.prettyPrint();
        System.out.println(response.getStatusCode());

    }


    // Creating complex request using external JSON library
    // have some collections which can solve the complexity problems
    // using map and list interface
    @Test
    public void postRequestUsingJSONLibrary(){

        //pros - we can construct a dynamic request payload
        //pros - we do not have to mention the data type
        //cons - verbose, complicated request construction

        // { } --> Use JsonObject
        // [ ] --> Use JsonArray

        JSONObject object = new JSONObject();
        object.put("id",new Faker().number().numberBetween(100,1000));
        object.put("firstName",new Faker().name().firstName());
        object.put("lastName",new Faker().name().lastName());
        object.put("email","abc@gmail.com");

        //here if we want to add multiple email address then we can use methods
        //such as accumulate/append
        object.accumulate("email","ghi@microsoft.com");
        object.append("email","def@hotmail.com");

        JSONArray jobList = new JSONArray();
        jobList.put("Automation Tester");
        jobList.put("Automation Trainer");

        object.put("jobs",jobList);

        JSONObject food = new JSONObject();
        food.put("breakfast",new Faker().food().fruit());
        food.put("lunch",new Faker().food().dish());
        JSONArray dinnerDishes = new JSONArray();
        dinnerDishes.put("chapathi");
        dinnerDishes.put(new Faker().food().vegetable());
        food.put("dinner",dinnerDishes);

        object.put("favFoods",food);
        Map<String,Object> payload = object.toMap(); // Jackson will serialize the request payload

        String uri = "https://reqres.in/api/users";
        Response response = given().header("Content-Type",ContentType.JSON)
                                    .body(payload)
                                    .log().all()
                                    .post(uri);
        response.prettyPrint();
        System.out.println(response.getStatusCode());


    }

}
