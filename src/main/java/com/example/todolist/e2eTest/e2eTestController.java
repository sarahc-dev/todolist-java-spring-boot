package com.example.todolist.e2eTest;

import com.example.todolist.todo.TodoService;
import com.example.todolist.todo.TodoServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
@Profile("test")
public class e2eTestController {
    private final TodoService todoService;

    @Autowired
    public e2eTestController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllTodos() {
        try {
            todoService.deleteAllTodos();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new TodoServiceException("Error deleting todos", e);
        }
    }
}
