package com.example.todolist.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
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

    @Test
    public void todosShouldInitiallyReturnEmptyTodosList() throws Exception {
        when(service.getTodos()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/todos")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
//                .andExpect(jsonPath("$[0]._id", equalTo("1")))
//                .andExpect(jsonPath("$[0].title", equalTo("Feed cat")))
//                .andExpect(jsonPath("$[0].completed", equalTo(false)));
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
                .andExpect(jsonPath("$.title", equalTo("get milk")));
    }
}
