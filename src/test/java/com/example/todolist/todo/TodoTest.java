package com.example.todolist.todo;

import com.example.todolist.todo.Todo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TodoTest {
    @Test
    void gettersAndSettersTest() {
        Todo todo = new Todo();
        todo.set_id("123");
        todo.setTitle("New todo");
        todo.setCompleted(false);

        assertEquals("123", todo.get_id());
        assertEquals("New todo", todo.getTitle());
        assertFalse(todo.isCompleted());
    }

    @Test
    void constructsTodoWithArgs() {
        Todo todo1 = new Todo("123", "New todo", true);
        assertEquals("123", todo1.get_id());
        assertEquals("New todo", todo1.getTitle());
        assertTrue(todo1.isCompleted());
    }

    @Test
    void constructsTodoNoId() {
        Todo todoNoId = new Todo("No id");
        assertNull(todoNoId.get_id());
        assertEquals("No id", todoNoId.getTitle());
        assertFalse(todoNoId.isCompleted());
    }

    @Test
    void returnsTodoAsString() {
        Todo todo = new Todo("123", "New todo", true);
        assertEquals("Todo{_id='123', title='New todo', completed=true}", todo.toString());
    }
}
