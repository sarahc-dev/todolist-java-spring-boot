package com.example.todolist.todo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TodoRepository extends MongoRepository<Todo, String> {
}
