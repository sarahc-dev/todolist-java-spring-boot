package com.example.todolist.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    ArrayList<Todo> todos;

    public TodoService() {
        todos = new ArrayList<Todo>();
    }
    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public Todo addTodo(Todo todo) {
        todos.add(todo);
        return todo;
    }
}
