package com.example.todolist.todo;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    ArrayList<Todo> todos;

//    public TodoService() {
//        todos = new ArrayList<Todo>();
//    }
    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public ResponseEntity<Todo> editTodo(String id, Map<String, Object> updatedField) {
        Optional<Todo> existingTodoOptional = todoRepository.findById(id);

        if (existingTodoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Todo existingTodo = existingTodoOptional.get();

        if (updatedField.containsKey("completed")) {
            Object completedValue = updatedField.get("completed");
            existingTodo.setCompleted((boolean) completedValue);
        } else if (updatedField.containsKey("title")) {
            Object completedValue = updatedField.get("title");
            existingTodo.setTitle((String) completedValue);
        }

        Todo updatedTodo = todoRepository.save(existingTodo);
        return ResponseEntity.ok(updatedTodo);
    }

    public ResponseEntity<Todo> deleteTodo(String id) {
//      Mongoose method returns the deleted id. To do the equivalent in Java, need to separately get item by id
//      Not necessary but added in as comparison
        Optional<Todo> retrieveTodoOptional = todoRepository.findById(id);
        if (retrieveTodoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Todo deletedTodo = retrieveTodoOptional.get();

        todoRepository.deleteById(id);

        return ResponseEntity.ok(deletedTodo);
    }
}
