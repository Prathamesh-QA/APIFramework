package com.qa.pojo;
import java.util.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavFood {

    private String breakfast;
    private String lunch;
    private List<String> dinner;

}
