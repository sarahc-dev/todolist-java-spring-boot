package com.example.todolist.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void todosShouldReturnTodosList() throws Exception {
        when(service.getTodos()).thenReturn(List.of(new Todo("1", "New todo", false)));
        mockMvc.perform(get("/api/todos")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]._id", equalTo("1")))
                .andExpect(jsonPath("$[0].title", equalTo("New todo")))
                .andExpect(jsonPath("$[0].completed", equalTo(false)));
    }
}
