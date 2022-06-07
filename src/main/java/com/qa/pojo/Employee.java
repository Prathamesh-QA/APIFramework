package com.qa.pojo;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonInclude(value = JsonInclude.Include.NON_NULL)      //this will check the data member has a null/empty input if so it will remove the data member from the request
@JsonPropertyOrder(alphabetic = true)                   // this will arrange the fields of the request in alphabetical order or  @JsonPropertyOrder(value = {"firstname"})
                                                        // this will make the firstname at the top priority  and rest will be followed
@JsonIgnoreProperties(value = {"createdAt"})             // we have to pass the value of the data members which we want to ignore in the constructed request
public class Employee {

    // when to use setter methods and constructor
    // when user wants to keep the object as immutable in that case the class should have constructor
    // when user wants to keep the object as non immutable in that case the class should have constructor
    // so that user can write its own logic while setting the value in any of the class member

    private int id;
    private String username;
    @JsonIgnore
    private String lastname;  // this will exclude the field during construction of the json request here email will be excluded
    private String email;
    private List<String> jobs;
    private List<Marks> marks;
    private FavFood favFood;

}
