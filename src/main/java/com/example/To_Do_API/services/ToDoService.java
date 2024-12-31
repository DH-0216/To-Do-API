package com.example.To_Do_API.services;

import com.example.To_Do_API.model.ToDo;
import com.example.To_Do_API.model.User;
import com.example.To_Do_API.repository.ToDoRepository;
import com.example.To_Do_API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;
    @Autowired
    private UserRepository userRepository;

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public Optional<ToDo> getToDoById(String id) {
        return toDoRepository.findById(id);
    }

    public List<ToDo> getToDosByUsername(String username) {
        return toDoRepository.findByUsername(username);
    }

    public ToDo createToDo(ToDo toDo) {
        Optional<User> user = userRepository.findByUsername(toDo.getUsername());
        if (user.isPresent()) {
            return toDoRepository.save(toDo);
        } else {
            throw new RuntimeException("User not found with username: " + toDo.getUsername());
        }
    }

    public ToDo updateToDo(String id, ToDo updatedToDo) {
        Optional<User> user = userRepository.findByUsername(updatedToDo.getUsername());
        if (user.isPresent()) {
            return toDoRepository.findById(id).map(toDo -> {
                toDo.setTitle(updatedToDo.getTitle());
                toDo.setDescription(updatedToDo.getDescription());
                toDo.setStatus(updatedToDo.getStatus());
                toDo.setUsername(updatedToDo.getUsername());
                return toDoRepository.save(toDo);
            }).orElseThrow(() -> new RuntimeException("ToDo not found with id: " + id));
        } else {
            throw new RuntimeException("User not found with username: " + updatedToDo.getUsername());
        }

    }

    public void deleteToDoById(String id) {
        toDoRepository.deleteById(id);
    }
}
