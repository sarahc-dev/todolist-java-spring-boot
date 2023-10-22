package com.example.todolist.todo;

public class Todo {
    private String _id;
    private String title;
    private boolean completed;

    public Todo() {
    }

    public Todo(String _id, String title, boolean completed) {
        this._id = _id;
        this.title = title;
        this.completed = completed;
    }

    public Todo(String title) {
        this.title = title;
        this.completed = false;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
