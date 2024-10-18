package com.example.todo;

public class Tasks {
    // atributos
    String task;
    boolean done;

    // construtor
    public Tasks(String task, boolean done) {
        this.task = task;
        this.done = done;
    }

    // getters e setters
    public String getTask() {
        return this.task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean getDone() {
        return this.done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}