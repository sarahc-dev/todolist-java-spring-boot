package com.example.todolist.todo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    public List<Todo> getTodos() {
        return List.of(new Todo("1", "New todo", false));
    }
}
