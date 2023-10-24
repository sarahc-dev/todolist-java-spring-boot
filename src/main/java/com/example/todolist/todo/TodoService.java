package com.example.todolist.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getTodos() {
            return todoRepository.findAll();
    }

    public Todo addTodo(Todo todo) {
            return todoRepository.save(todo);
    }

    public Todo editTodo(String id, Map<String, Object> updatedField) {
        Optional<Todo> existingTodoOptional = todoRepository.findById(id);

        if (existingTodoOptional.isEmpty()) {
            throw new TodoServiceException("id does not exist", new RuntimeException());
        }

        Todo existingTodo = existingTodoOptional.get();

        if (updatedField.containsKey("completed")) {
            Object completedValue = updatedField.get("completed");
            existingTodo.setCompleted((boolean) completedValue);
        } else if (updatedField.containsKey("title")) {
            Object completedValue = updatedField.get("title");
            existingTodo.setTitle((String) completedValue);
        }

        return todoRepository.save(existingTodo);
    }

    public Todo deleteTodo(String id) {
//      Mongoose method returns the deleted id. To do the equivalent in Java, need to separately get item by id
//      Not necessary but added in as comparison
        Optional<Todo> retrieveTodoOptional = todoRepository.findById(id);
        if (retrieveTodoOptional.isEmpty()) {
            throw new TodoServiceException("id does not exist", new RuntimeException());
        }
        Todo deletedTodo = retrieveTodoOptional.get();

        todoRepository.deleteById(id);

        return deletedTodo;
    }
}
