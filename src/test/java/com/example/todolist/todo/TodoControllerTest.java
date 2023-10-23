package com.example.todolist.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(TodoController.class)
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService service;

    @Mock
    private MongoTemplate mongoTemplate;

    @Test
    public void getTodosShouldInitiallyReturnEmptyTodosList() throws Exception {
        when(service.getTodos()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/todos")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getTodosShouldReturnListOfTodos() throws Exception {
        Todo mockTodo1 = new Todo("1", "get milk", false);
        Todo mockTodo2 = new Todo("2", "feed cat", false);
        List<Todo> todolist = new ArrayList<>();
        todolist.add(mockTodo1);
        todolist.add(mockTodo2);

        when(service.getTodos()).thenReturn(todolist);
        mockMvc.perform(get("/api/todos")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]._id", equalTo("1")))
                .andExpect(jsonPath("$[0].title", equalTo("get milk")))
                .andExpect(jsonPath("$[0].completed", equalTo(false)))
                .andExpect(jsonPath("$[1]._id", equalTo("2")))
                .andExpect(jsonPath("$[1].title", equalTo("feed cat")))
                .andExpect(jsonPath("$[1].completed", equalTo(false)));
    }

    @Test
    public void addATodoAndReturnAddedTodo() throws Exception {
        Todo newTodo = new Todo("get milk");

        String todoJson = new ObjectMapper().writeValueAsString(newTodo);

        when(service.addTodo(any(Todo.class))).thenReturn(newTodo);
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", equalTo("get milk")))
                .andExpect(jsonPath("$.completed", equalTo(false)));
    }

    @Test
    public void marksATodoAsComplete() throws Exception {
        Todo updatedTodo = new Todo("1", "get milk", true);
        ResponseEntity<Todo> responseEntity = ResponseEntity.ok(updatedTodo);

        String newValue = "{\"completed\": true}";

        HashMap<String, Object> updateTodoField = new HashMap<>();
        updateTodoField.put("completed", true);
        when(service.editTodo("1", updateTodoField)).thenReturn(responseEntity);
        mockMvc.perform(patch("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", equalTo("1")))
                .andExpect(jsonPath("$.title", equalTo("get milk")))
                .andExpect(jsonPath("$.completed", equalTo(true)));
    }
}
