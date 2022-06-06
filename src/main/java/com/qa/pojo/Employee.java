package com.qa.pojo;

import java.util.*;
import lombok.*;

@AllArgsConstructor
@Data
public class Employee {

    // when to use setter methods and constructor
    // when user wants to keep the object as immutable in that case the class should have constructor
    // when user wants to keep the object as non immutable in that case the class should have constructor
    // so that user can write its own logic while setting the value in any of the class member

    private int id;
    private String username;
    private String lastname;
    private String email;
    private List<String> jobs;
    private List<Marks> marks;
    private FavFood favFood;

}
