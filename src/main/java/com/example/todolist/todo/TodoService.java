package com.example.todolist.todo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService {
    ArrayList<Todo> todos;

    public TodoService() {
        todos = new ArrayList<Todo>();
    }
    public ArrayList<Todo> getTodos() {
        return todos;
    }
}
