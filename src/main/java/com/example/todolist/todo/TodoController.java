package com.example.todolist.todo;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todos")
@CrossOrigin
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getTodos() {
        return todoService.getTodos();
    }

    @PostMapping
    public ResponseEntity<Todo> addTodo(@RequestBody Todo newTodo) {
        Todo todo = todoService.addTodo(newTodo);
        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }

//    @PostMapping
//    public ResponseEntity<Todo> addTodo(@RequestBody Todo todo) {
//        Todo newTodo = todoService.addTodo(todo);
//        return new ResponseEntity<Todo>(newTodo, HttpStatus.CREATED);
//    }
}
