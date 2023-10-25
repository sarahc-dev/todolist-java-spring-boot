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
        try {
            List<Todo> todos = todoService.getTodos();
            return new ResponseEntity<>(todos, HttpStatus.OK);
        } catch (Exception e) {
            throw new TodoServiceException("Error getting todos", e);
        }
    }

    @PostMapping
    public ResponseEntity<Todo> addTodo(@RequestBody Todo newTodo) {
        if (newTodo.getTitle() == null || newTodo.getTitle().trim().isEmpty()) {
            throw new TodoServiceException("Title should not be empty", new RuntimeException("Error"), HttpStatus.BAD_REQUEST);
        }

        try {
            Todo addedTodo = todoService.addTodo(newTodo);
            return new ResponseEntity<>(addedTodo, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new TodoServiceException("Error adding todo", e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Todo> editTodo(
            @PathVariable String id,
            @RequestBody Map<String, Object> updatedField) {

        if (updatedField.containsKey("title") && updatedField.get("title") == "") {
            throw new TodoServiceException("Title should not be empty", new RuntimeException("Error"), HttpStatus.BAD_REQUEST);
        }

        try {
            Todo editedTodo = todoService.editTodo(id, updatedField);
            return new ResponseEntity<>(editedTodo, HttpStatus.OK);
        } catch (TodoServiceException e) {
            throw new TodoServiceException("Todo does not exist", e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new TodoServiceException("Error editing todo", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable String id) {
        try {
            Todo deletedTodo = todoService.deleteTodo(id);
            return new ResponseEntity<>(deletedTodo, HttpStatus.OK);
        } catch (TodoServiceException e) {
            throw new TodoServiceException("Todo does not exist", e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new TodoServiceException("Error deleting todo", e);
        }
    }

    @ExceptionHandler
    public ResponseEntity<String> handleTodoServiceException(TodoServiceException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        return new ResponseEntity<>(ex.getMessage(), httpStatus);
    }
}
