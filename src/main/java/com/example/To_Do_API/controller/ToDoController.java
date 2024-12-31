package com.example.To_Do_API.controller;

import com.example.To_Do_API.model.ToDo;
import com.example.To_Do_API.model.User;
import com.example.To_Do_API.repository.UserRepository;
import com.example.To_Do_API.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createToDo(@RequestBody ToDo toDo) {
        if (toDo.getUsername() == null) {
            throw new RuntimeException("Username is required");
        }
        ToDo createdToDo = toDoService.createToDo(toDo);
        return ResponseEntity.ok("To Do create successful: " + createdToDo.toString());
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllToDos() {
        List<ToDo> todos = toDoService.getAllToDos();
        if (todos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No ToDos found");
        }
        return ResponseEntity.ok(todos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getToDoById(@PathVariable String id) {
        Optional<ToDo> todo = toDoService.getToDoById(id);
        if (todo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ToDo found with id: " + id);
        }
        return toDoService.getToDoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getToDosByUsername(@PathVariable String username) {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username: " + username + " not found");
        }

        List<ToDo> todos = toDoService.getToDosByUsername(username);

        if (todos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ToDos found for username: " + username);
        }

        return ResponseEntity.ok(todos);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateToDo(@PathVariable String id, @RequestBody ToDo updatedToDo) {
        if (updatedToDo.getUsername() == null || updatedToDo.getUsername().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        try {
            ToDo toDo = toDoService.updateToDo(id, updatedToDo);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "ToDo updated successfully");
            response.put("updatedToDo", toDo);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "ToDo not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteToDoById(@PathVariable String id) {
        try {
            toDoService.deleteToDoById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "ToDo with id " + id + " deleted successfully");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "ToDo with id " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

}
