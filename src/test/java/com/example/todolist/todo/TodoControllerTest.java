package com.example.todolist.todo;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        when(service.addTodo(any(Todo.class))).thenReturn(newTodo);
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"get milk\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", equalTo("get milk")))
                .andExpect(jsonPath("$.completed", equalTo(false)));
    }

    @Test
    public void markATodoAsCompleteAndReturnUpdatedTodo() throws Exception {
        Todo updatedTodo = new Todo("1", "get milk", true);

        String newValue = "{\"completed\": true}";

        HashMap<String, Object> updateTodoField = new HashMap<>();
        updateTodoField.put("completed", true);
        when(service.editTodo("1", updateTodoField)).thenReturn(updatedTodo);
        mockMvc.perform(patch("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", equalTo("1")))
                .andExpect(jsonPath("$.title", equalTo("get milk")))
                .andExpect(jsonPath("$.completed", equalTo(true)));
    }

    @Test
    public void editsTodoTitleAndReturnUpdatedTodo() throws Exception {
        Todo updatedTodo = new Todo("2", "get coffee", false);

        String newValue = "{\"title\": \"get coffee\"}";

        HashMap<String, Object> updateTodoField = new HashMap<>();
        updateTodoField.put("title", "get coffee");
        when(service.editTodo("2", updateTodoField)).thenReturn(updatedTodo);
        mockMvc.perform(patch("/api/todos/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", equalTo("2")))
                .andExpect(jsonPath("$.title", equalTo("get coffee")))
                .andExpect(jsonPath("$.completed", equalTo(false)));
    }

    @Test
    public void deletesATodoAndReturnsDeletedTodo() throws Exception {
        Todo deletedTodo = new Todo("1", "get coffee", false);

        when(service.deleteTodo("1")).thenReturn(deletedTodo);
        mockMvc.perform(delete("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", equalTo("1")))
                .andExpect(jsonPath("$.title", equalTo("get coffee")))
                .andExpect(jsonPath("$.completed", equalTo(false)));
    }

    @Test
    public void handlesErrorGettingTodos() throws Exception {
        when(service.getTodos()).thenThrow(new RuntimeException("Error getting todos"));
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error getting todos"));
    }

    @Test
    public void handlesErrorAddingTodo() throws Exception {
        when(service.addTodo(any(Todo.class))).thenThrow(new RuntimeException("Error adding todo"));
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"new todo\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error adding todo"));
    }

    @Test
    public void returnsClientErrorIfEmptyTodo() throws Exception {
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title should not be empty"));
    }

    @Test
    public void handlesErrorEditingTodo() throws Exception {
        HashMap<String, Object> updateTodoField = new HashMap<>();
        updateTodoField.put("title", "get coffee");

        when(service.editTodo("1", updateTodoField)).thenThrow(new RuntimeException("Error editing todo"));
        mockMvc.perform(patch("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"get coffee\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error editing todo"));
    }

    @Test
    public void handlesIdNotFoundWhenEditingTodo() throws Exception {
        HashMap<String, Object> updateTodoField = new HashMap<>();
        updateTodoField.put("title", "get coffee");

        when(service.editTodo("1", updateTodoField)).thenThrow(new TodoServiceException("id not found", new RuntimeException()));
        mockMvc.perform(patch("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"get coffee\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo does not exist"));
    }

    @Test
    public void returnsClientErrorIfTitleIsEmpty() throws Exception {
        mockMvc.perform(patch("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title should not be empty"));
    }

    @Test
    public void handlesErrorDeletingTodo() throws Exception {
        when(service.deleteTodo("1")).thenThrow(new RuntimeException("Error deleting todo"));
        mockMvc.perform(delete("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error deleting todo"));
    }

    @Test
    public void handlesIdNotFoundWhenDeletingTodo() throws Exception {
        when(service.deleteTodo("1")).thenThrow(new TodoServiceException("id not found", new RuntimeException()));
        mockMvc.perform(delete("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo does not exist"));
    }
}
