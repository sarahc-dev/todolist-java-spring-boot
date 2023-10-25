package com.example.todolist.todo;

import com.example.todolist.EnableMongoTestServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableMongoTestServer
@EnableMongoRepositories
@ComponentScan("com.example.todolist")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void setup() {
        todoRepository.deleteAll();
        Todo testTodo1 = new Todo("get milk");
        Todo testTodo2 = new Todo("feed cat");
        todoRepository.save(testTodo1);
        todoRepository.save(testTodo2);
    }

    @Test
    @Order(1)
    void getsTodos() throws Exception {
        Todo retrieveTodo1 = todoRepository.findByTitle("get milk");
        String todoId1 = retrieveTodo1.get_id();
        Todo retrieveTodo2 = todoRepository.findByTitle("feed cat");
        String todoId2 = retrieveTodo2.get_id();

        mockMvc.perform(get("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]._id", equalTo(todoId1)))
                .andExpect(jsonPath("$[0].title", equalTo("get milk")))
                .andExpect(jsonPath("$[0].completed", equalTo(false)))
                .andExpect(jsonPath("$[1]._id", equalTo(todoId2)))
                .andExpect(jsonPath("$[1].title", equalTo("feed cat")))
                .andExpect(jsonPath("$[1].completed", equalTo(false)));
    }

    @Test
    void addTodo() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"walk dog\"}"))
                .andExpect(status().isCreated()).andDo(print())
                .andExpect(jsonPath("$._id", notNullValue()))
                .andExpect(jsonPath("$.title", equalTo("walk dog")))
                .andExpect(jsonPath("$.completed", equalTo(false)));
    }

    @Test
    void returnsExceptionWhenTodoEmpty() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title should not be empty"));
    }

    @Test
    void marksTodoAsComplete() throws Exception {
        Todo retrieveTodo1 = todoRepository.findByTitle("get milk");
        String todoId1 = retrieveTodo1.get_id();
        String path = "/api/todos/" + todoId1;
        mockMvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"completed\": true}"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$._id", equalTo(todoId1)))
                .andExpect(jsonPath("$.title", equalTo("get milk")))
                .andExpect(jsonPath("$.completed", equalTo(true)));
    }

    @Test
    void editsTodoTitle() throws Exception {
        Todo retrieveTodo1 = todoRepository.findByTitle("get milk");
        String todoId1 = retrieveTodo1.get_id();
        String path = "/api/todos/" + todoId1;
        mockMvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"get chocolate\"}"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$._id", equalTo(todoId1)))
                .andExpect(jsonPath("$.title", equalTo("get chocolate")))
                .andExpect(jsonPath("$.completed", equalTo(false)));
    }

    @Test
    void returnsExceptionWhenPatchWithIncorrectId() throws Exception {
        mockMvc.perform(patch("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"get chocolate\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo does not exist"));
    }

    @Test
    void returnsExceptionWhenTitleEmpty() throws Exception {
        Todo retrieveTodo1 = todoRepository.findByTitle("get milk");
        String todoId1 = retrieveTodo1.get_id();
        String path = "/api/todos/" + todoId1;
        mockMvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title should not be empty"));
    }

    @Test
    void deletesTodo() throws Exception {
        Todo retrieveTodo1 = todoRepository.findByTitle("get milk");
        String todoId1 = retrieveTodo1.get_id();
        String path = "/api/todos/" + todoId1;
        mockMvc.perform(delete(path)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", equalTo(todoId1)))
                .andExpect(jsonPath("$.title", equalTo("get milk")))
                .andExpect(jsonPath("$.completed", equalTo(false)));
        mockMvc.perform(get("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", equalTo("feed cat")))
                .andExpect(jsonPath("$[0].completed", equalTo(false)));
    }

    @Test
    void returnsExceptionWhenDeleteWithIncorrectId() throws Exception {
        mockMvc.perform(delete("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo does not exist"));
    }
}
