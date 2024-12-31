package com.example.To_Do_API.repository;

import com.example.To_Do_API.model.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends MongoRepository<ToDo, String> {
    List<ToDo> findByUsername(String username);
}