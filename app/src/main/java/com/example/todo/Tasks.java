package com.example.todo;

public class Tasks {

    String task;
    boolean done;

    public Tasks(String task, boolean done) {
        this.task = task;
        this.done = done;
    }

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