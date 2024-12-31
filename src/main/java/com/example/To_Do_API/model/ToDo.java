package com.example.To_Do_API.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "todos")
public class ToDo {
    private String id;
    private String title;
    private String description;
    private String status;
    private String username;
}
