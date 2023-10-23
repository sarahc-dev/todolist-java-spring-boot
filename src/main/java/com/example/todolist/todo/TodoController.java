package com.example.todolist.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<Todo>> getTodos() {
        List<Todo> todos = todoService.getTodos();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Todo> addTodo(@RequestBody Todo newTodo) {
        Todo todo = todoService.addTodo(newTodo);
        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Todo> editTodo(
            @PathVariable String id,
            @RequestBody Map<String, Object> updatedField) {


        return todoService.editTodo(id, updatedField);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable String id) {
        return todoService.deleteTodo(id);
    }
}
