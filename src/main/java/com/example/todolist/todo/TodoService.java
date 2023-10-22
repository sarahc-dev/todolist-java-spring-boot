package com.example.todolist.todo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TodoService {
    ArrayList<Todo> todos;

    public TodoService() {
        todos = new ArrayList<Todo>();
    }
    public ArrayList<Todo> getTodos() {
        return todos;
    }

    public Todo addTodo(Todo todo) {
        todos.add(todo);
        return todo;
    }
}
