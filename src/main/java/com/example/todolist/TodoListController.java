package com.example.todolist;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/todos")
@CrossOrigin
public class TodoListController {

    @GetMapping
    public String index() {
        return "ok";
    }
}
